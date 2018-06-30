package it.polimi.ingsw.server.controller.tool;

import it.polimi.ingsw.server.controller.Turn;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.exception.NotValidParametersException;
import it.polimi.ingsw.server.model.exception.NotValidValueException;
import it.polimi.ingsw.server.model.exception.OccupiedCellException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class SetDieToolTest {

    @Test
    void toolEffect() throws OccupiedCellException, NotValidParametersException, NotValidValueException {

        ArrayList<Player> playersList = new ArrayList<>();
        Player player = new Player ( "sergio");
        player.setDashboard("Aurora_Sagradis");
        playersList.add( player );
        player = new Player ( "christian");
        player.setDashboard("Chromatic_Splendor");
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

        ArrayList<Die> testDraftPool = new ArrayList<>();
        Die die1 = new Die(Color.VIOLET);
        die1.setNumber(1);
        Die die2 = new Die(Color.GREEN);
        die2.setNumber(2);
        Die die3 = new Die(Color.RED);
        die3.setNumber(3);

        testDraftPool.add(die1);
        testDraftPool.add(die2);
        testDraftPool.add(die3);

        gameBoard.setDraftPool(testDraftPool);

        Turn turn = new Turn(null, gameBoard.getPlayers().get(0), gameBoard, false);
        String nickname = gameBoard.getPlayers().get(0).getNickname();
        SetDieTool setDieTool = new SetDieTool(true, ToolNames.GROZING_PLIERS);
        Dashboard dashboard = gameBoard.getPlayers().get(0).getDashboard();
        dashboard.setDieOnCell(0, 0, new Die(Color.GREEN));
        dashboard.getMatrixScheme()[0][0].getDie().setNumber(2);
        dashboard.setDieOnCell(1, 0, new Die(Color.BLUE));
        dashboard.getMatrixScheme()[1][0].getDie().setNumber(4);
        dashboard.setDieOnCell(2, 0, new Die(Color.YELLOW));
        dashboard.getMatrixScheme()[2][0].getDie().setNumber(5);
        dashboard.setDieOnCell(1, 1, new Die(Color.RED));
        dashboard.getMatrixScheme()[1][1].getDie().setNumber(6);

        PlayerMove playerMove = new PlayerMove(nickname,"UseTool", 7, 0, false);
        Assertions.assertFalse(setDieTool.toolEffect(turn, playerMove));
        PlayerMove playerMove1 = new PlayerMove(nickname,"UseTool", 7, 0, true);
        Assertions.assertTrue(setDieTool.toolEffect(turn, playerMove1));
        DecoratedSetDieTool decoratedSetDieTool = new DecoratedSetDieTool( new SetDieTool(true, ToolNames.FLUX_BRUSH) );
        PlayerMove playerMove2 = new PlayerMove(nickname,"UseTool", 3, 0);
        Assertions.assertTrue(decoratedSetDieTool.toolEffect(turn, playerMove2));

        SetDieTool setDieTool2 = new SetDieTool(true, ToolNames.GRINDING_STONE);
        PlayerMove playerMove3 = new PlayerMove(nickname,"UseTool", 6, 0);
        Assertions.assertTrue(setDieTool2.toolEffect(turn, playerMove3));
    }
    }
