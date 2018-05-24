package it.polimi.ingsw.server.controller;


import it.polimi.ingsw.server.model.GameBoard;
import it.polimi.ingsw.server.model.exception.NotValidValueException;

import java.util.HashMap;
import java.util.Map;

public class GameManager {
    public Map<String, String> schemes = new HashMap<>();

    public void initializeGameBoard() throws NotValidValueException {
        GameBoard gameBoard = new GameBoard(schemes);
    }
}
