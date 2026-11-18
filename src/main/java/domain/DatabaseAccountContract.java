package domain;
import java.util.List;

/**
 * Contrato de persistência para contas bancárias.
 */

public interface DatabaseAccountContract {
    Account add(Account account);
    Account get(AccountId id);
    void remove(AccountId id);
    boolean contains(AccountId id);
    int count();
    List<Account> getAll();
}
