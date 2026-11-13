package domain;

import java.util.UUID;

public class Account {
    private AccountId id;
    private ClientId clientId;
    private double balance;
    
    public AccountId getId() {
        return id;
    }

    public ClientId getClientId() {
        return clientId;
    }
    
    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        if (balance < 0) {
            if (this.balance + balance < 0) {
                throw new exceptions.InsuficientBalanceException("Saldo Insuficiente na conta");
            }
        }
        this.balance += balance;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((clientId == null) ? 0 : clientId.hashCode());
        long temp;
        temp = Double.doubleToLongBits(balance);
        result = prime * result + (int) (temp ^ (temp >>> 32));
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
        if (Double.doubleToLongBits(balance) != Double.doubleToLongBits(other.balance))
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
        this.balance = 0.0;
    }

    public record AccountId(UUID id){
        public AccountId() {
            this(UUID.randomUUID());
        }
    }

    private static Account account = null;

    public static Account getAccount(ClientId clientId) {
        if (account == null) {
            account = new Account(clientId);
        }

        if (!account.clientId.equals(clientId)) {
            return null;
        }

        return account;
    }
}