package sample.function;

import static sample.function.FunctionUtils.PRECISION;

/**
 * Perform Levy function N.13.
 * Domain is [-4, 4]
 * Minimum is 0 at x = 1 & z = 1.
 */
public class Levy extends Function {

    public Levy() {
        super("Levy N. 13", 0, -10,
                70, 4, -180);
    }

    @Override
    public double getValueInPoint(double x, double z) {
        double p1 = Math.pow(Math.sin(3 * Math.PI * x), 2);
        double p2 = Math.pow((x - 1), 2) * (1 + Math.pow(Math.sin(3 * Math.PI * z), 2));
        double p3 = Math.pow((z - 1), 2) * (1 + Math.pow(Math.sin(2 * Math.PI * z), 2));
        return (double) Math.round((p1 + p2 + p3) * PRECISION) / PRECISION;
    }

}
