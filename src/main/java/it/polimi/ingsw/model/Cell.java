package it.polimi.ingsw.model;

public class Cell {
    private Restriction restriction;
    private Die die;
    private Boolean usedCell;

    public Cell (Restriction restriction) {
        this.restriction = restriction;
        this.usedCell = false;
    }

    public Restriction getRestriction() {
        return restriction;
    }

    public Die getDie() {
        return die;
    }

    public Boolean getUsedCell() {
        return usedCell;
    }

    public void setDie(Die die) {
        this.die = die;
        this.usedCell = true;
    }

    public Die removeDie() {
        Die die = this.die;
        this.die = null;
        this.usedCell = false;
        return die;
    }

    public Boolean allowedMove (Die die) {
        return true;
        //da implementare
    }

    void dump(){
        System.out.println(this);
    }

    @Override
    public String toString(){
        if (this.usedCell)
            return "This cell has he following Restriction: << " + this.restriction.toString () + " >> and contains the following " + this.die.toString();
        else
            return "This cell is empty";
    }
}
