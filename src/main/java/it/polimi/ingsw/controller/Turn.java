package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.action.PlacementMove;
import it.polimi.ingsw.model.Die;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exception.NotValidParametersException;
import it.polimi.ingsw.model.exception.OccupiedCellException;

public class Turn{


    private final boolean secondTurn;

    private String typeMove;

    private boolean turnIsOver;
    private boolean waitMove;
    private boolean placementDone;
    private boolean usedTool;
    private boolean hasSecondTurn;

    private Player player;
    private GameBoard gameBoard;
    private Round round;

    private int row;
    private int column;
    private Die die;

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
    }


    public synchronized void setTurnIsOver(boolean turnIsOver) {
        this.turnIsOver = turnIsOver;
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

    public synchronized void newMove(String typeMove) {
        this.typeMove = typeMove;
        this.waitMove = false;
        notifyAll();
    }

    public synchronized void newMove(String typeMove,int row,int column,Die die){
        this.typeMove = typeMove;
        this.row=row;
        this.column=column;
        this.die=die;
        this.waitMove = false;
        notifyAll();
    }

    public void turnManager() {

        Timer timer = new Timer(this);
        new Thread(timer).start();
        while (!isTurnIsOver()) {
            while(!isTurnIsOver() && isWaitMove()) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

                if (typeMove.equals("setdie") && !placementDone) {
                    PlacementMove placementMove = new PlacementMove(this.player,this.row,this.column,this.die);
                    try {
                        this.placementDone = placementMove.placeDie();
                        this.gameBoard.removeDieFromDraftPool(this.die);
                        if (isUsedTool()&&isPlacementDone())
                            this.turnIsOver = true;
                    } catch (OccupiedCellException | NotValidParametersException e) {
                        e.printStackTrace();
                    }


                } else if (typeMove.equals("usetool") && !usedTool) {
                    //codice dei tool
                    if(isPlacementDone()&&isUsedTool()){
                        this.turnIsOver = true;
                    }

                } else if (typeMove.equals("gothrough")) {
                    this.turnIsOver = true;
                }

                this.waitMove = true;
            }
        }
    }


