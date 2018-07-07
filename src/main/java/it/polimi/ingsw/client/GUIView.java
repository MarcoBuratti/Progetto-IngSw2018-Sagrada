package it.polimi.ingsw.client;

import it.polimi.ingsw.util.GraphicsClient;
import it.polimi.ingsw.util.InputController;
import it.polimi.ingsw.util.SchemeParser;
import it.polimi.ingsw.util.utilgui.ColorGUI;
import it.polimi.ingsw.util.utilgui.GameGUI;
import it.polimi.ingsw.util.utilgui.UtilGUI;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;

import static it.polimi.ingsw.util.utilgui.UtilGUI.cleanString;
import static it.polimi.ingsw.util.utilgui.UtilGUI.drawDashboard;

public class GUIView extends View {


    private InputController cliController;
    private GridPane grid;
    private boolean connectionSuccessful = false;
    private double h = 800;
    private double v = 600;

    private GameGUI gameGUI;
    private String privateAchievement;
    private Label text;


    GUIView() {
        setGraphicsClient(new GraphicsClient());
        cliController = new InputController();
    }


    @Override
    public void start() {
        getPrimaryStage().setTitle("Login Sagrada");
        grid = new GridPane();
        grid.heightProperty().addListener((observable, oldValue, newValue) -> v = (double) newValue);
        grid.widthProperty().addListener((observable, oldValue, newValue) -> h = (double) newValue);
        Scene scene = new Scene(grid, h, v);
        getPrimaryStage().setScene(scene);

        ImageView img = new ImageView(new Image(getClass().getResourceAsStream("/images/intro.jpg"), h * 9 / 20, v / 2, true, true));

        grid.setPadding(new Insets(v / 50, h / 20, v / 50, h / 20));
        grid.vgapProperty().bind(grid.heightProperty().divide(100));
        grid.hgapProperty().bind((grid.widthProperty().divide(50)));

        Label label1 = new Label(getGraphicsClient().askNick());
        setSize(label1);
        label1.setAlignment(Pos.CENTER);
        GridPane.setConstraints(label1, 0, 2);

        TextField nickNameTextField = new TextField();
        nickNameTextField.setPromptText("Nickname");
        setSize(nickNameTextField);
        GridPane.setConstraints(nickNameTextField, 1, 2);

        Label label2 = new Label(getGraphicsClient().printIP());
        setSize(label2);
        label2.setAlignment(Pos.CENTER);
        GridPane.setConstraints(label2, 2, 2);

        TextField ipAddressTextField = new TextField();
        ipAddressTextField.setPromptText("Server's IP address");
        setSize(ipAddressTextField);
        GridPane.setConstraints(ipAddressTextField, 3, 2);

        Label label3 = new Label(getGraphicsClient().printPort());
        setSize(label3);
        label3.setAlignment(Pos.CENTER);
        GridPane.setConstraints(label3, 0, 3);

        TextField portTextField = new TextField();
        portTextField.setPromptText("Server's port");
        setSize(portTextField);
        GridPane.setConstraints(portTextField, 1, 3);

        Label label4 = new Label("Connection:");
        setSize(label4);
        label4.setAlignment(Pos.CENTER);
        GridPane.setConstraints(label4, 2, 3);

        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll("RMI", "SOCKET");
        choiceBox.setValue("RMI");
        setSize(choiceBox);
        choiceBox.setCenterShape(true);
        GridPane.setConstraints(choiceBox, 3, 3);

        Button loginButton = new Button("Log in");
        loginButton.setStyle("-fx-font-family: Verdana;-fx-font-size:20px;-fx-font-weight:bold;");
        loginButton.maxHeightProperty().bind(grid.heightProperty().divide(10));
        loginButton.maxWidthProperty().bind((grid.widthProperty().subtract(h / 10)));
        loginButton.minHeightProperty().bind(grid.heightProperty().divide(10));
        loginButton.minWidthProperty().bind((grid.widthProperty()).subtract(h / 10));
        loginButton.requestFocus();
        loginButton.setOnAction(event -> {
                    if (checkChoose(nickNameTextField, ipAddressTextField, portTextField)) {
                        setChoice(choiceBox.getValue());
                        Platform.runLater(() -> getPrimaryStage().setScene(waitAnswer("Attendi risposta...")));
                        createConnection();
                        if ( connectionSuccessful ) {
                            Thread thread = new Thread(() -> getConnectionClient().handleName(getNickname()));
                            thread.start();
                        }
                    }
                }
        );
        BorderPane pane = new BorderPane();
        pane.setCenter(img);
        img.fitWidthProperty().bind(pane.widthProperty());
        img.fitHeightProperty().bind(pane.heightProperty());
        pane.maxHeightProperty().bind(grid.heightProperty().divide(2));
        pane.maxWidthProperty().bind((grid.widthProperty().subtract(h / 10)));
        pane.minHeightProperty().bind(grid.heightProperty().divide(2));
        pane.minWidthProperty().bind((grid.widthProperty()).subtract(h / 10));
        GridPane.setConstraints(loginButton, 0, 10, 4, 1);
        GridPane.setConstraints(pane, 0, 0, 4, 1);


        grid.getChildren().addAll(label1, label2, label3, label4, nickNameTextField, ipAddressTextField, portTextField, loginButton, choiceBox, pane);
        grid.setStyle("-fx-background-color: grey");
        getPrimaryStage().show();

    }

