package methods;

import java.util.ArrayList;

public class FixedPointIteration extends SolvingMethods {

    private double x0;

    public FixedPointIteration(ScalarFunction function, double x0) {
        super(function);
        this.x0 = x0;
    }

    public void setX0(double x0) {
        this.x0 = x0;
    }

    public double getX0() {
        return x0;
    }

    @Override
    public ArrayList<SolvingData> solveAllIterationsWithApproximateError (double maxEa) {

        ArrayList<SolvingData> dataList= new ArrayList<>();
        double xr;
        double ea;

        do {
            xr = this.getFunction().getF(x0)+x0;

            ea = this.approximateError(xr, x0);

            SolvingData data = new SolvingData(xr, ea);
            dataList.add(data);

            x0=xr;
        }
        while ((ea > maxEa) && !(this.getFunction().getF(xr)==0));

        return dataList;
    }

    @Override
    public ArrayList<SolvingDataWithEt> solveAllIterationsWithAllErrors (double maxEa, double trueValue) {

        ArrayList<SolvingDataWithEt> dataList= new ArrayList<>();
        double xr;
        double ea;
        double et;

        do {
            xr = this.getFunction().getF(x0)+x0;

            ea = this.approximateError(xr, x0);
            et = this.trueError(xr, trueValue);

            SolvingDataWithEt data = new SolvingDataWithEt(xr, ea, et);
            dataList.add(data);

            x0=xr;
        }
        while ((ea > maxEa) && !(this.getFunction().getF(xr)==0));

        return dataList;
    }
}
