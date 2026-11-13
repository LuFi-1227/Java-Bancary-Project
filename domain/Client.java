package domain;

import java.util.Optional;

import exceptions.NullArgumentException;

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
        return "Client [name=" + name + ", cpf=" + cpf + "]";
    }

    private static Client client = null;

    public static Client getClient(Optional<String> nome, Optional<String> numero, Optional<String> password, Optional<String> cpf) {
        if (client == null) {
            String nomeStr = nome.orElseThrow(() -> new NullArgumentException("Nome é obrigatório"));
            String numeroStr = numero.orElseThrow(() -> new NullArgumentException("Número é obrigatório"));
            String passwordStr = password.orElseThrow(() -> new NullArgumentException("Senha é obrigatória"));
            String cpfStr = cpf.orElseThrow(() -> new NullArgumentException("CPF é obrigatório"));

            client = new Client(nomeStr, numeroStr, passwordStr, cpfStr);
        }

        return client;
    }
}