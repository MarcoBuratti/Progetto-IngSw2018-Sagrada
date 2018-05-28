package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.RemoteView;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.model.GameBoard;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.achievement.CardAchievement;
import it.polimi.ingsw.server.model.exception.NotEnoughDiceLeftException;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;

public class Controller extends Observable implements Observer {
    private static final int NUMBER_OF_ROUNDS = 10;
    private Round currentRound;
    private GameBoard gameBoard;
    private ArrayList<RemoteView> remoteViews;
    private ArrayList<Player> players;
    private ArrayList<Pair<Player, Integer>> finalscore;

    public Controller(Server server) {
        gameBoard = new GameBoard(server.getPlayers());
        players = gameBoard.getPlayers();
    }

    public void setRemoteViews(Server server) {
        remoteViews = new ArrayList<>(server.getRemoteViews());
        for (RemoteView r : remoteViews)
            r.addObserver(this);
    }

    public void startGame() {
        for (int i = 0; i < NUMBER_OF_ROUNDS; i++) {
            currentRound = new Round(players, gameBoard);
            this.addObserver(currentRound);
            try {
                currentRound.initializeDraftPool();
            } catch (NotEnoughDiceLeftException e) {
                e.printStackTrace();
            }
            currentRound.roundManager();
            currentRound.endRound();
            Collections.rotate(players, 1);
        }
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    @Override
    public void update(Observable o, Object arg) {
        PlayerMove playerMove = (PlayerMove) arg;
        setChanged();
        notifyObservers(playerMove);
    }

    public void calculateFinalScores() {

        finalscore = new ArrayList<>();
        Collections.rotate(players, players.size()-1);

        for (Player p : this.players) {
            Pair<Player, Integer> pair = new Pair<>(p, calculateFinalScorePlayer(p));
            boolean added = false;
            for (int i = 0; i < finalscore.size() && !added; i++) {
                Player p1 = finalscore.get(i).getKey();
                Player p2 = pair.getKey();
                if (finalscore.get(i).getValue() < pair.getValue()) {
                    finalscore.add(i, pair);
                    added = true;
                } else if (finalscore.get(i).getValue() == pair.getValue())
                    if (p1.getPrivateAchievement().scoreEffect(p1.getDashboard()) < p2.getPrivateAchievement().scoreEffect(p2.getDashboard())) {
                        finalscore.add(i, pair);
                        added = true;
                    } else if (p1.getPrivateAchievement().scoreEffect(p1.getDashboard()) == p2.getPrivateAchievement().scoreEffect(p2.getDashboard()))
                        if (p1.getCurrentFavourToken() < p2.getCurrentFavourToken()) {
                            finalscore.add(i, pair);
                            added = true;
                        } else if (p1.getCurrentFavourToken() == p2.getCurrentFavourToken())
                            if (this.players.indexOf(p1) > this.players.indexOf(p2)) {
                                finalscore.add(i, pair);
                                added = true;
                            }

                if (!added) {
                    finalscore.add(pair);
                }

            }


        }


    }

    public int calculateFinalScorePlayer(Player player) {
        int score = 0;
        score += player.getPrivateAchievement().scoreEffect(player.getDashboard());
        for (CardAchievement c : gameBoard.getPublicAchievements()) {
            score += c.scoreEffect(player.getDashboard());

        }
        score += player.getCurrentFavourToken();
        score -= player.getDashboard().emptyCells();
        return score;

    }
}
