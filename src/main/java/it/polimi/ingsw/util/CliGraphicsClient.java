package it.polimi.ingsw.util;

import it.polimi.ingsw.client.interfaces.GraphicsInterface;

import java.util.StringTokenizer;

public class CliGraphicsClient implements GraphicsInterface {

    public void insert() {
        System.out.println("        \033[31;1mWelcome\033[0m");
        System.out.println("Please insert your nickname: ");
    }

    public void printConnection(){
        System.out.println("Press 1 to use Socket, 2 to use RMI.");
    }

    public void printGeneric(String s){
        System.out.println(s);
    }

    public void printMatrix(String s){

        /*StringTokenizer strtok = new StringTokenizer(s, " ");
        StringBuilder bld = new StringBuilder();
        while(strtok.hasMoreTokens()){
            String myToken = strtok.nextToken();
            if(myToken.equals("matrix"))
                ;
            else if(myToken.equals("NOR"))
                bld.append(" [ ] ");
            else
                bld.append(myToken + "[ ] ");
        }*/
        System.out.println(s);
    }
}
