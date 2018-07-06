package it.polimi.ingsw.server;

import it.polimi.ingsw.server.interfaces.ServerInterface;
import it.polimi.ingsw.server.rmi.RmiController;
import it.polimi.ingsw.server.socket.SocketConnectionServer;
import it.polimi.ingsw.util.CliGraphicsServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server extends UnicastRemoteObject {

    private static final int SOCKET_PORT_NUMBER = 1996;
    private static final int RMI_PORT_NUMBER = 1997;
    private ServerSocket serverSocket;
    private Registry registry;
    private ExecutorService executor;
    private ArrayList<ServerInterface> serverInterfaces;
    private ArrayList<String> nicknames;
    private Map<Game, Integer> games;
    private int gameID = 0;
    private boolean isServerOn;
    private ArrayList<RemoteView> remoteViews;
    private CliGraphicsServer cliGraphicsServer;
    private Lobby currentLobby;

    /**
     * Initialize all the attributes of the Server.
     * Server has the following attributes:
     *      - serverSocket: a ServerSocket Object used to accept the clients' connections
     *      - executor: an ExecutorService Object used to submit the SocketConnectionServer threads
     *      - serverInterfaces: an ArrayList of ServerInterface Objects containing all the ServerInterface Objects of the connected players
     *      - nicknames: an ArrayList containing all of the nicknames of the players who are currently playing or waiting in the lobby
     *      - games: an ArrayList containing all the Game Objects created while the server is on
     *      - gameID: an int representing the first ID that hasn't been already used to identify a Game Object
     *      - isServerOn: a boolean that is set as true when the Server is launched
     *      - remoteViews: an ArrayList containing all the RemoteView Objects of the currently connected players
     *      - cliGraphicsServer: a CliGraphicsServer Object used to print messages on server
     *      - currentLobby: a Lobby Object representing the Lobby where the players are currently waiting for the game to start
     * @throws IOException if it's impossible to initialize the serverSocket attribute
     */
    private Server() throws IOException {
        this.serverSocket = new ServerSocket(SOCKET_PORT_NUMBER);
        this.registry = LocateRegistry.createRegistry(RMI_PORT_NUMBER);
        executor = Executors.newCachedThreadPool();
        serverInterfaces = new ArrayList<>();

        isServerOn = true;
        nicknames = new ArrayList<>();
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

    /**
     *
     */
    private void start() {
        System.out.println("Server is on!");

        try {
            RmiController rmicontroller = new RmiController(this);
            registry.rebind("Server", rmicontroller);
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

    /**
     * Checks whether the player who is trying to log in has already logged in once or not.
     * @param newServerInterface the ServerInterface Object belonging to the player who's trying to connect
     * @return a boolean
     */
    public synchronized boolean alreadyLoggedIn(ServerInterface newServerInterface) {
        return nicknames.contains(newServerInterface.getPlayer().getNickname());
    }

    /**
     * Creates a new Lobby Object and sets is as the currentLobby attribute.
     */
    synchronized void setNewLobby () {
        this.currentLobby = new Lobby ( this );
    }

    /**
     * Tries to register a connection, saving all the references to the player who's trying to connect.
     * @param newServerInterface the ServerInterface Object belonging to the player who's trying to connect
     */
    public synchronized void registerConnection ( ServerInterface newServerInterface ) {

        if (alreadyLoggedIn(newServerInterface)) {
         registerOldPlayer(newServerInterface);
        }

        else {
            registerNewPlayer(newServerInterface);
        }
    }

    /**
     * A private method used by registerConnection. It's used to register a connection belonging to a player
     * who had already logged in before and had disconnected.
     * @param newServerInterface the ServerInterface Object belonging to the player who's trying to connect
     */
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

    /**
     * A private method used by registerConnection. It's used to register a connection belonging to a player
     * who has not logged in before.
     * @param newServerInterface the ServerInterface Object belonging to the player who's trying to connect
     */
    private void registerNewPlayer ( ServerInterface newServerInterface ) {

        serverInterfaces.add( newServerInterface );
        RemoteView remoteView = new RemoteView( newServerInterface );
        remoteViews.add ( remoteView );
        String nickname = newServerInterface.getPlayer().getNickname();
        nicknames.add( nickname );
        newServerInterface.send( "You have logged in as: " + nickname );

        try {
            cliGraphicsServer.printLoggedIn(newServerInterface.getPlayer().getNickname());
        } catch (Exception e) {
            cliGraphicsServer.printErr();
        }

        this.currentLobby.addRemoteView ( remoteView );

    }

    /**
     * If the player was already in game, it communicates to the Game Object that the player has disconnected.
     * Else if the player was waiting in the lobby, it communicates to the currentLobby ( Lobby Object) that the player has disconnected.
     * @param tempRemoteViews a temporary ArrayList used to search the RemoteView having reference to serverInterface
     * @param serverInterface the ServerInterface Object belonging to the player who's disconnected
     */
    private void disconnectPlayer ( ArrayList<RemoteView> tempRemoteViews, ServerInterface serverInterface ) {
        for (RemoteView r : tempRemoteViews) {
            if (r.getServerInterface() != null && r.getServerInterface().equals(serverInterface)) {
                if (r.isGameStarted()) {
                    r.getGame().playerDisconnected(r);
                } else {
                    currentLobby.playerDisconnected(r);
                }
            }
        }
    }

    /**
     * Tries to deregister a connection, deleting all the references to the player who's trying to connect.
     * @param serverInterface the ServerInterface Object belonging to the player who's trying to disconnect
     */
    public synchronized void deregisterConnection ( ServerInterface serverInterface ) {

        if (this.serverInterfaces.contains(serverInterface)) {

            cliGraphicsServer.printDereg(serverInterface.getPlayer().getNickname());
            serverInterfaces.remove(serverInterface);

            ArrayList<RemoteView> tempRemoteViews = new ArrayList<>(remoteViews);
            disconnectPlayer( tempRemoteViews, serverInterface );
        }
    }


    /**
     * Allows the user to remove a RemoteView Object from remoteViews.
     * @param remoteView the RemoteView Object the user wants to remove from remoteViews
     */
    synchronized void removeRemoteView ( RemoteView remoteView ) {
        this.remoteViews.remove( remoteView );
    }

    /**
     * Allows the user to remove a ServerInterface Object from serverInterface.
     * @param serverInterface the ServerInterface Object the user wants to remove from serverInterface
     */
    synchronized void removeServerInterface ( ServerInterface serverInterface ) {
        this.serverInterfaces.remove( serverInterface );
    }

    /**
     * Allows the user to remove a nickname ( String ) from nicknames.
     * @param nickname the nickname the user wants to remove from nicknames
     */
    synchronized void removeNickname ( String nickname ) {
        this.nicknames.remove( nickname );
    }

    /**
     * Allows the user to register a new Game Object when a new game starts.
     * @param game the Game Object the user wants to register
     */
    synchronized void registerGame ( Game game ) {
        games.put( game, gameID );
        System.out.println( "A new game has started! GameID: " + gameID );
        gameID++;
    }

    /**
     * Allows the user to deregister a Game Object when a game ends.
     * @param game the Game Object the user wants to deregister
     */
    synchronized void deregisterGame ( Game game ) {
        System.out.println( "The game having the following ID has ended! Game ID: " + games.get( game ));
        games.remove( game );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals (Object object) {
        if ( object != null && object.getClass() == this.getClass() ) {

            Server server = (Server) object;

            return ( this.serverInterfaces.equals(server.serverInterfaces) && this.nicknames.equals(server.nicknames) && this.games.equals(server.games)
                    && this.remoteViews.equals(server.remoteViews) && currentLobby.equals(server.currentLobby) && gameID == server.gameID );
        }
        else return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override //Please note this method is not used, but it's always recommended to override hashCode if equals is overridden
    public int hashCode() {
        return super.hashCode();
    }

}
