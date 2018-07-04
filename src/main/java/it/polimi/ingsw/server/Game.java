package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.interfaces.ServerInterface;
import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.SchemeCardsEnum;
import it.polimi.ingsw.util.CliGraphicsServer;
import it.polimi.ingsw.util.TimeParser;

import java.util.*;

public class Game extends Observable implements Runnable {
    private Server server;
    private Controller controller;
    private ModelView modelView;
    private ArrayList<RemoteView> remoteViews;
    private ArrayList<Player> players;
    private ArrayList<ServerInterface> serverInterfaces;
    private ArrayList<SchemeCardsEnum> schemes;
    private ArrayList<Color> privateAchievements;
    private boolean schemeChosen = false;
    private boolean gameEnded = false;
    private CliGraphicsServer cliGraphicsServer;
    private long schemeChoiceTime;
    private static final String SCHEME_CHOICE_TIMER = "Scheme_Choice_Timer";

    /**
     * Creates a new Game Object having references to the server, the controller and all of the players and their connections.
     * @param lobby the Lobby Object that created the new Game Object
     * @param server the server
     */
    public Game ( Lobby lobby, Server server ) {
        this.server = server;
        server.registerGame( this );
        this.remoteViews = lobby.getRemoteViews();

        this.players = new ArrayList<>();
        serverInterfaces = new ArrayList<>();

        for (RemoteView r: remoteViews) {
            r.setGame( this );
            this.players.add( r.getPlayer() );
            ServerInterface serverInterface = r.getServerInterface();
            if ( serverInterface != null ) {
                this.serverInterfaces.add( serverInterface );
                this.addObserver( serverInterface );
            }
        }

        setTime();
        cliGraphicsServer = new CliGraphicsServer();
    }

    /**
     * Calls a static method of the class TimeParser in order to read the time from a json file.
     */
    private synchronized void setTime () {
        this.schemeChoiceTime = TimeParser.readTime(SCHEME_CHOICE_TIMER);
    }

    /**
     * Deletes references to the connection belonging to the player who's disconnected and communicates to the other players that he's disconnected.
     * If the game has not ended and only one player is connected, calls the method gameFailed.
     * @param remoteView the RemoteView Object belonging to the player who has disconnected
     */
    synchronized void playerDisconnected ( RemoteView remoteView ) {

        for (RemoteView r : remoteViews)
            if ( r != null && r != remoteView && r.getServerInterface() != null)
                r.send(remoteView.getPlayer().getNickname() + " has disconnected from the server.");

        this.serverInterfaces.remove( remoteView.getServerInterface() );
        remoteView.removeConnection();


        if ( this.serverInterfaces.size() == 1 && !isGameEnded() )
            gameFailed();
    }

    /**
     * Adds references to the connection belonging to the player who's reconnected and communicates to the other players that he's reconnected.
     * @param remoteView the RemoteView Object belonging to the player who has reconnected
     */
    synchronized void playerReconnected ( RemoteView remoteView ) {

        remoteView.showGameBoard( modelView );

        for ( RemoteView r: remoteViews )
            if ( r != remoteView && r != null )
                r.send( remoteView.getPlayer().getNickname() + " has reconnected!");

        serverInterfaces.add( remoteView.getServerInterface() );
        this.addObserver( remoteView.getServerInterface() );
    }

    /**
     * Returns the remoteViews attribute.
     * @return a List of RemoteView Objects
     */
    public List<RemoteView> getRemoteViews() {
        return remoteViews;
    }

    /**
     * Returns the players attribute.
     * @return a List of Player Objects
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Communicates to the Controller, to the Server and to the connected player that the game has ended.
     * It also closes the connected player's connection.
     */
    private synchronized void gameFailed () {

        if (this.controller != null)
            this.controller.onePlayerLeftEnd();

        if (isSchemeChosen()) {

            serverInterfaces.get(0).send("You win!");
            cliGraphicsServer.printWinner(serverInterfaces.get(0).getPlayer().getNickname());
            serverInterfaces.get(0).close();
            this.endGame();

        } else {
            System.out.println("Game start failed because some of the players disconnected!");
            serverInterfaces.get(0).send("Sorry! Other players disconnected before the game started. Please try again.");
            serverInterfaces.get(0).close();
            this.endGame();
        }

    }

