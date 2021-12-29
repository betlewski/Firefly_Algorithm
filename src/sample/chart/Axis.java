package sample.chart;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class Axis extends Pane {

    public Axis(double size) {
        this(size, Color.BLACK);
    }

    public Axis(double size, Color gridColor) {
        Rectangle wall = new Rectangle(size, size);
        wall.setFill(Color.TRANSPARENT);
        getChildren().add(wall);

        for (int x = 0; x <= size; x += size / 10) {
            Line line = new Line(0, 0, 0, size);
            line.setStroke(gridColor);
            line.setStrokeWidth(2.0);
            line.setTranslateX(x);
            getChildren().addAll(line);
        }
        for (int y = 0; y <= size; y += size / 10) {
            Line line = new Line(0, 0, size, 0);
            line.setStroke(gridColor);
            line.setStrokeWidth(2.0);
            line.setTranslateY(y);
            getChildren().addAll(line);
        }
    }

}
