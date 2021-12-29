package sample.function;

import java.util.Arrays;
import java.util.List;

public class FunctionUtils {

    private static Function function;

    public static final double MIN_RANGE = -4.0;
    public static final double MAX_RANGE = 4.0;
    public static final double MAX_VALUE = 100000000;
    public static final int PRECISION = 1000;

    public static List<Function> getAllFunctions() {
        return Arrays.asList(
                new GoldsteinPrice(),
                new Levy(),
                new McCormick(),
                new Rastrigin());
    }

    public static Function getFunction() {
        return function;
    }

    public static void setFunction(Function functionType) {
        function = functionType;
    }

}
