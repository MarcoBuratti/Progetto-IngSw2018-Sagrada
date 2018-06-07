package it.polimi.ingsw.server.controller.tool;

import it.polimi.ingsw.server.controller.Turn;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.Die;
import it.polimi.ingsw.server.model.GameBoard;
import it.polimi.ingsw.server.model.exception.NotValidValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

class SpecialPlacementToolTest {

    @Test
    void toolEffect() throws NotValidValueException {
        Map<String, String> map = new HashMap<>();
        map.put("sergio", "Aurora_Sagradis");
        map.put("christian", "Chromatic_Splendor");
        GameBoard gameBoard = new GameBoard(map);
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
        die2.setNumber(5);
        Die die3 =new Die(Color.RED);
        die3.setNumber(3);

        testDraftPool.add(die1);
        testDraftPool.add(die2);
        testDraftPool.add(die3);

        gameBoard.setDraftPool(testDraftPool);
        Turn turn = new Turn(gameBoard.getPlayers().get(0), gameBoard, false);
        Turn turn1 = new Turn(gameBoard.getPlayers().get(0), gameBoard, false);
        String nickname = gameBoard.getPlayers().get(0).getNickname();
        SpecialPlacementTool specialPlacementTool= new SpecialPlacementTool(true,true,false, ToolNames.CORK_BAKED_STRAIGHTEDGE);
        ArrayList<Integer> intParameters = new ArrayList<>();
        intParameters.add(2);
        intParameters.add(3);
        PlayerMove playerMove = new PlayerMove(nickname,"UseTool",1,0,intParameters);
        Assertions.assertTrue(specialPlacementTool.toolEffect(turn,playerMove));
        intParameters = new ArrayList<>();
        intParameters.add(1);
        intParameters.add(2);
        PlayerMove playerMove1 = new PlayerMove(nickname,"UseTool",1,0,intParameters);
        Assertions.assertTrue(specialPlacementTool.toolEffect(turn1,playerMove1));

    }
}