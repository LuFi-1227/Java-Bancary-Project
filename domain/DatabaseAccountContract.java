package domain;
import java.util.List;
public interface DatabaseAccountContract {
    void add(Account account);
    Account get(AccountId id);
    void remove(AccountId id);
    boolean contains(AccountId id);
    int count();
    List<Account> getAll();
}
