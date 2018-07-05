package it.polimi.ingsw.client.connection;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.interfaces.ClientInterface;
import it.polimi.ingsw.util.ClientController;
import it.polimi.ingsw.util.GraphicsClient;
import it.polimi.ingsw.util.TypeMove;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public abstract class ConnectionClient extends Observable implements  ClientInterface {

    private ClientController clientInputController = new ClientController();
    private GraphicsClient graphicsClient = new GraphicsClient();
    private String tmpMove;
    private StringBuilder move;
    private View view;
    private String toolIndex;
    private String index;
    private boolean moveCtrl;
    private boolean continueToPlay = false;
    private boolean isOn = true;
    private boolean inputCtrl = true;
    private boolean waitOn;
    private boolean toolBreakFlag = false;


    public void setView(View view) {
        this.view = view;
    }

    public void game(){

        while (getIsOn()) {

            inputCtrl = true;
            move = new StringBuilder();
            do {
                askAction();
                if(getIsOn()) {
                    if (tmpMove.equals("1")) {
                        TypeMove.CHOOSE_INDEX_DIE.moveToDo(this);
                        TypeMove.CHOOSE_ROW_COLUMNS.moveToDo(this);
                        TypeMove.CHOOSE_SEND_MOVE.moveToDo(this);
                        inputCtrl = false;
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
                        for(int i = 0; i < toolEffects.size() && !isToolBreakFlag(); i++) {
                            toolEffects.get(i).moveToDo(this);
                            synchronized ( this ) {
                                while (isWaitOn()) {
                                    try {
                                        wait();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                        inputCtrl = false;
                    }
                    if (tmpMove.equals("3") || tmpMove.equals("4")) {
                        TypeMove.CHOOSE_SEND_MOVE.moveToDo(this);
                        inputCtrl = false;
                    }
                }
            } while (inputCtrl);
            System.out.println("Move handle: " + move.toString());
        }
    }

    public void askAction() {
        moveCtrl = true;
        do {
            tmpMove = view.getAction();
            if (getContinueToPlay()) {
                setIsOn(false);
                view.continueToPlay(tmpMove);
                moveCtrl = false;
                inputCtrl = false;
            }else {
                moveCtrl = firstInput(tmpMove);
                if (moveCtrl) view.showOutput(graphicsClient.printRulesFirst());
            }
        } while (moveCtrl);
        concatMove(tmpMove);

    }

    public void newPlaceMove(){
        move = new StringBuilder();
        concatMove("1");
        concatMove(index);
        setWaitOn(true);
    }

    public synchronized void setWaitOn( boolean bool ) {
        this.waitOn = bool;
        if ( !bool ) {
            notifyAll();
        }
    }

    public synchronized boolean isWaitOn () {
        return this.waitOn;
    }

    public void setIndexDash() {
        moveCtrl = true;
        do {
            index = view.getIndex();
            moveCtrl = secondInputDie(index);
        } while (moveCtrl);
        concatMove(index);
    }

    public void setRowColumn() {
        moveCtrl = true;
        String rowColumn;
        do {
            view.showOutput( graphicsClient.printRulesMatrix() );
            rowColumn = view.getInput();
            moveCtrl = thirdInputDie(rowColumn);
        } while (moveCtrl);
        System.out.println("dentro row");
        concatMove(rowColumn);
    }

    public void setToolIndex(){
        moveCtrl = true;
        do {
            view.showOutput( graphicsClient.printToolIndex() );
            toolIndex = view.getInput();
            moveCtrl = secondToolIndex(toolIndex);
        } while (moveCtrl);
    }
    //USATO PER TOOL
    public void setPlusMin(){
        moveCtrl = true;
        String plusMin;
        do {
            view.showOutput( graphicsClient.printPlusMin() );
            plusMin = view.getInput();
            moveCtrl = plusMin(plusMin);
        } while (moveCtrl);
        concatMove(plusMin);
    }
    //USATO PER TOOL
    public void setDieNum(){
        moveCtrl = true;
        String dieNum;
        do {
            view.showOutput( graphicsClient.printDieNum() );
            dieNum = view.getInput();
            moveCtrl = setDieNum(dieNum);
        } while (moveCtrl);

        concatMove(dieNum);
    }
    //USATO PER TOOL
    public void setRoundTrackIndex(){
        moveCtrl = true;
        String roundTrackIndex;
        do {
            view.showOutput( graphicsClient.printRoundDie() );
            roundTrackIndex = view.getInput();
            moveCtrl = roundTrackCtrl(roundTrackIndex);
        } while (moveCtrl);
        concatMove(roundTrackIndex);
    }

    public void goOn(){
        moveCtrl = true;
        String goOnString;
        do {
            view.showOutput( graphicsClient.printGoOn() );
            goOnString = view.getInput();
            moveCtrl = plusMin(goOnString);
        } while (moveCtrl);

        if (goOnString.equals("0")) {
            setToolBreakFlag(true);
            TypeMove.CHOOSE_SEND_MOVE.moveToDo(this);
        }

    }

    public void sendMove(){
        handleMove(move.toString());
    }

    private void concatMove(String s){
        move.append(s + " ");
        System.out.println("move: " + move.toString());
    }

    public synchronized boolean getIsOn() {
        return isOn;
    }

    public synchronized void setIsOn(boolean bool){
        this.isOn = bool;
    }

    public synchronized boolean getContinueToPlay(){
        return  continueToPlay;
    }

    public synchronized void setContinueToPlay(boolean bool){
        this.continueToPlay = bool;
    }

    public void setTool(String s) {
        clientInputController.setTool(s);
    }

    public boolean firstInput(String s) {
        return clientInputController.firstInput(s);
    }

    public boolean secondInputDie(String s) {
        return clientInputController.secondInputDie(s);
    }

    public boolean thirdInputDie(String s) {
        return clientInputController.thirdInputDie(s);
    }

    public boolean secondToolIndex(String s) {
        return clientInputController.secondToolIndex(s);
    }

    public List<TypeMove> thirdToolInput(String s){
        return clientInputController.thirdToolInput(s);
    }

    /*public List<String> thirdToolMessage(String s){
        return clientInputController.thirdToolMessage(s);
    }*/

    public String numberTool(String s){ return clientInputController.numberTool(s); }

    public boolean plusMin(String s){
        return clientInputController.plusMinCtrl(s);
    }

    public boolean setDieNum(String s){ return clientInputController.dieNumCtrl(s); }

    public boolean roundTrackCtrl(String s) {
        return  clientInputController.roundTrackCtrl(s);
    }

    public synchronized void setToolBreakFlag(boolean bool){
        this.toolBreakFlag = bool;
    }

    public synchronized boolean isToolBreakFlag() {
        return toolBreakFlag;
    }

    public void checkMessage(String str){

        if (str.equals("Please complete your move:"))
            setWaitOn(false);
        else if (str.equals("You cannot place this die anyway!") || str.equals("It's not your turn. Please wait.")){
            setToolBreakFlag(true);
            setWaitOn(false);
        }

    }
}