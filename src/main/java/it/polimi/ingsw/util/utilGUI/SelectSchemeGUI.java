package it.polimi.ingsw.util.utilGUI;

import it.polimi.ingsw.util.SchemeParser;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class SelectSchemeGUI {

    private static double H;

    public SelectSchemeGUI(Stage primaryStage,String fromServer, String privateAchievement,String nickname) {


        primaryStage.setWidth(H*2/3);
        primaryStage.setHeight(H);


        VBox vBox =new VBox();
        vBox.setSpacing(H/20);
        vBox.setAlignment(Pos.TOP_CENTER);
        Label text = new Label("Ciao "+nickname+"\nScegli lo schema da usare in questa partita");
        text.setMinHeight(H/20);
        text.setTextAlignment(TextAlignment.CENTER);
        vBox.getChildren().add(text);

        HBox hBox = new HBox();
        hBox.setSpacing(H/50);
        privateAchievement = UtilGUI.cleanString(privateAchievement);
        Label achievement = new Label(privateAchievement.substring(0,privateAchievement.length()-2));
        achievement.setMinHeight(H/20);
        achievement.setMinWidth(H/16);
        achievement.setStyle("-fx-font-family: Verdana;-fx-font-size:16px;");
        text.setStyle("-fx-font-size: 18px;" + "-fx-font-family: Verdana;" + "-fx-font-weight:bold;");
        achievement.setTextAlignment(TextAlignment.RIGHT);
        hBox.getChildren().add(achievement);



        Label color = new Label();
        color.setStyle(ColorGUI.valueOf(privateAchievement.substring(privateAchievement.length()-1)).getSetOnDie());
        color.setMinWidth(H/20);
        color.setMinHeight(H/20);
        color.setAlignment(Pos.CENTER_LEFT);
        hBox.getChildren().add(color);
        hBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(hBox);


        String substringSchemes = fromServer.substring(fromServer.indexOf(".") + 2);
        String[] schemes = substringSchemes.split(",");

        GridPane grid = new GridPane();
        grid.setVgap(H/50);
        grid.setHgap(H/50);
        grid.setAlignment(Pos.CENTER);
        Button[] b = new Button[4];

        for (int k = 0; k < 4; k++) {
            b[k] = new Button();
            b[k].setPrefSize(H/3.5,H/3.5);
            b[k].setAlignment(Pos.CENTER);
            SchemeParser parser = new SchemeParser(schemes[k]);

            b[k].setGraphic(UtilGUI.drawScheme(parser.getName(),parser.getToken(),UtilGUI.drawLabelScheme(parser.getStringScheme(),H/20,false),H));
            String numberChoose = String.valueOf(k + 1);
            b[k].setOnAction(event -> {
                System.out.println(numberChoose);
                text.setText("Hai scelto " + parser.getName() + ".\n Attendi l'inizio della partita...");
                for (Button button : b) {
                    button.setDisable(true);

                }
                b[Integer.parseInt(numberChoose)-1].setOpacity(1);

            });
            grid.add(b[k], k %2, k / 2);

        }
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(grid);
        vBox.setStyle("-fx-background-color: darkkhaki");
        Scene scene = new Scene(vBox,H*2/3,H);
        Platform.runLater(()->primaryStage.setScene(scene));
        primaryStage.show();
    }


    static{
        Rectangle2D rectangle2D = Screen.getPrimary().getVisualBounds();
        if(rectangle2D.getWidth()>rectangle2D.getHeight())
            H = rectangle2D.getHeight();
        else
            H = rectangle2D.getWidth();
    }


}
