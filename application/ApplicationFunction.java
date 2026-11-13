package application;
import application.adapters.AccountRecord;
import application.adapters.ClientRecord;
import domain.AccountId;
import domain.ClientId;
import infrastructure.InMemoryAccounts;
import infrastructure.InMemoryUsers;

public class ApplicationFunction {
    private InMemoryAccounts inMemoryAccounts;
    private InMemoryUsers inMemoryUsers;

    public ApplicationFunction() {
        this.inMemoryAccounts = new InMemoryAccounts();
        this.inMemoryUsers = new InMemoryUsers();
    }

    public boolean addClient(ClientRecord client) {
        this.inMemoryUsers.add(client.create());
        return true;
    }

    public boolean addAccount(AccountRecord account) {
        this.inMemoryAccounts.add(account.create());
        return true;
    }

    public boolean createSetOfClientAndAccount(ClientRecord client, AccountRecord account) {
        if (client.id() == null) {
            this.addClient(client);
        }
        if (account.id() == null) {
            this.addAccount(account);
        }
        return true;
    }

    public boolean removeClient(ClientRecord client) {
        this.inMemoryUsers.remove(client.id());
        return true;
    }

    public boolean removeAccount(AccountRecord account) {
        this.inMemoryAccounts.remove(account.id());
        return true;
    }

    public boolean removeSetOfClientAndAccount(ClientRecord client, AccountRecord account) {
        if (client.id() != null) {
            this.removeClient(client);
        }
        if (account.id() != null) {
            this.removeAccount(account);
        }
        return true;
    }

    public boolean transferFunds(AccountRecord fromAccount, AccountRecord toAccount, double amount) {
        if (fromAccount.id() != null && toAccount.id() != null) {
            fromAccount.create().setBalance(-1 * amount);
            toAccount.create().setBalance(amount);
        }else{
            return false;
        }
        return true;
    }

    public AccountRecord getAccount(AccountId account) {
        return AccountRecord.from(this.inMemoryAccounts.get(account));
    }

    public ClientRecord getClient(ClientId clientId) {
        return ClientRecord.from(this.inMemoryUsers.get(clientId));
    }
}
