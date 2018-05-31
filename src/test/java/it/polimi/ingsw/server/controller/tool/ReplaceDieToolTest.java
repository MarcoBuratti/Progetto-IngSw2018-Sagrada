package it.polimi.ingsw.server.controller.tool;

import it.polimi.ingsw.server.controller.Round;
import it.polimi.ingsw.server.controller.Turn;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.controller.tool.ReplaceDieTool;
import it.polimi.ingsw.server.controller.tool.ToolNames;
import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.Dashboard;
import it.polimi.ingsw.server.model.Die;
import it.polimi.ingsw.server.model.GameBoard;
import it.polimi.ingsw.server.model.exception.NotValidParametersException;
import it.polimi.ingsw.server.model.exception.NotValidRoundException;
import it.polimi.ingsw.server.model.exception.NotValidValueException;
import it.polimi.ingsw.server.model.exception.OccupiedCellException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class ReplaceDieToolTest {

    @Test
    void toolEffect() throws NotValidValueException, OccupiedCellException, NotValidParametersException, NotValidRoundException {
        Map<String, String> map = new HashMap<>();
        map.put("sergio", "Aurora_Sagradis");
        map.put("christian", "Chromatic_Splendor");
        GameBoard gameBoard = new GameBoard(map);

        Turn turn = new Turn(gameBoard.getPlayers().get(0), gameBoard, false);
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

        PlayerMove playerMove = new PlayerMove(nickname,"UseTool",ToolNames.EGLOMISE_BRUSH,new int[]{0,0,0,2});
        Assertions.assertTrue(replaceDieTool.toolEffect(turn,playerMove));
        ReplaceDieTool replaceDieTool1= new ReplaceDieTool(true, false, false,false,ToolNames.COPPER_FOIL_BURNISHER);

        PlayerMove playerMove1 = new PlayerMove(nickname,"UseTool",ToolNames.COPPER_FOIL_BURNISHER,new int[]{1,0,1,2});
        Assertions.assertTrue(replaceDieTool1.toolEffect(turn,playerMove1));
        PlayerMove playerMove2 = new PlayerMove(nickname,"UseTool",ToolNames.LATHEKIN,new int[]{1,2,1,3,0,2,0,1});
        ReplaceDieTool replaceDieTool2= new ReplaceDieTool(true, true, false,false,ToolNames.LATHEKIN);
        Assertions.assertTrue(replaceDieTool2.toolEffect(turn,playerMove2));

        Die d1 = new Die(Color.BLUE);
        Die d2 = new Die(Color.VIOLET);
        ArrayList<Die> diceList = new ArrayList<>();
        diceList.add(d1);
        diceList.add(d2);
        gameBoard.getRoundTrack().setDiceList(diceList, 1);


        ReplaceDieTool replaceDieTool3= new ReplaceDieTool(true, true, true,false,ToolNames.TAP_WHEEL);
        PlayerMove playerMove3 = new PlayerMove(nickname,"UseTool",ToolNames.TAP_WHEEL,new int[]{0,1,0,0,2,0,1,2});
        Assertions.assertTrue(replaceDieTool3.toolEffect(turn,playerMove3));

        for (int i = 0; i <4 ; i++) {
            for (int j = 0; j <5; j++) {
                System.out.println(dashboard.getMatrixScheme()[i][j]);
            }
            System.out.println("\n");
        }
    }
}