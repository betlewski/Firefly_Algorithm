package sample.chart;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.List;

import static sample.function.FunctionUtils.getFunction;

public class ChartUtils {

    // chart parameters
    public static final int SIZE = 400;
    public static final double FREQUENCY = 8.0 / (double) SIZE;

    public static float getValueInChartPoints(double chartX, double chartZ) {
        double functionX = chartX * FREQUENCY - 4;
        double functionZ = chartZ * FREQUENCY - 4;
        return (float) getFunction().getValueInPoint(functionX, functionZ);
    }

    public static float valueMapper(float functionY) {
        double chartY = functionY * getFunction().getChartAmp() + getFunction().getChartOff();
        return (float) chartY;
    }

    public static Point3D chartPointConverter(Point3D functionPoint) {
        double chartX = functionPoint.getX() / FREQUENCY;
        double chartZ = functionPoint.getZ() / FREQUENCY;
        float y = (float) getFunction().getValueInPoint(functionPoint.getX(), functionPoint.getZ());
        double chartY = valueMapper(y);
        return new Point3D(chartX, chartY, chartZ);
    }

    public static Group createCube() {
        Group cube = new Group();
        List<Axis> cubeFaces = new ArrayList<>();
        Axis r;

        // left face
        r = new Axis(SIZE, Color.RED);
        r.setTranslateX(-1 * SIZE - 1);
        r.setTranslateY(-0.5 * SIZE);
        r.setRotationAxis(Rotate.Y_AXIS);
        r.setRotate(90);
        cubeFaces.add(r);

        // right face
        r = new Axis(SIZE, Color.rgb(51, 204, 51));
        r.setTranslateX(-0.5 * SIZE);
        r.setTranslateY(-0.5 * SIZE);
        r.setTranslateZ(-0.5 * SIZE);
        cubeFaces.add(r);

        // bottom face
        r = new Axis(SIZE, Color.DARKBLUE);
        r.setTranslateX(-0.5 * SIZE);
        r.setTranslateY(-1 * SIZE);
        r.setRotationAxis(Rotate.X_AXIS);
        r.setRotate(90);
        cubeFaces.add(r);

        cube.getChildren().addAll(cubeFaces);
        return cube;
    }

    public static float[][] getFunctionValues() {
        float[][] tab = new float[SIZE][SIZE];
        for (int x = 0; x < SIZE; x++) {
            for (int z = 0; z < SIZE; z++) {
                tab[x][z] = getValueInChartPoints(x, z);
            }
        }
        return tab;
    }

    public static TriangleMesh createMesh(float[][] functionValues) {
        TriangleMesh mesh = new TriangleMesh();
        for (int x = 0; x < SIZE; x++) {
            for (int z = 0; z < SIZE; z++) {
                mesh.getPoints().addAll(x, valueMapper(functionValues[x][z]), z);
            }
        }
        // texture
        int length = SIZE;
        for (float x = 0; x < length - 1; x++) {
            for (float z = 0; z < length - 1; z++) {
                float x0 = x / (float) length;
                float z0 = z / (float) length;
                float x1 = (x + 1) / (float) length;
                float z1 = (z + 1) / (float) length;
                mesh.getTexCoords().addAll( //
                        x0, z0, // 0, top-left
                        x0, z1, // 1, bottom-left
                        x1, z1, // 2, top-right
                        x1, z1 // 3, bottom-right
                );
            }
        }
        // faces
        for (int x = 0; x < length - 1; x++) {
            for (int z = 0; z < length - 1; z++) {
                int tl = x * length + z; // top-left
                int bl = x * length + z + 1; // bottom-left
                int tr = (x + 1) * length + z; // top-right
                int br = (x + 1) * length + z + 1; // bottom-right
                int offset = (x * (length - 1) + z) * 8 / 2; // div 2 because we have u AND v in the list
                mesh.getFaces().addAll(bl, offset + 1, tl, offset, tr, offset + 2);
                mesh.getFaces().addAll(tr, offset + 2, br, offset + 3, bl, offset + 1);
            }
        }
        return mesh;
    }

    public static Image createDiffuseMap(float[][] functionValues) {
        WritableImage wr = new WritableImage(SIZE, SIZE);
        PixelWriter pw = wr.getPixelWriter();

        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                float value = functionValues[x][y];
                double normalized = normalizeValue(value, getFunction().getChartMin(),
                        getFunction().getChartMax(), 0.0, 1.0);
                Color color = Color.DARKBLUE.interpolate(Color.LIGHTBLUE, normalized);
                pw.setColor(x, y, color);
            }
        }
        return wr;
    }

    public static double normalizeValue(double value, double min, double max, double newMin, double newMax) {
        return (value - min) * (newMax - newMin) / (max - min) + newMin;
    }

    public static double clamp(double value, double min, double max) {
        if (Double.compare(value, min) < 0) {
            return min;
        }
        if (Double.compare(value, max) > 0) {
            return max;
        }
        return value;
    }

}
