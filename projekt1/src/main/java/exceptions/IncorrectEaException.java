package exceptions;

public class IncorrectEaException extends Exception {
    public IncorrectEaException() {
        super("Final value of approximate error must be greater than zero!");
    }
}
