package it.polimi.ingsw.controller.action;

import it.polimi.ingsw.controller.Round;
import it.polimi.ingsw.controller.Turn;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Dashboard;
import it.polimi.ingsw.model.Die;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.exception.NotValidParametersException;
import it.polimi.ingsw.model.exception.NotValidRoundException;
import it.polimi.ingsw.model.exception.NotValidValueException;
import it.polimi.ingsw.model.exception.OccupiedCellException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ToolReplaceDieTest {

    @Test
    void toolEffect() throws NotValidValueException, OccupiedCellException, NotValidParametersException, NotValidRoundException {
        Map<String, String> map = new HashMap<>();
        map.put("sergio", "Aurora Sagradis");
        map.put("christian", "Chromatic Splendor");
        GameBoard gameBoard = new GameBoard(map);

        Turn turn = new Turn(gameBoard.getPlayers().get(0), gameBoard, false, new Round(gameBoard.getPlayers(), gameBoard));
        ToolReplaceDie toolReplaceDie= new ToolReplaceDie(false, true, false,false);
        Dashboard dashboard =gameBoard.getPlayers().get(0).getDashboard();
        dashboard.setDieOnCell(0,0,new Die(Color.GREEN));
        dashboard.getMatrixScheme()[0][0].getDie().setNumber(2);
        dashboard.setDieOnCell(1,0,new Die(Color.BLUE));
        dashboard.getMatrixScheme()[1][0].getDie().setNumber(4);
        dashboard.setDieOnCell(2,0,new Die(Color.YELLOW));
        dashboard.getMatrixScheme()[2][0].getDie().setNumber(5);
        dashboard.setDieOnCell(1,1,new Die(Color.RED));
        dashboard.getMatrixScheme()[1][1].getDie().setNumber(6);

        PlayerMove playerMove = new PlayerMove("UseTool",new int[]{0,0,1,2});
        Assertions.assertTrue(toolReplaceDie.toolEffect(turn,playerMove));

        ToolReplaceDie toolReplaceDie1= new ToolReplaceDie(false, false, true,false);
        Die d1, d2;
        d1 = new Die(Color.BLUE);
        d2 = new Die(Color.RED);
        ArrayList<Die> diceList = new ArrayList<>();
        diceList.add(d1);
        diceList.add(d2);
        gameBoard.getRoundTrack().setDiceList(diceList, 1);

        PlayerMove playerMove1 = new PlayerMove("UseTool",new int[]{1,0,1,3});
        //Assertions.assertTrue(toolReplaceDie1.specialCheck(1,3,dashboard.getMatrixScheme()[1][0].getDie(),dashboard.getMatrixScheme()));
        Assertions.assertTrue(toolReplaceDie1.toolEffect(turn,playerMove1));
        PlayerMove playerMove2 = new PlayerMove("UseTool",new int[]{2,0,2,1});
        Assertions.assertFalse(toolReplaceDie1.toolEffect(turn,playerMove2));

        ToolReplaceDie toolReplaceDie2= new ToolReplaceDie(true, true, false,false);
        dashboard.setDieOnCell(0,0,new Die(Color.YELLOW));
        dashboard.getMatrixScheme()[0][0].getDie().setNumber(1);
        dashboard.setDieOnCell(3,0,new Die(Color.RED));
        dashboard.getMatrixScheme()[3][0].getDie().setNumber(6);
        PlayerMove playerMove3 = new PlayerMove("UseTool",new int[]{0,0,1,4,3,0,2,4});
        Assertions.assertTrue(toolReplaceDie2.toolEffect(turn,playerMove3));
        for (int i = 0; i <4 ; i++) {
            for (int j = 0; j <5; j++) {
                System.out.println(dashboard.getMatrixScheme()[i][j]);
            }
        }
    }
}