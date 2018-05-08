package it.polimi.ingsw.model;
import it.polimi.ingsw.model.restriction.*;

public class Cell {
    private Restriction restriction;
    private Die die;
    private Boolean usedCell;

    /**
     * Creates a Cell object which represents a dashboard's single cell.
     * The restriction argument must specify the restriction imposed by the chosen scheme
     * on the created cell (there may also be no restriction on the cell).
     * Every cell may contain a die, but the attribute die is initialized as null.
     * The Boolean attribute usedCell specifies whether there's a die on the cell or not.
     * @param restriction the restriction on the cell (colour, number or no restriction)
     */
    public Cell (Restriction restriction) {
        this.restriction = restriction;
        this.die = null;
        this.usedCell = false;
    }

    /**
     * Returns a ColourRestriction, NumberRestriction or NoRestriction object that represents
     * the restriction imposed on the cell.
     * @return the Cell object's restriction
     */
    public Restriction getRestriction() {
        return restriction;
    }

    /**
     * If existing, it returns the Die object representing the die placed on the cell.
     * Else it returns null.
     * @return the Cell object's die
     */
    public Die getDie() {
        return die;
    }

    /**
     * Returns a Boolean object that specifies whether there is a die placed on the cell or not.
     * @return the Cell object's usedCell attribute
     */
    public Boolean getUsedCell() {
        return usedCell;
    }

    public void setDie(Die die) {
        this.die = die;
        this.usedCell = true;
    }

    /**
     * If existing, it removes a Die object that was set as the Cell object's die attribute, setting it as null,
     * setting the usedCell attribute as false and returning the removed Die object.
     * Else it returns null.
     * @return the Die object removed from the Cell
     */
    public Die removeDie() {
        if(this.usedCell) {
            Die die = this.die;
            this.die = null;
            this.usedCell = false;
            return die;
        }
        else return null;
    }

    /**
     * Returns a Boolean object that specifies whether the die that the user wants to place on
     * the cell respects the rule imposed by the cell's restriction.
     * @param die the die the user wants to place on the cell
     * @return a Boolean that specifies whether the Die object is compatible with the Cell's restriction
     */
    public Boolean allowedMove (Die die) {
        return (this.restriction.restrictionCheck(die) && !this.getUsedCell());
    }

    void dump(){
        System.out.println(this);
    }

    @Override
    public boolean equals (Object myObject) {
        if(this.getClass() == myObject.getClass()) {
            Cell myCell = (Cell) myObject;
            if ( (this.restriction.equals(myCell.restriction)) && (this.usedCell == myCell.usedCell) )
                if ( this.usedCell ) {
                    if (this.die.equals(myCell.die))
                        return true;
                    else
                        return false;
                } else return true;
            else return false;
        } else return false;
    }

    @Override
    public String toString(){
        if (this.usedCell)
            return "This cell has the following Restriction: << " + this.restriction.toString () + " >> and contains the following " + this.die.toString();
        else
            return "This cell is empty";
    }

    @Override
    public Cell clone(){
        Cell cell = new Cell(this.restriction);
        if(this.usedCell)
            cell.setDie(this.die);
        return cell;
    }
}
