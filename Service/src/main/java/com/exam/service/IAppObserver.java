package com.exam.service;

import com.exam.domain.Category;
import com.exam.domain.Game;
import com.exam.domain.Round;
import com.exam.domain.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IAppObserver extends Remote {
    void startGame() throws RemoteException;

    void loggedIn(User user) throws RemoteException;

    void playerCountUpdated(Integer count) throws RemoteException;

    void loggedOut(User user) throws RemoteException;

    void setCategory(Category currentCategory) throws RemoteException;

    void setScores(Round currentRound) throws RemoteException;

    void finishGame(Game game) throws RemoteException;
}
