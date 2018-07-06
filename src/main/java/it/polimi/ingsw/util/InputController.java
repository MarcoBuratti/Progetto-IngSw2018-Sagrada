package it.polimi.ingsw.util;



public class InputController {

    /**
     *
     * @param s
     * @return
     */
    public boolean nameController(String s) {
        return s.contains(" ");
    }

    public boolean connectionController(String s) {
        return  (!s.matches("[1-2]"));
    }

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

    public boolean portController(String s) {
        if (!s.matches("[0-9]+")) return true;
        else if (Integer.parseInt(s) <= 1024 || Integer.parseInt(s) >= 65535) return true;
        else return false;
    }

    public boolean schemeController(String s){
        return  (!s.matches("[1-4]"));
    }

    public boolean continueToPlayController(String s){
        return  (!s.matches("[0-1]"));
    }

}
