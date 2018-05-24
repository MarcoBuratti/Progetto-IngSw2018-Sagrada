package it.polimi.ingsw.server.model;

public enum Color {
    GREEN("Green"), YELLOW("Yellow"), BLUE("Blue"), RED("Red"), VIOLET("Violet");
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
        return this.getName();
    }
}
