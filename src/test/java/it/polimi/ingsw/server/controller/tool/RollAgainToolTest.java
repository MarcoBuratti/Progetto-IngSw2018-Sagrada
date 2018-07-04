package it.polimi.ingsw.server.controller.tool;

import it.polimi.ingsw.server.controller.Turn;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.exception.NotValidValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class RollAgainToolTest {

    @Test
    void toolEffect() throws NotValidValueException {
        ArrayList<Player> playersList = new ArrayList<>();
        Player player = new Player ( "sergio");
        player.setDashboard("Aurora_Sagradis");
        playersList.add( player );
        player = new Player ( "christian");
        player.setDashboard("Chromatic_Splendor");
        playersList.add( player );
        FakeGameBoard gameBoard = new FakeGameBoard(playersList);
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
        System.out.println(gameBoard.getDraftPool());

        RollAgainTool rollAgainTool =new RollAgainTool(false, ToolNames.GLAZING_HAMMER);
        Turn turn = new Turn(null, gameBoard.getPlayers().get(0), gameBoard, false);
        String nickname = gameBoard.getPlayers().get(0).getNickname();
        PlayerMove playerMove = new PlayerMove(nickname,"UseTool", 5);
        Assertions.assertFalse(rollAgainTool.toolEffect(turn,playerMove));
        Turn turn1 = new Turn(null, gameBoard.getPlayers().get(0), gameBoard, true);
        Assertions.assertTrue(rollAgainTool.toolEffect(turn1,playerMove));
        System.out.println(gameBoard.getDraftPool());

    }
}