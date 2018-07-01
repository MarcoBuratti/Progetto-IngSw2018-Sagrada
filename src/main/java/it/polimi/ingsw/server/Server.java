package it.polimi.ingsw.server;

import it.polimi.ingsw.server.interfaces.ServerInterface;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.rmi.RmiController;
import it.polimi.ingsw.server.socket.SocketConnectionServer;
import it.polimi.ingsw.util.CliGraphicsServer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server extends UnicastRemoteObject {

    private static final int PORT_NUMBER = 1996;
    private ServerSocket serverSocket;
    private ExecutorService executor;
    private ArrayList<ServerInterface> serverInterfaces;
    private ArrayList<String> nicknames;
    private ArrayList<Player> players;
    private Map<Game, Integer> games;
    private int gameID = 0;
    private boolean isServerOn;
    private ArrayList<RemoteView> remoteViews;
    private CliGraphicsServer cliGraphicsServer;
    private Lobby currentLobby;

    private Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT_NUMBER);
        executor = Executors.newCachedThreadPool();
        serverInterfaces = new ArrayList<>();

        isServerOn = true;
        nicknames = new ArrayList<>();
        players = new ArrayList<>();
        remoteViews = new ArrayList<>();
        cliGraphicsServer = new CliGraphicsServer();
        currentLobby = new Lobby( this );
        games = new HashMap<>();
    }

    public static void main(String[] args) {

        try {
            new Server().start();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server initialization failed");
        }
    }

    private void start() {
        System.out.println("Server is on!");

        try {
            LocateRegistry.createRegistry(1099);
        } catch (RemoteException e) {
            System.out.println("The registry has already been created!");
        }
        try {
            RmiController rmicontroller = new RmiController(this);
            Naming.rebind("Server", rmicontroller);

        } catch (MalformedURLException e) {
            System.err.println("The selected object cannot be read!");
        } catch (RemoteException e) {
            System.err.println("ConnectionClient error: " + e.getMessage() + "!");
        }


        while (isServerOn) {
            try {
                Socket newSocket = serverSocket.accept();
                SocketConnectionServer connectionServer = new SocketConnectionServer(newSocket, this);
                executor.submit(connectionServer);
            } catch (IOException e) {
                System.out.println("ConnectionClient error!");
            }
        }
    }

    public synchronized boolean alreadyLoggedIn(ServerInterface newServerInterface) {
        return nicknames.contains(newServerInterface.getPlayer().getNickname());
    }

    synchronized void setNewLobby () {
        this.currentLobby = new Lobby ( this );
    }

    public synchronized void registerConnection ( ServerInterface newServerInterface ) {

        if (alreadyLoggedIn(newServerInterface)) {
         registerOldPlayer(newServerInterface);
        }

        else {
            registerNewPlayer(newServerInterface);
        }
    }

    private void registerOldPlayer ( ServerInterface newServerInterface ) {

        for ( RemoteView r: remoteViews ) {

            if ( r.getPlayer().getNickname().equals( newServerInterface.getPlayer().getNickname() ) ) {

                if ( r.getServerInterface() == null ) {
                    serverInterfaces.add( newServerInterface );
                    r.changeConnection(newServerInterface);
                    cliGraphicsServer.printLoggedAgain(r.getPlayer().getNickname());
                    newServerInterface.send("You have logged in again as: " + newServerInterface.getPlayer().getNickname());
                    if ( r.isGameStarted() )
                        r.getGame().playerReconnected( r );
                    else
                        currentLobby.playerReconnected( r );
                }

                else {
                    newServerInterface.send("This nickname has been already used! Please try again.");
                    newServerInterface.send("Terminate.");
                }

            }
        }
    }


    private void registerNewPlayer ( ServerInterface newServerInterface ) {

        serverInterfaces.add( newServerInterface );
        RemoteView remoteView = new RemoteView( newServerInterface );
        remoteViews.add ( remoteView );
        players.add( newServerInterface.getPlayer() );
        String nickname = newServerInterface.getPlayer().getNickname();
        nicknames.add( nickname );
        newServerInterface.send( "You have logged in as: " + nickname );

        try {
            cliGraphicsServer.printLoggedIn(newServerInterface.getPlayer().getNickname());
        } catch (Exception e) {
            System.out.println("sono in register new player");
            cliGraphicsServer.printErr();
        }

        this.currentLobby.addRemoteView ( remoteView );

    }

    private void disconnectPlayer ( ArrayList<RemoteView> tempRemoteViews, ServerInterface serverInterface ) {
        for (RemoteView r : tempRemoteViews) {
            if (r.getServerInterface() != null) {
                if (r.getServerInterface().equals(serverInterface)) {
                    if (r.isGameStarted()) {
                        r.getGame().playerDisconnected(r);
                    } else {
                        currentLobby.playerDisconnected(r);
                    }
                }
            }
        }
    }

    public synchronized void deregisterConnection ( ServerInterface serverInterface ) {

        if (this.serverInterfaces.contains(serverInterface)) {

            cliGraphicsServer.printDereg(serverInterface.getPlayer().getNickname());
            serverInterfaces.remove(serverInterface);

            ArrayList<RemoteView> tempRemoteViews = new ArrayList<>(remoteViews);
            disconnectPlayer( tempRemoteViews, serverInterface );
        }
    }


    synchronized void removeRemoteView ( RemoteView remoteView ) {
        this.remoteViews.remove( remoteView );
    }

    synchronized void removeServerInterface ( ServerInterface serverInterface ) {
        this.serverInterfaces.remove( serverInterface );
    }

    synchronized void removePlayer ( Player player ) {
        this.players.remove( player );
    }

    synchronized void removeNickname ( String nickname ) {
        this.nicknames.remove( nickname );
    }

    synchronized void registerGame ( Game game ) {
        games.put( game, gameID );
        System.out.println( "A new game has started! GameID: " + gameID );
        gameID++;
    }

    synchronized void deregisterGame ( Game game ) {
        System.out.println( "The game having the following ID has ended! Game ID: " + games.get( game ));
        games.remove( game );
    }

}
