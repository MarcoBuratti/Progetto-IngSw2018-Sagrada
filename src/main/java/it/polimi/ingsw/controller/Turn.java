package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.action.PlacementMove;
import it.polimi.ingsw.model.Die;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exception.NotValidParametersException;
import it.polimi.ingsw.model.exception.OccupiedCellException;

import java.util.Timer;
import java.util.TimerTask;

public class Turn{


    private final boolean secondTurn;
    private int timeTurn;
    private String typeMove;

    private boolean turnIsOver;
    private boolean waitMove;
    private boolean placementDone;
    private boolean usedTool;
    private boolean hasSecondTurn;

    private Player player;
    private GameBoard gameBoard;
    private Round round;

    private PlacementMove placementMove;

    public Turn(Player player, GameBoard gameBoard,boolean secondTurn,Round round) {
        this.usedTool = false;
        this.placementDone = false;
        this.turnIsOver = false;
        this.waitMove = true;
        this.hasSecondTurn=true;
        this.secondTurn=secondTurn;
        this.player = player;
        this.gameBoard = gameBoard;
        this.round =round;
        this.timeTurn=3*1000;
    }


    public synchronized void setTurnIsOver(boolean turnIsOver) {
        this.turnIsOver = turnIsOver;
        notifyAll();
    }

    public synchronized boolean isWaitMove() {
        return waitMove;
    }

    public synchronized boolean isTurnIsOver() {
        return turnIsOver;
    }

    public void setPlacementDone(boolean placementDone) {
        this.placementDone = placementDone;
    }

    public boolean isPlacementDone() {
        return placementDone;
    }

    public boolean isUsedTool() {
        return usedTool;
    }

    public boolean isHasSecondTurn() {
        return hasSecondTurn;
    }

    public Player getPlayer() {
        return player;
    }
    public GameBoard getGameBoard(){
        return gameBoard;
    }

    public synchronized void newMove(String typeMove) {
            this.typeMove = typeMove;
            this.waitMove = false;
            notifyAll();
    }

    public synchronized void newMove(String typeMove,int row,int column,Die die){
        this.typeMove = typeMove;
        this.placementMove= new PlacementMove(this.player,row,column,die);
        this.waitMove = false;
        notifyAll();
    }

    public void time(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(!isTurnIsOver())
                    setTurnIsOver(true);

            }
        },this.timeTurn);
    }

    public void turnManager() {

        this.time();
        while (!isTurnIsOver()) {
            synchronized(this){
            while(!isTurnIsOver() && isWaitMove())
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(!isWaitMove()) {
                if (typeMove.equals("setdie") && !placementDone) {
                    try {
                        this.placementDone = placementMove.placeDie();
                        this.gameBoard.removeDieFromDraftPool(placementMove.getDie());
                        if (isUsedTool() && isPlacementDone())
                            this.turnIsOver = true;
                    } catch (OccupiedCellException | NotValidParametersException e) {
                        e.printStackTrace();
                    }


                } else if (typeMove.equals("usetool") && !usedTool) {
                    //codice dei tool
                    if (isPlacementDone() && isUsedTool()) {
                        this.turnIsOver = true;
                    }

                } else if (typeMove.equals("gothrough")) {
                    setTurnIsOver(true);
                }

                this.waitMove = true;
            }
            }
        }
    }


