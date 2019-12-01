package components;

import java.util.ArrayList;

import methods.Bisection;
import methods.SecantMethod;
import methods.SolvingMethods;

public class Planet {

    private String name;
    private double orbitalEccentricity;
    private double distanceFromSun;

    //constructor
    public Planet(String name, double orbitalEccentricity, double distanceFromSun) {
        this.name = name;
        this.orbitalEccentricity = orbitalEccentricity;
        this.distanceFromSun = distanceFromSun;
    }


    // methods
    public double[] planetPosition (double eccentricAnomaly) {
        double x = distanceFromSun*Math.cos(eccentricAnomaly - orbitalEccentricity);
        double y = distanceFromSun*Math.sqrt(1-Math.pow(orbitalEccentricity,2))*Math.sin(eccentricAnomaly);
        double[] results = {x,y, eccentricAnomaly};
        return results;
    }

    public ArrayList<double[]> trajectory (SolvingMethods solvingMethod, double ea) {
        ArrayList<double[]> trajectory = new ArrayList<>();
        double[] tab;
        for (double i=0.0; i < 4*Math.PI; i+=0.01) {

            double meanAnomaly = i;

            solvingMethod.setFunction((x) -> meanAnomaly+(orbitalEccentricity*Math.sin(x))-x);

            if ((solvingMethod instanceof SecantMethod) && (trajectory.size() > 0)) {
                ((SecantMethod) solvingMethod).setLastXi(trajectory.get(trajectory.size()-1)[2]-1);
                ((SecantMethod) solvingMethod).setXi(trajectory.get(trajectory.size()-1)[2]);
            }

            double eccentricAnomaly = solvingMethod.solveFinalValueWithApproximateError(ea).getXr();
            tab = planetPosition(eccentricAnomaly);
            trajectory.add(tab);
        }

        return trajectory;
    }
}
