package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.interfaces.ServerInterface;
import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.SchemeCardsEnum;
import it.polimi.ingsw.server.rmi.RmiController;
import it.polimi.ingsw.server.socket.SocketConnectionServer;

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
            LocateRegistry.createRegistry(1099);//Creo un registy sulla porta 1099 (quella di default).
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
                Socket newSocket = serverSocket.accept();                //attende nuovi utenti
                SocketConnectionServer connectionServer = new SocketConnectionServer(newSocket, this); //Crea un oggetto connessione
                executor.submit(connectionServer);//Crea un thread per la singola connessione
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
        for (ServerInterface s: serverInterfaces)
            remoteViews.add(new RemoteView(s, modelView));
        this.controller.setRemoteViews(this);
        setPlayersConnected(true);
        System.out.println("Game started!");
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

    public ArrayList<Player> getPlayers() {return players;}

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
                        System.out.println(oldPlayer.getNickname() + " has logged in again.");
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
            timer.cancel();
            try {
                players.add(newServerInterface.getPlayer());
                nicknames.add(newServerInterface.getPlayer().getNickname());
                newServerInterface.send("You have logged in as: " + newServerInterface.getPlayer().getNickname());
                System.out.println(newServerInterface.getPlayer().getNickname() + " has logged in.");
            } catch(Exception e) {
                System.out.println("Client Connection Error!");
            }

            if(serverInterfaces.size()>=2) {
                this.gameStartTimer();
            }

        } else{
            newServerInterface.send("Game has already started! Please try again later.");
            newServerInterface.send("Terminate.");
        }
    }


    public synchronized void deregisterConnection(ServerInterface serverInterface) {
        serverInterfaces.remove(serverInterface);
        System.out.println(serverInterface.getPlayer().getNickname() + " has disconnected from the server.");
        serverInterface.getPlayer().removeServerInterface();
    }
}
