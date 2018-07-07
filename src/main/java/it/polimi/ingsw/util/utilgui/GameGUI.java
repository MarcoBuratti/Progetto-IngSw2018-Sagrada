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
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;

import static it.polimi.ingsw.util.utilgui.UtilGUI.cleanString;
import static it.polimi.ingsw.util.utilgui.UtilGUI.drawDashboard;


public class GameGUI {

    private static double dim;
    private HBox root;
    private GridPane buttons;
    private Label messages;
    private VBox achievements;
    private VBox tools;
    private ScrollPane roundTrack;
    private GridPane roundTrackDice;
    private ScrollPane showMessage;
    private VBox draftPool;
    private VBox opponents;
    private Label myDashBoard;
    private String move;
    private Stage secondStage;
    private boolean done;


    public void setMessageFromServer(String s){

        Platform.runLater(()->{showMessage.setContent(messages);
        messages.setText(s);});

    }

    public void setAchievements(String fromServer,String privateAchievement){

        newScreen();


        String s = privateAchievement.substring(privateAchievement.length()-1);
        String[] publicAchievements = fromServer.split(",");
        for (int i = 0; i < 3 ; i++) {
            System.out.println("_"+publicAchievements[i]+"__");
            ImageView img = new ImageView(new Image(getClass().getResourceAsStream("/images/achievements/"+publicAchievements[i]+".jpeg"),dim/6,dim/5,true,true));
            BorderPane borderPane = new BorderPane();
            borderPane.setCenter(img);
            borderPane.setMinSize(dim/6,dim/5);
            achievements.getChildren().add(borderPane);
        }
        ImageView img = new ImageView(new Image(getClass().getResourceAsStream("/images/achievements/"+s+".jpeg"),dim/6,dim/5,true,true));
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(img);
        borderPane.setMinSize(dim/6,dim/5);
        achievements.getChildren().add(borderPane);



    }

    public void setTools(String s){

        String[] tool = s.split(",");
        for (int i = 0; i < 6 ; i+=2) {
            System.out.println(tool[i]);
            ImageView img = new ImageView(new Image(getClass().getResourceAsStream("/images/tools/"+tool[i]+".jpeg"),dim/4,dim/4.5,true,true));

            Label label = new Label();
            label.setMinSize(dim/4,dim/4.5);
            label.setGraphic(img);
            Integer number = (i/2) + 1;

            label.setOnMouseClicked(event-> setMove(number.toString()));

            VBox vBox = new VBox();
            Label used = new Label();
            used.setTextAlignment(TextAlignment.CENTER);
            used.setFont(Font.font ("Verdana", dim/60));

            if(tool[i +1].equals("false"))
                used.setText("Cost: 1");
            else
                used.setText("Cost: 2");
            vBox.getChildren().addAll(label,used);
            tools.getChildren().add(vBox);
        }
    }

    public void setRoundTrack(String s) {

        String clean = cleanString(s);
        String[] rounds = clean.split("!");

        roundTrackDice.setVgap(dim / 250);
        roundTrackDice.setHgap(dim / 70);

        for (int i = 0; i < 10; i++) {
            Label label = new Label();
            String num = String.valueOf(i + 1);
            label.setText(num);
            int font = (int) (dim/40);
            label.setAlignment(Pos.CENTER);
            label.setStyle("-fx-font-size:"+font+"px;-fx-background-color: darkkhaki;-fx-border-color: black");
            label.setMinSize(dim/ 20, dim/ 20);
            label.setOnMouseClicked(event -> setMove("1 0"));
            roundTrackDice.add(label, i, 0);

        }
        if (!s.equals(" is empty")){
            for (int i = 0; i < 10 && i < rounds.length; i++) {
                String[] dice = rounds[i].substring(rounds[i].indexOf(':') + 2).split(" ");
                for (int j = 0; j < 10 && j < dice.length; j++) {
                    Label label = new Label();

                    GridPane die = new DrawDie().draw(ColorGUI.valueOf(dice[j].substring(0, 1)).getColor(),
                            Color.BLACK, dice[j].substring(1), dim / 20);
                    die.setAlignment(Pos.CENTER);
                    die.setStyle(ColorGUI.valueOf(dice[j].substring(0, 1)).getSetOnDie());
                    label.setGraphic(die);
                    Integer round = i + 1;
                    Integer die1 = j;
                    label.setOnMouseClicked(event -> setMove(round + "_" + die1));
                    roundTrackDice.add(label, i, j + 1);

                    label.setMinSize(dim / 20, dim / 20);
                }
            }
    }


        roundTrack.setContent(roundTrackDice);
        roundTrackDice.setPadding(new Insets(dim/40));
        roundTrackDice.setStyle("-fx-background-color: lightgreen");
        roundTrack.setMaxHeight(dim/5);
        roundTrack.setMinViewportWidth(dim/3);
        roundTrack.setStyle("-fx-border-color: lightgreen");


    }

