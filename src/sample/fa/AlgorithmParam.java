package sample.fa;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AlgorithmParam {

    public static final double DEFAULT_MAX_ATTRACTION = 1.0;
    public static final double DEFAULT_ABSORPTION = 1.0;
    public static final double DEFAULT_RANDOM_STEP = 0.8;
    public static final double DEFAULT_STEP_DOWN = 0.99;

    private final int swarmSize;
    private final int iterationsNumber;
    private final double maxAttractionCoefficient;
    private final double absorptionCoefficient;
    private final double randomStepCoefficient;
    private final double stepDownCoefficient;

    public static long chartWaitTime = 1000;

}
