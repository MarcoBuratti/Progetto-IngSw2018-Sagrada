package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.controller.Round;
import it.polimi.ingsw.server.model.GameBoard;
import it.polimi.ingsw.server.model.exception.NotEnoughDiceLeftException;
import it.polimi.ingsw.server.model.exception.NotValidValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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

        Round round =new Round(gameBoard.getPlayers(),gameBoard);
        int size = gameBoard.getDiceBag().getDiceSet().size();
        gameBoard.setDraftPool(gameBoard.getDraftPool());
        Assertions.assertEquals(7,gameBoard.getDraftPool().size());
        size -= 7;
        Assertions.assertEquals(size, gameBoard.getDiceBag().getDiceSet().size());
        round.endRound();
        Assertions.assertEquals(0,gameBoard.getDraftPool().size());

        Round round2 =new Round(gameBoard.getPlayers(),gameBoard);
        gameBoard.setDraftPool(gameBoard.getDraftPool());
        Assertions.assertEquals(7,gameBoard.getDraftPool().size());
        size -= 7;
        Assertions.assertEquals(size, gameBoard.getDiceBag().getDiceSet().size());
        round.endRound();
        Assertions.assertEquals(0,gameBoard.getDraftPool().size());

    }

}