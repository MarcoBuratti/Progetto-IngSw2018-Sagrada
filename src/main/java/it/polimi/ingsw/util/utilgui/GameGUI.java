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
import javafx.stage.Screen;
import javafx.stage.Stage;

import static it.polimi.ingsw.util.utilgui.UtilGUI.cleanString;
import static it.polimi.ingsw.util.utilgui.UtilGUI.drawDashboard;
import static it.polimi.ingsw.util.utilgui.UtilGUI.setCell;


public class GameGUI {

    private static double H;
    private HBox root;
    private GridPane buttons;
    private Label messages;
    private VBox achievements;
    private VBox tools;
    private ScrollPane roundTrack;
    private ScrollPane showMessage;
    private VBox draftPool;
    private VBox opponents;
    private Label myDashBoard;
    private String move;
    private boolean done;


    public void setMessageFromServer(String s){
        showMessage.setContent(messages);
        System.out.println(messages.getText());
        Platform.runLater(()->messages.setText(messages.getText()+"\n"+ s ));

    }

    public void setAchievements(String fromServer,String privateAchievement){

        newScreen();
        String s = privateAchievement.substring(privateAchievement.length()-1);
        String[] publicAchievements = fromServer.split(",");
        for (int i = 0; i < 3 ; i++) {
            System.out.println("_"+publicAchievements[i]+"__");
            ImageView img = new ImageView(new Image(getClass().getResourceAsStream("/images/achievements/"+publicAchievements[i]+".jpeg"),H/6,H/5,true,true));
            BorderPane borderPane = new BorderPane();
            borderPane.setCenter(img);
            borderPane.setMinSize(H/6,H/5);
            achievements.getChildren().add(borderPane);
        }
        ImageView img = new ImageView(new Image(getClass().getResourceAsStream("/images/achievements/"+s+".jpeg"),H/6,H/5,true,true));
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(img);
        borderPane.setMinSize(H/6,H/5);
        achievements.getChildren().add(borderPane);



    }

    public void setTools(String s){

        String[] tool = s.split(",");
        for (int i = 0; i < 6 ; i+=2) {
            System.out.println(tool[i]);
            ImageView img = new ImageView(new Image(getClass().getResourceAsStream("/images/tools/"+tool[i]+".jpeg"),H/4,H/4.5,true,true));

            Label label = new Label();
            label.setMinSize(H/4,H/4.5);
            label.setGraphic(img);
            Integer number = (i/2) + 1;
            System.out.println(number.toString());
            label.setOnMouseClicked(event-> setMove(number.toString()));
            tools.getChildren().add(label);
        }
    }

    public void setRoundTrack(String s) {

        String clean = cleanString(s);
        String[] rounds = clean.split("!");
        GridPane gridPane = new GridPane();
        gridPane.setVgap(H / 250);
        gridPane.setHgap(H / 70);

        for (int i = 0; i < 10; i++) {
            Label label = new Label();
            String num = String.valueOf(i + 1);
            label.setText(num);
            int font = (int) (H/40);
            label.setAlignment(Pos.CENTER);
            label.setStyle("-fx-font-size:"+font+"px;-fx-background-color: darkkhaki;-fx-border-color: black");
            label.setMinSize(H / 20, H / 20);
            gridPane.add(label, i, 0);

        }
        if (!s.equals(" is empty")){
            for (int i = 0; i < 10 && i < rounds.length; i++) {
                System.out.println(s);
                String[] dice = rounds[i].substring(rounds[i].indexOf(':') + 2).split(" ");
                for (int j = 0; j < 10 && j < dice.length; j++) {
                    System.out.println("-"+dice[j]+"-");
                    Label label = new Label();

                    GridPane die = new DrawDie().draw(ColorGUI.valueOf(dice[j].substring(0, 1)).getColor(),
                            Color.BLACK, dice[j].substring(1), H / 20);
                    die.setAlignment(Pos.CENTER);
                    die.setStyle(ColorGUI.valueOf(dice[j].substring(0, 1)).getSetOnDie());
                    label.setGraphic(die);
                    Integer round = i + 1;
                    Integer die1 = j;
                    label.setOnMouseClicked(event -> System.out.println(round + "_" + die1));
                    gridPane.add(label, i, j + 1);

                    label.setMinSize(H / 20, H / 20);
                }
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
            String[] dice =cleanString(string).split(" ");
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
                label.setOnMouseClicked(event -> setMove(number.toString()));
                draftPool.getChildren().add(label);
                i++;

            }
    }

    public void setPlayers(String s,String nickname){

        int firstToken = s.indexOf('!');
        int secondToken = s.substring(firstToken + 1).indexOf('!');
        String name = s.substring(8,firstToken);
        String token = s.substring(firstToken+16,firstToken+ secondToken + 1 );
        String dashBoard = s.substring(firstToken + secondToken +2);

        if(name.equals(nickname)){

            myDashBoard.setGraphic(UtilGUI.drawScheme(name,token,drawLabelScheme(dashBoard,H/15,true),H));

        }

        else{

            Label label = new Label();
            label.setGraphic(UtilGUI.drawScheme(name,token,drawLabelScheme(dashBoard,H/20,false),H));
            opponents.getChildren().add(label);



        }}

    public void show(Stage primaryStage){

        Platform.runLater(()->primaryStage.setScene(new Scene(root)));


    }


    private void drawButtons(){
        Button placeDie = new Button("Piazza un dado");
        placeDie.setOnAction(event->setMove("1"));
        Button useTool = new Button("Usa Tool");
        useTool.setOnAction(event->setMove("2"));
        Button skip = new Button("Passa il turno");
        skip.setOnAction(event->setMove("3"));
        Button quit = new Button("Esci dal gioco");
        quit.setOnAction(event->setMove("4"));

        placeDie.setMinSize(H/7,H/20);
        useTool.setMinSize(H/7,H/20);
        skip.setMinSize(H/7,H/20);
        quit.setMinSize(H/7,H/20);

        buttons.add(placeDie, 0,0);
        buttons.add(useTool, 1,0);
        buttons.add(skip, 0,1);
        buttons.add(quit, 1,1);

    }

    private GridPane drawLabelScheme(String s, double h, boolean set){

        String cleanedString = cleanString(s);
        String[] arr = cleanedString.split("]");
        GridPane scheme = new GridPane();
        scheme.setHgap(0);
        scheme.setVgap(0);
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 5; j++) {
                int ind = i * 5 + j;
                Label label = drawDashboard(arr[ind],h);
                if (set){
                    Integer number= ind;
                    label.setOnMouseClicked(event -> setMove(number/5+" "+number%5));
                }
                label.setMinSize(h,h);
                label.setMaxSize(h,h);
                scheme.add(label,j,i);
            }
        return scheme;


    }

