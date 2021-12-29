package sample.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import lombok.Getter;
import sample.chart.Chart3D;
import sample.fa.Algorithm;
import sample.fa.AlgorithmParam;
import sample.function.Function;

import java.util.List;

import static sample.controller.Icon.*;
import static sample.fa.AlgorithmParam.*;
import static sample.function.FunctionUtils.*;

@Getter
public class Controller {

    @FXML
    private TextField swarmSizeText;
    @FXML
    private TextField iterationsNumberText;
    @FXML
    private Slider maxAttractionSlider;
    @FXML
    private TextField maxAttractionText;
    @FXML
    private Slider absorptionSlider;
    @FXML
    private TextField absorptionText;
    @FXML
    private Slider randomStepSlider;
    @FXML
    private TextField randomStepText;
    @FXML
    private Slider stepDownSlider;
    @FXML
    private TextField stepDownText;
    @FXML
    private ComboBox<Function> functionComboBox;
    @FXML
    private Button actionButton;
    @FXML
    private Button resetButton;
    @FXML
    private Slider chartDelaySlider;
    @FXML
    private SubScene subScene;
    @FXML
    private Label realResultLabel;
    @FXML
    private Label actualResultLabel;

    private Chart3D chart3D;
    private Algorithm algorithm = null;
    private boolean playActionButton;
    private static final double COEF_PRECISION = 100.0;
    private static final Color BACKGROUND_COLOR = Color.rgb(25, 0, 25);

    private int swarmSize;
    private int iterationsNumber;
    private double maxAttractionCoefficient;
    private double absorptionCoefficient;
    private double randomStepCoefficient;
    private double stepDownCoefficient;

    @FXML
    public void initialize() {
        setDefaultValues();
        initComboBox();
        initSliders();
        initButtons();
    }

    private void setDefaultValues() {
        maxAttractionCoefficient = DEFAULT_MAX_ATTRACTION;
        absorptionCoefficient = DEFAULT_ABSORPTION;
        randomStepCoefficient = DEFAULT_RANDOM_STEP;
        stepDownCoefficient = DEFAULT_STEP_DOWN;
    }

    private void initComboBox() {
        List<Function> functions = getAllFunctions();
        functionComboBox.setItems(FXCollections.observableArrayList(functions));
        functionComboBox.valueProperty().addListener(
                (observable, oldValue, actualValue) -> {
                    setFunction(actualValue);
                    initChart(actualValue);
                });
        Function defaultFunction = functions.get(0);
        functionComboBox.setValue(defaultFunction);
    }

    private void initSliders() {
        maxAttractionSlider.setValue(maxAttractionCoefficient);
        maxAttractionText.setText(String.valueOf(maxAttractionCoefficient));
        maxAttractionSlider.valueProperty().addListener((observable, oldValue, actualValue) -> {
            double value = Math.round(actualValue.doubleValue() * COEF_PRECISION) / COEF_PRECISION;
            maxAttractionCoefficient = value;
            maxAttractionText.setText(String.valueOf(value));
        });
        absorptionSlider.setValue(absorptionCoefficient);
        absorptionText.setText(String.valueOf(absorptionCoefficient));
        absorptionSlider.valueProperty().addListener((observable, oldValue, actualValue) -> {
            double value = Math.round(actualValue.doubleValue() * COEF_PRECISION) / COEF_PRECISION;
            absorptionCoefficient = value;
            absorptionText.setText(String.valueOf(value));
        });
        randomStepSlider.setValue(randomStepCoefficient);
        randomStepText.setText(String.valueOf(randomStepCoefficient));
        randomStepSlider.valueProperty().addListener((observable, oldValue, actualValue) -> {
            double value = Math.round(actualValue.doubleValue() * COEF_PRECISION) / COEF_PRECISION;
            randomStepCoefficient = value;
            randomStepText.setText(String.valueOf(value));
        });
        stepDownSlider.setValue(stepDownCoefficient);
        stepDownText.setText(String.valueOf(stepDownCoefficient));
        stepDownSlider.valueProperty().addListener((observable, oldValue, actualValue) -> {
            double value = Math.round(actualValue.doubleValue() * COEF_PRECISION) / COEF_PRECISION;
            stepDownCoefficient = value;
            stepDownText.setText(String.valueOf(value));
        });
        long delaySliderSum = (long) (chartDelaySlider.getMax() + chartDelaySlider.getMin());
        chartDelaySlider.setValue(delaySliderSum - chartWaitTime);
        chartDelaySlider.valueProperty().addListener((observable, oldValue, actualValue) ->
                chartWaitTime = delaySliderSum - actualValue.longValue());
    }

