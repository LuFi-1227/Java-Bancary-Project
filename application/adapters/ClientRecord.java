package application.adapters;
import domain.ClientId;

import java.util.Optional;

import domain.Client;

public record ClientRecord(
    ClientId id,
    String name,
    String number,
    String password,
    String cpf
) {
    public static ClientRecord from(Client client) {
        return new ClientRecord(client.getId(), client.getName(), client.getNumber(), client.getPassword(), client.getCpf());
    }

    public Client create() {
        return Client.getClient(Optional.ofNullable(this.name), Optional.ofNullable(this.number), Optional.ofNullable(this.password), Optional.ofNullable(this.cpf));
    }
}
