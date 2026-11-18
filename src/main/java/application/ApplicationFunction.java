package application;
import java.util.HashMap;
import java.util.Optional;

import application.adapters.AccountRecord;
import application.adapters.ClientRecord;
import domain.AccountId;
import domain.ClientId;
import infrastructure.InMemoryAccounts;
import infrastructure.InMemoryUsers;
import java.math.BigDecimal;

/**
 * Coordena os casos de uso relacionados a clientes, contas e transferências.
 */
public class ApplicationFunction {
    private InMemoryAccounts inMemoryAccounts;
    private InMemoryUsers inMemoryUsers;
    private HashMap<ClientId, AccountId> accountClientMap = new HashMap<>();
    private static ApplicationFunction instance;

    private ApplicationFunction() {
        this.inMemoryAccounts = new InMemoryAccounts();
        this.inMemoryUsers = new InMemoryUsers();
    }

    public static ApplicationFunction getInstance() {
        if (instance == null) {
            instance = new ApplicationFunction();
        }
        return instance;
    }

    public void resetState() {
        this.inMemoryAccounts = new InMemoryAccounts();
        this.inMemoryUsers = new InMemoryUsers();
        this.accountClientMap = new HashMap<>();
    }

    public ClientRecord addClient(ClientRecord client) {
        if (client == null) {
            throw new exceptions.NullArgumentException("Cliente nao pode ser nulo");
        }
        String normalizedCpf = client.cpf() == null ? "" : client.cpf().replaceAll("\\D", "");
        if (this.inMemoryUsers.getByCpf(normalizedCpf) != null) {
            throw new IllegalArgumentException("Ja existe um cliente com este CPF");
        }
        return ClientRecord.from(this.inMemoryUsers.add(
                client.create(Optional.of(Boolean.TRUE))));
    }

    public AccountRecord addAccount(AccountRecord account) {
        if (account == null) {
            throw new exceptions.NullArgumentException("Conta nao pode ser nula");
        }
        if (account.id() != null && this.inMemoryAccounts.contains(account.id())) {
            throw new IllegalArgumentException("Ja existe uma conta com este identificador");
        }
        return AccountRecord.from(this.inMemoryAccounts.add(account.create(Optional.of(!this.inMemoryAccounts.contains(account.id())))));
    }

    public boolean createSetOfClientAndAccount(ClientRecord client) {
        if (client == null || client.id() != null) {
            return false;
        }
        ClientRecord clientRec = this.addClient(client);
        try {
            AccountRecord account = this.addAccount(
                    new AccountRecord(null, clientRec.id(), BigDecimal.ZERO));
            accountClientMap.put(clientRec.id(), account.id());
            return true;
        } catch (RuntimeException exception) {
            this.inMemoryUsers.remove(clientRec.id());
            throw exception;
        }
    }

    public boolean removeClient(ClientRecord client) {
        if (client == null || client.id() == null) {
            return false;
        }
        AccountId accountId = accountClientMap.remove(client.id());
        if (accountId != null) {
            this.inMemoryAccounts.remove(accountId);
        }
        this.inMemoryUsers.remove(client.id());
        return true;
    }

    public boolean removeAccount(AccountRecord account) {
        if (account == null || account.id() == null) {
            return false;
        }
        this.inMemoryAccounts.remove(account.id());
        accountClientMap.values().remove(account.id());
        return true;
    }

    public boolean removeSetOfClientAndAccount(ClientRecord client, AccountRecord account) {
        if (client != null && client.id() != null) {
            this.removeClient(client);
        }
        if (account != null && account.id() != null) {
            this.removeAccount(account);
        }
        return true;
    }

    public boolean transferFunds(AccountRecord fromAccount, AccountRecord toAccount, BigDecimal amount) {
        if (fromAccount == null || toAccount == null
                || fromAccount.id() == null || toAccount.id() == null
                || fromAccount.id().equals(toAccount.id())) {
            return false;
        }
        BigDecimal normalizedAmount = normalizeAmount(amount);
        if (normalizedAmount == null) {
            return false;
        }
        domain.Account source = this.inMemoryAccounts.get(fromAccount.id());
        domain.Account target = this.inMemoryAccounts.get(toAccount.id());
        if (source == null || target == null) {
            return false;
        }
        if (source.getBalance().compareTo(normalizedAmount) < 0) {
            throw new exceptions.InsufficientBalanceException("Saldo insuficiente na conta");
        }
        source.transferOut(normalizedAmount);
        target.transferIn(normalizedAmount);
        return true;
    }

    public boolean transferFunds(AccountRecord fromAccount, BigDecimal amount, boolean isDeposit) {
        if (fromAccount == null || fromAccount.id() == null) {
            return false;
        }
        BigDecimal normalizedAmount = normalizeAmount(amount);
        if (normalizedAmount == null) {
            return false;
        }
        domain.Account account = this.inMemoryAccounts.get(fromAccount.id());
        if (account == null) {
            return false;
        }
        if (isDeposit) {
            account.deposit(normalizedAmount);
        } else {
            account.withdraw(normalizedAmount);
        }
        return true;
    }

    private BigDecimal normalizeAmount(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            return null;
        }
        BigDecimal normalized = amount.stripTrailingZeros();
        if (normalized.scale() > 2) {
            return null;
        }
        return normalized.setScale(2);
    }

    public AccountRecord getAccount(AccountId account) {
        domain.Account stored = account == null ? null : this.inMemoryAccounts.get(account);
        return stored == null ? null : AccountRecord.from(stored);
    }

    public ClientRecord getClient(ClientId clientId) {
        domain.Client stored = clientId == null ? null : this.inMemoryUsers.get(clientId);
        return stored == null ? null : ClientRecord.from(stored);
    }

    public String getClientInfo(ClientId clientId) {
        ClientRecord client = this.getClient(clientId);
        AccountRecord account = this.getAccount(accountClientMap.get(clientId));
        if (client == null) {
            return "Client not found.";
        }
        if (account == null) {
            return "Cliente " + client.name() + " sem conta associada.";
        }
        return "Cliente: " + client.name() + " | CPF: " + client.cpf()
                + " | Saldo: R$ " + account.balance();
    }

    public ClientRecord getClientByPersonalInfo(Optional<String> name, Optional<String> number, Optional<String> cpf){
        if (name == null || number == null || cpf == null) {
            return null;
        }
        if (name.isPresent()) {
            domain.Client stored = this.inMemoryUsers.getByName(name.get());
            return stored == null ? null : ClientRecord.from(stored);
        } else if (number.isPresent()) {
            domain.Client stored = this.inMemoryUsers.getByNumber(number.get());
            return stored == null ? null : ClientRecord.from(stored);
        } else if (cpf.isPresent()) {
            String normalizedCpf = cpf.get().replaceAll("\\D", "");
            domain.Client stored = this.inMemoryUsers.getByCpf(normalizedCpf);
            return stored == null ? null : ClientRecord.from(stored);
        } else {
            return null;
        }
    }

    public AccountRecord getAccountByClientId(ClientId clientId) {
        AccountId accountId = accountClientMap.get(clientId);
        if (accountId != null) {
            return this.getAccount(accountId);
        } else {
            return null;
        }
    }

    public java.util.List<domain.Account.StatementEntry> getStatement(ClientId clientId) {
        AccountRecord accountRecord = getAccountByClientId(clientId);
        if (accountRecord == null) {
            return java.util.List.of();
        }
        domain.Account account = inMemoryAccounts.get(accountRecord.id());
        return account == null ? java.util.List.of() : account.getStatements();
    }
}