    public void activeButtons(){
        done =false;
        inWait();


    }

    public void activeDraftPool(){
       // draftPool.setDisable(false);
        done =false;
        inWait();
//        draftPool.setDisable(true);

    }

    public void activeDashboard(){
       // myDashBoard.setDisable(false);
        done =false;
        inWait();
        //myDashBoard.setDisable(true);

    }

    public void activeRoundTrack(){
        //roundTrack.setDisable(false);
        done =false;
        inWait();
       // roundTrack.setDisable(true);

    }

    public void activeTools(){
       // tools.setDisable(false);
        done =false;
        inWait();
       // tools.setDisable(true);

    }


    public synchronized void setMove(String move) {
        this.move = move;
        done= true;
        notifyAll();
    }

    public synchronized String getMove() {
        return move;
    }

    private synchronized void inWait(){

        try {
            while(!done) {
                wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



    }

    private void newScreen(){
        root = new HBox();
        root.setSpacing(H/25);
        root.setPadding(new Insets(H/100));

        achievements = new VBox();
        achievements.setSpacing(H/100);


        BorderPane borderPane = new BorderPane();

        tools = new VBox();
        tools.setSpacing(H/100);
        borderPane.setTop(tools);
       // tools.setDisable(true);
        tools.setOpacity(1);

        borderPane.setPadding(new Insets(0,0,H/20,0));
        buttons = new GridPane();
        drawButtons();
        //buttons.setDisable(true);
        buttons.setOpacity(1);
        borderPane.setBottom(buttons);
        root.getChildren().addAll(achievements,borderPane);

        VBox center = new VBox();
        center.setAlignment(Pos.TOP_CENTER);
        center.setSpacing(H/20);
        center.setPadding(new Insets(H/20,0,0,0));

        showMessage = new ScrollPane();
        showMessage.setMaxHeight(H/6);
        showMessage.setMaxWidth(H/2);
        showMessage.setMinViewportWidth(H/2);
        showMessage.setMinViewportHeight(H/6);
        showMessage.setStyle("-fx-border-color: lightgreen");


        messages = new Label();
        int font = (int) (H/25);
        messages.setStyle("-fx-font-size:"+font+"px;");
        center.getChildren().add(showMessage);

        roundTrack = new ScrollPane();
        center.getChildren().add(roundTrack);
        //roundTrack.setDisable(true);
        roundTrack.setOpacity(1);



        myDashBoard = new Label();
        center.getChildren().add(myDashBoard);
        //myDashBoard.setDisable(true);
        myDashBoard.setOpacity(1);
        root.getChildren().add(center);


        draftPool = new VBox();
        root.getChildren().add(draftPool);
        //draftPool.setDisable(true);
        draftPool.setOpacity(1);


        opponents = new VBox();
        opponents.setSpacing(H/20);
        root.getChildren().add(opponents);





    }


    static{
        Rectangle2D rectangle2D = Screen.getPrimary().getVisualBounds();
        if(rectangle2D.getWidth()>rectangle2D.getHeight())
            H = rectangle2D.getHeight();
        else
            H = rectangle2D.getWidth();
    }
}
