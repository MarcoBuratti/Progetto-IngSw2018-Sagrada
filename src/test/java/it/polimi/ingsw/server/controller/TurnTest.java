package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.controller.tool.Tool;
import it.polimi.ingsw.server.controller.tool.ToolFactory;
import it.polimi.ingsw.server.controller.tool.ToolNames;
import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.Die;
import it.polimi.ingsw.server.model.GameBoard;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.exception.NotValidValueException;
import org.junit.jupiter.api.Test;

import java.util.*;

class TurnTest {

    @Test
    void turnManager() throws NotValidValueException {
        ArrayList<Player> playersList = new ArrayList<>();
        Player player = new Player ( "sergio" , null );
        player.setDashboard("Aurora_Sagradis");
        playersList.add( player );
        player = new Player ( "christian" , null );
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

        Turn turn = new Turn(gameBoard.getPlayers().get(1), gameBoard, true);
        String nickname = gameBoard.getPlayers().get(1).getNickname();
        ArrayList<Die> testDraftPool= new ArrayList<>();
        Die die1 =new Die(Color.BLUE);
        die1.setNumber(1);
        Die die2 =new Die(Color.GREEN);
        die2.setNumber(2);
        Die die3 =new Die(Color.RED);
        die3.setNumber(3);

        testDraftPool.add(die1);
        testDraftPool.add(die2);
        testDraftPool.add(die3);

        gameBoard.setDraftPool(testDraftPool);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                turn.newMove(new PlayerMove(nickname,"GoThrough"));
            }
        }, 2000);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ArrayList<Integer> intParameters = new ArrayList<>();
                intParameters.add(1);
                intParameters.add(1);
                turn.newMove(new PlayerMove(nickname,"PlaceDie", 2, intParameters));
            }
        }, 500);

       timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ArrayList<Integer> intParameters = new ArrayList<>();
                intParameters.add(0);
                intParameters.add(0);
                turn.newMove(new PlayerMove(nickname,"PlaceDie",1, intParameters));
            }
        }, 1000);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ArrayList<Integer> intParameters = new ArrayList<>();
                intParameters.add(0);
                intParameters.add(1);
                turn.newMove(new PlayerMove(nickname,"PlaceDie",2, intParameters));
            }
        }, 1500);
       timer.schedule(new TimerTask() {
            @Override
            public void run() {
                turn.newMove(new PlayerMove(nickname,"UseTool", 6,1));
            }
        }, 900);

        turn.turnManager();



        System.out.println(gameBoard.getPlayers().get(1).getDashboard().getMatrixScheme()[1][1].toString());
        System.out.println(gameBoard.getPlayers().get(1).getDashboard().getMatrixScheme()[0][0].toString());
        System.out.println(gameBoard.getPlayers().get(1).getDashboard().getMatrixScheme()[1][0].toString());
        System.out.println(gameBoard.getDraftPool());

    }
}