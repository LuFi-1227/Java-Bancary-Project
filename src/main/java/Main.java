import application.ApplicationFunction;
import application.adapters.AccountRecord;
import application.adapters.ClientRecord;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Scanner;

/**
 * Interface de console do sistema bancário.
 */
public class Main {
    private static final ApplicationFunction application = ApplicationFunction.getInstance();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=================================");
        System.out.println(" Sistema Bancario em Java - DIO ");
        System.out.println("=================================");

        boolean running = true;
        while (running) {
            printMenu();
            String option = scanner.nextLine().trim();
            try {
                switch (option) {
                    case "1" -> cadastrarCliente();
                    case "2" -> consultarCliente();
                    case "3" -> realizarDeposito();
                    case "4" -> realizarSaque();
                    case "5" -> realizarTransferencia();
                    case "6" -> consultarSaldo();
                    case "7" -> consultarExtrato();
                    case "0" -> running = false;
                    default -> System.out.println("Opcao invalida.");
                }
            } catch (RuntimeException exception) {
                System.out.println("Erro: " + exception.getMessage());
            }
        }

        System.out.println("Sistema encerrado.");
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("1 - Cadastrar cliente");
        System.out.println("2 - Consultar cliente");
        System.out.println("3 - Depositar");
        System.out.println("4 - Sacar");
        System.out.println("5 - Transferir");
        System.out.println("6 - Consultar saldo");
        System.out.println("7 - Consultar extrato");
        System.out.println("0 - Sair");
        System.out.print("Escolha: ");
    }

    private static void cadastrarCliente() {
        System.out.print("Nome: ");
        String name = scanner.nextLine().trim();
        System.out.print("Telefone: ");
        String number = scanner.nextLine().trim();
        System.out.print("CPF: ");
        String cpf = scanner.nextLine().trim();
        System.out.print("Senha: ");
        String password = scanner.nextLine().trim();

        ClientRecord client = new ClientRecord(null, name, number, password, cpf);
        if (!application.createSetOfClientAndAccount(client)) {
            System.out.println("Nao foi possivel cadastrar o cliente.");
            return;
        }
        ClientRecord saved = application.getClientByPersonalInfo(
                Optional.empty(), Optional.empty(), Optional.of(cpf));
        System.out.println("Cliente cadastrado com sucesso.");
        System.out.println(saved);
    }

    private static void consultarCliente() {
        System.out.print("CPF do cliente: ");
        String cpf = scanner.nextLine().trim();
        ClientRecord client = application.getClientByPersonalInfo(
                Optional.empty(), Optional.empty(), Optional.of(cpf));
        if (client == null) {
            System.out.println("Cliente nao encontrado.");
            return;
        }
        System.out.println(application.getClientInfo(client.id()));
    }

    private static void realizarDeposito() {
        ClientRecord client = obterClientePorCpf();
        if (client == null) {
            return;
        }
        BigDecimal amount = lerValor("Valor do deposito");
        application.transferFunds(application.getAccountByClientId(client.id()), amount, true);
        System.out.println("Deposito realizado.");
    }

    private static void realizarSaque() {
        ClientRecord client = obterClientePorCpf();
        if (client == null) {
            return;
        }
        BigDecimal amount = lerValor("Valor do saque");
        application.transferFunds(application.getAccountByClientId(client.id()), amount, false);
        System.out.println("Saque realizado.");
    }

    private static void realizarTransferencia() {
        System.out.print("CPF da conta de origem: ");
        String sourceCpf = scanner.nextLine().trim();
        ClientRecord source = application.getClientByPersonalInfo(
                Optional.empty(), Optional.empty(), Optional.of(sourceCpf));
        if (source == null) {
            System.out.println("Conta de origem nao encontrada.");
            return;
        }

        System.out.print("CPF da conta de destino: ");
        String targetCpf = scanner.nextLine().trim();
        ClientRecord target = application.getClientByPersonalInfo(
                Optional.empty(), Optional.empty(), Optional.of(targetCpf));
        if (target == null) {
            System.out.println("Conta de destino nao encontrada.");
            return;
        }

        BigDecimal amount = lerValor("Valor da transferencia");
        AccountRecord sourceAccount = application.getAccountByClientId(source.id());
        AccountRecord targetAccount = application.getAccountByClientId(target.id());
        if (!application.transferFunds(sourceAccount, targetAccount, amount)) {
            System.out.println("Nao foi possivel realizar a transferencia.");
            return;
        }
        System.out.println("Transferencia realizada.");
    }

    private static void consultarSaldo() {
        ClientRecord client = obterClientePorCpf();
        if (client != null) {
            System.out.println(application.getClientInfo(client.id()));
        }
    }

    private static ClientRecord obterClientePorCpf() {
        System.out.print("CPF do cliente: ");
        String cpf = scanner.nextLine().trim();
        ClientRecord client = application.getClientByPersonalInfo(
                Optional.empty(), Optional.empty(), Optional.of(cpf));
        if (client == null) {
            System.out.println("Cliente nao encontrado.");
        }
        return client;
    }

    private static BigDecimal lerValor(String label) {
        System.out.print(label + ": ");
        try {
            String input = scanner.nextLine().trim().replace(",", ".");
            BigDecimal value = new BigDecimal(input);
            if (value.signum() <= 0) {
                throw new IllegalArgumentException("O valor deve ser maior que zero.");
            }
            if (value.scale() > 2) {
                throw new IllegalArgumentException("Use no maximo duas casas decimais.");
            }
            return value;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Informe um valor monetario valido.");
        }
    }

    private static void consultarExtrato() {
        ClientRecord client = obterClientePorCpf();
        if (client == null) {
            return;
        }
        java.util.List<domain.Account.StatementEntry> statement =
                application.getStatement(client.id());
        if (statement.isEmpty()) {
            System.out.println("Nenhuma movimentacao encontrada.");
            return;
        }
        System.out.println("Extrato:");
        for (domain.Account.StatementEntry entry : statement) {
            System.out.printf("%s | %s | R$ %s | saldo: R$ %s%n",
                    entry.dateTime(),
                    entry.description(),
                    entry.amount(),
                    entry.balanceAfter());
        }
    }
}
