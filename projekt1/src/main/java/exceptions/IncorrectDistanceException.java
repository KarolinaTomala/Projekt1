package exceptions;

public class IncorrectDistanceException extends Exception {
    public IncorrectDistanceException() {
        super("Distance value must be grater than zero!");
    }
}