    private void initButtons() {
        setPlayButton();
        actionButton.setOnAction(event -> {
            if (playActionButton) {
                playActionButton = false;
                actionButton.setGraphic(PAUSE_ICON);
                if (algorithm == null || !algorithm.isAlive()) {
                    startAlgorithm();
                } else {
                    algorithm.rerun();
                }
            } else {
                setPlayButton();
                if (algorithm.isAlive()) {
                    algorithm.pause();
                }
            }
        });
        resetButton.setGraphic(STOP_ICON);
        resetButton.setOnAction(event -> {
            if (algorithm != null && algorithm.isAlive()) {
                algorithm.stop();
                resetControls();
                initChart(functionComboBox.getValue());
            }
        });
    }

    private void initChart(Function function) {
        realResultLabel.setText(String.format("%.2f", function.getRealMin()));
        actualResultLabel.setText("-");
        drawChart();
    }

    private void drawChart() {
        chart3D = new Chart3D();
        Group root = chart3D.createChart();
        subScene.setRoot(root);
        subScene.setCamera(new PerspectiveCamera());
        subScene.setFill(BACKGROUND_COLOR);
        subScene.setOnMousePressed(me -> chart3D.setOnMousePressed(me.getSceneX(), me.getSceneY()));
        subScene.setOnMouseDragged(me -> chart3D.setOnMouseDragged(me.getSceneX(), me.getSceneY()));
        chart3D.makeZoomable(root);
    }

    private void startAlgorithm() {
        if (checkData()) {
            disableControls();
            AlgorithmParam param = new AlgorithmParam(swarmSize, iterationsNumber, maxAttractionCoefficient,
                    absorptionCoefficient, randomStepCoefficient, stepDownCoefficient);
            algorithm = new Algorithm(param);
            new Thread(() -> algorithm.show(this)).start();
        } else {
            setPlayButton();
        }
    }

    private boolean checkData() {
        if (swarmSizeText.getText().isEmpty() || iterationsNumberText.getText().isEmpty()) {
            displayErrorAlert("Nie podano wszystkich danych!");
            return false;
        }
        try {
            swarmSize = Integer.parseInt(swarmSizeText.getText());
            iterationsNumber = Integer.parseInt(iterationsNumberText.getText());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            displayErrorAlert("Podano niewłaściwe dane!");
            return false;
        }
        if (swarmSize < 1 || swarmSize > 1000 || iterationsNumber < 1 || iterationsNumber > 1000) {
            displayErrorAlert("Podano dane przekraczające zakres!");
            return false;
        }
        return true;
    }

    private void displayErrorAlert(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Napotkano błędy");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

    private void setPlayButton() {
        playActionButton = true;
        Platform.runLater(() -> actionButton.setGraphic(PLAY_ICON));
    }

    public void resetControls() {
        setControls(true);
        setPlayButton();
    }

    public void setControlsAfterFinish() {
        resetControls();
        subScene.setFill(Color.BLACK);
    }

    private void disableControls() {
        setControls(false);
    }

    private void setControls(boolean value) {
        functionComboBox.setDisable(!value);
        swarmSizeText.setDisable(!value);
        iterationsNumberText.setDisable(!value);
        maxAttractionSlider.setDisable(!value);
        absorptionSlider.setDisable(!value);
        randomStepSlider.setDisable(!value);
        stepDownSlider.setDisable(!value);
    }

}