    @Override
    public void connectionSuccess() {
        this.connectionSuccessful = true;
    }


    private void setSize(Control labeled) {

        labeled.maxHeightProperty().bind(grid.heightProperty().divide(10));
        labeled.maxWidthProperty().bind((grid.widthProperty().subtract(16 / 3)));
        labeled.minHeightProperty().bind(grid.heightProperty().divide(10));
        labeled.minWidthProperty().bind((grid.widthProperty()).divide(16 / 3));
        labeled.setStyle("-fx-font-family: Verdana;-fx-font-size:14px;");


    }

    private boolean checkChoose(TextField nickNameTextField, TextField ipAddressTextField, TextField portTextField) {

        boolean allOk = true;

        if (cliController.nameController(nickNameTextField.getText()) || nickNameTextField.getText().length() == 0) {
            setRedColor(nickNameTextField);
            if (nickNameTextField.getText().length() == 0)
                nickNameTextField.setPromptText("Insert your nickname!");
            else
                nickNameTextField.setPromptText(getGraphicsClient().wrongNick());
            nickNameTextField.clear();
            allOk = false;

        } else {
            setNickname(nickNameTextField.getText());
        }

        if (cliController.ipController(ipAddressTextField.getText()) || ipAddressTextField.getText().length() == 0) {
            ipAddressTextField.clear();
            setRedColor(ipAddressTextField);
            ipAddressTextField.setPromptText("Incorrect IP Address!");
            allOk = false;

        } else {
            setAddress(ipAddressTextField.getText());
        }
        if (cliController.portController(portTextField.getText()) || portTextField.getText().length() == 0) {
            portTextField.clear();
            setRedColor(portTextField);
            portTextField.setPromptText("Incorrect Port!");
            allOk = false;

        } else {
            setPort(portTextField.getText());
        }
        return allOk;

    }

    private void setRedColor(TextField textField) {
        textField.setStyle("-fx-prompt-text-fill: red");

    }

    private Scene waitAnswer(String s) {
        Label textField = new Label(s);
        textField.setTextAlignment(TextAlignment.CENTER);
        textField.setStyle("-fx-font-size: 30px;");
        return new Scene(textField, h, v);
    }

    @Override
    public void loginSuccess(String fromServer) {

        Label label = new Label(fromServer);
        label.setAlignment(Pos.CENTER);
        label.setStyle("-fx-font-size: 30px;");
        Platform.runLater(() -> {gameGUI = new GameGUI();
        getPrimaryStage().setScene(new Scene(label, h / 2, v / 2));});
        Thread thread = new Thread(() -> getConnectionClient().game());
        thread.start();
    }

    @Override
    public void errorLogin() {
        Platform.runLater(() -> getPrimaryStage().setScene(waitAnswer(getGraphicsClient().errorConnection())));

    }

