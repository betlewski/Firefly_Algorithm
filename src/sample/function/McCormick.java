package sample.function;

import static sample.function.FunctionUtils.PRECISION;

/**
 * Perform McCormick function.
 * Domain is [-4, 4]
 * Minimum is -4.44 at x = -3.45 & z = -4.
 */
public class McCormick extends Function {

    public McCormick() {
        super("McCormick", -4.44,-10,
                50, 4, -150);
    }

    @Override
    public double getValueInPoint(double x, double z) {
        double p1 = Math.sin(x + z) + Math.pow(x - z, 2);
        double p2 = -1.5 * x + 2.5 * z + 1;
        return (double) Math.round((p1 + p2) * PRECISION) / PRECISION;
    }

}
