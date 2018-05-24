package it.polimi.ingsw.controller.tool;

import it.polimi.ingsw.controller.Round;
import it.polimi.ingsw.controller.Turn;
import it.polimi.ingsw.controller.action.PlayerMove;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Die;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.exception.NotValidValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RerollToolTest {

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
        System.out.println(gameBoard.getDraftPool());

        RerollTool rerollTool =new RerollTool(false,ToolNames.GLAZING_HAMMER);
        Turn turn = new Turn(gameBoard.getPlayers().get(0), gameBoard, false, new Round(gameBoard.getPlayers(), gameBoard));
        PlayerMove playerMove = new PlayerMove("UseTool",ToolNames.GLAZING_HAMMER);
        Assertions.assertFalse(rerollTool.toolEffect(turn,playerMove));
        Turn turn1 = new Turn(gameBoard.getPlayers().get(0), gameBoard, true, new Round(gameBoard.getPlayers(), gameBoard));
        Assertions.assertTrue(rerollTool.toolEffect(turn1,playerMove));
        System.out.println(gameBoard.getDraftPool());

    }
}