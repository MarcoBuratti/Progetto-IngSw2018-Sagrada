package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.RemoteView;
import it.polimi.ingsw.server.controller.action.PlacementMove;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.controller.tool.*;
import it.polimi.ingsw.server.model.GameBoard;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.exception.NotValidParametersException;
import it.polimi.ingsw.server.model.exception.OccupiedCellException;
import it.polimi.ingsw.util.TimeParser;

import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

public class Turn {


    private static final String TURN_TIMER = "Turn_Timer";
    private final boolean secondTurn;
    private long time;
    private String moveType;
    private boolean turnOver;
    private boolean waitMove;
    private boolean placementDone;
    private boolean usedTool;
    private Player player;
    private RemoteView remoteView;
    private GameBoard gameBoard;
    private PlayerMove playerMove;

    /**
     * Creates a new Turn, setting its attribute as the ones of the Round which called the constructor.
     *
     * @param remoteView the RemoteView Object associated with the player
     * @param player     the Player Object associated with the player who's playing the turn
     * @param gameBoard  the GameBoard Object representing the game board
     * @param secondTurn a boolean specifying whether it's the player's second turn or not
     */
    public Turn(RemoteView remoteView, Player player, GameBoard gameBoard, boolean secondTurn) {

        this.usedTool = false;
        this.placementDone = false;
        this.turnOver = false;
        this.waitMove = true;
        this.secondTurn = secondTurn;
        this.player = player;
        this.remoteView = remoteView;
        this.gameBoard = gameBoard;
        setTime();
    }

    /**
     * Calls a static method of the class TimeParser in order to read the time from a json file.
     */
    private synchronized void setTime() {
        this.time = TimeParser.readTime(TURN_TIMER);
    }

    /**
     * Sets the turnOver attribute as true and calls the notifyAll method.
     */
    public synchronized void setTurnIsOver() {
        this.turnOver = true;
        notifyAll();
    }

    /**
     * Sets the waitMove attribute as true and calls the notifyAll method.
     */
    private synchronized void setWaitMove() {
        this.waitMove = true;
        notifyAll();
    }

    /**
     * Returns the waitMove attribute.
     *
     * @return a boolean
     */
    private synchronized boolean isWaitMove() {
        return waitMove;
    }

    /**
     * Returns the negated turnOver attribute.
     *
     * @return a boolean specifying if the turn has not ended yet.
     */
    private synchronized boolean notEndedTurn() {
        return !turnOver;
    }

    /**
     * Returns the placementDone attribute.
     *
     * @return a boolean
     */
    public boolean isPlacementDone() {
        return placementDone;
    }

    /**
     * Allows the user to set the placementDone attribute as the placementDone parameter.
     *
     * @param placementDone a boolean
     */
    public void setPlacementDone(boolean placementDone) {
        this.placementDone = placementDone;
    }

    /**
     * Returns the secondTurn attribute.
     *
     * @return a boolean
     */
    public boolean isSecondTurn() {
        return secondTurn;
    }

    /**
     * Returns the player attribute.
     *
     * @return a Player Object representing the player who's playing the turn
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the gameBoard attribute.
     *
     * @return a GameBoard Object representing the game board
     */
    public GameBoard getGameBoard() {
        return gameBoard;
    }

    /**
     * Returns the playerMove attribute.
     *
     * @return a PlayerMove Object representing the last received player move
     */
    public PlayerMove getPlayerMove() {
        return playerMove;
    }

    /**
     * Sends a message to the player if its connection is on.
     *
     * @param message the String the user wants to send to the player
     */
    public void sendToPlayer(String message) {
        if (this.remoteView != null && this.remoteView.isOn())
            this.remoteView.send(message);
    }

    /**
     * Send to all the players an update of the game board (needed because of the change of current tokens) and then communicates to the player
     * who used the tool that the tool has been used successfully.
     */
    private void sendToPlayerAndUpdate() {
        if (this.remoteView != null && this.remoteView.isOn()) {
            this.gameBoard.update();
            this.remoteView.send("The selected tool has been used successfully");
        }
    }

    /**
     * As a new player move is received, it sets all the attributes related to it and calls notifyAll.
     *
     * @param playerMove the PlayerMove Object received from the Round
     */
    synchronized void newMove(PlayerMove playerMove) {
        this.moveType = playerMove.getMoveType();
        this.playerMove = playerMove;
        this.waitMove = false;
        notifyAll();
    }

    /**
     * Communicates to the player that the time is over and sets turnOver as true, then calls notifyAll.
     */
    private synchronized void timeOut() {
        this.turnOver = true;
        this.sendToPlayer("The time is over!");
        notifyAll();
    }

