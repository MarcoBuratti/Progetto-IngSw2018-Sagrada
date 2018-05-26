package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.RemoteView;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.model.GameBoard;
import it.polimi.ingsw.server.model.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;

public class Controller extends Observable implements Observer {
    private static final int NUMBER_OF_ROUNDS = 10;
    private Round currentRound;
    private GameBoard gameBoard;
    private ArrayList<RemoteView> remoteViews;
    private ArrayList<Player> players;

    public Controller(Server server) {
        gameBoard = new GameBoard(server.getPlayers());
        players = gameBoard.getPlayers();
    }

    public void setRemoteViews (Server server) {
        remoteViews = new ArrayList<>(server.getRemoteViews());
        for (RemoteView r: remoteViews)
            r.addObserver(this);
    }

    public void startGame () {
        for ( int i = 0 ; i < NUMBER_OF_ROUNDS ; i++ ) {
            currentRound = new Round(players, gameBoard);
            this.addObserver(currentRound);
            currentRound.roundManager();
            currentRound.endRound();
            Collections.rotate(players, 1);
        }
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    @Override
    public void update(Observable o, Object arg) {
        PlayerMove playerMove = (PlayerMove) arg;
        setChanged();
        notifyObservers(playerMove);
    }
}
