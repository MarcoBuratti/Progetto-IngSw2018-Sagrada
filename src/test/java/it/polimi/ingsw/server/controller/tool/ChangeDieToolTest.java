package it.polimi.ingsw.server.controller.tool;

import it.polimi.ingsw.server.controller.Round;
import it.polimi.ingsw.server.controller.Turn;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.Die;
import it.polimi.ingsw.server.model.GameBoard;
import it.polimi.ingsw.server.model.RoundTrack;
import it.polimi.ingsw.server.model.exception.NotValidRoundException;
import it.polimi.ingsw.server.model.exception.NotValidValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class ChangeDieToolTest {

    @Test
    void toolEffect() throws NotValidValueException, NotValidRoundException {
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


        Die d1 = new Die(Color.BLUE);
        d1.setNumber(4);
        Die d2 = new Die(Color.RED);
        d2.setNumber(2);
        ArrayList<Die> diceList = new ArrayList<>();
        diceList.add(d1);
        diceList.add(d2);
        gameBoard.getRoundTrack().setDiceList(diceList, 1);
        Die d3 = new Die(Color.YELLOW);
        d3.setNumber(6);
        ArrayList<Die> diceList2 = new ArrayList<>();
        diceList2.add(d3);
        gameBoard.getRoundTrack().setNextRound(diceList2);

        Turn turn = new Turn(gameBoard.getPlayers().get(0), gameBoard, false, new Round(gameBoard.getPlayers(), gameBoard));

        System.out.println(gameBoard.getDraftPool());
        ChangeDieTool changeDieTool=new ChangeDieTool(false,ToolNames.LENS_CUTTER);
        PlayerMove playerMove = new PlayerMove("UseTool",ToolNames.LENS_CUTTER,0,new int[]{1,0});
        Assertions.assertTrue(changeDieTool.toolEffect(turn,playerMove));
        System.out.println(gameBoard.getDraftPool());
        ChangeDieTool changeDieTool1=new ChangeDieTool(true,ToolNames.FLUX_REMOVER);
        PlayerMove playerMove1 = new PlayerMove("UseTool",ToolNames.FLUX_REMOVER,2);
        Assertions.assertTrue(changeDieTool1.toolEffect(turn,playerMove1));
        System.out.println(gameBoard.getDraftPool());
    }
}