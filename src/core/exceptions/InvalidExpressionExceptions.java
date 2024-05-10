package core.exceptions;

/**
 * Exception thrown when an expression is invalid.
 */
public class InvalidExpressionExceptions extends Exception {

    /**
     * Constructs a new InvalidExpressionExceptions with a default message "Invalid Expression".
     */
    public InvalidExpressionExceptions() {
        super("Invalid Expression");
    }

    /**
     * Constructs a new InvalidExpressionExceptions with the specified message.
     * @param message The detail message.
     */
    public InvalidExpressionExceptions(String message) {
        super(message);
    }
}
