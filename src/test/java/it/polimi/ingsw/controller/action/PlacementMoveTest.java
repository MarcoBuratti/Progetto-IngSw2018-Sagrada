package it.polimi.ingsw.controller.action;

import it.polimi.ingsw.controller.Round;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.achievement.PrivateAchievement;
import it.polimi.ingsw.model.exception.NotEnoughDiceLeftException;
import it.polimi.ingsw.model.exception.NotValidParametersException;
import it.polimi.ingsw.model.exception.NotValidValueException;
import it.polimi.ingsw.model.exception.OccupiedCellException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PlacementMoveTest {

    @Test
    void positionDie() throws NotValidValueException, NotEnoughDiceLeftException, NotValidParametersException, OccupiedCellException {

        Map<String, String> map = new HashMap<>();
        map.put("sergio", "Aurora Sagradis");
        map.put("christian", "Scheme Test");
        map.put("marco", "Fulgor del Cielo");
        GameBoard gameBoard = new GameBoard(map);

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