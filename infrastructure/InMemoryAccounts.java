package infrastructure;
import domain.DatabaseAccountContract;
import domain.Account;
import domain.AccountId;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class InMemoryAccounts implements DatabaseAccountContract {
    private HashMap<AccountId, Account> accounts = new HashMap<>();

    public void add(Account account) {
        accounts.put(account.getId(), account);
    }

    public Account get(AccountId id) {
        return accounts.get(id);
    }

    public void remove(AccountId id) {
        accounts.remove(id);
    }

    public boolean contains(AccountId id) {
        return accounts.containsKey(id);
    }

    public int count() {
        return accounts.size();
    }

    public List<Account> getAll() {
        return new ArrayList<>(accounts.values());
    }
}
