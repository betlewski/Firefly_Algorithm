package sample.function;

import static sample.function.FunctionUtils.PRECISION;

/**
 * Perform Eggholder function.
 * Domain is [-4, 4]
 * Minimum is -45 at x = 4 & z = 4.
 */
public class Eggholder extends Function {

    public Eggholder() {
        super("Eggholder", -45, -45,
                100, 3.5, -100);
    }

    @Override
    public double getValueInPoint(double x, double z) {
        double p1 = -(z + 47) * Math.sin(Math.sqrt(Math.abs(z + x / 2 + 47)));
        double p2 = -x * Math.sin(Math.sqrt(Math.abs(x - (z + 47))));
        return (double) Math.round((p1 + p2) * PRECISION) / PRECISION;
    }

}
