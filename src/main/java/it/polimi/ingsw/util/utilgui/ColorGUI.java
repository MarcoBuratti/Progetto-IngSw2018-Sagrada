package it.polimi.ingsw.util.utilgui;

import javafx.scene.paint.Color;

public enum ColorGUI {


    WHITE("-fx-background-color: lightyellow;" + "-fx-border-color: black;", "-fx-background-color:white;" + "-fx-border-color: black;", Color.WHITE),
    G("-fx-background-color: limegreen;" + "-fx-border-color: black;", "-fx-background-color: green;" + "-fx-border-color: black;", Color.GREEN),
    Y("-fx-background-color: yellow;" + "-fx-border-color: black;", "-fx-background-color: gold;" + "-fx-border-color: black;", Color.GOLD),
    B("-fx-background-color: steelblue;" + "-fx-border-color: black;", "-fx-background-color: blue;" + "-fx-border-color: black;", Color.BLUE),
    R("-fx-background-color: tomato;" + "-fx-border-color: black;", "-fx-background-color: red;" + "-fx-border-color: black;", Color.RED),
    V("-fx-background-color: darkorchid;" + "-fx-border-color: black;", "-fx-background-color: purple;" + "-fx-border-color: black;", Color.PURPLE);

    private String setOnButton;
    private String setOnDie;
    private Color color;

    ColorGUI(String setOnButton, String setOnDie, Color color) {
        this.setOnButton = setOnButton;
        this.setOnDie = setOnDie;
        this.color = color;
    }

    public String getSetOnButton() {
        return setOnButton;
    }

    public String getSetOnDie() {
        return setOnDie;
    }

    public Color getColor() {
        return color;
    }
}
