package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Dashboard;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.achievement.PrivateAchievement;
import it.polimi.ingsw.model.exception.NotEnoughDiceLeftException;
import it.polimi.ingsw.model.exception.NotValidValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class RoundTest {

    @Test
    void testInizializeAndCloseRound() throws NotValidValueException, NotEnoughDiceLeftException {

        Map<String, String> map = new HashMap<>();
        map.put("sergio", "Aurora Sagradis");
        map.put("christian", "Chromatic Splendor");
        map.put("marco", "Fulgor del Cielo");
        GameBoard gameBoard = new GameBoard(map);

        Round round =new Round(gameBoard.getPlayers());
        int size = gameBoard.getDiceBag().getDiceSet().size();
        round.initializeDraftPool(gameBoard);
        gameBoard.setDraftPool(round.getDraftPool());
        Assertions.assertEquals(7,gameBoard.getDraftPool().size());
        size -= 7;
        Assertions.assertEquals(size, gameBoard.getDiceBag().getDiceSet().size());
        round.endRound(gameBoard);
        Assertions.assertEquals(0,gameBoard.getDraftPool().size());

        Round round2 =new Round(gameBoard.getPlayers());
        round2.initializeDraftPool(gameBoard);
        gameBoard.setDraftPool(round2.getDraftPool());
        Assertions.assertEquals(7,gameBoard.getDraftPool().size());
        size -= 7;
        Assertions.assertEquals(size, gameBoard.getDiceBag().getDiceSet().size());
        round.endRound(gameBoard);
        Assertions.assertEquals(0,gameBoard.getDraftPool().size());

    }

}