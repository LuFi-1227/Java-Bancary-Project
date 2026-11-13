package domain;
import java.util.UUID;
public record AccountId(UUID id){
    public AccountId() {
        this(UUID.randomUUID());
    }
}
