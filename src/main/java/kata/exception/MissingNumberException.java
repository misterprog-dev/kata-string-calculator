package kata.exception;

public class MissingNumberException extends Exception {
    public MissingNumberException() {
        super("Number expected but EOF found.");
    }
}
