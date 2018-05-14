package it.polimi.ingsw.controller.action;

import it.polimi.ingsw.controller.Round;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Dashboard;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.Player;
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

        Round round=new Round(gameBoard.getPlayers());
        round.initializeDraftPool(gameBoard);
        gameBoard.setDraftPool(round.getDraftPool());
        PlacementMove placementMove = new PlacementMove("0 0 2",gameBoard.getPlayers().get(0),gameBoard );
        Assertions.assertTrue(placementMove.positionDie());
        System.out.println(gameBoard.getPlayers().get(0).getDashboard().getMatrixScheme()[0][0]);
        Assertions.assertEquals(6,gameBoard.getDraftPool().size());
        Assertions.assertFalse(placementMove.positionDie());
        PlacementMove placementMove1 = new PlacementMove("1 1 2",gameBoard.getPlayers().get(0),gameBoard);
        Assertions.assertTrue(placementMove1.positionDie());
        System.out.println(gameBoard.getPlayers().get(0).getDashboard().getMatrixScheme()[1][1]);
        Assertions.assertEquals(5,gameBoard.getDraftPool().size());
        PlacementMove placementMove2= new PlacementMove("3 3 4",gameBoard.getPlayers().get(0),gameBoard);
        Assertions.assertFalse(placementMove2.positionDie());
        System.out.println(gameBoard.getPlayers().get(0).getDashboard().getMatrixScheme()[3][3]);
        Assertions.assertEquals(5,gameBoard.getDraftPool().size());


    }
}