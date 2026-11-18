package infrastructure;
import domain.DatabaseClientContract;
import domain.Client;
import domain.ClientId;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import exceptions.NullArgumentException;

/**
 * Implementação em memória do repositório de clientes.
 */
public class InMemoryUsers implements DatabaseClientContract {
    private HashMap<ClientId, Client> users = new HashMap<>();

    public Client add(Client user) {
        if (user == null) {
            throw new NullArgumentException("Cliente nao pode ser nulo");
        }
        if (this.getByCpf(user.getCpf()) != null) {
            throw new IllegalArgumentException("Ja existe um cliente com este CPF");
        }
        users.put(user.getId(), user);
        return user;
    }

    public Client get(ClientId id) {
        return users.get(id);
    }

    public void remove(ClientId id) {
        users.remove(id);
    }

    public boolean contains(ClientId id) {
        return users.containsKey(id);
    }

    public int count() {
        return users.size();
    }

    public List<Client> getAll() {
        return new ArrayList<>(users.values());
    }

    public Client getByName(String name) {
        if (name == null) {
            return null;
        }
        for (Client user : this.getAll()) {
            if (user.getName().equalsIgnoreCase(name.trim())) {
                return user;
            }
        }
        return null;
    }

    public Client getByNumber(String number) {
        if (number == null) {
            return null;
        }
        for (Client user : users.values()) {
            if (user.getNumber().replaceAll("\\D", "")
                    .equals(number.replaceAll("\\D", ""))) {
                return user;
            }
        }
        return null;
    }

    public Client getByCpf(String cpf) {
        if (cpf == null) {
            return null;
        }
        String normalized = cpf.replaceAll("\\D", "");
        for (Client user : users.values()) {
            if (user.getCpf().equals(normalized)) {
                return user;
            }
        }
        return null;
    }
}
