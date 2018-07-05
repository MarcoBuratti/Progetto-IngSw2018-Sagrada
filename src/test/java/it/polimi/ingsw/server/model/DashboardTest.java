
package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exception.NotValidParametersException;
import it.polimi.ingsw.server.model.exception.NotValidValueException;
import it.polimi.ingsw.server.model.exception.OccupiedCellException;
import it.polimi.ingsw.server.model.restriction.NoRestriction;
import it.polimi.ingsw.util.GraphicsClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

class DashboardTest {

    @Test
    void getMatrixScheme() throws NotValidParametersException, NotValidValueException, OccupiedCellException {

        Dashboard dashboard = new Dashboard("Scheme_Test");

        Cell[][] matrixScheme2 = new Cell[4][5];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                matrixScheme2[i][j] = new Cell(new NoRestriction());
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                Assertions.assertEquals(dashboard.getMatrixScheme()[i][j].toString(), matrixScheme2[i][j].toString());
            }
        }

        dashboard.setDieOnCell(0,0,new Die(Color.GREEN));
        dashboard.getMatrixScheme()[0][0].getDie().setNumber(2);
        dashboard.setDieOnCell(1,0,new Die(Color.BLUE));
        dashboard.getMatrixScheme()[1][0].getDie().setNumber(4);
        dashboard.setDieOnCell(2,0,new Die(Color.YELLOW));
        dashboard.getMatrixScheme()[2][0].getDie().setNumber(5);
        dashboard.setDieOnCell(3,3,new Die(Color.BLUE));
        dashboard.getMatrixScheme()[3][3].getDie().setNumber(1);


        matrixScheme2[0][0].setDie(new Die(Color.GREEN));
        matrixScheme2[0][0].getDie().setNumber(2);
        matrixScheme2[1][0].setDie(new Die(Color.BLUE));
        matrixScheme2[1][0].getDie().setNumber(4);
        matrixScheme2[2][0].setDie(new Die(Color.YELLOW));
        matrixScheme2[2][0].getDie().setNumber(5);
        matrixScheme2[3][3].setDie(new Die(Color.BLUE));
        matrixScheme2[3][3].getDie().setNumber(1);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                Assertions.assertEquals(dashboard.getMatrixScheme()[i][j].toString(), matrixScheme2[i][j].toString());
            }
        }

        dashboard.setDieOnCell(3,0,new Die(Color.VIOLET));
        dashboard.getMatrixScheme()[3][0].getDie().setNumber(3);
        dashboard.setDieOnCell(1,1,new Die(Color.RED));
        dashboard.getMatrixScheme()[1][1].getDie().setNumber(1);
        dashboard.setDieOnCell(2,2,new Die(Color.YELLOW));
        dashboard.getMatrixScheme()[2][2].getDie().setNumber(6);
        dashboard.setDieOnCell(3,4,new Die(Color.GREEN));
        dashboard.getMatrixScheme()[3][4].getDie().setNumber(6);
        dashboard.setDieOnCell(0,4,new Die(Color.BLUE));
        dashboard.getMatrixScheme()[0][4].getDie().setNumber(6);
        dashboard.setDieOnCell(2,4,new Die(Color.VIOLET));
        dashboard.getMatrixScheme()[2][4].getDie().setNumber(5);
        dashboard.setDieOnCell(1,4,new Die(Color.BLUE));
        dashboard.getMatrixScheme()[1][4].getDie().setNumber(2);
        dashboard.setDieOnCell(0,1,new Die(Color.RED));
        dashboard.getMatrixScheme()[0][1].getDie().setNumber(6);
        dashboard.setDieOnCell(1,2,new Die(Color.YELLOW));
        dashboard.getMatrixScheme()[1][2].getDie().setNumber(3);
        dashboard.setDieOnCell(1,3,new Die(Color.GREEN));
        dashboard.getMatrixScheme()[1][3].getDie().setNumber(1);
        dashboard.setDieOnCell(0,2,new Die(Color.VIOLET));
        dashboard.getMatrixScheme()[0][2].getDie().setNumber(3);
        dashboard.setDieOnCell(2,1,new Die(Color.RED));
        dashboard.getMatrixScheme()[2][1].getDie().setNumber(4);
        dashboard.setDieOnCell(2,3,new Die(Color.BLUE));
        dashboard.getMatrixScheme()[2][3].getDie().setNumber(5);
        dashboard.setDieOnCell(0,3,new Die(Color.YELLOW));
        dashboard.getMatrixScheme()[0][3].getDie().setNumber(4);
        dashboard.setDieOnCell(3,1,new Die(Color.BLUE));
        dashboard.getMatrixScheme()[3][1].getDie().setNumber(2);
        dashboard.setDieOnCell(3,2,new Die(Color.VIOLET));
        dashboard.getMatrixScheme()[3][2].getDie().setNumber(5);

        matrixScheme2[3][0].setDie(new Die(Color.VIOLET));
        matrixScheme2[3][0].getDie().setNumber(3);
        matrixScheme2[1][1].setDie(new Die(Color.RED));
        matrixScheme2[1][1].getDie().setNumber(1);
        matrixScheme2[2][2].setDie(new Die(Color.YELLOW));
        matrixScheme2[2][2].getDie().setNumber(6);
        matrixScheme2[3][4].setDie(new Die(Color.GREEN));
        matrixScheme2[3][4].getDie().setNumber(6);
        matrixScheme2[0][4].setDie(new Die(Color.BLUE));
        matrixScheme2[0][4].getDie().setNumber(6);
        matrixScheme2[2][4].setDie(new Die(Color.VIOLET));
        matrixScheme2[2][4].getDie().setNumber(5);
        matrixScheme2[1][4].setDie(new Die(Color.BLUE));
        matrixScheme2[1][4].getDie().setNumber(2);
        matrixScheme2[0][1].setDie(new Die(Color.RED));
        matrixScheme2[0][1].getDie().setNumber(6);
        matrixScheme2[1][2].setDie(new Die(Color.YELLOW));
        matrixScheme2[1][2].getDie().setNumber(3);
        matrixScheme2[1][3].setDie(new Die(Color.GREEN));
        matrixScheme2[1][3].getDie().setNumber(1);
        matrixScheme2[0][2].setDie(new Die(Color.VIOLET));
        matrixScheme2[0][2].getDie().setNumber(3);
        matrixScheme2[2][1].setDie(new Die(Color.RED));
        matrixScheme2[2][1].getDie().setNumber(4);
        matrixScheme2[2][3].setDie(new Die(Color.BLUE));
        matrixScheme2[2][3].getDie().setNumber(5);
        matrixScheme2[0][3].setDie(new Die(Color.YELLOW));
        matrixScheme2[0][3].getDie().setNumber(4);
        matrixScheme2[3][1].setDie(new Die(Color.BLUE));
        matrixScheme2[3][1].getDie().setNumber(2);
        matrixScheme2[3][2].setDie(new Die(Color.VIOLET));
        matrixScheme2[3][2].getDie().setNumber(5);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                Assertions.assertEquals(dashboard.getMatrixScheme()[i][j].toString(), matrixScheme2[i][j].toString());
            }
        }

    }

    @Test
    void setDieOnCell() throws NotValidParametersException, NotValidValueException, OccupiedCellException {

        Dashboard dashboard = new Dashboard("Scheme_Test");

        Die die1 = new Die(Color.GREEN);
        die1.setNumber(1);
        Die die2 = new Die(Color.BLUE);
        die2.setNumber(2);
        Die die3 = new Die(Color.YELLOW);
        die3.setNumber(3);
        Die die4 = new Die(Color.BLUE);
        die4.setNumber(4);
        Cell EmptyCell = new Cell(new NoRestriction());

        Assertions.assertEquals(20,dashboard.emptyCells());

        dashboard.setDieOnCell(0,0, die1);
        dashboard.setDieOnCell(1,0, die2);
        dashboard.setDieOnCell(2,0, die3);
        dashboard.setDieOnCell(3,3, die4);

        Assertions.assertEquals(16,dashboard.emptyCells());

       Assertions.assertThrows(NotValidParametersException.class, () -> dashboard.setDieOnCell(7,0, new Die(Color.GREEN)));
       Assertions.assertThrows(NotValidParametersException.class, () -> dashboard.setDieOnCell(0,8, new Die(Color.GREEN)));
       Assertions.assertThrows(NotValidParametersException.class, () -> dashboard.setDieOnCell(10,9, new Die(Color.GREEN)));

        Assertions.assertEquals(dashboard.getMatrixScheme()[0][0].getDie().toString(), die1.toString());
        Assertions.assertEquals(dashboard.getMatrixScheme()[1][0].getDie().toString(), die2.toString());
        Assertions.assertEquals(dashboard.getMatrixScheme()[2][0].getDie().toString(), die3.toString());
        Assertions.assertEquals(dashboard.getMatrixScheme()[3][3].getDie().toString(), die4.toString());
        Assertions.assertEquals(dashboard.getMatrixScheme()[2][3].toString(), EmptyCell.toString());

    }

    @Test
    void jsonTester() throws NotValidValueException, OccupiedCellException, IOException, ParseException {
        List<SchemesEnum> schemesEnum = Arrays.asList(SchemesEnum.values());
        GraphicsClient graphicsClient;
        graphicsClient = new GraphicsClient();
        JSONParser parser = new JSONParser();
        for (int i = 0; i < schemesEnum.size(); i++){
           /* String sub = schemesEnum.get(i).getName();
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("src/main/resources/dashboard_client/"+ sub +".json"));
            String change = (String) jsonObject.get("String");
            change = change.replace("\u001B[37m", "\u001b[0m");
            jsonObject.put("String", change);
            try (FileWriter up = new FileWriter("src/main/resources/dashboard_client/"+ sub +".json")) {
                up.write(jsonObject.toJSONString());
                System.out.println(change);
                System.out.println(sub + " eseguito " + i);
            } catch (IOException e) {
                System.out.println(e.toString() );
            }*/
            Dashboard dashboard = new Dashboard(schemesEnum.get(i).getName());
            System.out.println(dashboard.getFavourToken() + " " + schemesEnum.get(i).getName());
            System.out.println(dashboard);
        }

        try {
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("src/main/files/up.json"));
            String achievement = (String) jsonObject.get("Public Achievements");
            graphicsClient.printAchievements(achievement);
            String tool = (String) jsonObject.get("Tools");
            graphicsClient.printTool(tool);
            String roundTrack = (String) jsonObject.get("Round track");
            graphicsClient.printRoundTrack(roundTrack);
            String draft = (String) jsonObject.get("Draft");
            if(draft != null) graphicsClient.printDraft(draft);
            String number = (String) jsonObject.get("numberPlayer");
            int player = Integer.parseInt(number);
            System.out.println(number);
            for (int i = 0; i < player; i++) {
                String request = "scheme" + i;
                System.out.println(request);
                String scheme = (String) jsonObject.get(request);
                graphicsClient.printScheme(scheme);
            }

        } catch (IOException | ParseException e) {
            System.err.println(e.toString());
        }

    }



    @Test
    void ciao(){

    }
}



