package sample.function;

import static sample.function.FunctionUtils.PRECISION;

/**
 * Perform Goldstein-Price function.
 * Domain is [-4, 4]
 * Minimum is 3 at x = 0 & z = -1.
 */
public class GoldsteinPrice extends Function {

    public GoldsteinPrice() {
        super("Goldstein-Price", 3, -5000000,
                30000000, 0.000008, -190);
    }

    @Override
    public double getValueInPoint(double x, double z) {
        double p1a = Math.pow((x + z + 1), 2);
        double p1b = 19 - 14 * x + 3 * Math.pow(x, 2) - 14 * z + 6 * x * z + 3 * Math.pow(z, 2);
        double p1 = 1 + p1a * p1b;
        double p2a = Math.pow((2 * x - 3 * z), 2);
        double p2b = 18 - 32 * x + 12 * Math.pow(x, 2) + 48 * z - 36 * x * z + 27 * Math.pow(z, 2);
        double p2 = 30 + p2a * p2b;
        return (double) Math.round((p1 * p2) * PRECISION) / PRECISION;
    }

}
