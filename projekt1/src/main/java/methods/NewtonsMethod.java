package methods;

import java.util.ArrayList;

public class NewtonsMethod extends SolvingMethods {

    private double xi;

    public NewtonsMethod(ScalarFunction function, double xi) {
        super(function);
        this.xi = xi;
    }

    public void setXi(double xi) {
        this.xi = xi;
    }

    public double getXi() {
        return xi;
    }

    @Override
    public ArrayList<SolvingData> solveAllIterationsWithApproximateError(double maxEa) {
        ArrayList<SolvingData> dataList= new ArrayList<>();
        DerivativeApproximation derivativeApproximation = new DerivativeApproximation(this.getFunction(), 0.001);
        double nextXi;
        double ea;

        do {
            nextXi = xi - (this.getFunction().getF(xi)/derivativeApproximation.getDerivativeApproximation(xi));

            ea = this.approximateError(nextXi, xi);

            SolvingData data = new SolvingData(nextXi, ea);
            dataList.add(data);

            xi=nextXi;
        }
        while ((ea > maxEa) && !(this.getFunction().getF(nextXi)==0));

        return dataList;
    }

    @Override
    public ArrayList<SolvingDataWithEt> solveAllIterationsWithAllErrors(double maxEa, double trueValue) {
        ArrayList<SolvingDataWithEt> dataList= new ArrayList<>();
        DerivativeApproximation derivativeApproximation = new DerivativeApproximation(this.getFunction(), 0.001);
        double nextXi;
        double ea;
        double et;

        do {
            nextXi = xi - (this.getFunction().getF(xi)/derivativeApproximation.getDerivativeApproximation(xi));

            ea = this.approximateError(nextXi, xi);
            et = this.trueError(nextXi, trueValue);

            SolvingDataWithEt data = new SolvingDataWithEt(nextXi, ea, et);
            dataList.add(data);

            xi=nextXi;
        }
        while ((ea > maxEa) && !(this.getFunction().getF(nextXi)==0));

        return dataList;
    }
}
