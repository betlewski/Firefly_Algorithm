package sample.chart;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;

import static sample.chart.ChartUtils.*;

public class Chart3D {

    // mouse interaction
    private double mouseOldX, mouseOldY;
    private final Rotate rotateX = new Rotate(200, Rotate.X_AXIS);
    private final Rotate rotateY = new Rotate(-45, Rotate.Y_AXIS);

    private Group cube;
    private List<Node> actualObjects = new ArrayList<>();

    public Group createChart() {
        Group root = new Group();
        root.setTranslateX(420);
        root.setTranslateY(400);

        // ambient light
        AmbientLight ambient = new AmbientLight();
        ambient.setLightOn(true);
        root.getChildren().add(ambient);

        // axis walls
        cube = createCube();
        cube.getTransforms().addAll(rotateX, rotateY);
        root.getChildren().add(cube);

        // function mesh
        float[][] functionValues = getFunctionValues();
        TriangleMesh mesh = createMesh(functionValues);
        Image diffuseMap = createDiffuseMap(functionValues);
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(diffuseMap);
        material.setSpecularColor(Color.TRANSPARENT);
        MeshView meshView = new MeshView(mesh);
        meshView.setTranslateX(-0.5 * SIZE);
        meshView.setTranslateZ(-0.5 * SIZE);
        meshView.setMaterial(material);
        meshView.setCullFace(CullFace.NONE);
        meshView.setDrawMode(DrawMode.FILL);
        meshView.setDepthTest(DepthTest.ENABLE);
        cube.getChildren().addAll(meshView);

        return root;
    }

    public void refresh(List<Point3D> points) {
        // remove objects from last iteration
        cube.getChildren().removeAll(actualObjects);
        actualObjects = new ArrayList<>();
        points.sort(Comparator.comparingDouble(Point3D::getY));

        for (Point3D point : points) {
            Point3D chartPoint = chartPointConverter(point);

            // calculate point color based on the rank
            PhongMaterial sphereMaterial = new PhongMaterial();
            double normalized = normalizeValue(points.indexOf(point),
                    0, points.size(), 0.0, 1.0);
            Color color = Color.YELLOW.interpolate(Color.WHITE, normalized);
            sphereMaterial.setDiffuseColor(color);
            sphereMaterial.setSpecularColor(Color.BLACK);

            // create sphere imitating firefly
            Sphere sphere = new Sphere();
            sphere.setRadius(4);
            sphere.setMaterial(sphereMaterial);
            sphere.setTranslateX(chartPoint.getX());
            sphere.setTranslateY(chartPoint.getY());
            sphere.setTranslateZ(chartPoint.getZ());
            actualObjects.add(sphere);
        }
        cube.getChildren().addAll(actualObjects);
    }

    public void setOnMousePressed(double mouseOldX, double mouseOldY) {
        this.mouseOldX = mouseOldX;
        this.mouseOldY = mouseOldY;
    }

    public void setOnMouseDragged(double mousePosX, double mousePosY) {
        rotateX.setAngle(rotateX.getAngle() - (mousePosY - mouseOldY));
        rotateY.setAngle(rotateY.getAngle() + (mousePosX - mouseOldX));
        mouseOldX = mousePosX;
        mouseOldY = mousePosY;
    }

    public void makeZoomable(Group control) {
        final double MAX_SCALE = 20.0;
        final double MIN_SCALE = 0.1;

        control.addEventFilter(ScrollEvent.ANY, event -> {
            double delta = 1.2;
            double scale = control.getScaleX();
            if (event.getDeltaY() < 0) {
                scale /= delta;
            } else {
                scale *= delta;
            }
            scale = clamp(scale, MIN_SCALE, MAX_SCALE);
            control.setScaleX(scale);
            control.setScaleY(scale);
            event.consume();
        });
    }

}