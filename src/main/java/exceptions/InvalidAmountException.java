package exceptions;

/**
 * Indica que um valor monetário é inválido.
 */
public class InvalidAmountException extends RuntimeException {
    public InvalidAmountException(String message) {
        super(message);
    }
}
