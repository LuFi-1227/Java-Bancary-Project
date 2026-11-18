package exceptions;

/**
 * Indica que um argumento obrigatório não foi informado.
 */
public class NullArgumentException extends RuntimeException {
    public NullArgumentException(String message) {
        super(message);
    }
}
