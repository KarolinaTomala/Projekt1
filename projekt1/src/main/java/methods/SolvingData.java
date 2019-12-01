package methods;

public class SolvingData {
    private double xr;
    private double ea;

    public SolvingData(double xr, double ea) {
        this.xr = xr;
        this.ea = ea;
    }

    public double getXr() {
        return xr;
    }

    public double getEa() {
        return ea;
    }

    @Override
    public String toString() {
        return String.format("%-25s %-25s", xr, ea);
    }
}
