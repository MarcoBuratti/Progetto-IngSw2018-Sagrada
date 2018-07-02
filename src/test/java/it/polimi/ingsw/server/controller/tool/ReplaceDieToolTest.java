package it.polimi.ingsw.server.controller.tool;

import it.polimi.ingsw.server.controller.Turn;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.exception.NotValidParametersException;
import it.polimi.ingsw.server.model.exception.NotValidRoundException;
import it.polimi.ingsw.server.model.exception.NotValidValueException;
import it.polimi.ingsw.server.model.exception.OccupiedCellException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

class ReplaceDieToolTest {
    private final static String USE_TOOL = "UseTool";

    @Test
    void toolEffect() throws NotValidValueException, OccupiedCellException, NotValidParametersException, NotValidRoundException {
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

        Turn turn = new Turn(null, gameBoard.getPlayers().get(0), gameBoard, false);
        String nickname = gameBoard.getPlayers().get(0).getNickname();
        ReplaceDieTool replaceDieTool= new ReplaceDieTool(false, true, false,false, ToolNames.EGLOMISE_BRUSH);
        Dashboard dashboard =gameBoard.getPlayers().get(0).getDashboard();
        dashboard.setDieOnCell(0,0,new Die(Color.VIOLET));
        dashboard.getMatrixScheme()[0][0].getDie().setNumber(2);
        dashboard.setDieOnCell(1,0,new Die(Color.BLUE));
        dashboard.getMatrixScheme()[1][0].getDie().setNumber(4);
        dashboard.setDieOnCell(2,0,new Die(Color.VIOLET));
        dashboard.getMatrixScheme()[2][0].getDie().setNumber(5);
        dashboard.setDieOnCell(1,1,new Die(Color.RED));
        dashboard.getMatrixScheme()[1][1].getDie().setNumber(6);


        ArrayList<Integer> intParameters = new ArrayList<>();
        intParameters.add(0);
        intParameters.add(0);
        intParameters.add(0);
        intParameters.add(2);
        PlayerMove playerMove = new PlayerMove(nickname, USE_TOOL, 2, intParameters);
        Assertions.assertTrue(replaceDieTool.toolEffect(turn,playerMove));
        /*ReplaceDieTool replaceDieTool1= new ReplaceDieTool(true, false, false,false,ToolNames.COPPER_FOIL_BURNISHER);


        intParameters = new ArrayList<>();
        intParameters.add(1);
        intParameters.add(0);
        intParameters.add(1);
        intParameters.add(2);
        PlayerMove playerMove1 = new PlayerMove(nickname,USE_TOOL, 0, intParameters);
        Assertions.assertTrue(replaceDieTool1.toolEffect(turn,playerMove1));
        intParameters = new ArrayList<>();
        intParameters.add(1);
        intParameters.add(2);
        intParameters.add(1);
        intParameters.add(3);
        intParameters.add(0);
        intParameters.add(2);
        intParameters.add(0);
        intParameters.add(1);
        PlayerMove playerMove2 = new PlayerMove(nickname,USE_TOOL,8, intParameters);
        ReplaceDieTool replaceDieTool2= new ReplaceDieTool(true, true, false,false,ToolNames.LATHEKIN);
        Assertions.assertTrue(replaceDieTool2.toolEffect(turn,playerMove2));

        Die d1 = new Die(Color.BLUE);
        Die d2 = new Die(Color.VIOLET);
        ArrayList<Die> diceList = new ArrayList<>();
        diceList.add(d1);
        diceList.add(d2);
        gameBoard.getRoundTrack().setDiceList(diceList, 1);


        ReplaceDieTool replaceDieTool3= new ReplaceDieTool(true, true, true,false,ToolNames.TAP_WHEEL);
        intParameters = new ArrayList<>();
        intParameters.add(0);
        intParameters.add(1);
        intParameters.add(0);
        intParameters.add(0);
        intParameters.add(2);
        intParameters.add(0);
        intParameters.add(1);
        intParameters.add(2);
        PlayerMove playerMove3 = new PlayerMove(nickname,USE_TOOL, 11, intParameters);
        Assertions.assertTrue(replaceDieTool3.toolEffect(turn,playerMove3));*/

        for (int i = 0; i <4 ; i++) {
            for (int j = 0; j <5; j++) {
                System.out.println(dashboard.getMatrixScheme()[i][j]);
            }
            System.out.println("\n");
        }
    }
}