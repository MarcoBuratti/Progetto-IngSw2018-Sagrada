package it.polimi.ingsw.model;

import it.polimi.ingsw.model.restriction.*;

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
        return this.restriction.restrictionCheck(die);
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

    @Override
    public Cell clone(){
        Cell cell= new Cell(this.getRestriction());

        if(getUsedCell())
            cell.setDie(this.getDie());

        return cell;

    }
}
