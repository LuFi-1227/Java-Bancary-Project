package infrastructure;
import domain.DatabaseClientContract;
import domain.Client;
import domain.ClientId;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class InMemoryUsers implements DatabaseClientContract {
    private HashMap<ClientId, Client> users = new HashMap<>();

    public void add(Client user) {
        users.put(user.getId(), user);
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
}
