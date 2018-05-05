package it.polimi.ingsw.model;

public enum Colour {
    GREEN("Green"), YELLOW("Yellow"), BLUE("Blue"), RED("Red"), VIOLET("Violet");
    private String name;

    Colour (String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    @Override
    public String toString(){
        return this.getName();
    }
}
