package it.polimi.ingsw.controller;


import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.exception.NotValidNumberException;

import java.util.HashMap;
import java.util.Map;

public class GameManager {
    public Map<String, String> schemes = new HashMap<>();

    public void initializeGameBoard() throws NotValidNumberException {
        GameBoard gameBoard = new GameBoard(schemes);
    };
}
