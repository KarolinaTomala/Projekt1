package methods;

import java.util.ArrayList;

public class RegulaFalsi extends SolvingMethods{

    private double xl;
    private double xu;

    public RegulaFalsi(ScalarFunction function, double xl, double xu) throws Exception {
        super(function);

        if (this.getFunction().getF(xl)*this.getFunction().getF(xu) > 0)
            throw new Exception("Na podanym przedziale funkcja nie ma miejsc zerowych");
        else if ((this.getFunction().getF(xl) == 0) || (this.getFunction().getF(xu) == 0))
            throw new Exception("Zadana granica przedziału jest miejscem zerowym funkcji.");

        this.xl = xl;
        this.xu = xu;
    }

    public void setXl(double xl) throws Exception {
        if (this.getFunction().getF(xl)*this.getFunction().getF(xu) > 0)
            throw new Exception("Na podanym przedziale funkcja nie ma miejsc zerowych");
        else if (this.getFunction().getF(xl) == 0)
            throw new Exception("Zadana granica przedziału jest miejscem zerowym funkcji.");

        this.xl = xl;
    }

    public void setXu(double xu) throws Exception {
        if (this.getFunction().getF(xl)*this.getFunction().getF(xu) > 0)
            throw new Exception("Na podanym przedziale funkcja nie ma miejsc zerowych");
        else if (this.getFunction().getF(xu) == 0)
            throw new Exception("Zadana granica przedziału jest miejscem zerowym funkcji.");

        this.xu = xu;
    }

    public double getXl() {
        return xl;
    }

    public double getXu() {
        return xu;
    }

    @Override
    public ArrayList<SolvingData> solveAllIterationsWithApproximateError (double maxEa) {
        ArrayList<SolvingData> dataList= new ArrayList<>();
        double xr;
        double lastXr=0;
        double ea=0;

        do {
            xr = xu - ((this.getFunction().getF(xu)*(xl-xu))/(this.getFunction().getF(xl)-this.getFunction().getF(xu)));

            ea = this.approximateError(xr, lastXr);

            SolvingData data = new SolvingData(xr, ea);
            dataList.add(data);

            if (this.getFunction().getF(xl)*this.getFunction().getF(xr) < 0)
                xu = xr;
            else if (this.getFunction().getF(xr)*this.getFunction().getF(xu) < 0)
                xl = xr;
            else
                break;

            lastXr = xr;
        }
        while(ea > maxEa);

        return dataList;
    }

    @Override
    public ArrayList<SolvingDataWithEt> solveAllIterationsWithAllErrors (double maxEa, double trueValue) {
        ArrayList<SolvingDataWithEt> dataList= new ArrayList<>();
        double xr;
        double lastXr=0;
        double ea=0;
        double et=0;

        do {
            xr = xu - ((this.getFunction().getF(xu)*(xl-xu))/(this.getFunction().getF(xl)-this.getFunction().getF(xu)));

            ea = this.approximateError(xr, lastXr);
            et = this.trueError(xr, trueValue);

            SolvingDataWithEt data = new SolvingDataWithEt(xr, ea, et);
            dataList.add(data);

            if (this.getFunction().getF(xl)*this.getFunction().getF(xr) < 0)
                xu = xr;
            else if (this.getFunction().getF(xr)*this.getFunction().getF(xu) < 0)
                xl = xr;
            else
                break;

            lastXr = xr;
        }
        while(ea > maxEa);

        return dataList;
    }
}
