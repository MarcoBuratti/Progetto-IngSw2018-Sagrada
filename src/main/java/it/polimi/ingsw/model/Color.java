package it.polimi.ingsw.model;

public enum Color {
    GREEN("Green"), YELLOW("Yellow"), BLUE("Blue"), RED("Red"), VIOLET("Violet");
    private String name;

    Color (String name){
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
