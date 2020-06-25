package com.exam.server;

import com.exam.domain.*;
import com.exam.repository.GameRepository;
import com.exam.repository.UserRepository;
import com.exam.repository.WordRepository;
import com.exam.service.AppServiceException;
import com.exam.service.IAppObserver;
import com.exam.service.IAppServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class AppServicesImpl implements IAppServices {
    private final UserRepository userRepo;
    private final GameRepository gameRepository;
    private final WordRepository wordRepository;
    private final Map<Integer, LoggedUser> loggedUsers;
    private final Random rand;
    private Game game;
    private Round currentRound;
    private Integer roundNumber;
    private LetterSet currentLetterSet;

    @Autowired
    public AppServicesImpl(UserRepository userRepository, GameRepository gameRepository, WordRepository wordRepository) {
        this.userRepo = userRepository;
        this.gameRepository = gameRepository;
        this.wordRepository = wordRepository;
        this.loggedUsers = new ConcurrentHashMap<>();
        this.rand = new Random();
        addData();
    }

    private void addData() {
        userRepo.save(new Student(null, "ion1", "a", "Ionel1"));
        userRepo.save(new Student(null, "ion2", "a", "Ionel2"));
        userRepo.save(new Student(null, "ion3", "a", "Ionel3"));
        userRepo.save(new Student(null, "ion4", "a", "Ionel4"));

        LetterSet letterSet = new LetterSet(null, "__W_____W____", new HashSet<>());
        wordRepository.save(letterSet);

        LetterSet letterSet1 = new LetterSet(null, "____W___W__W_", new HashSet<>());
        wordRepository.save(letterSet1);
    }

    @Override
    public synchronized User login(String username, String password, IAppObserver client) throws AppServiceException {
        User user = userRepo.findByUsernameAndPassword(username, password);
        if (user == null)
            return null;
        if (loggedUsers.containsKey(user.getId()))
            throw new AppServiceException("User already logged in");
        loggedUsers.put(user.getId(), new LoggedUser(user.getUserType(), client));
        return user;
    }

    @Override
    public String getLetterSet() {
        return currentLetterSet.getConfiguration().substring(1);
    }

    @Override
    public synchronized void playerCountChanged(Integer id) {
        for (var user : loggedUsers.values()) {
            try {
                user.getObserver().playerCountUpdated(loggedUsers.size(), id);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void setNewLetterSet() {
        int value = rand.nextInt(2);
        for (var letterSet : wordRepository.findAll())
            if (value-- == 0) {
                this.currentLetterSet = letterSet;
                break;
            }
    }

    @Override
    public synchronized void logout(Integer userID) throws AppServiceException {
        LoggedUser loggedUser = loggedUsers.get(userID);
        if (loggedUser != null) {
            loggedUsers.remove(userID);
        } else
            throw new AppServiceException("User isn't logged in");
    }

    @Override
    public void notifyStartGame() {
        setNewLetterSet();
        game = new Game(null, currentLetterSet.getConfiguration());
        currentRound = new Round(null, game, new HashSet<>(), currentLetterSet);
        roundNumber = 0;
        for (var user : loggedUsers.values()) {
            try {
                user.getObserver().startGame();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleRound() {
        for (var user : loggedUsers.values()) {
            try {
                user.getObserver().setScores(null, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        currentRound = new Round(null, game, new HashSet<>(), currentLetterSet);
    }


    @Override
    public void sendWord(User user, Integer position) {
        char userID = user.getId().toString().charAt(0);
        String configuration = currentLetterSet.getConfiguration();
        Integer oldPosition = configuration.indexOf(userID);
        if (oldPosition == -1)
            oldPosition++;
        char[] confArray = configuration.toCharArray();
        if (confArray[oldPosition + position] == '_') {
            confArray[oldPosition + position] = userID;
            confArray[oldPosition] = '_';
        } else {
            boolean chosen = false;
            int currentPosition = position + oldPosition;
            if (confArray[currentPosition] == 'W') {
                confArray[oldPosition] = '_';
                while (!chosen) {
                    if (confArray[currentPosition] == '_') {
                        confArray[currentPosition] = userID;
                        chosen = true;
                    }
                    currentPosition--;
                    if (currentPosition == 0)
                        chosen = true;
                }
            } else {
                if (confArray[currentPosition] != 'W' && confArray[currentPosition] != '_') {
                    char oldID = userID;
                    userID = confArray[currentPosition];
                    confArray[currentPosition] = oldID;
                    confArray[oldPosition] = '_';
                    while (!chosen) {
                        if (confArray[currentPosition] == '_') {
                            confArray[currentPosition] = userID;
                            chosen = true;
                        }
                        currentPosition--;
                        if (currentPosition == 0)
                            chosen = true;
                    }
                }
            }
        }
        configuration = String.valueOf(confArray);
        currentLetterSet.setConfiguration(configuration);
        currentRound.getWords().add(new Word(null, (Student) user, currentRound, configuration, position));
        for (var userr : loggedUsers.values()) {
            try {
                userr.getObserver().setScores(user, position);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        if (currentRound.getWords().size() == 3) {
            game.addRound(currentRound);
            handleRound();
            roundNumber++;
            if (roundNumber == 3)
                gameFinished();
        }
    }

    private void gameFinished() {
        gameRepository.save(game);
        char[] arr = currentLetterSet.getConfiguration().toCharArray();
        int winner = 1;
        for (int i = arr.length - 1; i > 0; i--)
            if (arr[i] != '_' && arr[i] != 'W') {
                winner = arr[i] - '0';
                break;
            }
        for (var user : loggedUsers.values()) {
            try {
                user.getObserver().finishGame(userRepo.findByID(winner).getName());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}