package it.polimi.ingsw.util;



public class InputController {

    /**
     * Returns a boolean specifying whether the string s contains the space character or not.
     * @param s the string the user wants to check
     * @return a boolean
     */
    public boolean nameController(String s) {
        return s.contains(" ");
    }

    /**
     * Returns a boolean specifying if the string value is 1 or 2.
     * @param s the input String
     * @return a boolean
     */
    public boolean connectionController(String s) {
        return  (!s.matches("[1-2]"));
    }

    /**
     * Returns a boolean specifying whether the ip address contained in the string s is written in the correct form or not.
     * @param s the string containing the ip address
     * @return a boolean
     */
    public boolean ipController(String s) {

        String[] ctrl = s.split("\\.");
        if (ctrl.length != 4) return true;
        int j;
        for (int i = 0; i < 4; i++) {
            if (!ctrl[i].matches("[0-9]+")) return true;
            j = Integer.parseInt(ctrl[i]);
            if (j < 0 || j > 255) return true;
        }
        return false;
    }

    /**
     * Returns a boolean specifying whether the port contained in the string s is written in the correct form or not.
     * @param s the string containing the port
     * @return a boolean
     */
    public boolean portController(String s) {
        if (!s.matches("[0-9]+")) return true;
        else return Integer.parseInt(s) <= 1024 || Integer.parseInt(s) >= 65535;
    }


    /**
     * Returns a boolean specifying whether the string value is included between 1 and 4 ( boundaries included ) or not.
     * @param s the input String
     * @return a boolean
     */
    public boolean schemeController(String s){
        return  (!s.matches("[1-4]"));
    }

    //TODO DA TOGLIERE
    public boolean continueToPlayController(String s){
        return  (!s.matches("[0-1]"));
    }

}
