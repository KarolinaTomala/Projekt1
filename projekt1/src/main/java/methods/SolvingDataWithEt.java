package methods;

public class SolvingDataWithEt extends SolvingData {
    private double et;

    public SolvingDataWithEt(double xr, double ea, double et) {
        super(xr, ea);
        this.et = et;
    }

    public double getEt() {
        return et;
    }

    @Override
    public String toString() {
        return String.format("%-25s %-25s %-25s", this.getXr(), this.getEa(), et);
    }
}
