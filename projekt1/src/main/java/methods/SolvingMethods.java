package methods;

import java.util.ArrayList;
import java.util.List;

public abstract class SolvingMethods {
    private ScalarFunction function;

    public SolvingMethods(ScalarFunction function) {
        this.function = function;
    }

    public ScalarFunction getFunction() {
        return function;
    }

    public void setFunction(ScalarFunction function) {
        this.function = function;
    }

    public abstract ArrayList<SolvingData> solveAllIterationsWithApproximateError (double maxEa);
    public abstract ArrayList<SolvingDataWithEt> solveAllIterationsWithAllErrors (double maxEa, double trueValue);

    public double approximateError (double xr, double lastXr) {
        double result = 100;
        try {
            result = Math.abs((xr-lastXr)/xr)*100;
        }
        catch (ArithmeticException e) {
            System.out.println("Cannot divide by 0!");
        }
        return result;
    }

    public double trueError (double xr, double trueValue) {
        return trueValue - xr;
    }

    public SolvingData solveFinalValueWithApproximateError (double maxEa) {
        List<SolvingData> list = solveAllIterationsWithApproximateError(maxEa);
        return list.get(list.size()-1);
    }

    public SolvingDataWithEt solveFinalValueWithAllErrors (double maxEa, double trueValue) {
        List<SolvingDataWithEt> list = solveAllIterationsWithAllErrors(maxEa, trueValue);
        return list.get(list.size()-1);
    }
}
