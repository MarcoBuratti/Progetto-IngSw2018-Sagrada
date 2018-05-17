package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Die;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.exception.NotValidValueException;
import org.junit.jupiter.api.Test;

import java.util.*;

class TurnTest {

    @Test
    void turnManager() throws NotValidValueException {
        Map<String, String> map = new HashMap<>();
        map.put("sergio", "Aurora Sagradis");
        map.put("christian", "Chromatic Splendor");
        map.put("marco", "Fulgor del Cielo");
        GameBoard gameBoard = new GameBoard(map);

        Turn turn = new Turn(gameBoard.getPlayers().get(0), gameBoard, false, new Round(gameBoard.getPlayers(), gameBoard));

        ArrayList<Die> testDraftPool= new ArrayList<Die>();
        Die die1 =new Die(Color.BLUE);
        die1.setNumber(1);
        Die die2 =new Die(Color.GREEN);
        die2.setNumber(2);
        Die die3 =new Die(Color.RED);
        die3.setNumber(3);

        testDraftPool.add(die1);
        testDraftPool.add(die2);
        testDraftPool.add(die3);

        gameBoard.setDraftPool(testDraftPool);
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
                turn.newMove("setdie", 1, 1, die2);
            }
        }, 500);
       timer.schedule(new TimerTask() {
            @Override
            public void run() {
                turn.newMove("setdie", 0, 0, die1);
            }
        }, 1000);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                turn.newMove("setdie", 0, 1, die2);
            }
        }, 1500);
        turn.turnManager();

        System.out.println(gameBoard.getPlayers().get(0).getDashboard().getMatrixScheme()[1][1].toString());
        System.out.println(gameBoard.getPlayers().get(0).getDashboard().getMatrixScheme()[0][0].toString());
        System.out.println(gameBoard.getPlayers().get(0).getDashboard().getMatrixScheme()[1][0].toString());
        System.out.println(gameBoard.getDraftPool());

    }
}