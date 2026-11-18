package exceptions;

/**
 * Indica que uma conta não possui saldo suficiente.
 */
public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
