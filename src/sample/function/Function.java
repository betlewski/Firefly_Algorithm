package sample.function;

import lombok.Getter;

import static sample.function.FunctionUtils.MAX_VALUE;

@Getter
public class Function {

    private final String name;
    private final double realMin;
    private final double chartMin;
    private final double chartMax;
    private final double chartAmp;
    private final double chartOff;

    public Function(String name, double realMin,
                    double chartMin, double chartMax,
                    double chartAmp, double chartOff) {
        this.name = name;
        this.realMin = realMin;
        this.chartMin = chartMin;
        this.chartMax = chartMax;
        this.chartAmp = chartAmp;
        this.chartOff = chartOff;
    }

    public double getValueInPoint(double x, double z) {
        return 0;
    }

    public double getFitness(double x, double z) {
        return MAX_VALUE - getValueInPoint(x, z);
    }

    @Override
    public String toString() {
        return name;
    }

}
