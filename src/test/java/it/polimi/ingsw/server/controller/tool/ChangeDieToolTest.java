package it.polimi.ingsw.server.controller.tool;

import it.polimi.ingsw.server.controller.Turn;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.exception.NotValidRoundException;
import it.polimi.ingsw.server.model.exception.NotValidValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

class ChangeDieToolTest {

    @Test
    void toolEffect() throws NotValidValueException, NotValidRoundException {
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

        Turn turn = new Turn(null, gameBoard.getPlayers().get(0), gameBoard, false);
        String nickname = gameBoard.getPlayers().get(0).getNickname();
        System.out.println(gameBoard.getDraftPool());
        ChangeDieTool changeDieTool=new ChangeDieTool(false,ToolNames.LENS_CUTTER);
        ArrayList<Integer> intParameters = new ArrayList<>();
        intParameters.add(1);
        intParameters.add(0);
        PlayerMove playerMove = new PlayerMove(nickname, "UseTool", 9,0, intParameters);
        Assertions.assertTrue(changeDieTool.toolEffect(turn,playerMove));
        System.out.println(gameBoard.getDraftPool());

        Turn turn1 = new Turn(null, gameBoard.getPlayers().get(1), gameBoard, false);
        String nickname1 = gameBoard.getPlayers().get(1).getNickname();

        DecoratedChangeDieTool decoratedChangeDieTool = new DecoratedChangeDieTool( new ChangeDieTool( true, ToolNames.FLUX_REMOVER ) );
        PlayerMove playerMove1 = new PlayerMove( nickname1, "UseTool", 4, 2);
        System.out.println( "DraftPool prima di FLUX_REMOVER\n" + gameBoard.getDraftPool() );
        System.out.println( gameBoard.getDiceBag().toString() );
        Assertions.assertTrue( decoratedChangeDieTool.toolEffect( turn1, playerMove1 ) );
        System.out.println( "DraftPool dopo FLUX_REMOVER\n" + gameBoard.getDraftPool() );
        System.out.println( gameBoard.getDiceBag().toString() );
        intParameters.add(0, 5);
        intParameters.add(1, 0);
        intParameters.add(2, 0);
        System.out.println( gameBoard.getPlayers().get(1).getDashboard() );
        Assertions.assertTrue(decoratedChangeDieTool.placeDie( turn1, new PlayerMove( nickname1, "PlaceDie", 2, intParameters )));
        System.out.println( gameBoard.getPlayers().get(1).getDashboard() );
    }
}