    /**
     * Launches a timer that calls timeOut when the time is over if the turn has not ended yet for other reasons.
     */
    private void launchTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (notEndedTurn()) {
                    timeOut();
                }
            }
        }, this.time);
    }

    /**
     * Manages the playerMove inside turnManager.
     */
    private void moveHandler() {

        if (moveType.equals("PlaceDie") && !placementDone) {

            this.tryPlacementMove(this.playerMove);

            if (placementDone && usedTool)
                setTurnIsOver();


        } else if (moveType.equals("UseTool") && !usedTool) {
            this.useTool();

            if (placementDone && usedTool)
                setTurnIsOver();


        } else if (moveType.equals("GoThrough")) {

            setTurnIsOver();

        } else remoteView.incorrectMove();

    }

    /**
     * Manages the turn.
     */
    void turnManager() {

        this.launchTimer();

        while (notEndedTurn()) {

            synchronized (this) {

                while (notEndedTurn() && isWaitMove())
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }

            if (!isWaitMove() && moveType != null) {

                moveHandler();

                this.waitMove = true;
            }
        }
    }

    /**
     * Manages a placement move.
     *
     * @param playerMove the PlayerMove Object representing the placement move the player sent
     */
    public void tryPlacementMove(PlayerMove playerMove) {

        Optional<Integer> dieIndex = playerMove.getIndexDie();

        if (dieIndex.isPresent()) {
            Integer dieIndexValue = dieIndex.get();
            try {
                PlacementMove placementMove = new PlacementMove(player, playerMove.getIntParameters(0),
                        playerMove.getIntParameters(1), gameBoard.getDraftPool().get(dieIndexValue));
                this.placementDone = placementMove.placeDie();
                if (isPlacementDone()) {
                    this.gameBoard.removeDieFromDraftPool(placementMove.getDie());
                    this.sendToPlayer("The die has been placed on the selected cell.");
                } else this.sendToPlayer("Incorrect move! Please try again.");
            } catch (OccupiedCellException | NotValidParametersException e) {
                e.printStackTrace();
            }
        } else
            throw new IllegalArgumentException();
    }

    /**
     * This method is called when the player is trying to use a tool that needs a placement move too.
     * It waits for a new placement move and then manages it if it arrives before the turn has ended.
     *
     * @param decoratedTool the Tool the player is trying to use
     */
    private void waitPlaceDieMove(PlaceToolDecorator decoratedTool) {
        setWaitMove();
        boolean correctMove = false;
        synchronized (this) {
            while (!correctMove) {
                while (notEndedTurn() && isWaitMove()) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (notEndedTurn()) {
                    correctMove = decoratedTool.placeDie(this, playerMove);
                    if (!correctMove) {
                        sendToPlayer("Try again placing the die!");
                        setWaitMove();
                    }
                } else {
                    correctMove = true;
                    sendToPlayer("You cannot place the die anymore!");
                }
            }
        }
    }

    /**
     * This method is called when the player is trying to use a tool that needs a placement move too.
     * It manages the first part of the move (the one that doesn't need a placement move) and then calls waitPlaceDieMove.
     *
     * @param tool            the tool the player's trying to use
     * @param toolAlreadyUsed a boolean specifying whether the tool has been already used once or not
     */
    private void needPlacementToolHandler(Tool tool, boolean toolAlreadyUsed) {
        PlaceToolDecorator decoratedTool;

        if (tool.getToolName().equals(ToolNames.FLUX_BRUSH))
            decoratedTool = new DecoratedSetDieTool(tool);
        else if (tool.getToolName().equals(ToolNames.FLUX_REMOVER))
            decoratedTool = new DecoratedChangeDieTool(tool);
        else throw new IllegalArgumentException();

        usedTool = decoratedTool.toolEffect(this, playerMove);
        if (usedTool) {
            if (!toolAlreadyUsed)
                tool.setAlreadyUsed(true);
            sendToPlayer("Please complete your move:");
            waitPlaceDieMove(decoratedTool);
        }
    }

    /**
     * Manages a common tool move. Tools that need a placement move are not included.
     *
     * @param tool            the tool the player's trying to use
     * @param toolAlreadyUsed a boolean specifying whether the tool has been already used once or not
     */
    private void toolHandler(Tool tool, boolean toolAlreadyUsed) {
        usedTool = tool.toolEffect(this, playerMove);
        if (!toolAlreadyUsed)
            tool.setAlreadyUsed(true);
    }

    /**
     * Manages a tool move.
     */
    private void useTool() {

        Optional<Integer> toolIndex = playerMove.getExtractedToolIndex();
        if (toolIndex.isPresent()) {
            Integer toolIndexValue = toolIndex.get();
            Tool tool = gameBoard.getTools().get(toolIndexValue);

            boolean toolAlreadyUsed = tool.isAlreadyUsed();
            if (this.player.hasEnoughToken(toolAlreadyUsed)) {

                if (tool.needPlacement() && !isPlacementDone()) {
                    needPlacementToolHandler(tool, toolAlreadyUsed);
                } else if (!tool.needPlacement()) {
                    toolHandler(tool, toolAlreadyUsed);
                }

                if (usedTool) {
                    this.player.useToken(toolAlreadyUsed);
                    this.sendToPlayerAndUpdate();
                } else
                    this.sendToPlayer("Incorrect move! Please try again.");
            } else this.sendToPlayer("You don't have enough favour tokens left to use this tool!");

        } else
            throw new IllegalArgumentException();


    }

}


