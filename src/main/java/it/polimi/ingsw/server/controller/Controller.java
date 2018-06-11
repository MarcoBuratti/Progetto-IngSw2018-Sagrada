package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.Game;
import it.polimi.ingsw.server.RemoteView;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.controller.tool.Tool;
import it.polimi.ingsw.server.interfaces.ServerInterface;
import it.polimi.ingsw.server.model.GameBoard;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.achievement.CardAchievement;
import it.polimi.ingsw.server.model.exception.NotEnoughDiceLeftException;
import javafx.util.Pair;

import java.util.*;

public class Controller extends Observable implements Observer {
    private static final int NUMBER_OF_ROUNDS = 1;
    private static final String PLACE_DIE = "PlaceDie";
    private static final String USE_TOOL = "UseTool";
    private static final String GO_THROUGH = "GoThrough";
    private Round currentRound;
    private GameBoard gameBoard;
    private ArrayList<Player> players;
    private boolean gameEnded;
    private boolean onePlayerLeft = false;

    public Controller(Game game) {
        gameBoard = new GameBoard(game.getPlayers());
        players = gameBoard.getPlayers();
    }

    public void setRemoteViews(Game game) {
        ArrayList<RemoteView> remoteViews = new ArrayList<>(game.getRemoteViews());
        for (RemoteView r : remoteViews)
            r.addObserver(this);
    }

    public String startGame() {

        for (int i = 0; i < NUMBER_OF_ROUNDS && !onePlayerLeft ; i++) {
            currentRound = new Round(players, gameBoard);
            this.addObserver(currentRound);
            try {
                currentRound.initializeDraftPool();
            } catch (NotEnoughDiceLeftException e) {
                e.printStackTrace();
            }
            currentRound.roundManager();
            if(!onePlayerLeft)
                 currentRound.endRound();
            Collections.rotate(players, players.size()-1);
        }

        ArrayList<Pair<Player, Integer>> finalScores = this.calculateFinalScores();
        Player winner = finalScores.get(0).getKey();
        if (!onePlayerLeft) {
            //TODO Pensa a modificarlo con for con indice
            for (Pair p : finalScores) {
                Player player = (Player) p.getKey();
                ServerInterface playerConnection = player.getServerInterface();
                if (playerConnection != null) {
                    if (player.getNickname().equals(winner.getNickname()))
                        playerConnection.send("You win!");
                    else
                        playerConnection.send("You lose!");

                    for (Pair pair : finalScores) {
                        Player otherPlayer = (Player) pair.getKey();
                        playerConnection.send("Player: " + otherPlayer.getNickname() + " , Score: " + pair.getValue());
                    }
                }
            }
            setGameEnded(true);
        }
        return winner.getNickname();
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    private ArrayList<Pair<Player, Integer>> calculateFinalScores() {

        ArrayList<Pair<Player, Integer>> finalScores = new ArrayList<>();
        Collections.rotate(players, 1);

        for (Player p : this.players) {

            Pair<Player, Integer> pair = new Pair<>(p, calculateFinalScorePlayer(p));
            boolean added = false;

            for (int i = 0; i < finalScores.size() && !added; i++) {
                if (compare(finalScores.get(i), pair)) {
                    finalScores.add(i, pair);
                    added = true;
                }
            }

            if (!added) {
                finalScores.add(pair);
            }
        }

        return finalScores;
    }

    private boolean compare(Pair<Player,Integer> pair1,Pair<Player,Integer> pair2){

        Player p1 = pair1.getKey();
        Player p2 = pair2.getKey();
        if (pair1.getValue() < pair2.getValue())
            return true;
        else if (pair1.getValue().equals(pair2.getValue()) &&
                p1.getPrivateAchievement().scoreEffect(p1.getDashboard()) < p2.getPrivateAchievement().scoreEffect(p2.getDashboard()))
                return true;
        else if (p1.getPrivateAchievement().scoreEffect(p1.getDashboard()) == p2.getPrivateAchievement().scoreEffect(p2.getDashboard()) &&
            p1.getCurrentFavourToken() < p2.getCurrentFavourToken())
                return true;
        else return p1.getCurrentFavourToken() == p2.getCurrentFavourToken() &&
                    this.players.indexOf(p1) > this.players.indexOf(p2);


    }

    private int calculateFinalScorePlayer(Player player) {
        int score = 0;
        score += player.getPrivateAchievement().scoreEffect(player.getDashboard());
        for (CardAchievement c : gameBoard.getPublicAchievements()) {
            score += c.scoreEffect(player.getDashboard());

        }
        score += player.getCurrentFavourToken();
        score -= player.getDashboard().emptyCells();
        return score;
    }

    public synchronized void onePlayerLeftEnd () {
        this.onePlayerLeft = true;
        if(this.currentRound != null)
            this.currentRound.onePlayerLeftEnd();
    }

    private void moveHandler (PlayerMove playerMove, Observable o) {
        Optional<Integer> dieIndex = playerMove.getIndexDie();
        if (dieIndex.isPresent()) {
            Integer dieIndexValue = dieIndex.get();
            if (dieIndexValue < gameBoard.getDraftPool().size()) {
                setChanged();
                notifyObservers(playerMove);
            } else {
                RemoteView remoteView = (RemoteView) o;
                remoteView.incorrectMove();
            }
        } else {
            RemoteView remoteView = (RemoteView) o;
            remoteView.incorrectMove();
        }
    }

    private void toolMoveHandler (PlayerMove playerMove, Observable o) {
        int toolCost;
        Optional<Integer> toolIndex = playerMove.getExtractedToolIndex();
        if (toolIndex.isPresent()) {
            Integer toolIndexValue = toolIndex.get();
            if (gameBoard.getTools().get(toolIndexValue).isAlreadyUsed())
                toolCost = 2;
            else toolCost = 1;

            if (currentRound.getCurrentPlayer().getCurrentFavourToken() >= toolCost) {
                moveHandler(playerMove, o);
            }
        } else {
            RemoteView remoteView = (RemoteView) o;
            remoteView.incorrectMove();
        }
    }

    @Override
    public void update(Observable o, Object arg) {

        PlayerMove playerMove = (PlayerMove) arg;

        if ( playerMove.getPlayerNickname().equals(currentRound.getCurrentPlayer().getNickname()) ) {


            switch (playerMove.getMoveType()) {
                case PLACE_DIE:
                    moveHandler(playerMove, o);
                    break;

                case USE_TOOL:
                    toolMoveHandler(playerMove, o);
                    break;

                case GO_THROUGH:
                default:
                    setChanged();
                    notifyObservers(playerMove);
                    break;
            }
        }
        else {
            RemoteView remoteView = (RemoteView) o;
            remoteView.notYourTurn();
        }
    }

    public synchronized boolean isGameEnded() {
        return gameEnded;
    }

    private synchronized void setGameEnded(boolean gameEnded) {
        this.gameEnded = gameEnded;
    }
}
