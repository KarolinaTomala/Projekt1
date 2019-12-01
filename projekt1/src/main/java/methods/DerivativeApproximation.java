package methods;

public class DerivativeApproximation {
    ScalarFunction function;
    double h;

    public DerivativeApproximation(ScalarFunction function, double h) {
        this.function = function;
        this.h = h;
    }

    public double getDerivativeApproximation (double x) {
        return (function.getF(x+h)-function.getF(x-h))/(2*h);
    }
}
