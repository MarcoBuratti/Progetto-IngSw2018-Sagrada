package it.polimi.ingsw.server.model;

public enum Color {
    GREEN("\u001B[32mG"), YELLOW("\u001B[33mY"), BLUE("\u001B[34mB"),
        RED("\u001B[31mR"), VIOLET("\u001B[35mV");
    private String name;

    /**
     * Creates a Color object representing a color, used for restrictions or dice.
     * @param name the name of the color represented in the Color object
     */
    Color (String name){
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
        String firstChar = "";
        return  firstChar + this.getName();
    }
}
