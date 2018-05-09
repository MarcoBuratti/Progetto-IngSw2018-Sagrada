package it.polimi.ingsw.controller;


import it.polimi.ingsw.model.GameBoard;

import java.util.HashMap;
import java.util.Map;

public class GameManager {
    public Map<String, String> schemes = new HashMap<>();

    public void initializeGameBoard(){
        GameBoard gameBoard = new GameBoard(schemes);
    };
}
