package it.polimi.ingsw.controller.action;

public class Placement {
    private int row;
    private int column;
    private int indexDieOnDiceStock;

    public Placement(String input) {
        this.row=Integer.parseInt(input.substring(0,input.indexOf(" ")));
        input=input.substring(input.indexOf("")+1);
        this.column=Integer.parseInt(input.substring(0,input.indexOf("")));
        input=input.substring(input.indexOf("")+1);
        this.indexDieOnDiceStock=Integer.parseInt(input);

    }

    public int getRow(){
        return this.row;
    }

    public int getColumn() {
        return column;
    }

    public int getIndexDieOnDiceStock() {
        return indexDieOnDiceStock;
    }
}
