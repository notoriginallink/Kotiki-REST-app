package ru.tolstov.lab1.repositories;

import ru.tolstov.lab1.Bank;
import ru.tolstov.lab1.Client;
import ru.tolstov.lab1.Phone;

import java.util.Optional;

public interface ClientRepository {
    Optional<Client> getClientByPhoneNumber(Bank bank, Phone phone);
    void registerClient(Client client);
    void updateAddress(Client client, String address);
    void updatePassportID(Client client, String passportID);
}
