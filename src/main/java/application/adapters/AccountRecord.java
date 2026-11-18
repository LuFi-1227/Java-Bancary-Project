package application.adapters;

import java.util.Optional;

import domain.Account;
import domain.AccountId;
import domain.ClientId;
import java.math.BigDecimal;

/**
 * Registro de transporte para os dados de uma conta.
 *
 * @param id identificador da conta
 * @param clientId identificador do cliente
 * @param balance saldo atual
 */

public record AccountRecord(
    AccountId id,
    ClientId clientId,
    BigDecimal balance
) {

    public static AccountRecord from(Account account) {
        return new AccountRecord(account.getId(), account.getClientId(), account.getBalance());
    }

    public Account create(Optional<Boolean> isValid) {
        return Account.getAccount(this.clientId, isValid);
    }
}