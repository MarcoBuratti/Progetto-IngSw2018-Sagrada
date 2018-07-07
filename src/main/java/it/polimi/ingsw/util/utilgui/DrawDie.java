package it.polimi.ingsw.util.utilgui;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class DrawDie {


    public GridPane draw(Color color, Color circle, String number, double h) {

        GridPane grid = new GridPane();
        grid.setMinSize(h, h);
        grid.setHgap(h / 14);
        grid.setVgap(h / 14);
        switch (number) {
            case "1":
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        Circle c;
                        if (i == 1 && j == 1)
                            c = new Circle(h / 12, circle);
                        else
                            c = new Circle(h / 12, color);
                        GridPane.setConstraints(c, i, j);
                        grid.getChildren().add(c);

                    }
                }
                return grid;
            case "2":
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        Circle c;
                        if ((i == 0 || i == 2) && i == j)
                            c = new Circle(h / 12, circle);
                        else
                            c = new Circle(h / 12, color);
                        GridPane.setConstraints(c, i, j);
                        grid.getChildren().add(c);

                    }
                }
                return grid;
            case "3":
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        Circle c;
                        if (i == j)
                            c = new Circle(h / 12, circle);
                        else
                            c = new Circle(h / 12, color);
                        GridPane.setConstraints(c, i, j);
                        grid.getChildren().add(c);

                    }
                }
                return grid;
            case "4":
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        Circle c;
                        if (i == 1 || j == 1)
                            c = new Circle(h / 12, color);
                        else
                            c = new Circle(h / 12, circle);
                        GridPane.setConstraints(c, i, j);
                        grid.getChildren().add(c);

                    }
                }
                return grid;
            case "5":
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        Circle c;
                        if ((i == 1 || j == 1) && i != j)
                            c = new Circle(h / 12, color);
                        else
                            c = new Circle(h / 12, circle);
                        GridPane.setConstraints(c, i, j);
                        grid.getChildren().add(c);

                    }
                }
                return grid;
            case "6":
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        Circle c;
                        if (i == 1)
                            c = new Circle(h / 12, color);
                        else
                            c = new Circle(h / 12, circle);
                        GridPane.setConstraints(c, i, j);
                        grid.getChildren().add(c);

                    }
                }
                return grid;
            default:
                throw new IllegalArgumentException();


        }
    }

}

