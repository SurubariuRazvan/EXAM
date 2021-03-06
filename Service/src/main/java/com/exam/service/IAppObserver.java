package com.exam.service;

import com.exam.domain.Game;
import com.exam.domain.Round;
import com.exam.domain.User;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IAppObserver extends Remote {
    void startGame() throws RemoteException;

    void loggedIn(User user) throws RemoteException;

    void playerCountUpdated(Integer count, Integer id) throws RemoteException;

    void loggedOut(User user) throws RemoteException;

    void finishGame(String configuration) throws RemoteException;

    void setScores(User user, Integer position) throws RemoteException;
}
