package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class Ciao {

    private List<Color> prova = Arrays.asList(Color.values());

    public void stampa(){

        ListIterator<Color> iterator = prova.listIterator();

        System.out.println("Traversing the list in forward direction:");
        while(iterator.hasNext()){
            System.out.println(iterator.next());
        }
        System.out.println("\nTraversing the list in backward direction:");
        while(iterator.hasPrevious()){
            System.out.println(iterator.previous());
        }

        String string = "1243";
        int a = Integer.parseInt(string);
        System.out.println(a);
        string= "12.34.52.76";
        String[] scanner= string.split("\\.");
        List<String> parameterList = new ArrayList<>();
        System.out.println(scanner[0]);
        for (int i=0;i<4;i++) {
            parameterList.add(scanner[i]);
            System.out.println(scanner[i]);
        }
        
        System.out.println("lol");
    }

}


