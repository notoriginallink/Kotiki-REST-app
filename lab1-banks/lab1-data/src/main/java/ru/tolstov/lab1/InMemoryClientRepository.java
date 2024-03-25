package ru.tolstov.lab1;

import ru.tolstov.lab1.Bank;
import ru.tolstov.lab1.Client;
import ru.tolstov.lab1.Phone;
import ru.tolstov.lab1.repositories.ClientRepository;

import java.util.*;

public class InMemoryClientRepository implements ClientRepository {
    private final Map<Phone, List<Client>> clients;
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

        var phone = client.getPhoneNumber();
        clients.get(phone).add(client);
    }

    @Override
    public void updateAddress(Client client, String address) {
        clients.get(client.getPhoneNumber()).stream()
                .filter(c -> c.getBank().equals(client.getBank()))
                .findAny()
                .orElseThrow(() -> new RuntimeException("No such client in repository"));
    }

    @Override
    public void updatePassportID(Client client, String passportID) {
        clients.get(client.getPhoneNumber()).stream()
                .filter(c -> c.getBank().equals(client.getBank()))
                .findAny()
                .orElseThrow(() -> new RuntimeException("No such client in repository"));
    }
}
