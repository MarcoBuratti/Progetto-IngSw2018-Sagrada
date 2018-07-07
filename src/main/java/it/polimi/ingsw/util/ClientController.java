package it.polimi.ingsw.util;

import java.util.List;

public class ClientController {
    public ClientTool[] tool;

    /**
     * Saves the reference to the tools reading the related json files.
     *
     * @param s a String containing the tools' names
     */
    public void setTool(String s) {
        tool = ToolParser.readTools(s);
    }

    /**
     * Returns a boolean specifying whether the string value is included between 1 and 4 ( boundaries included ) or not.
     *
     * @param s the input String
     * @return a boolean
     */
    public boolean firstInput(String s) {
        return (!s.matches("[1-4]"));
    }

    /**
     * Returns a boolean specifying whether the string value is included between 0 and 8 ( boundaries included ) or not.
     *
     * @param s the input String
     * @return a boolean
     */
    public boolean secondInputDie(String s) {
        return (!s.matches("[0-8]"));
    }

    /**
     * Returns a boolean specifying whether the string contains two values separated from a space
     * ( the first one included between 0 and 3 ( boundaries included ) and the second one included between 0 and 4 ( boundaries included ) or not.
     *
     * @param s the input String
     * @return a boolean
     */
    public boolean thirdInputDie(String s) {
        if (!s.matches("[0-3]\\s[0-4]")) return true;
        String[] ctrl = s.split(" ");
        return ctrl.length != 2;
    }

    /**
     * Returns a boolean specifying whether the string value is included between 1 and 3 ( boundaries included ) or not.
     *
     * @param s the input String
     * @return a boolean
     */
    public boolean secondToolIndex(String s) {
        return (!s.matches("[1-3]"));
    }

    /**
     * Returns a List of TypeMove instances containing the TypeMove Objects used to construct the player move associated with the corresponding tool.
     *
     * @param s the input String
     * @return a List of TypeMove Objects
     */
    public List<TypeMove> thirdToolInput(String s) {
        int i = Integer.parseInt(s) - 1;
        return tool[i].getMove();

    }

    /**
     * Returns a boolean specifying if the string value is 0 or 1.
     *
     * @param s the input String
     * @return a boolean
     */
    public boolean plusMinCtrl(String s) {
        return (!s.matches("[0-1]"));
    }

    /**
     * Returns a boolean specifying whether the string value is included between 1 and 6 ( boundaries included ) or not.
     *
     * @param s the input String
     * @return a boolean
     */
    public boolean dieNumCtrl(String s) {
        return (!s.matches("[1-6]"));
    }

    /**
     * Returns a String containing the tool index
     *
     * @param s the index of the tool in the array of tools
     * @return a String
     */
    public String numberTool(String s) {
        int i = Integer.parseInt(s) - 1;
        return tool[i].getNumber();
    }

    /**
     * Returns a boolean specifying whether the string contains two values separated from a space
     * ( the first one included between 0 and 9 ( boundaries included ) and the second one included between 0 and 8 ( boundaries included ) or not.
     *
     * @param s the input String
     * @return a boolean
     */
    public boolean roundTrackCtrl(String s) {
        if (!s.matches("[0-9]\\s[0-8]")) return true;
        String[] ctrl = s.split(" ");
        return ctrl.length != 2;
    }
}
