package it.polimi.ingsw.util;


import java.util.List;

public class ToolClient {
    private String number;
    private List<TypeMove> move;
    private List<String> message;

    public ToolClient (List<String> message, String number, List<TypeMove> move){
        this.message = message;
        this.number = number;
        this.move = move;
    }

    public List<TypeMove> getMove(){
        return this.move;
    }

    public List<String> getMessage(){return this.message; }

    public String getNumber(){
        return this.number;
    }

}
