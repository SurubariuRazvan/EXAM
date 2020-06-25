package com.exam.client.controller;

import com.exam.client.gui.GuiUtility;
import com.exam.domain.*;
import com.exam.service.IAppObserver;
import com.exam.service.IAppServices;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.io.Serializable;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class GameController extends UnicastRemoteObject implements Initializable, IAppObserver, Serializable {
    public Label letterSet;
    public Button send;
    public Label generatedNumber;
    public StackPane rootPane;
    public BorderPane menuPane;
    public Label lastPlayer;
    public Label lastGN;
    private IAppServices appService;
    private User user;
    private Random rand;

    public GameController() throws RemoteException {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setService(IAppServices appService, User user) {
        this.appService = appService;
        this.user = user;
        this.rand = new Random();
        Platform.runLater(() -> {
            letterSet.setText(appService.getLetterSet());
        });
    }


    @Override
    public void playerCountUpdated(Integer count, Integer id) {
    }

    @Override
    public void startGame() {
    }

    @Override
    public void loggedIn(User user) {
    }

    @Override
    public void loggedOut(User user) {
    }

    @Override
    public void setScores(User user, Integer position) {
        Platform.runLater(() -> {
            letterSet.setText(appService.getLetterSet());
            if (user == null)
                send.setDisable(false);
            else {
                lastPlayer.setText(user.getName());
                lastGN.setText(position.toString());
            }
        });
    }

    @Override
    public void finishGame(String winner) {
        Platform.runLater(() -> {
            GuiUtility.showError(rootPane, menuPane, "Won the game!!", winner);
            send.setDisable(true);
        });
    }

    public void send(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            send.setDisable(true);
        });
        Integer number = rand.nextInt(4) + 1;
        generatedNumber.setText(number.toString());
        appService.sendWord(user, number);
    }
}

