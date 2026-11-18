package domain;

import java.util.Optional;

import exceptions.NullArgumentException;

/**
 * Representa os dados pessoais e o identificador de um cliente.
 */
public class Client {
    private ClientId id;
    private String name;
    private String number;
    private String password;
    private String cpf;

    public ClientId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCpf() {
        return cpf;
    }

    private Client(String name, String number, String password, String cpf) {
        this.id = new ClientId();
        this.name = name;
        this.number = number;
        this.password = password;
        this.cpf = cpf;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((number == null) ? 0 : number.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((cpf == null) ? 0 : cpf.hashCode());
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
        Client other = (Client) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (number == null) {
            if (other.number != null)
                return false;
        } else if (!number.equals(other.number))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        if (cpf == null) {
            if (other.cpf != null)
                return false;
        } else if (!cpf.equals(other.cpf))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Client [id= " + id + ", name=" + name + ", cpf=" + cpf + "]";
    }

    public static Client getClient(Optional<String> nome, Optional<String> numero, Optional<String> password, Optional<String> cpf, Optional<Boolean> isValid) {
        if (isValid.orElse(false) || isValid.isEmpty()) // Restrições podem vir a existir aqui
        {
            String nomeStr = nome.orElseThrow(() -> new NullArgumentException("Nome é obrigatório"));
            String numeroStr = numero.orElseThrow(() -> new NullArgumentException("Número é obrigatório"));
            String passwordStr = password.orElseThrow(() -> new NullArgumentException("Senha é obrigatória"));
            String cpfStr = cpf.orElseThrow(() -> new NullArgumentException("CPF é obrigatório"));
            if (nomeStr.isBlank()) {
                throw new NullArgumentException("Nome não pode ser vazio");
            }
            String normalizedCpf = cpfStr.replaceAll("\\D", "");
            if (!isValidCpf(normalizedCpf)) {
                throw new NullArgumentException("CPF invalido");
            }
            if (passwordStr.isBlank()) {
                throw new NullArgumentException("Senha não pode ser vazia");
            }

            Client client = new Client(nomeStr.trim(), numeroStr.trim(), passwordStr, normalizedCpf);
            return client;
        }
        else
        {
            throw new NullArgumentException("Algum requisito não foi atendido para criar o cliente");
        }

    }

    private static boolean isValidCpf(String cpf) {
        if (cpf == null || cpf.length() != 11 || cpf.chars().distinct().count() == 1) {
            return false;
        }
        try {
            int firstDigit = calculateCpfDigit(cpf.substring(0, 9), 10);
            int secondDigit = calculateCpfDigit(cpf.substring(0, 10), 11);
            return cpf.charAt(9) - '0' == firstDigit
                    && cpf.charAt(10) - '0' == secondDigit;
        } catch (NumberFormatException exception) {
            return false;
        }
    }

    private static int calculateCpfDigit(String digits, int factor) {
        int sum = 0;
        for (char digit : digits.toCharArray()) {
            sum += (digit - '0') * factor--;
        }
        int remainder = (sum * 10) % 11;
        return remainder == 10 ? 0 : remainder;
    }
}