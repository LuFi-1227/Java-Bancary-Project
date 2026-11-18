package application;

import application.adapters.AccountRecord;
import application.adapters.ClientRecord;
import exceptions.InsufficientBalanceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes dos principais casos de uso da aplicação bancária.
 */
class ApplicationFunctionTest {
    private static ApplicationFunction application;

    @BeforeEach
    void setup() {
        application = ApplicationFunction.getInstance();
        application.resetState();
    }

    @Test
    void shouldCreateClientAndAccount() {
        ClientRecord input = new ClientRecord(null, "Ana Silva", "11999990001",
                "senha123", "111.444.777-35");
        assertTrue(application.createSetOfClientAndAccount(input));

        ClientRecord saved = application.getClientByPersonalInfo(
                Optional.empty(), Optional.empty(), Optional.of("11144477735"));
        assertNotNull(saved);
        assertEquals(BigDecimal.ZERO.setScale(2),
                application.getAccountByClientId(saved.id()).balance());
    }

    @Test
    void shouldDepositAndTransferMoney() {
        ClientRecord sourceInput = new ClientRecord(null, "Bruno Souza", "11999990002",
                "senha123", "52998224725");
        ClientRecord targetInput = new ClientRecord(null, "Carla Lima", "11999990003",
                "senha123", "98765432100");
        assertTrue(application.createSetOfClientAndAccount(sourceInput));
        assertTrue(application.createSetOfClientAndAccount(targetInput));

        ClientRecord source = application.getClientByPersonalInfo(
                Optional.empty(), Optional.empty(), Optional.of("52998224725"));
        ClientRecord target = application.getClientByPersonalInfo(
                Optional.empty(), Optional.empty(), Optional.of("98765432100"));

        AccountRecord sourceAccount = application.getAccountByClientId(source.id());
        AccountRecord targetAccount = application.getAccountByClientId(target.id());
        assertTrue(application.transferFunds(sourceAccount, new BigDecimal("200.00"), true));
        assertTrue(application.transferFunds(sourceAccount, targetAccount, new BigDecimal("75.50")));

        assertEquals(new BigDecimal("124.50"),
                application.getAccountByClientId(source.id()).balance());
        assertEquals(new BigDecimal("75.50"),
                application.getAccountByClientId(target.id()).balance());
    }

    @Test
    void shouldRejectInsufficientBalance() {
        ClientRecord input = new ClientRecord(null, "Diego Alves", "11999990004",
                "senha123", "93541134780");
        assertTrue(application.createSetOfClientAndAccount(input));
        ClientRecord saved = application.getClientByPersonalInfo(
                Optional.empty(), Optional.empty(), Optional.of("93541134780"));
        AccountRecord account = application.getAccountByClientId(saved.id());

        assertThrows(InsufficientBalanceException.class,
                () -> application.transferFunds(account, new BigDecimal("10.00"), false));
    }

    @Test
    void shouldRejectDuplicateCpf() {
        ClientRecord first = new ClientRecord(null, "Eva Santos", "11999990005",
                "senha123", "12345678909");
        ClientRecord duplicate = new ClientRecord(null, "Fabio Santos", "11999990006",
                "senha123", "12345678909");

        assertTrue(application.createSetOfClientAndAccount(first));
        assertThrows(IllegalArgumentException.class,
                () -> application.createSetOfClientAndAccount(duplicate));
    }

    @Test
    void shouldRejectInvalidAmountAndSameAccountTransfer() {
        ClientRecord input = new ClientRecord(null, "Gabi Nunes", "11999990007",
                "senha123", "98765432100");
        assertTrue(application.createSetOfClientAndAccount(input));
        ClientRecord saved = application.getClientByPersonalInfo(
                Optional.empty(), Optional.empty(), Optional.of("98765432100"));
        AccountRecord account = application.getAccountByClientId(saved.id());

        assertFalse(application.transferFunds(account, BigDecimal.ZERO, true));
        assertFalse(application.transferFunds(account, account, new BigDecimal("10.00")));
    }

    @Test
    void shouldRegisterDepositAndWithdrawalInStatement() {
        ClientRecord input = new ClientRecord(null, "Hugo Prado", "11999990008",
                "senha123", "11144477735");
        assertTrue(application.createSetOfClientAndAccount(input));
        ClientRecord saved = application.getClientByPersonalInfo(
                Optional.empty(), Optional.empty(), Optional.of("11144477735"));
        AccountRecord account = application.getAccountByClientId(saved.id());

        assertTrue(application.transferFunds(account, new BigDecimal("100.00"), true));
        assertTrue(application.transferFunds(account, new BigDecimal("25.50"), false));

        assertEquals(2, application.getStatement(saved.id()).size());
        assertEquals("Saque", application.getStatement(saved.id()).get(1).description());
    }

    @Test
    void shouldRejectScaleAndNotChangeBalance() {
        ClientRecord input = new ClientRecord(null, "Iara Reis", "11999990009",
                "senha123", "11144477735");
        assertTrue(application.createSetOfClientAndAccount(input));
        ClientRecord saved = application.getClientByPersonalInfo(
                Optional.empty(), Optional.empty(), Optional.of("11144477735"));
        AccountRecord account = application.getAccountByClientId(saved.id());

        assertFalse(application.transferFunds(account, new BigDecimal("10.123"), true));
        assertEquals(new BigDecimal("0.00"),
                application.getAccountByClientId(saved.id()).balance());
    }
}