    @Override
    public void showSchemes(String fromServer) {

        getPrimaryStage().setWidth(h * 2 / 3);
        getPrimaryStage().setHeight(h);


        VBox vBox = new VBox();
        vBox.setSpacing(h / 20);
        vBox.setAlignment(Pos.TOP_CENTER);
        text = new Label("Welcome to Sagrada,  " + getNickname() + "\nPlease choose one of the following schemes");
        text.setMinHeight(h / 20);
        text.setTextAlignment(TextAlignment.CENTER);
        vBox.getChildren().add(text);

        HBox hBox = new HBox();
        hBox.setSpacing(h / 50);
        privateAchievement = UtilGUI.cleanString(privateAchievement);
        Label achievement = new Label(privateAchievement.substring(0, privateAchievement.length() - 2));
        achievement.setMinHeight(h / 20);
        achievement.setMinWidth(h / 16);
        achievement.setStyle("-fx-font-family: Verdana;-fx-font-size:16px;");
        text.setStyle("-fx-font-size: 18px;" + "-fx-font-family: Verdana;" + "-fx-font-weight:bold;");
        achievement.setTextAlignment(TextAlignment.RIGHT);
        hBox.getChildren().add(achievement);


        Label color = new Label();
        color.setStyle(ColorGUI.valueOf(privateAchievement.substring(privateAchievement.length() - 1)).getSetOnDie());
        color.setMinWidth(h / 20);
        color.setMinHeight(h / 20);
        color.setAlignment(Pos.CENTER_LEFT);
        hBox.getChildren().add(color);
        hBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(hBox);


        String substringSchemes = fromServer.substring(fromServer.indexOf('.') + 2);
        String[] schemes = substringSchemes.split(",");

        GridPane gridPane = new GridPane();
        gridPane.setVgap(h / 50);
        gridPane.setHgap(h / 50);
        gridPane.setAlignment(Pos.CENTER);
        Button[] b = new Button[4];

        for (int k = 0; k < 4; k++) {
            b[k] = new Button();
            b[k].setPrefSize(h / 3.5, h / 3.5);
            b[k].setAlignment(Pos.CENTER);
            SchemeParser parser = new SchemeParser(schemes[k]);

            b[k].setGraphic(UtilGUI.drawScheme(parser.getName(), parser.getToken(), drawLabelScheme(parser.getStringScheme(), h / 20), h));
            String numberChoose = String.valueOf(k + 1);

            b[k].setOnAction(event -> {

                for (Button button : b) {
                    button.setDisable(true);

                }
                b[Integer.parseInt(numberChoose) - 1].setOpacity(1);
                Thread thread = new Thread(() ->getConnectionClient().handleScheme(fromServer,numberChoose));
                thread.start();



            });
            gridPane.add(b[k], k % 2, k / 2);


        }
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(gridPane);
        vBox.setStyle("-fx-background-color: grey");
        Platform.runLater(() -> getPrimaryStage().setScene(new Scene(vBox, h * 2 / 3, h)));
    }

    @Override
    public void showAnswer(String s) {
        text = new Label(s);

    }

    @Override
    public void showPrivateAchievement(String s) {
        this.privateAchievement = s;
    }

    @Override
    public void startGame(String s) {


        getPrimaryStage().setX(0);
        getPrimaryStage().setY(0);
        getPrimaryStage().setWidth(Screen.getPrimary().getBounds().getWidth());
        getPrimaryStage().setHeight(Screen.getPrimary().getBounds().getHeight());


    }

    @Override
    public void showPublicAchievements(String s) {
        gameGUI.setAchievements(s, privateAchievement);

    }

    @Override
    public void showTools(String s) {
        gameGUI.setTools(s);
    }

    @Override
    public void showRoundTrack(String s) {
        gameGUI.setRoundTrack(s);
    }

    @Override
    public void showDraftPool(String s) {
        gameGUI.setDraftPool(s);
    }


    @Override
    public void showPlayers(String s) {
        gameGUI.setPlayers(s, getNickname());
    }

    @Override
    public void endUpdate() {
        getConnectionClient().setInputControl(true);
        Platform.runLater(()->gameGUI.newSecondStage());
        gameGUI.show(getPrimaryStage());

    }

    @Override
    public String getAction() {
        gameGUI.activeButtons();
        return gameGUI.getMove();
    }

    @Override
    public String getIndex() {
        gameGUI.activeDraftPool();
        return gameGUI.getMove();
    }

    @Override
    public String getRowColumn() {
        gameGUI.activeDashboard();
        return gameGUI.getMove();
    }

    @Override
    public String getRoundTrack() {
        gameGUI.activeRoundTrack();
        return gameGUI.getMove();
    }

    @Override
    public String getTool() {
        gameGUI.activeTools();
        return gameGUI.getMove();
    }

    @Override
    public String getPlusOrMinus() {
        gameGUI.activePlusOrMinusWindow();
        return  gameGUI.getMove();
    }

    @Override
    public String getDieNumber() {
        gameGUI.activeNumberDiceWindow();
        return gameGUI.getMove();
    }

    @Override
    public String getNumber() {
        gameGUI.activeNumberWindow();
        return gameGUI.getMove();
    }

    @Override
    public void showGenericMessage(String s) {

        gameGUI.setMessageFromServer(s);

    }

    @Override
    public void newGame(String s) { //TODO SISTEMARE
    }

    @Override
    public void terminate(String s) {
        gameGUI.setMessageFromServer(s);
        getPrimaryStage().close();
    }

    @Override
    public void showOutput(String s) { //TODO SISTEMARE
    }


    private GridPane drawLabelScheme(String s, double h){

        String cleanedString = cleanString(s);
        String[] arr = cleanedString.split("]");
        GridPane labelScheme = new GridPane();
        labelScheme.setHgap(0);
        labelScheme.setVgap(0);
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 5; j++) {
                int ind = i * 5 + j;
                Label label = drawDashboard(arr[ind],h);
                label.setMinSize(h,h);
                label.setMaxSize(h,h);
                labelScheme.add(label,j,i);
            }
        return labelScheme;


    }
}
