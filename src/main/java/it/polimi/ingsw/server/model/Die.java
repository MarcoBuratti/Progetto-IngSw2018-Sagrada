package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exception.NotValidValueException;

import java.util.Random;

public class Die {
    private static final int NUMBER_OF_SIDES = 6;
    private int number;
    private Color color;

    /**
     * Creates a Die object, which represents a die.
     * The colour argument must specify the die's colour.
     * The number on the side facing upwards is randomly selected between 1 and the number of sides of the dice (boundaries included).
     * @param color the colour of the die that the user wants to create
     */
    public Die (Color color){
        Random random  = new Random();
        this.color = color;
        this.number = random.nextInt(NUMBER_OF_SIDES) + 1;
    }

    /**
     * Returns an int that represents the number on the die's side facing upwards.
     * @return the number of the Die object
     */
    public int getNumber(){
        return number;
    }

    /**
     * Returns a Colour object which represents the die's colour.
     * @return the colour of the Die object
     */
    public Color getColor() {
        return color;
    }

    /**
     * Gives the user the possibility to set the die's number (choosing the side facing upwards).
     * The user is allowed to set an int between 1 and the number of sides of the die (boundaries included).
     * @param number the number that the user wants to set.
     * @throws NotValidValueException if the user tries to use a number which is not included in the allowed range.
     */
    public void setNumber(int number) throws NotValidValueException {
            if (number > 0 && number <= NUMBER_OF_SIDES)
                this.number = number;
            else
                throw new NotValidValueException();
    }

    /**
     * The die's number is randomly selected again.
     * The number is randomly selected between 1 and the number of sides of the dice (boundaries included).
     */
    public void extractAgain () {
        Random random = new Random();
        this.number = random.nextInt(NUMBER_OF_SIDES) + 1;
    }


    @Override
    /**
     * Returns a string which represents the die, specifying its colour and the number on its side facing upwards.
     */
    public String toString(){
        return "Die: Color " + this.color + " , Number: " + this.number;
    }

    @Override
    public boolean equals (Object myObject) {
        if( this.getClass() == myObject.getClass() ) {
            Die myDie = (Die) myObject;
            return ( (this.color.equals(myDie.color)) && (this.number == myDie.number) );
        } else return false;
    }
}