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

    /**
     * Creates a new Lobby Object and initializes all of its attributes, saving the reference to the Server too.
     * @param server the Server that creates the new Lobby
     */
    Lobby ( Server server ) {
        this.server = server;
        this.remoteViews = new ArrayList<>();
        this.serverInterfaces = new ArrayList<>();
        executor = Executors.newCachedThreadPool();
    }

    /**
     * Deletes the reference to the disconnected player's connection and communicates to the other players that the player has disconnected.
     * @param remoteView the RemoteView Object belonging to the player who had disconnected
     */
    synchronized void playerDisconnected ( RemoteView remoteView ) {

        for (RemoteView r : remoteViews)
            if ( r != null && r != remoteView )
                r.send(remoteView.getPlayer().getNickname() + " has disconnected from the server.");

        this.serverInterfaces.remove( remoteView.getServerInterface() );
        remoteView.removeConnection();

    }

    /**
     * Adds the reference to the reconnected player's connection and communicates to the other players that te player has reconnected.
     * @param remoteView the RemoteView Object belonging to the player who had reconnected
     */
    synchronized void playerReconnected ( RemoteView remoteView ) {

        for ( RemoteView r: remoteViews )
            if ( r != remoteView && r != null )
                r.send( remoteView.getPlayer().getNickname() + " has reconnected!");

        serverInterfaces.add( remoteView.getServerInterface() );

    }

    /**
     * Launches a timer that starts the game when the time is over.
     */
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

    /**
     * Launches a new game creating a new Game Object and submitting it to the executor ( ExecutorService ) if at least two players are connected,
     * else closes the lobby and tells the connected player that the game start has failed.
     */
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

    /**
     * Tells the connected player that the game start has failed and closes it's connection.
     */
    private void gameFailed () {

        System.out.println("Game start failed because some of the players disconnected!");
        serverInterfaces.get(0).send("Sorry! Other players disconnected before the game started. Please try again.");
        serverInterfaces.get(0).close();

    }

    /**
     * Returns the remoteViews attribute.
     * @return an ArrayList of RemoteView Objects
     */
    ArrayList<RemoteView> getRemoteViews() {
        return remoteViews;
    }

    /**
     * Adds a RemoteView Object to the remoteViews attribute.
     * It also launches the timer if two players are connected or starts the game if four players are connected.
     * @param remoteView the RemoteView Object that the user wants to add to remoteViews
     */
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
