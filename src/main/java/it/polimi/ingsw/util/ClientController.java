package it.polimi.ingsw.util;

import java.util.List;

public class ClientController {
    public ToolClient [] tool;

    public void setTool(String s) {
        ParserTool parserTool = new ParserTool(s);
        tool = parserTool.getToolClients();
    }

    public boolean firstInput(String s) {
        return  (!s.matches("[1-4]"));
    }

    public boolean secondInputDie(String s) {
        return (!s.matches("[0-8]"));
    }

    public boolean thirdInputDie(String s) {
        if (!s.matches("[0-3]\\s[0-4]")) return true;
        String [] ctrl = s.split(" ");
        if(ctrl.length != 2)    return true;
        else return false;
    }

    public boolean secondToolIndex(String s) {
        return (!s.matches("[1-3]"));
    }

    public List<TypeMove> thirdToolInput(String s){
        int i = Integer.parseInt(s) -1;
        return tool[i].getMove();

    }

    public List<String> thirdToolMessage(String s){
        int i = Integer.parseInt(s) - 1;
        return tool[i].getMessage();
    }

    public boolean  plusMinCtrl(String s){
        return  (!s.matches("[0-1]"));
    }

    public boolean dieNumCtrl(String s){ return  (!s.matches("[1-6]")); }

    public String numberTool(String s){
        int i = Integer.parseInt(s) -1;
        return tool[i].getNumber();
    }

    public boolean roundTrackCtrl(String s) {
        if (!s.matches("[0-9]\\s[0-8]")) return true;
        String [] ctrl = s.split(" ");
        if(ctrl.length != 2)    return true;
        else return false;
    }
}
