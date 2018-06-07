package it.polimi.ingsw.server.controller.action;

import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.Die;
import it.polimi.ingsw.server.model.GameBoard;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.exception.NotEnoughDiceLeftException;
import it.polimi.ingsw.server.model.exception.NotValidParametersException;
import it.polimi.ingsw.server.model.exception.NotValidValueException;
import it.polimi.ingsw.server.model.exception.OccupiedCellException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class PlacementMoveTest {

    @Test
    void positionDie() throws NotValidValueException, NotEnoughDiceLeftException, NotValidParametersException, OccupiedCellException {

        ArrayList<Player> playersList = new ArrayList<>();
        Player player;
        player = new Player ( "christian" , null );
        player.setDashboard("Scheme_Test");
        playersList.add( player );
        player = new Player ( "marco" , null );
        player.setDashboard("Fulgor_del_Cielo");
        playersList.add( player );
        player = new Player ( "sergio" , null );
        player.setDashboard("Aurora_Sagradis");
        playersList.add( player );
        GameBoard gameBoard = new GameBoard(playersList);

        Die die1 = new Die(Color.BLUE);
        die1.setNumber(4);
        Die die2 = new Die(Color.VIOLET);
        die2.setNumber(4);
        Die die3 = new Die(Color.YELLOW);
        die3.setNumber(1);

        PlacementMove placementMove = new PlacementMove(gameBoard.getPlayers().get(0),0,0,die1);
        Assertions.assertTrue(placementMove.placeDie());
        System.out.println(gameBoard.getPlayers().get(0).getDashboard().getMatrixScheme()[0][0]);
        PlacementMove placementMove1 = new PlacementMove(gameBoard.getPlayers().get(0),0,1,die2);
        Assertions.assertFalse(placementMove1.placeDie());
        PlacementMove placementMove2= new PlacementMove(gameBoard.getPlayers().get(0),0,1,die3);
        PlacementMove placementMove3= new PlacementMove(gameBoard.getPlayers().get(0),1,1,die2);
        Assertions.assertTrue(placementMove3.placeDie());
        Assertions.assertTrue((placementMove2.placeDie()));


    }
}