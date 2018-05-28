package it.polimi.ingsw.server.controller.tool;

import it.polimi.ingsw.server.controller.Round;
import it.polimi.ingsw.server.controller.Turn;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.controller.tool.SpecialPlacementTool;
import it.polimi.ingsw.server.controller.tool.ToolNames;
import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.Die;
import it.polimi.ingsw.server.model.GameBoard;
import it.polimi.ingsw.server.model.exception.NotValidValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class SpecialPlacementToolTest {

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
        PlayerMove playerMove = new PlayerMove(nickname,"UseTool",ToolNames.CORK_BAKED_STRAIGHTEDGE,0,new int[]{2,3});
        Assertions.assertTrue(specialPlacementTool.toolEffect(turn,playerMove));
        PlayerMove playerMove1 = new PlayerMove(nickname,"UseTool",ToolNames.CORK_BAKED_STRAIGHTEDGE,0,new int[]{1,2});
        Assertions.assertTrue(specialPlacementTool.toolEffect(turn1,playerMove1));

    }
}