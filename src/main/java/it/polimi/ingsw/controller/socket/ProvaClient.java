package it.polimi.ingsw.controller.socket;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProvaClient {
    public static void main (String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            Client client = new Client(br.readLine());
            client.run();
        }catch (IOException e){
            System.out.println(e.toString());
        }
    }
}
