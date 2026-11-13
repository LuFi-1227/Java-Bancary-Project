package application.adapters;

import domain.Account;
import domain.AccountId;
import domain.ClientId;

public record AccountRecord(
    AccountId id,
    ClientId clientId,
    double balance
) {

    public static AccountRecord from(Account account) {
        return new AccountRecord(account.getId(), account.getClientId(), account.getBalance());
    }

    public Account create() {
        return Account.getAccount(this.clientId);
    }
}