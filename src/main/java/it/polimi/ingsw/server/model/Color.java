package it.polimi.ingsw.server.model;

public enum Color {
    GREEN("Green", "\u001B[32mG"), YELLOW("Yellow", "\u001B[33mY"), BLUE("Blue", "\u001B[34mB"),
        RED("Red", "\u001B[31mR"), VIOLET("Violet", "\u001B[35mV");
    private String colorPrint;
    private String name;

    /**
     * Creates a Color object representing a color, used for restrictions or dice.
     * @param name the name of the color represented in the Color object
     */
    Color (String name, String colorPrint){
        this.colorPrint = colorPrint;
        this.name = name;
    }

    /**
     * Returns a String object representing the color.
     * @return the Color object's name attribute
     */
    public String getName(){
        return this.name;
    }

    @Override
    public String toString(){
        return  this.colorPrint;
    }
}
