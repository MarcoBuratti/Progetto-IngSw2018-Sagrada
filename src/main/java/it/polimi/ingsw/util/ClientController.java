package it.polimi.ingsw.util;

import it.polimi.ingsw.client.interfaces.ClientInputController;

public class ClientController implements ClientInputController {
    public String [] tool;

    @Override
    public void setTool(String s) {
        s = s.substring(s.indexOf(":") + 2);
        tool = s.split(",");
    }

    @Override
    public boolean firstInput(String s) {
        return  (!s.matches("[1-4]"));
    }

    @Override
    public boolean secondInputDie(String s) {
        return (!s.matches("[0-8]"));
    }

    @Override
    public boolean thirdInputDie(String s) {
        if (!s.matches("[0-3]\\s[0-4]")) {
            System.out.println("male");
            return true;
        }
        String [] ctrl = s.split(" ");
        System.out.println("0000" + ctrl[0] + "11111" + ctrl[1]);
        if(ctrl.length != 2)    return true;
        if(Integer.parseInt( ctrl[0] ) < 0 || Integer.parseInt( ctrl[0] ) > 3) return true;
        if(Integer.parseInt( ctrl[1] ) < 0 || Integer.parseInt( ctrl[1] ) > 4) return true;
        else return false;
    }

    @Override
    public boolean secondInputTool(String s) {
        return false;
    }

}
