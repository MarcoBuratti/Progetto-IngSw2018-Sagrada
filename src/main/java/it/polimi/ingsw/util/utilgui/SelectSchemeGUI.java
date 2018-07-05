package it.polimi.ingsw.util.utilgui;

import it.polimi.ingsw.client.GUIView;
import it.polimi.ingsw.util.ParserScheme;
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

    private static double dim;

    public void setStage(Stage primaryStage, String fromServer, String privateAchievement, String nickname, GUIView guiView) {


        primaryStage.setWidth(dim*2/3);
        primaryStage.setHeight(dim);


        VBox vBox =new VBox();
        vBox.setSpacing(dim/20);
        vBox.setAlignment(Pos.TOP_CENTER);
        Label text = new Label("Ciao "+nickname+"\nScegli lo schema da usare in questa partita");
        text.setMinHeight(dim/20);
        text.setTextAlignment(TextAlignment.CENTER);
        vBox.getChildren().add(text);

        HBox hBox = new HBox();
        hBox.setSpacing(dim/50);
        privateAchievement = UtilGUI.cleanString(privateAchievement);
        Label achievement = new Label(privateAchievement.substring(0,privateAchievement.length()-2));
        achievement.setMinHeight(dim/20);
        achievement.setMinWidth(dim/16);
        achievement.setStyle("-fx-font-family: Verdana;-fx-font-size:16px;");
        text.setStyle("-fx-font-size: 18px;" + "-fx-font-family: Verdana;" + "-fx-font-weight:bold;");
        achievement.setTextAlignment(TextAlignment.RIGHT);
        hBox.getChildren().add(achievement);



        Label color = new Label();
        color.setStyle(ColorGUI.valueOf(privateAchievement.substring(privateAchievement.length()-1)).getSetOnDie());
        color.setMinWidth(dim/20);
        color.setMinHeight(dim/20);
        color.setAlignment(Pos.CENTER_LEFT);
        hBox.getChildren().add(color);
        hBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(hBox);


        String substringSchemes = fromServer.substring(fromServer.indexOf('.') + 2);
        String[] schemes = substringSchemes.split(",");

        GridPane grid = new GridPane();
        grid.setVgap(dim/50);
        grid.setHgap(dim/50);
        grid.setAlignment(Pos.CENTER);
        Button[] b = new Button[4];

        for (int k = 0; k < 4; k++) {
            b[k] = new Button();
            b[k].setPrefSize(dim/3.5,dim/3.5);
            b[k].setAlignment(Pos.CENTER);
            ParserScheme parser = new ParserScheme(schemes[k]);

            b[k].setGraphic(UtilGUI.drawScheme(parser.getName(),parser.getToken(),UtilGUI.drawLabelScheme(parser.getStringScheme(),dim/20,false),dim));
            String numberChoose = String.valueOf(k + 1);
            b[k].setOnAction(event -> {
                text.setText("Hai scelto " + parser.getName() + ".\n Attendi l'inizio della partita...");
                for (Button button : b) {
                    button.setDisable(true);

                }
                b[Integer.parseInt(numberChoose)-1].setOpacity(1);
                guiView.getConnectionClient().handleScheme(fromServer,numberChoose);
                //guiView.getConnectionClient().game();



            });
            grid.add(b[k], k %2, k / 2);

        }
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(grid);
        vBox.setStyle("-fx-background-color: darkkhaki");
        Scene scene = new Scene(vBox,dim*2/3,dim);
        Platform.runLater(()->primaryStage.setScene(scene));
        primaryStage.show();
    }


    static{
        Rectangle2D rectangle2D = Screen.getPrimary().getVisualBounds();
        if(rectangle2D.getWidth()>rectangle2D.getHeight())
            dim = rectangle2D.getHeight();
        else
            dim = rectangle2D.getWidth();
    }


}
