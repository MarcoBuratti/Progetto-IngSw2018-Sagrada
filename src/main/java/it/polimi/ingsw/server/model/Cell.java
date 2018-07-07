package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exception.OccupiedCellException;
import it.polimi.ingsw.server.model.restriction.Restriction;

public class Cell {
    private Restriction restriction;
    private Die die;
    private boolean usedCell;

    /**
     * Creates a Cell object which represents a dashboard's single cell.
     * The restriction argument must specify the restriction imposed by the chosen scheme
     * on the created cell (there may also be no restriction on the cell).
     * Every cell may contain a die, but the attribute die is initialized as null.
     * The boolean attribute usedCell specifies whether there's a die on the cell or not.
     *
     * @param restriction the restriction on the cell (colour, number or no restriction)
     */
    public Cell(Restriction restriction) {
        this.restriction = restriction;
        this.die = null;
        this.usedCell = false;
    }

    /**
     * Returns a ColorRestriction, ValueRestriction or NoRestriction object that represents
     * the restriction imposed on the cell.
     *
     * @return the Cell object's restriction
     */
    public Restriction getRestriction() {
        return restriction;
    }

    /**
     * If existing, it returns the Die object representing the die placed on the cell.
     * Else it returns null.
     *
     * @return the Cell object's die
     */
    public Die getDie() {
        return die;
    }

    /**
     * Allows the user to set a Die object as the Cell object's die attribute if the cell is not occupied.
     *
     * @param die the die the user wants to place on the cell
     * @throws OccupiedCellException if the user tries to set a die on an occupied cell
     */
    public void setDie(Die die) throws OccupiedCellException {
        if (!this.usedCell) {
            this.die = die;
            this.usedCell = true;
        } else throw new OccupiedCellException();
    }

    /**
     * Returns a boolean that specifies whether there is a die on the cell or not.
     *
     * @return the Cell object's usedCell attribute
     */
    public boolean getUsedCell() {
        return usedCell;
    }

    /**
     * If existing, it removes a Die object that was set as the Cell object's die attribute, setting it as null,
     * setting the usedCell attribute as false and returning the removed Die object.
     * Else it returns null.
     *
     * @return the Die object removed from the Cell
     */
    Die removeDie() {
        if (this.usedCell) {
            Die myDie = this.die;
            this.die = null;
            this.usedCell = false;
            return myDie;
        } else return null;
    }

    /**
     * Returns a boolean that specifies whether the die that the user wants to place on
     * the cell respects the rule imposed by the cell's restriction or not.
     *
     * @param die the die the user wants to place on the cell
     * @return a boolean that specifies whether the Die object is compatible with the Cell's restriction
     */
    public boolean allowedMove(Die die) {
        return (this.restriction.restrictionCheck(die) && !this.getUsedCell());
    }


    /**
     * Returns a Cell object which is a copy of the original Cell object, having its same attributes.
     *
     * @return a copy of the Cell object.
     */
    Cell copyConstructor() throws OccupiedCellException {
        Cell cell = new Cell(this.restriction);
        if (this.usedCell)
            cell.setDie(this.die);
        return cell;
    }

    /**
     * Compares two Cell Objects.
     *
     * @param myCell the Cell Object the user wants to compare to the one this method is called from
     * @return a boolean specifying whether the Cell Object has all the same attributes as myCell argument
     */
    boolean cellEquals(Cell myCell) {

        if ((this.restriction.restrictionEquals(myCell.restriction)) && (this.usedCell == myCell.usedCell)) {
            if (this.usedCell) {
                return (this.die.equals(myCell.die));
            } else return true;
        }

        return false;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        if (this.usedCell)
            return "This cell has the following Restriction: << " + this.restriction.toString() + "\033[0m" + " >> and contains the following " + this.die.toString();
        else
            return "This cell is empty and has the following Restriction: << " + this.restriction.toString() + "\033[0m >>";
    }


}
