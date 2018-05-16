package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Die;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.exception.NotValidValueException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

class TurnTest {

    @Test
    void turnManager() throws NotValidValueException {
        Map<String, String> map = new HashMap<>();
        map.put("sergio", "Aurora Sagradis");
        map.put("christian", "Chromatic Splendor");
        map.put("marco", "Fulgor del Cielo");
        GameBoard gameBoard = new GameBoard(map);

        Turn turn = new Turn(gameBoard.getPlayers().get(0), gameBoard, false, new Round(gameBoard.getPlayers(), gameBoard));


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                turn.newMove("gothrough");
            }
        }, 2000);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                turn.newMove("setdie", 1, 1, new Die(Color.GREEN));
            }
        }, 500);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                turn.newMove("setdie", 0, 0, new Die(Color.BLUE));
            }
        }, 1000);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                turn.newMove("setdie", 1, 0, new Die(Color.RED));
            }
        }, 1500);
        turn.turnManager();

        System.out.println(gameBoard.getPlayers().get(0).getDashboard().getMatrixScheme()[1][1].toString());
        System.out.println(gameBoard.getPlayers().get(0).getDashboard().getMatrixScheme()[0][0].toString());
        System.out.println(gameBoard.getPlayers().get(0).getDashboard().getMatrixScheme()[1][0].toString());


    }
}