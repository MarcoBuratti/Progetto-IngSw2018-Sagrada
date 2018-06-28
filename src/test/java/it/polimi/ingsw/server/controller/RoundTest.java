package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.GameBoard;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.exception.NotEnoughDiceLeftException;
import it.polimi.ingsw.server.model.exception.NotValidValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class RoundTest {

    @Test
    void testInitializeAndCloseRound() throws NotValidValueException, NotEnoughDiceLeftException {

        ArrayList<Player> playersList = new ArrayList<>();
        Player player = new Player ( "sergio");
        player.setDashboard("Aurora_Sagradis");
        playersList.add( player );
        player = new Player ( "christian");
        player.setDashboard("Chromatic_Splendor");
        playersList.add( player );
        player = new Player ( "marco");
        player.setDashboard("Fulgor_del_Cielo");
        playersList.add( player );
        GameBoard gameBoard = new GameBoard(playersList);

        Round round = new Round(null, gameBoard.getPlayers(), gameBoard);
        int size = gameBoard.getDiceBag().getDiceSet().size();
        round.initializeDraftPool();
        gameBoard.setDraftPool(gameBoard.getDraftPool());
        Assertions.assertEquals(7,gameBoard.getDraftPool().size());
        size -= 7;
        Assertions.assertEquals(size, gameBoard.getDiceBag().getDiceSet().size());
        round.endRound();
        Assertions.assertEquals(0,gameBoard.getDraftPool().size());

        Round round2 = new Round(null, gameBoard.getPlayers(),gameBoard);
        round2.initializeDraftPool();
        gameBoard.setDraftPool(gameBoard.getDraftPool());
        Assertions.assertEquals(7,gameBoard.getDraftPool().size());
        size -= 7;
        Assertions.assertEquals(size, gameBoard.getDiceBag().getDiceSet().size());
        round.endRound();
        Assertions.assertEquals(0,gameBoard.getDraftPool().size());

    }

}