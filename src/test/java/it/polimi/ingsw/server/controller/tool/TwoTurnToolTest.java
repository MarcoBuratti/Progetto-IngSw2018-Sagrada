package it.polimi.ingsw.server.controller.tool;

import it.polimi.ingsw.server.controller.Turn;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.exception.NotValidValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

class TwoTurnToolTest {

    @Test
    void toolEffect() throws NotValidValueException {
        ArrayList<Player> playersList = new ArrayList<>();
        Player player;
        player = new Player ( "christian" , null );
        player.setDashboard("Chromatic_Splendor");
        playersList.add( player );
        player = new Player ( "sergio" , null );
        player.setDashboard("Aurora_Sagradis");
        playersList.add( player );
        GameBoard gameBoard = new GameBoard(playersList);
        ArrayList<Tool> tools = new ArrayList<>();
        ToolNames[] toolList = ToolNames.values();
        ToolFactory abstractToolFactory = new ToolFactory();
        for (ToolNames aToolList : toolList) {
            Tool toolFactory = abstractToolFactory.getTool(aToolList);
            tools.add(toolFactory);
        }
        gameBoard.setTools(tools);

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
        ArrayList<Integer> intParameters;

        Turn turn = new Turn(gameBoard.getPlayers().get(0), gameBoard, false);
        Turn turn1 = new Turn(gameBoard.getPlayers().get(0), gameBoard, true);
        String nickname = gameBoard.getPlayers().get(0).getNickname();
        intParameters = new ArrayList<>();
        intParameters.add(0);
        intParameters.add(0);
        PlayerMove playerMove= new PlayerMove(nickname,"PlaceDie",0, intParameters);
        intParameters = new ArrayList<>();
        intParameters.add(0);
        intParameters.add(1);
        PlayerMove playerMove1= new PlayerMove(nickname,"PlaceDie",0,intParameters);
        PlayerMove playerMove2= new PlayerMove(nickname,"UseTool", 10,0,intParameters);
        TwoTurnTool twoTurnTool=new TwoTurnTool(false,ToolNames.RUNNING_PLIERS);
        turn.tryPlacementMove(playerMove);
        Assertions.assertTrue(turn.isPlacementDone());
        Assertions.assertTrue(twoTurnTool.toolEffect(turn,playerMove2));
        Assertions.assertFalse(twoTurnTool.toolEffect(turn1,playerMove2));
        Assertions.assertFalse(turn.isPlacementDone());
        turn.tryPlacementMove(playerMove1);

        Dashboard dashboard =gameBoard.getPlayers().get(0).getDashboard();
        for (int i = 0; i <4 ; i++) {
            for (int j = 0; j <5; j++) {
                System.out.println(dashboard.getMatrixScheme()[i][j]);
            }
            System.out.println("\n");
        }





    }
}