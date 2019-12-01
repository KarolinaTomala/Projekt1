package exceptions;

public class IncorrectEccentricityException extends Exception {
    public IncorrectEccentricityException() {
        super("Eccentricity value must be greater than zero!");
    }
}
