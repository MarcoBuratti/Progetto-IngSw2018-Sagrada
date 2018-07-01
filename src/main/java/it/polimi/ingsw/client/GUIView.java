package it.polimi.ingsw.client;

import it.polimi.ingsw.util.GraphicsClient;
import it.polimi.ingsw.util.InputController;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GUIView extends View {

    private GraphicsClient graphicsClient;
    private InputController cliController;
    private GridPane grid;
    private Scene scene;
    private String inputString;
    private double h = 800;
    private double v = 600;


    public GUIView() {
        graphicsClient = new GraphicsClient();
        cliController = new InputController();
    }


    @Override
    public void start() {
        super.primaryStage.setTitle("Login Sagrada");
        grid = new GridPane();
        grid.heightProperty().addListener((observable, oldValue, newValue) -> v= (double) newValue);
        grid.widthProperty().addListener((observable, oldValue, newValue) -> h= (double) newValue);
        scene = new Scene(grid,h,v);
        super.primaryStage.setScene(scene);

        ImageView img = new ImageView(new Image(getClass().getResourceAsStream("/images/intro.jpg"),h*9/20,v/2,true,true));

        grid.setPadding(new Insets(v/50,h/20,v/50,h/20));
        grid.vgapProperty().bind(grid.heightProperty().divide(100));
        grid.hgapProperty().bind((grid.widthProperty().divide(50)));

        Label label1 = new Label(graphicsClient.askNick());
        setSize(label1);
        label1.setAlignment(Pos.CENTER);
        GridPane.setConstraints(label1, 0, 2);

        TextField nickNameTextField = new TextField();
        nickNameTextField.setPromptText("Inserire nickname");
        setSize(nickNameTextField);
        GridPane.setConstraints(nickNameTextField, 1, 2);

        Label label2 = new Label(graphicsClient.printIP());
        setSize(label2);
        label2.setAlignment(Pos.CENTER);
        GridPane.setConstraints(label2, 2, 2);

        TextField ipAddressTextField = new TextField();
        ipAddressTextField.setPromptText("Inserire indirizzo IP");
        setSize(ipAddressTextField);
        GridPane.setConstraints(ipAddressTextField, 3, 2);

        Label label3 = new Label(graphicsClient.printPort());
        setSize(label3);
        label3.setAlignment(Pos.CENTER);
        GridPane.setConstraints(label3, 0, 3);

        TextField portTextField = new TextField();
        portTextField.setPromptText("Inserire porta");
        setSize(portTextField);
        GridPane.setConstraints(portTextField, 1, 3);

        Label label4 = new Label("Connessione:");
        setSize(label4);
        label4.setAlignment(Pos.CENTER);
        GridPane.setConstraints(label4, 2, 3);

        ChoiceBox choiceBox = new ChoiceBox();
        choiceBox.getItems().addAll("RMI", "SOCKET");
        choiceBox.setValue("RMI");
        setSize(choiceBox);
        choiceBox.setCenterShape(true);
        GridPane.setConstraints(choiceBox, 3, 3);

        Button loginButton = new Button("Log in");
        loginButton.setStyle("-fx-font-family: Verdana;-fx-font-size:20px;-fx-font-weight:bold;");
        loginButton.maxHeightProperty().bind(grid.heightProperty().divide(10));
        loginButton.maxWidthProperty().bind((grid.widthProperty().subtract(h/10)));
        loginButton.minHeightProperty().bind(grid.heightProperty().divide(10));
        loginButton.minWidthProperty().bind((grid.widthProperty()).subtract(h/10));
        loginButton.requestFocus();
        loginButton.setOnAction(event -> Platform.runLater(() -> {
            if (checkChoose(nickNameTextField, ipAddressTextField, portTextField)) {
                setChoice(choiceBox.getValue().toString());
                super.primaryStage.close();
                createConnection();




            }
        }));
        BorderPane pane = new BorderPane();
        pane.setCenter(img);
        img.fitWidthProperty().bind(pane.widthProperty());
        img.fitHeightProperty().bind(pane.heightProperty());
        pane.maxHeightProperty().bind(grid.heightProperty().divide(2));
        pane.maxWidthProperty().bind((grid.widthProperty().subtract(h/10)));
        pane.minHeightProperty().bind(grid.heightProperty().divide(2));
        pane.minWidthProperty().bind((grid.widthProperty()).subtract(h/10));
        GridPane.setConstraints(loginButton, 0, 10, 4, 1);
        GridPane.setConstraints(pane, 0, 0, 4, 1);


        grid.getChildren().addAll(label1, label2, label3, label4, nickNameTextField, ipAddressTextField, portTextField, loginButton, choiceBox,pane);
        grid.setStyle("-fx-background-color: darkkhaki");
        primaryStage.show();

    }


    private void setSize(Control labeled){

        labeled.maxHeightProperty().bind(grid.heightProperty().divide(10));
        labeled.maxWidthProperty().bind((grid.widthProperty().subtract(16/3)));
        labeled.minHeightProperty().bind(grid.heightProperty().divide(10));
        labeled.minWidthProperty().bind((grid.widthProperty()).divide(16/3));
        labeled.setStyle("-fx-font-family: Verdana;-fx-font-size:14px;");



    }

    private boolean checkChoose(TextField nickNameTextField,TextField ipAddressTextField,TextField portTextField){

        boolean allOk =true;

        if (cliController.nameController(nickNameTextField.getText())||nickNameTextField.getText().length()==0) {
            setRedColor(nickNameTextField);
            if(nickNameTextField.getText().length()==0)
                nickNameTextField.setPromptText("Inserire nickname!");
            else
                nickNameTextField.setPromptText(graphicsClient.wrongNick());
            nickNameTextField.clear();
            allOk=false;

        }else{
            setNickname(nickNameTextField.getText());
        }

        if (cliController.ipController(ipAddressTextField.getText())||ipAddressTextField.getText().length()==0) {
            ipAddressTextField.clear();
            setRedColor(ipAddressTextField);
            ipAddressTextField.setPromptText("Indirizzo IP non valido!");
            allOk=false;

        }else{
            setAddress(ipAddressTextField.getText());
        }
        if (cliController.portController(portTextField.getText())||portTextField.getText().length()==0) {
            portTextField.clear();
            setRedColor(portTextField);
            portTextField.setPromptText("Porta non valida!");
            allOk=false;

        }else{
            setPort(portTextField.getText());
        }
        return allOk;

    }

    private void setRedColor(TextField textField){
        textField.setStyle("-fx-prompt-text-fill: red");

    }

    @Override
    public void showInput(String s) {

    }

    @Override
    public void showOutput(String s) {

    }

    @Override
    public String getInput() {
        return null;
    }


    @Override
    public void setScheme() {

    }

    @Override
    public void continueToPlay(String s) {

    }
}
