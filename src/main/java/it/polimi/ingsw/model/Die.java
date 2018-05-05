package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.NumberNotValidException;

import java.util.Random;

public class Die {
    private static final int NUMBER_OF_SIDES = 6;
    private int number;
    private Colour colour;

    public Die (Colour colour){
        Random random  = new Random();
        this.colour = colour;
        this.number = random.nextInt(NUMBER_OF_SIDES) + 1;
    }

    public int getNumber(){
        return number;
    }

    public Colour getColour() {
        return colour;
    }

    public void setNumber(int number) throws NumberNotValidException {
        try {
            if (number > 0 && number < 7)
                this.number = number;
            else
                throw new NumberNotValidException();
        }
        catch (Exception e){
            System.out.println(e);
        }

    }

    public void extractAgain () {
        Random random = new Random();
        this.number = random.nextInt(NUMBER_OF_SIDES) + 1;
    }

    void dump(){
        System.out.println(this);
    }

    @Override
    public String toString(){
        return "Die: Colour " + this.colour + " , Number: " + this.number + "\n";
    }
}
