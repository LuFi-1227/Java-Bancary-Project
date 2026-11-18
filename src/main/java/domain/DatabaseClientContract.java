package domain;
import java.util.List;

/**
 * Contrato de persistência para clientes.
 */

public interface DatabaseClientContract {
    Client add(Client user);
    Client get(ClientId id);
    Client getByName(String name);
    Client getByNumber(String number);
    Client getByCpf(String cpf);
    void remove(ClientId id);
    boolean contains(ClientId id);
    int count();
    List<Client> getAll();
}
