package it.polimi.ingsw.server.controller.tool;

import it.polimi.ingsw.server.controller.Round;
import it.polimi.ingsw.server.controller.Turn;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.controller.tool.ToolNames;
import it.polimi.ingsw.server.controller.tool.TwoTurnTool;
import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.Dashboard;
import it.polimi.ingsw.server.model.Die;
import it.polimi.ingsw.server.model.GameBoard;
import it.polimi.ingsw.server.model.exception.NotValidValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class TwoTurnToolTest {

    @Test
    void toolEffect() throws NotValidValueException {
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

        PlayerMove playerMove= new PlayerMove("PlaceDie",0,new int[]{0,0});
        PlayerMove playerMove1= new PlayerMove("PlaceDie",0,new int[]{0,1});
        PlayerMove playerMove2= new PlayerMove("UseTool", ToolNames.RUNNING_PLIERS,0,new int[]{0,1});
        TwoTurnTool twoTurnTool=new TwoTurnTool(false,ToolNames.RUNNING_PLIERS);
        Turn turn = new Turn(gameBoard.getPlayers().get(0), gameBoard, false);
        Turn turn1 = new Turn(gameBoard.getPlayers().get(0), gameBoard, true);
        turn.setMove(playerMove);
        Assertions.assertTrue(turn.isPlacementDone());
        Assertions.assertTrue(twoTurnTool.toolEffect(turn,playerMove2));
        Assertions.assertFalse(twoTurnTool.toolEffect(turn1,playerMove2));
        Assertions.assertFalse(turn.isPlacementDone());
        turn.setMove(playerMove1);

        Dashboard dashboard =gameBoard.getPlayers().get(0).getDashboard();
        for (int i = 0; i <4 ; i++) {
            for (int j = 0; j <5; j++) {
                System.out.println(dashboard.getMatrixScheme()[i][j]);
            }
            System.out.println("\n");
        }





    }
}