package it.polimi.ingsw.util.utilgui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;


public class GameGUI {

    private static double H;
    private HBox root;
    private Label messages;
    private VBox achievements;
    private VBox tools;
    private ScrollPane roundTrack;
    private VBox draftPool;
    private VBox opponents;
    private VBox myDashBoard;


    public GameGUI(){
        System.out.println("costruttore");


        root = new HBox();
        root.setSpacing(H/25);
        root.setPadding(new Insets(H/100));

        achievements = new VBox();
        achievements.setSpacing(H/100);

        BorderPane borderPane = new BorderPane();

        tools = new VBox();
        tools.setSpacing(H/100);
        borderPane.setTop(tools);

        borderPane.setPadding(new Insets(0,0,H/20,0));
        borderPane.setBottom(drawButtons());
        root.getChildren().addAll(achievements,borderPane);

        VBox center = new VBox();
        center.setAlignment(Pos.TOP_CENTER);
        center.setSpacing(H/20);
        center.setPadding(new Insets(H/20,0,0,0));

        messages = new Label();
        int font = (int) (H/20);
        messages.setStyle("-fx-font-size:"+font+";");
        center.getChildren().add(messages);

        roundTrack = new ScrollPane();
        center.getChildren().add(roundTrack);


        myDashBoard = new VBox();
        center.getChildren().add(myDashBoard);
        root.getChildren().add(center);


        draftPool = new VBox();
        root.getChildren().add(draftPool);


        opponents = new VBox();
        opponents.setSpacing(H/20);
        root.getChildren().add(opponents);

    }

    public void setMessageFromServer(String s){
        messages.setText(s);

    }

    public void setAchievements(String fromServer,String privateAchievement){



        for (int i = 0; i < 3 ; i++) {
            ImageView img = new ImageView(new Image(getClass().getResourceAsStream("/images/intro.jpg"),H/6,H/5,true,true));
            BorderPane borderPane = new BorderPane();
            borderPane.setCenter(img);
            borderPane.setMinSize(H/6,H/5);
            achievements.getChildren().add(borderPane);
        }
        String s = privateAchievement.substring(privateAchievement.length()-1);
        ImageView img = new ImageView(new Image(getClass().getResourceAsStream("/images/achievements/"+s+".jpg"),H/6,H/5,true,true));
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(img);
        borderPane.setMinSize(H/6,H/5);
        achievements.getChildren().add(borderPane);



    }

    public void setTools(String s){

        String[] tool = s.split(",");
        for (int i = 0; i < 3 ; i++) {
            ImageView img = new ImageView(new Image(getClass().getResourceAsStream("/images/tools/"+tool[i]+".jpg"),H/6,H/5,true,true));

            Label label = new Label();
            label.setMinSize(H/6,H/5);
            label.setGraphic(img);
            Integer number = i;
            label.setOnMouseClicked(event->{
                System.out.println(number.toString());

                label.setDisable(true);
                label.setOpacity(1);});
            tools.getChildren().add(label);
        }
    }

    public void setRoundTrack(String s){

        String clean = UtilGUI.cleanString(s);
        String[] rounds = clean.split("\n");
        GridPane gridPane = new GridPane();
        gridPane.setVgap(H / 250);
        gridPane.setHgap(H / 70);
        for (int i = 0; i < 10; i++) {
            Label label = new Label();
            String num = String.valueOf(i + 1);
            label.setText(num);
            int font = (int) (H/30);
            label.setTextAlignment(TextAlignment.CENTER);
            label.setStyle("-fx-font-size:"+font+"px;-fx-background-color: darkkhaki;-fx-border-color: black");
            gridPane.add(label, i, 0);

        }

        for (int i = 0; i < 10 && i<rounds.length; i++) {
            String[] dice = rounds[i].substring(rounds[i].indexOf(':') + 2).split(" ");
            for (int j = 0; j < 10 && j < dice.length; j++) {
                Label label = new Label();
                GridPane die = new DrawDie().draw(ColorGUI.valueOf(dice[j].substring(0,1)).getColor(),
                        Color.BLACK, dice[j].substring(1), H);
                die.setAlignment(Pos.CENTER);
                die.setStyle(ColorGUI.valueOf(dice[j].substring(0,1)).getSetOnDie());
                label.setGraphic(die);
                gridPane.add(label,j+1 , i);

                label.setMinSize(H / 18, H / 18);
            }
        }

        roundTrack.setContent(gridPane);
        gridPane.setPadding(new Insets(H/40));
        gridPane.setStyle("-fx-background-color: lightgreen");
        roundTrack.setMaxHeight(H/5);
        roundTrack.setMinViewportWidth(H/3);
        roundTrack.setStyle("-fx-border-color: lightgreen");


    }

    public void setDraftPool(String string){

            draftPool.setSpacing(H/20);
            draftPool.setPadding(new Insets(0,H/18,0,H/18));
            draftPool.setAlignment(Pos.CENTER);
            draftPool.setSpacing(H/40);
            draftPool.setStyle("-fx-background-color: darkkhaki;-fx-border-color: black");
            String[] dice =UtilGUI.cleanString(string).split(" ");
            int i = 0;
            for (String s: dice
                    ) {
                GridPane die = new DrawDie().draw(ColorGUI.valueOf(s.substring(s.length() - 2,s.length() - 1)).getColor(),
                        Color.BLACK,s.substring(s.length() - 1),H/17);
                die.setAlignment(Pos.CENTER);
                die.setStyle(ColorGUI.valueOf(s.substring(s.length() - 2,s.length() - 1)).getSetOnDie());
                Label label = new Label();
                label.setGraphic(die);
                label.setMinSize(H/17,H/17);
                Integer number = i;
                label.setOnMouseClicked(event -> System.out.println(number));
                draftPool.getChildren().add(label);
                i++;

            }
    }

    public void setPlayers(String s,String nickname){

        int firstToken = s.indexOf('\n');
        int secondToken = s.substring(firstToken + 1).indexOf('\n');
        String name = s.substring(8,firstToken);
        String token = s.substring(firstToken+16,firstToken+ secondToken + 1 );
        String dashBoard = s.substring(firstToken + secondToken +2);

        if(name.equals(nickname)){

            myDashBoard = UtilGUI.drawScheme(name,token,UtilGUI.drawLabelScheme(dashBoard,H/15,true),H);

        }

        else{

            Label label = new Label();
            label.setGraphic(UtilGUI.drawScheme(name,token,UtilGUI.drawLabelScheme(dashBoard,H/20,false),H));
            opponents.getChildren().add(label);



    }}

    public void show(Stage primaryStage){

        primaryStage.setTitle("Sagrada");
        Platform.runLater(()->primaryStage.setScene(new Scene(root)));
        primaryStage.show();

    }


    private GridPane drawButtons(){
        Button move = new Button("Piazza un dado");
        Button useTool = new Button("Usa Tool");
        Button skip = new Button("Passa il turno");
        Button quit = new Button("Esci dal gioco");

        move.setMinSize(H/7,H/20);
        useTool.setMinSize(H/7,H/20);
        skip.setMinSize(H/7,H/20);
        quit.setMinSize(H/7,H/20);

        GridPane buttons = new GridPane();
        buttons.add(move, 0,0);
        buttons.add(useTool, 1,0);
        buttons.add(skip, 0,1);
        buttons.add(quit, 1,1);

        return buttons;

    }

    static{
        Rectangle2D rectangle2D = Screen.getPrimary().getVisualBounds();
        if(rectangle2D.getWidth()>rectangle2D.getHeight())
            H = rectangle2D.getHeight();
        else
            H = rectangle2D.getWidth();
    }
}
