package it.polimi.ingsw.controller.action;

import it.polimi.ingsw.controller.Round;
import it.polimi.ingsw.controller.Turn;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Dashboard;
import it.polimi.ingsw.model.Die;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.exception.NotValidParametersException;
import it.polimi.ingsw.model.exception.NotValidValueException;
import it.polimi.ingsw.model.exception.OccupiedCellException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ToolSetDieTest {

    @Test
    void toolEffect() throws OccupiedCellException, NotValidParametersException, NotValidValueException {

        Map<String, String> map = new HashMap<>();
        map.put("sergio", "Aurora Sagradis");
        map.put("christian", "Chromatic Splendor");
        GameBoard gameBoard = new GameBoard(map);

        ArrayList<Die> testDraftPool= new ArrayList<>();
        Die die1 =new Die(Color.VIOLET);
        die1.setNumber(1);
        Die die2 =new Die(Color.GREEN);
        die2.setNumber(2);
        Die die3 =new Die(Color.RED);
        die3.setNumber(3);

        testDraftPool.add(die1);
        testDraftPool.add(die2);
        testDraftPool.add(die3);

        gameBoard.setDraftPool(testDraftPool);

        Turn turn = new Turn(gameBoard.getPlayers().get(0), gameBoard, false, new Round(gameBoard.getPlayers(), gameBoard));
        ToolSetDie toolSetDie= new ToolSetDie(true,ToolNames.FLUX_BRUSH);
        Dashboard dashboard =gameBoard.getPlayers().get(0).getDashboard();
        dashboard.setDieOnCell(0,0,new Die(Color.GREEN));
        dashboard.getMatrixScheme()[0][0].getDie().setNumber(2);
        dashboard.setDieOnCell(1,0,new Die(Color.BLUE));
        dashboard.getMatrixScheme()[1][0].getDie().setNumber(4);
        dashboard.setDieOnCell(2,0,new Die(Color.YELLOW));
        dashboard.getMatrixScheme()[2][0].getDie().setNumber(5);
        dashboard.setDieOnCell(1,1,new Die(Color.RED));
        dashboard.getMatrixScheme()[1][1].getDie().setNumber(6);

        PlayerMove playerMove = new PlayerMove("UseTool",ToolNames.FLUX_BRUSH,0);
        System.out.println(gameBoard.getDraftPool().get(0));
        Assertions.assertTrue(toolSetDie.toolEffect(turn,playerMove));
        turn.newMove(new PlayerMove("PlaceDie",0, new int[]{0, 1}));
        Assertions.assertTrue(toolSetDie.toolEffect(turn,playerMove));
        System.out.println(gameBoard.getDraftPool().get(0));
        toolSetDie.placementDie(turn);
        for (int i = 0; i <4 ; i++) {
            for (int j = 0; j <5; j++) {
                System.out.println(dashboard.getMatrixScheme()[i][j]);
            }
        }

        System.out.println("\n\n\n");
        ToolSetDie toolSetDie1= new ToolSetDie(true,ToolNames.GRINDING_STONE);
        Turn turn1 = new Turn(gameBoard.getPlayers().get(0), gameBoard, false, new Round(gameBoard.getPlayers(), gameBoard));
        System.out.println(gameBoard.getDraftPool().get(0));
        PlayerMove playerMove1 = new PlayerMove("UseTool",ToolNames.GRINDING_STONE,0);
        turn.newMove(new PlayerMove("PlaceDie",0, new int[]{0, 1}));
        Assertions.assertTrue(toolSetDie1.toolEffect(turn1,playerMove1));
        System.out.println(gameBoard.getDraftPool().get(0));

        for (int i = 0; i <4 ; i++) {
            for (int j = 0; j <5; j++) {
                System.out.println(dashboard.getMatrixScheme()[i][j]);
            }
        }

    }

    }
