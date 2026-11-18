package domain;
import java.util.UUID;

/**
 * Identificador único de uma conta bancária.
 *
 * @param id identificador UUID da conta
 */
public record AccountId(UUID id){
    public AccountId() {
        this(UUID.randomUUID());
    }
}
