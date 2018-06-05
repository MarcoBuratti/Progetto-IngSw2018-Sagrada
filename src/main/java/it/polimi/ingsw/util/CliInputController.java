package it.polimi.ingsw.util;


import it.polimi.ingsw.client.interfaces.CliController;

public class CliInputController implements CliController {

    public boolean nameController(String s){
        return s.contains(" ");
    }

    public boolean connecionController( int i){
        if(i == 1 || i == 2) return  false;
        else return  true;
    }

    public boolean ipController(String s){

        String [] ctrl = s.split("\\.");
        if (ctrl.length != 4) return true;
        int j;
        for(int i = 0; i<3; i++) {
            if(!ctrl[i].matches("[0-9]+")) return true;
            j = Integer.parseInt(ctrl[i]);
            if(j < 0 || j > 255)    return true;
        }
        return false;
    }

    public boolean portController(int s){
        if(s<=1024 || s>=65535) return true;
        else return false;
    }

}