    /**
     * Launches a timer. When the time is over, the schemeChoiceTimeOut method is called.
     */
    private void schemeChoiceTimer () {

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                if (!schemeChosen)
                    schemeChoiceTimeOut();

            }
        }, schemeChoiceTime);
    }

    /**
     * Sets schemeChosen as true and notifies all ( unlocking wait ).
     */
    private synchronized void schemeChoiceTimeOut () {
        schemeChosen = true;
        notifyAll();
    }

    /**
     * Returns the schemeChosen attribute.
     * @return a boolean
     */
    public boolean isSchemeChosen() {
        return schemeChosen;
    }

    /**
     * Returns a String containing four scheme names, extracting theme randomly from the schemes attribute.
     * @return a String
     */
    public synchronized String selectSchemes() {
        StringBuilder bld = new StringBuilder();
        bld.append(this.schemes.get(0).getFirstScheme());
        bld.append(",");
        bld.append(this.schemes.get(0).getSecondScheme());
        bld.append(",");
        this.schemes.remove(0);
        bld.append(this.schemes.get(0).getFirstScheme());
        bld.append(",");
        bld.append(this.schemes.get(0).getSecondScheme());
        this.schemes.remove(0);
        return bld.toString();
    }

    /**
     * Returns a Color Object representing the extracted private achievement for the player.
     * @return a Color Object
     */
    public synchronized Color selectPrivateAchievement() {
        Color privateAchievement = this.privateAchievements.get(0);
        this.privateAchievements.remove(0);
        return privateAchievement;
    }

    /**
     * Manages the end of the game ( making the server deregister all the players who have played the game ).
     */
    private synchronized void endGame() {

        for ( RemoteView r: remoteViews ) {
            server.removeRemoteView( r );
            Player p = r.getPlayer();
            server.removeNickname( p.getNickname() );
            if ( r.getServerInterface() != null )
                server.removeServerInterface( r.getServerInterface() );
        }

        serverInterfaces.clear();
        players.clear();
        remoteViews.clear();
        server.deregisterGame( this );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        System.out.println("A new game is about to start!");
        schemes = new ArrayList<>(Arrays.asList(SchemeCardsEnum.values()));
        Collections.shuffle(schemes);
        privateAchievements = new ArrayList<>(Arrays.asList(Color.values()));
        Collections.shuffle(privateAchievements);
        setChanged();
        notifyObservers();
        schemeChoiceTimer();

        synchronized (this) {
            try {
                while (!schemeChosen)
                    wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.controller = new Controller(this);
        modelView = new ModelView(controller.getGameBoard());

        for (RemoteView r : remoteViews)
            r.setModelView(modelView);

        this.controller.setRemoteViews(this);
        ArrayList<ServerInterface> tempServerInterfaces = new ArrayList<>( serverInterfaces );

        for (ServerInterface s : tempServerInterfaces)
            s.send("The game has started!");

        if (serverInterfaces.size() > 1) {
            cliGraphicsServer.printStart();
            String winner = this.controller.startGame();
            if ( controller.isGameEnded() ) {
                setGameEnded();
                tempServerInterfaces = new ArrayList<>(serverInterfaces);
                for (ServerInterface serverInterface : tempServerInterfaces) {
                    serverInterface.close();
                }
                cliGraphicsServer.printWinner(winner);
                this.endGame();
            }
        }

        else {
            gameFailed();
        }
    }

    /**
     * Returns the gameEnded attribute.
     * @return a boolean specifying whether the game has ended or not.
     */
    private synchronized boolean isGameEnded() {
        return gameEnded;
    }

    /**
     * Allows the user to set the gameEnded attribute as true.
     */
    private synchronized void setGameEnded() {
        this.gameEnded = true;
    }
}
