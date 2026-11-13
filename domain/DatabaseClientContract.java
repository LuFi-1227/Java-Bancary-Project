package domain;
import java.util.List;
public interface DatabaseClientContract {
    void add(Client user);
    Client get(ClientId id);
    void remove(ClientId id);
    boolean contains(ClientId id);
    int count();
    List<Client> getAll();
}
