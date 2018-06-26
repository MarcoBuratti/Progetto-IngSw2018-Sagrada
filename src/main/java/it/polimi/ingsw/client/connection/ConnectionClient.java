package it.polimi.ingsw.client.connection;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.interfaces.ClientInterface;
import it.polimi.ingsw.util.ClientController;
import it.polimi.ingsw.util.GraphicsClient;
import it.polimi.ingsw.util.TypeMove;

import java.io.IOException;
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
    private String RowColumn;
    private String PlusMin;
    private String DieNum;
    private String roundTrackIndex;
    private boolean moveCtrl;
    private boolean continueToPlay = false;
    private boolean isOn = true;
    private boolean inputCtrl = true;


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
                        ArrayList<String> message = (ArrayList<String> ) thirdToolMessage(toolIndex);
                        Integer toolNumber = Integer.parseInt(toolIndex);
                        toolNumber--;
                        int i = 0;
                        concatMove(toolNumber.toString());
                        for (TypeMove toolEffect : toolEffects) {
                            toolEffect.moveToDo(this);
                        }
                        inputCtrl = false;
                    }
                    if (tmpMove.equals("3") || tmpMove.equals("4")) {
                        TypeMove.CHOOSE_SEND_MOVE.moveToDo(this);
                        inputCtrl = false;
                    }
                }
            } while (inputCtrl);
            System.out.println("Move handel: " + move.toString());
        }
    }

    public void askAction() {
        moveCtrl = true;
        do {
            tmpMove = view.getInput();
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
        System.out.println("dentro ask");

    }

    public void setIndexDash() {
        moveCtrl = true;
        do {
            view.showOutput( graphicsClient.printRulesDash() );
            index = view.getInput();
            moveCtrl = secondInputDie(index);
        } while (moveCtrl);
        System.out.println("dentro index");
        concatMove(index);
    }

    public void setRowColumn() {
        moveCtrl = true;
        do {
            view.showOutput( graphicsClient.printRulesMatrix() );
            RowColumn = view.getInput();
            moveCtrl = thirdInputDie(RowColumn);
        } while (moveCtrl);
        System.out.println("dentro row");
        concatMove(RowColumn);
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
        do {
            view.showOutput( graphicsClient.printPlusMin() );
            PlusMin = view.getInput();
            moveCtrl = plusMin(PlusMin);
        } while (moveCtrl);
        concatMove(PlusMin);
    }
    //USATO PER TOOL
    public void setDieNum(){
        moveCtrl = true;
        do {
            view.showOutput( graphicsClient.printDieNum() );
            DieNum = view.getInput();
            moveCtrl = setDieNum(DieNum);
        } while (moveCtrl);
        concatMove(DieNum);
    }
    //USATO PER TOOL
    public void setRoundTrackIndex(){
        moveCtrl = true;
        do {
            view.showOutput( graphicsClient.printRoundDie() );
            roundTrackIndex = view.getInput();
            moveCtrl = roundTrackCtrl(roundTrackIndex);
        } while (moveCtrl);
        concatMove(roundTrackIndex);
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

    public List<String> thirdToolMessage(String s){
        return clientInputController.thirdToolMessage(s);
    }

    public String numberTool(String s){ return clientInputController.numberTool(s); }

    public boolean plusMin(String s){
        return clientInputController.plusMinCtrl(s);
    }

    public boolean setDieNum(String s){ return clientInputController.dieNumCtrl(s); }

    public boolean roundTrackCtrl(String s) {
        return  clientInputController.roundTrackCtrl(s);
    }

}