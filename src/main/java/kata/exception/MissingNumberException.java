package kata.exception;

public class MissingNumberException extends RuntimeException {
    public MissingNumberException() {
        super("Number expected but EOF found.");
    }
}
