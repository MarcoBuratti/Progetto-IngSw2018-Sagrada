package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.interfaces.ServerInterface;
import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.SchemeCardsEnum;
import it.polimi.ingsw.util.CliGraphicsServer;

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
    private CliGraphicsServer cliGraphicsServer;

    public Game ( Lobby lobby, Server server ) {
        this.server = server;
        server.registerGame( this );
        this.remoteViews = lobby.getRemoteViews();

        this.players = new ArrayList<>();
        serverInterfaces = new ArrayList<>();

        for (RemoteView r: remoteViews) {
            r.setGame( this );
            this.players.add(r.getPlayer());
            ServerInterface serverInterface = r.getPlayer().getServerInterface();
            if ( serverInterface != null ) {
                this.serverInterfaces.add( serverInterface );
                this.addObserver( serverInterface );
            }
        }

        cliGraphicsServer = new CliGraphicsServer();
    }


    synchronized void playerDisconnected ( RemoteView remoteView ) {

        for (RemoteView r : remoteViews)
            if ( r != null && r != remoteView )
                if ( r.getServerInterface() != null )
                    r.send(remoteView.getPlayer().getNickname() + " has disconnected from the server.");

        this.serverInterfaces.remove( remoteView.getServerInterface() );
        remoteView.removeConnection();
        remoteView.getPlayer().removeServerInterface();


        if ( this.serverInterfaces.size() == 1 )
            gameFailed();
    }

    synchronized void playerReconnected ( RemoteView remoteView ) {

        remoteView.showGameBoard( modelView );

        for ( RemoteView r: remoteViews )
            if ( r != remoteView && r != null )
                r.send( remoteView.getPlayer().getNickname() + " has reconnected!");

        serverInterfaces.add( remoteView.getServerInterface() );
        this.addObserver( remoteView.getServerInterface() );
    }

    public ArrayList<RemoteView> getRemoteViews() {
        return remoteViews;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

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

    private void schemeChoiceTimer () {
        final int schemeChoiceTime = 10 * 1000;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!schemeChosen)
                    schemeChoiceTimeOut();

            }
        }, schemeChoiceTime);
    }

    private synchronized void schemeChoiceTimeOut () {
        schemeChosen = true;
        notifyAll();
    }

    public boolean isSchemeChosen() {
        return schemeChosen;
    }

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

    public synchronized Color selectPrivateAchievement() {
        Color privateAchievement = this.privateAchievements.get(0);
        this.privateAchievements.remove(0);
        return privateAchievement;
    }

    private synchronized void endGame() {

        for ( RemoteView r: remoteViews ) {
            server.removeRemoteView( r );
            Player p = r.getPlayer();
            server.removeNickname( p.getNickname() );
            server.removePlayer( p );
            server.removeServerInterface( p.getServerInterface() );
        }

        serverInterfaces.clear();
        players.clear();
        remoteViews.clear();
        server.deregisterGame( this );

    }

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
            this.controller.startGame();
        }

        else {
            gameFailed();
        }
    }
}
