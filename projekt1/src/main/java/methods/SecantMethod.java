package methods;

import java.util.ArrayList;

public class SecantMethod extends SolvingMethods{
    private double xi;
    private double lastXi;

    public SecantMethod(ScalarFunction function, double xi, double lastXi) {
        super(function);
        this.xi = xi;
        this.lastXi = lastXi;
    }

    public void setXi(double xi) {
        this.xi = xi;
    }

    public void setLastXi(double lastXi) {
        this.lastXi = lastXi;
    }

    public double getXi() {
        return xi;
    }

    public double getLastXi() {
        return lastXi;
    }

    @Override
    public ArrayList<SolvingData> solveAllIterationsWithApproximateError(double maxEa) {
        ArrayList<SolvingData> dataList= new ArrayList<>();
        double nextXi;
        double ea;

        do {
            nextXi = xi - ((this.getFunction().getF(xi)*(lastXi-xi))/(this.getFunction().getF(lastXi)-this.getFunction().getF(xi)));

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
        double nextXi;
        double ea;
        double et;

        do {
            nextXi = xi - ((this.getFunction().getF(xi)*(lastXi-xi))/(this.getFunction().getF(lastXi)-this.getFunction().getF(xi)));

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
