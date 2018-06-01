package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.interfaces.ServerInterface;
import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.SchemeCardsEnum;
import it.polimi.ingsw.server.model.achievement.PrivateAchievement;
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
    private boolean isServerOn;
    private boolean playersConnected;
    private Controller controller;
    private ModelView modelView;
    private ArrayList<SchemeCardsEnum> schemes;
    private ArrayList<Color> privateAchievements;
    private final int lobbyTime = 5 * 1000;
    private ArrayList<RemoteView> remoteViews;
    boolean gameStarted;
    private Timer timer;
    private CliGraphicsServer cliGraphicsServer = new CliGraphicsServer();

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT_NUMBER);
        executor = Executors.newCachedThreadPool();
        serverInterfaces = new ArrayList<>();

        isServerOn = true;
        playersConnected = false;
        nicknames = new ArrayList<>();
        players = new ArrayList<>();
        remoteViews = new ArrayList<>();
        schemes = new ArrayList<>(Arrays.asList(SchemeCardsEnum.values()));
        Collections.shuffle(schemes);
        privateAchievements = new ArrayList<>(Arrays.asList(Color.values()));
        Collections.shuffle(privateAchievements);
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
            System.err.println("Connection error: " + e.getMessage() + "!");
        }


        while (isServerOn) {
            try {
                Socket newSocket = serverSocket.accept();
                SocketConnectionServer connectionServer = new SocketConnectionServer(newSocket, this);
                executor.submit(connectionServer);
            } catch (IOException e) {
                System.out.println("Connection error!");
            }
        }
    }

    private void startGame() {
        this.timer.cancel();
        this.setGameStarted(true);
        this.controller = new Controller(this);
        modelView = new ModelView(controller.getGameBoard());
        for (RemoteView r: remoteViews)
            r.setModelView(modelView);
        this.controller.setRemoteViews(this);
        setPlayersConnected(true);
        cliGraphicsServer.printStart();
        for(ServerInterface s: serverInterfaces)
            s.send("The game has started!");
        this.controller.startGame();
    }

    private synchronized boolean getPlayersConnected() { return this.playersConnected; }

    private synchronized void setPlayersConnected(boolean bool){
        this.playersConnected = bool;
    }

    private void gameStartTimer(){
        this.timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(!getPlayersConnected()) {
                        startGame();
                }
            }
        },this.lobbyTime);
    }

    public ArrayList<SchemeCardsEnum> getSchemes (){
        return this.schemes;
    }

    public ArrayList<Color> getPrivateAchievements () { return this.privateAchievements; }

    public ArrayList<Player> getPlayers() { return players; }

    public ArrayList<RemoteView> getRemoteViews() {
        return remoteViews;
    }

    public synchronized boolean alreadyLoggedIn(ServerInterface newServerInterface) {
        if (nicknames.contains(newServerInterface.getPlayer().getNickname()))
            return true;
        else return false;
    }

    public synchronized boolean isGameStarted() {
        return gameStarted;
    }

    public synchronized void setGameStarted(boolean bool) {
        this.gameStarted = bool;
    }

    public synchronized String selectSchemes () {
        StringBuilder bld = new StringBuilder();
        bld.append( this.schemes.get(0).getFirstScheme() );
        bld.append(",");
        bld.append( this.schemes.get(0).getSecondScheme() );
        bld.append(",");
        this.schemes.remove(0);
        bld.append( this.schemes.get(0).getFirstScheme() );
        bld.append(",");
        bld.append( this.schemes.get(0).getSecondScheme() );
        this.schemes.remove(0);
        return bld.toString();
    }

    public synchronized Color selectPrivateAchievement () {
        Color privateAchievement = this.privateAchievements.get(0);
        this.privateAchievements.remove(0);
        return privateAchievement;
    }

    public synchronized void registerConnection(ServerInterface newServerInterface) {

        if (alreadyLoggedIn(newServerInterface)){
            Player oldPlayer;
            for (Player p: players) {
                if (p.getNickname().equals(newServerInterface.getPlayer().getNickname())) {
                    oldPlayer = p;
                    if(p.getServerInterface() == null) {
                        serverInterfaces.add(newServerInterface);
                        for (RemoteView r : remoteViews) {
                            if (r.getPlayer().getNickname().equals(newServerInterface.getPlayer().getNickname()))
                                r.changeConnection(newServerInterface);
                        }
                        cliGraphicsServer.printLoggedAgain( oldPlayer.getNickname() );
                        newServerInterface.send("You have logged in again as: " + newServerInterface.getPlayer().getNickname());
                    }
                    else {
                        newServerInterface.send("This nickname has been already used! Please try again.");
                        newServerInterface.send("Terminate.");
                    }
                }
            }
        }

        else if (!playersConnected && nicknames.size()<4) {
            serverInterfaces.add(newServerInterface);
            remoteViews.add(new RemoteView(newServerInterface));
            players.add(newServerInterface.getPlayer());
            nicknames.add(newServerInterface.getPlayer().getNickname());
            newServerInterface.send("You have logged in as: " + newServerInterface.getPlayer().getNickname());
            try {
                cliGraphicsServer.printLoggedIn( newServerInterface.getPlayer().getNickname() );
            } catch(Exception e) {
                cliGraphicsServer.printErr();
            }
            if(remoteViews.size()==2) {
                this.gameStartTimer();
            }
            else if( ((remoteViews.size()==4)) ) {
                this.startGame();
            }

        } else{
            newServerInterface.send("Game has already started! Please try again later.");
            newServerInterface.send("Terminate.");
        }
    }

    public synchronized void deregisterConnection(ServerInterface serverInterface) {
        cliGraphicsServer.printDereg( serverInterface.getPlayer().getNickname() );
        for (RemoteView r: remoteViews) {
            if (r.getServerInterface() != null) {
                if (r.getServerInterface().equals(serverInterface))
                    r.removeConnection();
                else r.send(serverInterface.getPlayer().getNickname() + " has disconnected from the server.");
            }
        }

        serverInterfaces.remove(serverInterface);
        serverInterface.getPlayer().removeServerInterface();

        if (serverInterfaces.size() == 1) {
            this.controller.onePlayerLeftEnd();
            serverInterfaces.get(0).send("You win!");
            cliGraphicsServer.printWinner (serverInterfaces.get(0).getPlayer().getNickname());
        }

    }

}
