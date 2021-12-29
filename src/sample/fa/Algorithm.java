package sample.fa;

import javafx.application.Platform;
import javafx.geometry.Point3D;
import sample.controller.Controller;
import sample.function.FunctionUtils;

import java.util.ArrayList;
import java.util.List;

import static sample.fa.AlgorithmParam.*;
import static sample.function.FunctionUtils.MAX_VALUE;

public class Algorithm {

    private List<Firefly> swarm;
    private int swarmSize;
    private int iterationsNumber;
    private double maxAttractionCoefficient;
    private double absorptionCoefficient;
    private double randomStepCoefficient;
    private double stepDownCoefficient;

    private boolean alive = false;
    private boolean paused = false;
    private final Object lock = new Object();
    private double bestValueAtAll = MAX_VALUE;

    private Controller controller;

    public Algorithm(int swarmSize, int iterationsNumber) {
        this(new AlgorithmParam(swarmSize, iterationsNumber, DEFAULT_MAX_ATTRACTION,
                DEFAULT_ABSORPTION, DEFAULT_RANDOM_STEP, DEFAULT_STEP_DOWN));
    }

    public Algorithm(AlgorithmParam param) {
        swarmSize = param.getSwarmSize();
        iterationsNumber = param.getIterationsNumber();
        maxAttractionCoefficient = param.getMaxAttractionCoefficient();
        absorptionCoefficient = param.getAbsorptionCoefficient();
        randomStepCoefficient = param.getRandomStepCoefficient();
        stepDownCoefficient = param.getStepDownCoefficient();
    }

    private Thread algorithm = new Thread(() -> {
        initSwarm();
        for (int i = 1; i <= iterationsNumber && isAlive(); i++) {
            List<Point3D> points = iterate();
            Platform.runLater(() -> {
                controller.getChart3D().refresh(points);
                controller.getActualResultLabel().setText(String.format("%.2f", getBestValueAtAll()));
            });
//            System.out.println("-- ITERATION " + (i + 1) + " | Best found value: " + results() + "\n");
            delay();
            allowPause();
        }
        alive = false;
        controller.setControlsAfterFinish();
//        System.out.println("-- FINISHED");
    });

    public void show(Controller controller) {
        this.controller = controller;
        alive = true;
        algorithm.start();
    }

    private void initSwarm() {
        swarm = new ArrayList<>();
        for (int i = 0; i < swarmSize; i++) {
            Firefly firefly = new Firefly(FunctionUtils.MIN_RANGE, FunctionUtils.MAX_RANGE);
            swarm.add(firefly);
        }
//        results();
//        System.out.println("-- INITIALIZATION\n");
    }

    private List<Point3D> iterate() {
        List<Point3D> points = new ArrayList<>();
        for (Firefly current : swarm) {
            boolean foundBetter = false;
            for (Firefly another : swarm) {
                if (another.getLuminescence() > current.getLuminescence()) {
                    double distance = current.getDistanceTo(another);
                    double attraction = calculateAttraction(distance);
                    current.moveToAnother(another, attraction);
                    current.moveRandomly(randomStepCoefficient);
                    current.updateLuminescence();
                    foundBetter = true;
                }
            }
            if (!foundBetter) {
                current.moveRandomly(randomStepCoefficient);
                current.updateLuminescence();
            }
            Point3D point = new Point3D(current.getPosX(), current.eval(), current.getPosZ());
            points.add(point);
        }
        randomStepUpdate();
        return points;
    }

    private double calculateAttraction(double distance) {
        double expIndex = -1 * absorptionCoefficient * Math.pow(distance, 2);
        double expValue = Math.exp(expIndex);
        return maxAttractionCoefficient * expValue;
    }

    private void randomStepUpdate() {
        randomStepCoefficient = randomStepCoefficient * stepDownCoefficient;
    }

    private double getBestValueAtAll() {
        double bestValueInStep = getBestValueInStep();
        if (bestValueInStep < bestValueAtAll) {
            bestValueAtAll = bestValueInStep;
        }
        return (Math.round(bestValueAtAll * 100.0) / 100.0);
    }

    private double getBestValueInStep() {
        double bestValue = swarm.get(0).eval();
        for (Firefly current : swarm) {
            double currentValue = current.eval();
            if (currentValue < bestValue) {
                bestValue = currentValue;
            }
        }
        return (Math.round(bestValue * 100.0) / 100.0);
    }

    private void delay() {
        try {
            Thread.sleep(chartWaitTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void allowPause() {
        synchronized (lock) {
            while (paused) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void pause() {
        paused = true;
    }

    public void rerun() {
        paused = false;
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    public void stop() {
        alive = false;
    }

    public boolean isAlive() {
        return alive;
    }

//    private double results() {
//        Firefly best = swarm.get(0);
//        for (Firefly current : swarm) {
//            System.out.println("--- x = " + (Math.round(current.getPosX() * 100.0) / 100.0) +
//                    ", z = " + (Math.round(current.getPosZ() * 100.0) / 100.0) +
//                    ", value = " + (Math.round(current.eval() * 100.0) / 100.0));
//            if (current.eval() < best.eval()) {
//                best = current;
//            }
//        }
//        return (Math.round(best.eval() * 100.0) / 100.0);
//    }

//    public void doResearch() {
//        alive = true;
//        final int samples = 1000;
//        double standDeviation = 0.0;
//        System.out.println("---------- STARTING RESEARCH ----------");
//        for (int k = 1; k <= samples; k++) {
//            swarm = new ArrayList<>();
//            for (int i = 0; i < swarmSize; i++) {
//                Firefly firefly = new Firefly(FunctionUtils.MIN_RANGE, FunctionUtils.MAX_RANGE);
//                swarm.add(firefly);
//            }
//            for (int i = 0; i < iterationsNumber; i++) {
//                iterate();
//            }
//            double bestValue = getBestValueInStep();
//            standDeviation += Math.pow(bestValue, 2);
//            if (bestValue < bestValueAtAll) {
//                bestValueAtAll = bestValue;
//            }
//            System.out.println(k + "/" + samples);
//        }
//        standDeviation = Math.sqrt(standDeviation / (double) samples);
//        System.out.println("---------- COMPLETED ----------");
//        System.out.println("The best value: " + bestValueAtAll);
//        System.out.println("Standard Deviation: " + standDeviation);
//        alive = false;
//    }

}
