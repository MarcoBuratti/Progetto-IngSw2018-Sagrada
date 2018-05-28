package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.controller.Round;
import it.polimi.ingsw.server.controller.Turn;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.controller.tool.Tool;
import it.polimi.ingsw.server.controller.tool.ToolFactory;
import it.polimi.ingsw.server.controller.tool.ToolNames;
import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.Die;
import it.polimi.ingsw.server.model.GameBoard;
import it.polimi.ingsw.server.model.exception.NotValidValueException;
import org.junit.jupiter.api.Test;

import java.util.*;

class TurnTest {

    @Test
    void turnManager() throws NotValidValueException {
        Map<String, String> map = new HashMap<>();
        map.put("sergio", "Aurora Sagradis");
        map.put("christian", "Chromatic Splendor");
        map.put("marco", "Fulgor del Cielo");
        GameBoard gameBoard = new GameBoard(map);

        Turn turn = new Turn(gameBoard.getPlayers().get(0), gameBoard, true);
        String nickname = gameBoard.getPlayers().get(0).getNickname();
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

        List<ToolNames> toolList = Arrays.asList(ToolNames.values());
        ToolFactory abstractToolFactory = new ToolFactory();
         ArrayList<Tool> tools = new ArrayList<>();

        for (ToolNames t:toolList) {

            Tool toolFactory = abstractToolFactory.getTool(t);
            tools.add(toolFactory);
        }

        gameBoard.setTools(tools);
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
                turn.newMove(new PlayerMove(nickname,"PlaceDie", 2, new int[]{ 1,1}));
            }
        }, 500);
       timer.schedule(new TimerTask() {
            @Override
            public void run() {
                turn.newMove(new PlayerMove(nickname,"PlaceDie",1, new int[]{0, 0}));
            }
        }, 1000);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                turn.newMove(new PlayerMove(nickname,"PlaceDie",2,new int[] {0, 1}));
            }
        }, 1500);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                turn.newMove(new PlayerMove(nickname,"UseTool",ToolNames.GRINDING_STONE,1));
            }
        }, 900);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                turn.newMove(new PlayerMove(nickname,"UseTool",ToolNames.GRINDING_STONE,0));
            }
        }, 1900);

        turn.turnManager();



        System.out.println(gameBoard.getPlayers().get(0).getDashboard().getMatrixScheme()[1][1].toString());
        System.out.println(gameBoard.getPlayers().get(0).getDashboard().getMatrixScheme()[0][0].toString());
        System.out.println(gameBoard.getPlayers().get(0).getDashboard().getMatrixScheme()[1][0].toString());
        System.out.println(gameBoard.getDraftPool());

    }
}