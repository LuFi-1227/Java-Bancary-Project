package domain;

import java.util.Optional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa uma conta bancária, seu saldo e seu extrato.
 */
public class Account {
    private AccountId id;
    private ClientId clientId;
    private BigDecimal balance;
    private final List<StatementEntry> statements = new ArrayList<>();

    public record StatementEntry(LocalDateTime dateTime, String description,
                                 BigDecimal amount, BigDecimal balanceAfter) {}
    
    public AccountId getId() {
        return id;
    }

    public ClientId getClientId() {
        return clientId;
    }
    
    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        if (balance == null) {
            throw new exceptions.NullArgumentException("Valor nao pode ser nulo");
        }
        if (balance.signum() < 0) {
            if (this.balance.add(balance).signum() < 0) {
                throw new exceptions.InsufficientBalanceException("Saldo insuficiente na conta");
            }
        }
        this.balance = this.balance.add(balance).setScale(2, java.math.RoundingMode.HALF_UP);
    }

    public void deposit(BigDecimal amount) {
        validatePositiveAmount(amount);
        setBalance(amount);
        addStatement("Deposito", amount);
    }

    public void withdraw(BigDecimal amount) {
        validatePositiveAmount(amount);
        setBalance(amount.negate());
        addStatement("Saque", amount.negate());
    }

    public void transferOut(BigDecimal amount) {
        validatePositiveAmount(amount);
        setBalance(amount.negate());
        addStatement("Transferencia enviada", amount.negate());
    }

    public void transferIn(BigDecimal amount) {
        validatePositiveAmount(amount);
        setBalance(amount);
        addStatement("Transferencia recebida", amount);
    }

    public List<StatementEntry> getStatements() {
        return Collections.unmodifiableList(statements);
    }

    private void validatePositiveAmount(BigDecimal amount) {
        if (amount == null) {
            throw new exceptions.NullArgumentException("Valor nao pode ser nulo");
        }
        if (amount.signum() <= 0) {
            throw new exceptions.InvalidAmountException("O valor deve ser maior que zero");
        }
        if (amount.scale() > 2) {
            throw new exceptions.InvalidAmountException("O valor deve ter no maximo duas casas decimais");
        }
    }

    private void addStatement(String description, BigDecimal amount) {
        statements.add(new StatementEntry(LocalDateTime.now(), description,
                amount.setScale(2, java.math.RoundingMode.HALF_UP), balance));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((clientId == null) ? 0 : clientId.hashCode());
        result = prime * result + balance.hashCode();
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Account other = (Account) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (clientId == null) {
            if (other.clientId != null)
                return false;
        } else if (!clientId.equals(other.clientId))
            return false;
        if (!balance.equals(other.balance))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Account [id=" + id + ", clientId=" + clientId + ", balance=" + balance + "]";
    }

    private Account(ClientId clientId) {
        this.id = new AccountId();
        this.clientId = clientId;
        this.balance = BigDecimal.ZERO.setScale(2);
    }

    public static Account getAccount(ClientId clientId, Optional<Boolean> isValid) {
        if (isValid.orElse(false) || isValid.isEmpty()) {
            return new Account(clientId);
        }
        else {
            throw new exceptions.NullArgumentException("Algum requisito não foi atendido para criar a conta");
        }
    }
}