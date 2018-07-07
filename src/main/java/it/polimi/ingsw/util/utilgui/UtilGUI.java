package it.polimi.ingsw.util.utilgui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class UtilGUI {


    public static void setCell(String s, Labeled labeled, double h) {
        DrawDie drawDie = new DrawDie();
        if (s.matches("[1-6]")) {
            GridPane grid = drawDie.draw(Color.SILVER, Color.DARKSLATEGRAY, s, h);
            grid.setAlignment(Pos.CENTER);
            grid.setMinSize(h, h);
            labeled.setGraphic(grid);

            labeled.setStyle("-fx-background-color:silver;" + "-fx-border-color: black;");
        } else if (s.equals(" ")) {
            labeled.setStyle(ColorGUI.WHITE.getSetOnButton());
        } else {
            labeled.setStyle(ColorGUI.valueOf(s).getSetOnButton());
        }
        labeled.setMinSize(h, h);


    }

    public static VBox drawScheme(String name, String token, GridPane gridMatrix, double H) {


        VBox vBox1 = new VBox();
        HBox hBox1 = new HBox();


        Label label = new Label(name);
        label.setMinSize(H * 3 / 20, H / 20);

        Label label1 = new Label("Token: " + token);
        label1.setPrefSize(H * 2 / 20, H / 20);
        label.setStyle("-fx-font-size:" + H / 60 + "px;" + "-fx-font-family: Verdana;");
        label1.setStyle("-fx-font-size:" + H / 45.5 + "px;" + "-fx-font-family: Times New Roman;");
        label1.setTextAlignment(TextAlignment.RIGHT);
        hBox1.getChildren().addAll(label, label1);
        vBox1.getChildren().add(hBox1);
        vBox1.setMinSize(H / 4, H / 4);
        vBox1.getChildren().add(gridMatrix);


        return vBox1;


    }

    public static String cleanString(String s) {
        return s.replaceAll("\\u001B\\[34m", "").
                replaceAll("\\u001B\\[37m", "").
                replaceAll("\\u001B\\[31m", "").
                replaceAll("\\u001B\\[33m", "").
                replaceAll("\\u001B\\[32m", "").
                replaceAll("\\u001B\\[0m", "").
                replaceAll("\\u001B\\[35m", "");


    }

    public static Label drawDashboard(String cell, double h) {
        Label label = new Label();
        if (cell.substring(cell.length() - 2).equals("  ")) {
            setCell(cell.substring(cell.length() - 4, cell.length() - 3), label, h);
        } else {
            GridPane die = new DrawDie().draw(ColorGUI.valueOf(cell.substring(cell.length() - 2, cell.length() - 1)).getColor(),
                    Color.BLACK, cell.substring(cell.length() - 1), h);
            die.setAlignment(Pos.CENTER);
            die.setStyle(ColorGUI.valueOf(cell.substring(cell.length() - 2, cell.length() - 1)).getSetOnDie());
            label.setGraphic(die);

        }
        return label;
    }

}