    public void setDraftPool(String string){

            draftPool.setSpacing(dim/20);
            draftPool.setPadding(new Insets(0,dim/18,0,dim/18));
            draftPool.setAlignment(Pos.CENTER);
            draftPool.setSpacing(dim/40);
            draftPool.setStyle("-fx-background-color: darkkhaki;-fx-border-color: black");
            String[] dice =cleanString(string).split(" ");
            int i = 0;
            for (String s: dice
                    ) {
                GridPane die = new DrawDie().draw(ColorGUI.valueOf(s.substring(s.length() - 2,s.length() - 1)).getColor(),
                        Color.BLACK,s.substring(s.length() - 1),dim/17);
                die.setAlignment(Pos.CENTER);
                die.setStyle(ColorGUI.valueOf(s.substring(s.length() - 2,s.length() - 1)).getSetOnDie());
                Label label = new Label();
                label.setGraphic(die);
                label.setMinSize(dim/17,dim/17);
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

            myDashBoard.setGraphic(UtilGUI.drawScheme(name,token,drawLabelScheme(dashBoard,dim/15,true),dim));

        }

        else{

            Label label = new Label();
            label.setGraphic(UtilGUI.drawScheme(name,token,drawLabelScheme(dashBoard,dim/20,false),dim));
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

        placeDie.setMinSize(dim/7,dim/20);
        useTool.setMinSize(dim/7,dim/20);
        skip.setMinSize(dim/7,dim/20);
        quit.setMinSize(dim/7,dim/20);

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
        Platform.runLater(()->draftPool.setDisable(false));
        done =false;
        inWait();
        draftPool.setDisable(true);

    }

    public void activeDashboard(){
        Platform.runLater(()->myDashBoard.setDisable(false));
        done =false;
        inWait();
        myDashBoard.setDisable(true);

    }

    public void activeRoundTrack(){
        Platform.runLater(()->roundTrackDice.setDisable(false));
        done =false;
        inWait();
        roundTrackDice.setDisable(true);

    }

    public  void activeTools(){
        Platform.runLater(()->tools.setDisable(false));
        done =false;
        inWait();
        tools.setDisable(true);

    }


    public  void activeNumberWindow(){


        selectNumberWindow(secondStage);
        done =false;
        inWait();

    }

    public  void activePlusOrMinusWindow(){

        twoChoiceWindow(secondStage,true);
        done =false;
        inWait();

    }

    public void activeNumberDiceWindow(){

        twoChoiceWindow(secondStage,false);
        done =false;
        inWait();

    }



    private void selectNumberWindow(Stage stage){

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setSpacing(dim/15);
        vBox.setPadding(new Insets(dim/50));
        Label label = new Label("Choose a number");
        label.setFont(Font.font ("Verdana", dim/40));

        GridPane numbers = new GridPane();
        numbers.setHgap(dim/100);
        numbers.setVgap(dim/100);
        numbers.setAlignment(Pos.TOP_CENTER);

        for (int i = 0; i < 6 ; i++){

            Integer choose = i + 1;
            Button button = new Button(choose.toString());
            button.setTextAlignment(TextAlignment.CENTER);
            button.setMinSize(dim/15,dim/15);
            button.setOnAction(event-> {setMove(choose.toString());
            stage.close();});
            button.setFont(Font.font ("Verdana", dim/40));
            numbers.add(button,i%3,i/3);

        }
        vBox.getChildren().addAll(label,numbers);

        Platform.runLater(()->{stage.setScene(new Scene(vBox,dim/3,dim/4));
            stage.show();});
    }

    private void twoChoiceWindow(Stage stage,boolean type){

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setSpacing(dim/15);
        vBox.setPadding(new Insets(dim/50));
        Label label = new Label();
        if(type)
            label.setText("Choose plus or minus");
        else
            label.setText("Choose die's number");
        label.setFont(Font.font ("Verdana", dim/40));

        GridPane numbers = new GridPane();
        numbers.setHgap(dim/100);
        numbers.setVgap(dim/100);
        numbers.setAlignment(Pos.TOP_CENTER);

        for (int i = 0; i < 2 ; i++){
            String string;
            Integer choose = i;
            if(type)
                if(i==0)
                    string = "-";
                else
                    string = "+";
            else
                if(i==0)
                    string = "1";
                else
                    string = "2";

            Button button = new Button(string);
            button.setTextAlignment(TextAlignment.CENTER);
            button.setMinSize(dim/15,dim/15);
            button.setOnAction(event-> {setMove(choose.toString());
            stage.close();});
            button.setFont(Font.font ("Verdana", dim/40));
            numbers.add(button,i,0);

        }
        vBox.getChildren().addAll(label,numbers);

        Platform.runLater(()->{stage.setScene(new Scene(vBox,dim/3,dim/4));
        stage.show();});




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

    public void newScreen(){
        root = new HBox();
        root.setSpacing(dim/25);
        root.setPadding(new Insets(dim/100));

        achievements = new VBox();
        achievements.setSpacing(dim/100);


        BorderPane borderPane = new BorderPane();

        tools = new VBox();
        tools.setSpacing(dim/100);
        borderPane.setTop(tools);
        tools.setDisable(true);
        tools.setOpacity(1);

        borderPane.setPadding(new Insets(0,0,dim/20,0));
        buttons = new GridPane();
        drawButtons();
        buttons.setOpacity(1);
        borderPane.setBottom(buttons);
        root.getChildren().addAll(achievements,borderPane);

        VBox center = new VBox();
        center.setAlignment(Pos.TOP_CENTER);
        center.setSpacing(dim/20);
        center.setPadding(new Insets(dim/20,0,0,0));

        showMessage = new ScrollPane();
        showMessage.setMaxHeight(dim/6);
        showMessage.setMaxWidth(dim/2);
        showMessage.setMinViewportWidth(dim/2);
        showMessage.setMinViewportHeight(dim/6);
        showMessage.setStyle("-fx-border-color: lightgreen");


        messages = new Label();
        int font = (int) (dim/25);
        messages.setStyle("-fx-font-size:"+font+"px;");
        center.getChildren().add(showMessage);

        roundTrack = new ScrollPane();
        roundTrackDice = new GridPane();
        center.getChildren().add(roundTrack);
        roundTrackDice.setDisable(true);
        roundTrackDice.setOpacity(1);



        myDashBoard = new Label();
        center.getChildren().add(myDashBoard);
        myDashBoard.setDisable(true);
        myDashBoard.setOpacity(1);
        root.getChildren().add(center);


        draftPool = new VBox();
        root.getChildren().add(draftPool);
        draftPool.setDisable(true);
        draftPool.setOpacity(1);


        opponents = new VBox();
        opponents.setSpacing(dim/20);
        root.getChildren().add(opponents);

    }

    public void newSecondStage(){
        secondStage = new Stage();
    }


    static{
        Rectangle2D rectangle2D = Screen.getPrimary().getVisualBounds();
        if(rectangle2D.getWidth()>rectangle2D.getHeight())
            dim = rectangle2D.getHeight();
        else
            dim = rectangle2D.getWidth();
    }
}
