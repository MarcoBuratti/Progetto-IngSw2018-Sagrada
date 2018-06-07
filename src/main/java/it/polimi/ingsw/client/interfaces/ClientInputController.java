package it.polimi.ingsw.client.interfaces;

public interface ClientInputController {

    void setTool(String s);

    boolean firstInput(String s);

    boolean secondInputDie(String s);

    boolean thirdInputDie(String s);

    boolean secondInputTool(String s);
}
