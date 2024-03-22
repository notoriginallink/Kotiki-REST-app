package ru.tolstov.repositories;

import ru.tolstov.Bank;
import ru.tolstov.Client;
import ru.tolstov.Phone;

import java.util.Optional;

public interface ClientRepository {
    Optional<Client> getClientByPhoneNumber(Bank bank, Phone phone);
    void registerClient(Client client);
    void updateAddress(Client client, String address);
    void updatePassportID(Client client, String passportID);
}
