package domain;

import java.util.UUID;

/**
 * Identificador único de um cliente.
 *
 * @param id UUID utilizado como identificador
 */
public record ClientId(UUID id){
    public ClientId() {
        this(UUID.randomUUID());
    }
}