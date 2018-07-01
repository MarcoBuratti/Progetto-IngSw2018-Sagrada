package it.polimi.ingsw.server;

import it.polimi.ingsw.server.interfaces.ServerInterface;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Lobby {
    private Server server;
    private ExecutorService executor;
    private ArrayList<RemoteView> remoteViews;
    private ArrayList<ServerInterface> serverInterfaces;
    private Timer timer;
    private boolean gameStarted;

    Lobby ( Server server ) {
        this.server = server;
        this.remoteViews = new ArrayList<>();
        this.serverInterfaces = new ArrayList<>();
        executor = Executors.newCachedThreadPool();
    }

    synchronized void playerDisconnected ( RemoteView remoteView ) {

        for (RemoteView r : remoteViews)
            if ( r != null && r != remoteView )
                r.send(remoteView.getPlayer().getNickname() + " has disconnected from the server.");

        this.serverInterfaces.remove( remoteView.getServerInterface() );
        remoteView.removeConnection();

    }

    synchronized void playerReconnected ( RemoteView remoteView ) {

        for ( RemoteView r: remoteViews )
            if ( r != remoteView && r != null )
                r.send( remoteView.getPlayer().getNickname() + " has reconnected!");

        serverInterfaces.add( remoteView.getServerInterface() );

    }

    private void gameStartTimer() {
        timer = new Timer();
        int lobbyTime = 10 * 1000;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(!gameStarted)
                    startGame();
            }
        }, lobbyTime);
    }

    private void startGame() {
        timer.cancel();
        server.setNewLobby();
        gameStarted = true;
        if ( serverInterfaces.size() >= 2 ) {
            executor.submit( new Game(this, this.server ));
        }
        else
            gameFailed();
    }

    private void gameFailed () {

        System.out.println("Game start failed because some of the players disconnected!");
        serverInterfaces.get(0).send("Sorry! Other players disconnected before the game started. Please try again.");
        serverInterfaces.get(0).close();

    }

    ArrayList<RemoteView> getRemoteViews() {
        return remoteViews;
    }

    void addRemoteView ( RemoteView remoteView ) {
        this.remoteViews.add( remoteView );
        this.serverInterfaces.add( remoteView.getServerInterface() );
        if ( remoteViews.size() == 2 )
            gameStartTimer();
        else if ( remoteViews.size() == 4 ) {
            startGame();
        }
    }
}
