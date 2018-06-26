package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.controller.action.PlacementMove;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.controller.tool.Tool;
import it.polimi.ingsw.server.model.GameBoard;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.exception.NotEnoughFavourTokensLeft;
import it.polimi.ingsw.server.model.exception.NotValidParametersException;
import it.polimi.ingsw.server.model.exception.OccupiedCellException;

import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

public class Turn {


    private final boolean secondTurn;
    private int timeTurn;
    private String typeMove;
    private boolean turnIsOver;
    private boolean waitMove;
    private boolean placementDone;
    private boolean usedTool;
    private Player player;
    private GameBoard gameBoard;
    private PlayerMove playerMove;

    public Turn(Player player, GameBoard gameBoard, boolean secondTurn) {
        this.usedTool = false;
        this.placementDone = false;
        this.turnIsOver = false;
        this.waitMove = true;
        this.secondTurn = secondTurn;
        this.player = player;
        this.gameBoard = gameBoard;
        this.timeTurn = 60 * 1000;
    }

    public synchronized void setTurnIsOver() {
        this.turnIsOver = true;
        if (this.player.getServerInterface() != null)
            this.player.getServerInterface().send("Your turn has ended.");
        notifyAll();
    }

    public synchronized void onePlayerLeft() {
        this.turnIsOver = true;
        notifyAll();
    }

    public synchronized void setWaitMove(boolean waitMove) {
        this.waitMove = waitMove;
        notifyAll();
    }

    private synchronized boolean isWaitMove() {
        return waitMove;
    }

    private synchronized boolean isTurnIsOver() {
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

    public boolean isSecondTurn() {
        return secondTurn;
    }

    public Player getPlayer() {
        return player;
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public String getTypeMove() {
        return this.typeMove;
    }

    public PlayerMove getPlayerMove() {
        return playerMove;
    }

    public synchronized void newMove(PlayerMove playerMove) {
        this.typeMove = playerMove.getMoveType();
        this.playerMove = playerMove;
        this.waitMove = false;
        notifyAll();
    }

    private synchronized void timeOut() {
        this.turnIsOver = true;
        if (this.player.getServerInterface() != null)
            this.player.getServerInterface().send("The time is over! Your turn has ended.");
        notifyAll();
    }

    private void launchTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isTurnIsOver()) {
                    timeOut();
                }
            }
        }, this.timeTurn);
    }

    public void turnManager() {

        this.launchTimer();

        if (this.player.getServerInterface() != null) {
            //this.player.getServerInterface().send("UpdateFromServer");
        }

        while (!isTurnIsOver()) {

            synchronized (this) {

                while (!isTurnIsOver() && isWaitMove())
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }

            if (!isWaitMove() && typeMove != null) {
                System.out.println("if else");
                if (typeMove.equals("PlaceDie") && !placementDone) {

                    this.tryPlacementMove(this.playerMove);

                    if (placementDone && usedTool)
                        setTurnIsOver();


                } else if (typeMove.equals("UseTool") && !usedTool) {
                    System.out.println("Dentro usa tool");
                    this.useTool();

                    if (placementDone && usedTool)
                        setTurnIsOver();


                } else if (typeMove.equals("GoThrough")) {

                    setTurnIsOver();

                }

                this.waitMove = true;
            }
        }
    }

    public void tryPlacementMove(PlayerMove playerMove) {

        if (playerMove.getIndexDie().isPresent()) {
            try {
                PlacementMove placementMove = new PlacementMove(player, playerMove.getIntParameters(0),
                        playerMove.getIntParameters(1), gameBoard.getDraftPool().get(playerMove.getIndexDie().get()));
                this.placementDone = placementMove.placeDie();
                if (isPlacementDone()) {
                    this.gameBoard.removeDieFromDraftPool(placementMove.getDie());
                    if (this.player.getServerInterface() != null)
                        this.player.getServerInterface().send("The die has been placed on the selected cell.");
                } else if (this.player.getServerInterface() != null)
                    this.player.getServerInterface().send("Incorrect move! Please try again.");
            } catch (OccupiedCellException | NotValidParametersException e) {
                e.printStackTrace();
            }
        } else
            throw new IllegalArgumentException();
    }

    private void useTool() {

        Optional<Integer> toolIndex = playerMove.getExtractedToolIndex();
        if (toolIndex.isPresent()) {
            Integer toolIndexValue = toolIndex.get();
            Tool tool = gameBoard.getTools().get(toolIndexValue);

            try {
                this.player.useToken(tool.isAlreadyUsed());
                usedTool = tool.toolEffect(this, playerMove);
                if (tool.needPlacement()) {
                    setWaitMove(true);
                    synchronized (this) {
                        while (!isTurnIsOver() && isWaitMove()) {
                            wait();
                            tool.placementDie(this);
                        }

                    }
                }

                System.out.println("UsedTool è uguale a: " + usedTool);
                if (isUsedTool()) {
                    if (this.player.getServerInterface() != null) {
                        this.gameBoard.update();
                        this.player.getServerInterface().send("The selected tool has been used successfully");
                    }
                } else if (this.player.getServerInterface() != null)
                    this.player.getServerInterface().send("Incorrect move! Please try again.");

            } catch (NotEnoughFavourTokensLeft | InterruptedException e) {
                throw new IllegalArgumentException();

            }
        } else
            throw new IllegalArgumentException();


    }

}


