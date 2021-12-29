package sample.function;

import static sample.function.FunctionUtils.PRECISION;

/**
 * Perform Rastrigin function.
 * Domain is [-4, 4]
 * Minimum is 0 at x = 0 & z = 0.
 */
public class Rastrigin extends Function {

    public Rastrigin() {
        super("Rastrigin", 0, 0,
                65, 4, -150);
    }

    @Override
    public double getValueInPoint(double x, double z) {
        double p1 = Math.pow(x, 2) - 10 * Math.cos(2 * Math.PI * x);
        double p2 = Math.pow(z, 2) - 10 * Math.cos(2 * Math.PI * z);
        return (double) Math.round((20 + p1 + p2) * PRECISION) / PRECISION;
    }

}
