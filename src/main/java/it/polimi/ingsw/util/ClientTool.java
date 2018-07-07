package it.polimi.ingsw.util;


import java.util.List;

public class ClientTool {
    private String number;
    private List<TypeMove> move;

    /**
     * Create a ClientTool Object, containing a String representing the tool index (0-11) and
     * the list of the TypeMove instances read from a JSON file associated with that tool.
     * @param number a String containing the tool index
     * @param move a List of TypeMove representing the move associated with the tool
     */
    ClientTool(String number, List<TypeMove> move){
        this.number = number;
        this.move = move;
    }

    /**
     * Returns the move attribute.
     * @return a List of TypeMove
     */
    public List<TypeMove> getMove(){
        return this.move;
    }

    /**
     * Returns the number attribute.
     * @return a String
     */
    public String getNumber(){
        return this.number;
    }

}
