package it.polimi.ingsw.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class ParserScheme {


        private String stringScheme;
        private String name;
        private String token;

        public ParserScheme(String name){

            JSONParser parser = new JSONParser();

            try {
                JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("src/main/resources/dashboard_client/"+name+".json"));
                this.name=(String)jsonObject.get("Name");
                this.token=(String)jsonObject.get("token");
                this.stringScheme = (String)jsonObject.get("String");

            } catch (IOException e) {
                System.out.println(e.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        public String getStringScheme() {
            return stringScheme;
        }

        public String getName() {
            return name;
        }

        public String getToken() {
            return token;
        }


    }
