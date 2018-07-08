package it.polimi.ingsw.client.connection;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.interfaces.ClientInterface;
import it.polimi.ingsw.util.ClientController;
import it.polimi.ingsw.util.TypeMove;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public abstract class ConnectionClient extends Observable implements ClientInterface {

    private ClientController clientInputController = new ClientController();
    private String tmpMove;
    private StringBuilder move;
    private View view;
    private String toolIndex;
    private String index;
    private boolean moveCtrl;
    private boolean isOn = true;
    private boolean inputCtrl = false;
    private boolean waitOn;
    private boolean toolBreakFlag = false;
    private int moveIndex;
    private int i = 0;

    /**
     * Sets the view attribute as the view argument.
     *
     * @param view the View Object
     */
    public void setView(View view) {
        this.view = view;
    }

    /**
     * Manages the input during a game.
     */
    public void game() {

        while (getIsOn()) {


            while (inputCtrl) {
                move = new StringBuilder();
                askAction();
                if (getIsOn()) {
                    if (tmpMove.equals("1")) {

                        TypeMove.CHOOSE_INDEX_DIE.moveToDo(this);
                        TypeMove.PLACE_COORDINATES.moveToDo(this);
                        TypeMove.CHOOSE_SEND_MOVE.moveToDo(this);

                    }
                    if (tmpMove.equals("2")) {

                        TypeMove.CHOOSE_TOOL_INDEX.moveToDo(this);
                        String numberOfTool = numberTool(toolIndex);
                        concatMove(numberOfTool);
                        ArrayList<TypeMove> toolEffects = (ArrayList<TypeMove>) thirdToolInput(toolIndex);
                        Integer toolNumber = Integer.parseInt(toolIndex);
                        toolNumber--;
                        concatMove(toolNumber.toString());
                        toolBreakFlag = false;

                        for (i = 0; i < toolEffects.size() && !isToolBreakFlag(); i++) {

                            toolEffects.get(i).moveToDo(this);
                            synchronized (this) {
                                while (isWaitOn()) {
                                    try {
                                        wait();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                        }

                    }
                    if (tmpMove.equals("3") || tmpMove.equals("4")) {
                        TypeMove.CHOOSE_SEND_MOVE.moveToDo(this);

                    }
                }
            }
        }
    }

    /**
     * Asks to the player the action he wants to make.
     */
    private void askAction() {
        moveCtrl = true;
        do {
            tmpMove = view.getAction();
            moveCtrl = firstInput(tmpMove);
        } while (moveCtrl);

        concatMove(tmpMove);

    }

    /**
     * Initialize a move as a new StringBuilder Object and appends to it the value needed to create a placement move.
     * It also saves the value of current index i - 1 in moveIndex to restore the call to the right method in the
     * for cycle inside the use tool section in game method.
     */
    public void newPlaceMove() {
        move = new StringBuilder();
        concatMove("1");
        concatMove(index);
        moveIndex = i - 1;
    }

    /**
     * Returns the waitOn attribute.
     *
     * @return a boolean
     */
    private synchronized boolean isWaitOn() {
        return this.waitOn;
    }

    /**
     * Sets the waitOn attribute as bool.
     *
     * @param bool the value the user wants to set
     */
    public synchronized void setWaitOn(boolean bool) {
        this.waitOn = bool;
        if (!bool) {
            notifyAll();
        }
    }

    /**
     * Asks the user for the index of the chosen scheme until it's correct and then appends it to move.
     */
    public void setIndexDash() {
        moveCtrl = true;
        do {
            index = view.getIndex();
            moveCtrl = secondInputDie(index);
        } while (moveCtrl);

        concatMove(index);
    }

    /**
     * Asks the user for the coordinates until they're correct and then appends them to move.
     */
    public void setRowColumn( boolean place ) {
        moveCtrl = true;
        String rowColumn;
        do {
            rowColumn = view.getRowColumn(place);
            moveCtrl = thirdInputDie(rowColumn);
        } while (moveCtrl);
        concatMove(rowColumn);
    }

    /**
     * Asks the user the index of the chosen tool until it's correct and then appends it to move.
     */
    public void setToolIndex() {
        moveCtrl = true;
        do {
            toolIndex = view.getTool();
            moveCtrl = secondToolIndex(toolIndex);
        } while (moveCtrl);
    }

    /**
     * Asks the user a value that must be 0 or 1 and appends it to move.
     */
    public void setPlusMin() {
        moveCtrl = true;
        String plusMin;
        do {
            plusMin = view.getPlusOrMinus();
            moveCtrl = plusMin(plusMin);
        } while (moveCtrl);
        concatMove(plusMin);
    }

    /**
     * Asks the user for the index of the chosen die until it's acceptable and then appends it to move.
     */
    public void setDieNum() {
        moveCtrl = true;
        String dieNum;
        do {
            dieNum = view.getNumber();
            moveCtrl = setDieNum(dieNum);
        } while (moveCtrl);

        concatMove(dieNum);
    }

    /**
     * Asks the user for the index of the chosen round until it's acceptable and then appends it to move.
     */
    public void setRoundTrackIndex() {
        moveCtrl = true;
        String roundTrackIndex;
        do {
            roundTrackIndex = view.getRoundTrack();
            moveCtrl = roundTrackCtrl(roundTrackIndex);
        } while (moveCtrl);
        concatMove(roundTrackIndex);
    }

    /**
     * Asks the user if he wants to place a second die or not (tool) :
     * Sends the move if the player wants to place one die, asks for the second die coordinates if he wants to place two dice.
     */
    public void goOn() {
        moveCtrl = true;
        String goOnString;
        do {
            goOnString = view.getDieNumber();
            moveCtrl = plusMin(goOnString);
        } while (moveCtrl);

        if (goOnString.equals("0")) {
            setToolBreakFlag();
            TypeMove.CHOOSE_SEND_MOVE.moveToDo(this);
        }

    }

    /**
     * Calls handle move using move.toString() as parameter.
     */
    public void sendMove() {
        handleMove(move.toString());
    }

    /**
     * Sets waitOn as true and sends the move to the server.
     */
    public void sendMoveAndWait() {
        setWaitOn(true);
        handleMove(move.toString());
    }

    /**
     * Appends s and a space to move.
     *
     * @param s the string to append
     */
    private void concatMove(String s) {
        move.append(s).append(" ");
    }

    /**
     * Returns the isOn attribute.
     *
     * @return a boolean
     */
    public synchronized boolean getIsOn() {
        return isOn;
    }

    /**
     * Sets the isOn attribute as bool.
     *
     * @param bool a boolean
     */
    public synchronized void setIsOn(boolean bool) {
        this.isOn = bool;
    }

    /**
     * Allows the user to set the extracted tools in ClientTool.
     *
     * @param s the name of the tool
     */
    public void setTool(String s) {
        clientInputController.setTool(s);
    }

    /**
     * Returns true if the input is 1, 2, 3 or 4.
     *
     * @param s the String to check
     * @return a boolean
     */
    private boolean firstInput(String s) {
        return clientInputController.firstInput(s);
    }

    /**
     * Returns true if the input is included between 1 and 9 ( boundaries included ).
     *
     * @param s the String to check
     * @return a boolean
     */
    private boolean secondInputDie(String s) {
        return clientInputController.secondInputDie(s);
    }

    /**
     * Returns true if the input is a String containing two values separated by a space
     * and values are acceptable as row-column indexes.
     *
     * @param s the String to check
     * @return a boolean
     */
    private boolean thirdInputDie(String s) {
        return clientInputController.thirdInputDie(s);
    }

    /**
     * Returns true if the value contained in the string is acceptable as tool index (1,2,3).
     *
     * @param s the String to check
     * @return a boolean
     */
    private boolean secondToolIndex(String s) {
        return clientInputController.secondToolIndex(s);
    }

    /**
     * Returns a List containing the TypeMove Enum classes.
     *
     * @param s the String to check
     * @return a List of TypeMove Objects
     */
    private List<TypeMove> thirdToolInput(String s) {
        return clientInputController.thirdToolInput(s);
    }

    /**
     * Returns a String representing the tool index ( 0-11 ). This is necessary to read create a PlayerMove Object.
     *
     * @param s the String to check
     * @return a String
     */
    private String numberTool(String s) {
        return clientInputController.numberTool(s);
    }

    /**
     * Returns true if the value contained in s is 0 or 1.
     *
     * @param s the String to check
     * @return a boolean
     */
    private boolean plusMin(String s) {
        return clientInputController.plusMinCtrl(s);
    }

    /**
     * Returns true if the value contained in s is included between 1 and 9 ( boundaries included ).
     *
     * @param s the String to check
     * @return a boolean
     */
    private boolean setDieNum(String s) {
        return clientInputController.dieNumCtrl(s);
    }

    /**
     * Returns true if the value contained in the String is included between 1 and 10 ( boundaries included ).
     *
     * @param s the String to check
     * @return a boolean
     */
    private boolean roundTrackCtrl(String s) {
        return clientInputController.roundTrackCtrl(s);
    }

    /**
     * Sets the toolBreakFlag attribute as true.
     */
    private synchronized void setToolBreakFlag() {
        this.toolBreakFlag = true;
    }

    /**
     * Returns the toolBreakFlag attribute.
     *
     * @return a boolean
     */
    private synchronized boolean isToolBreakFlag() {
        return toolBreakFlag;
    }

    /**
     * Checks if the message received from the server is a message that should unlock some methods using setWaitOn.
     *
     * @param str the String received from the server
     */
    protected void checkMessage(String str) {

        switch (str) {
            case "Please complete your move:":
                setWaitOn(false);
                break;
            case "You cannot place this die anyway!":
            case "It's not your turn. Please wait.":
            case "You don't have enough favour tokens left to use this tool!":
            case "You cannot place the die anymore!":
            case "The die has been placed on the selected cell.":
                setToolBreakFlag();
                setWaitOn(false);
                break;
            case "Try again placing the die!":
                i = moveIndex;
                setWaitOn(false);
                break;
            default:
                break;
        }

    }

    /**
     * Sets the inputCtrl attribute as the inputCtrl parameter.
     *
     * @param inputCtrl the boolean the user wants to set
     */
    public void setInputControl(boolean inputCtrl) {
        this.inputCtrl = inputCtrl;
    }

}
