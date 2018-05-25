package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.interfaces_and_abstract_classes.ServerAbstractClass;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.SchemeCardsEnum;
import it.polimi.ingsw.server.model.exception.NotValidValueException;
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
    private List<ServerAbstractClass> serverAbstractClasses;
    private ArrayList<String> nicknames;
    private ArrayList<Player> players;
    private boolean isServerOn;
    private boolean playersConnected;
    private boolean gameStarted = false;
    private Controller controller;
    private ModelView modelView;
    private ArrayList<SchemeCardsEnum> schemes;
    private final int lobbyTime = 5 * 1000;
    private ArrayList<RemoteView> remoteViews;
    private Timer timer;

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT_NUMBER);
        executor = Executors.newCachedThreadPool();
        serverAbstractClasses = new ArrayList<>();

        isServerOn = true;
        playersConnected = false;
        nicknames = new ArrayList<>();
        players = new ArrayList<>();
        remoteViews = new ArrayList<>();
        schemes = new ArrayList<>(Arrays.asList(SchemeCardsEnum.values()));
        Collections.shuffle(schemes);
    }

    public static void main(String[] args) {

        try {
            new Server().start();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server initialization failed");
        }
    }

    public void start() {
        try {
            LocateRegistry.createRegistry(1099);//Creo un registy sulla porta 1099 (quella di default).
        } catch (RemoteException e) {
            System.out.println("The registry has already been created!");
        }
        try {
            RmiController rmicontroller = new RmiController(this);
            Naming.rebind("Server", rmicontroller);

        } catch (MalformedURLException e) {
            System.err.println("The selected object cannot be registered!");
        } catch (RemoteException e) {
            System.err.println("Connection error: " + e.getMessage() + "!");
        }


        while (isServerOn) {
            try {
                Socket newSocket = serverSocket.accept();                //attende nuovi utenti
                SocketConnectionServer connectionServer = new SocketConnectionServer(newSocket, this); //Crea un oggetto connessione
                executor.submit(connectionServer);//Crea un thread per la singola connessione
                if (playersConnected && !gameStarted) {
                    this.timer.cancel();
                    this.controller = new Controller(this);
                    modelView = new ModelView(controller.getGameBoard());
                    for (ServerAbstractClass s: serverAbstractClasses)
                        remoteViews.add(new RemoteView(s, modelView));
                    setGameStarted(true);
                    System.out.println("Game started!");
                }

            } catch (IOException e) {
                System.out.println("Connection error!");
            }
        }
    }

    private synchronized void setGameStarted(boolean bool) {
        this.gameStarted = bool;
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
                if(!getPlayersConnected())
                    setPlayersConnected(true);
            }
        },this.lobbyTime);
    }

    public ArrayList<SchemeCardsEnum> getSchemes (){
        return this.schemes;
    }

    public ArrayList<String> getNicknames() {
        return nicknames;
    }

    public ArrayList<Player> getPlayers() {return players;}

    public ArrayList<RemoteView> getRemoteViews() {
        return remoteViews;
    }

    public synchronized void registerConnection(ServerAbstractClass newServerAbstractClass) {

        if (nicknames.contains(newServerAbstractClass.getPlayer().getNickname())){
            Player oldPlayer;
            for (Player p: players) {
                if (p.getNickname().equals(newServerAbstractClass.getPlayer().getNickname())) {
                    oldPlayer = p;
                    if(p.getServerAbstractClass() == null) {
                        newServerAbstractClass.setPlayer(oldPlayer);
                        oldPlayer.setServerAbstractClass(newServerAbstractClass);
                        serverAbstractClasses.add(newServerAbstractClass);
                        if (oldPlayer.getDashboard() == null) {
                            try {
                                String chosenScheme = newServerAbstractClass.askForChosenScheme();
                                newServerAbstractClass.getPlayer().setDashboard(chosenScheme);
                            } catch (IOException | NotValidValueException e) {
                                System.err.println(e.toString());
                            }
                        }
                        for (RemoteView r : remoteViews) {
                            if (r.getPlayer().equals(newServerAbstractClass.getPlayer()))
                                r.ChangeConnection(newServerAbstractClass);
                        }
                        System.out.println(oldPlayer.getNickname() + " has logged in again.");
                        newServerAbstractClass.send("You have logged in as: " + newServerAbstractClass.getPlayer().getNickname());
                    }
                    else {
                        newServerAbstractClass.send("This nickname has been already used! Please try again.");
                        newServerAbstractClass.send("Terminate.");
                    }

                }
            }

        }
        else if (!playersConnected && nicknames.size()<4) {
            serverAbstractClasses.add(newServerAbstractClass);
            try {
                String chosenScheme = newServerAbstractClass.askForChosenScheme();
                newServerAbstractClass.getPlayer().setDashboard(chosenScheme);
                players.add(newServerAbstractClass.getPlayer());
                nicknames.add(newServerAbstractClass.getPlayer().getNickname());
                System.out.println(newServerAbstractClass.getPlayer().getNickname() + " has logged in.");
            } catch(Exception e) {
                System.out.println("Client Connection Error!");
            }
            if(!playersConnected && serverAbstractClasses.size()==2) {
                this.gameStartTimer();
            }
            if( (!playersConnected && (serverAbstractClasses.size()==4)) )
                this.setPlayersConnected(true);
        } else{
            newServerAbstractClass.send("Game has already started! Please try again later.");
            newServerAbstractClass.send("Terminate.");
        }
    }


    public synchronized void deregisterConnection(ServerAbstractClass serverAbstractClass) {
        serverAbstractClasses.remove(serverAbstractClass);
        System.out.println(serverAbstractClass.getPlayer().getNickname() + " has disconnected from the server.");
        serverAbstractClass.getPlayer().removeServerAbstractClass();
    }
}
