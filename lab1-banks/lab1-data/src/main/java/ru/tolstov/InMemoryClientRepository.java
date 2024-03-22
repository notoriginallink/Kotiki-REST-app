package ru.tolstov;

import ru.tolstov.repositories.ClientRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class InMemoryClientRepository implements ClientRepository {
    private final HashMap<Phone, List<Client>> clients;
    public InMemoryClientRepository() {
        clients = new HashMap<>();
    }
    @Override
    public Optional<Client> getClientByPhoneNumber(Bank bank, Phone phone) {
        return clients.get(phone).stream()
                .filter(c -> c.getBank().equals(bank))
                .findAny();
    }

    @Override
    public void registerClient(Client client) {
        if (!clients.containsKey(client.getPhoneNumber()))
            clients.put(client.getPhoneNumber(), new ArrayList<>());

        clients.get(client.getPhoneNumber()).add(client);
    }

    @Override
    public void updateAddress(Client client, String address) {
        var clientInMemory = clients.get(client.getPhoneNumber()).stream()
                .filter(c -> c.getBank().equals(client.getBank()))
                .findAny();

        if (clientInMemory.isEmpty())
            throw new RuntimeException("No such client in repository");

        clientInMemory.get().setAddress(address);
    }

    @Override
    public void updatePassportID(Client client, String passportID) {
        var clientInMemory = clients.get(client.getPhoneNumber()).stream()
                .filter(c -> c.getBank().equals(client.getBank()))
                .findAny();

        if (clientInMemory.isEmpty())
            throw new RuntimeException("No such client in repository");

        clientInMemory.get().setPassportID(passportID);
    }
}
