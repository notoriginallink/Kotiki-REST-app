package ru.tolstov.lab1.clients;

import ru.tolstov.lab1.Bank;
import ru.tolstov.lab1.Client;
import ru.tolstov.lab1.ServiceOperationResult;
import ru.tolstov.lab1.accounts.Account;

import java.util.List;
import java.util.Optional;

/**
 * Service for client management. It provides methods for creation of new client and update information of those which
 * are already created.
 * **/
public interface ClientService {
    /**
     * Creates new client
     * @param phoneNumber an 11 digit string, that is *unique* ID for client in a bank (one bank can't have
     *                    more than one client with same phone number)
     * @param firstName client's first name. Required field
     * @param lastName client's last name. Resuired field
     * @param address client's address, can be null
     * @param passportId client's passportID, can be null
     * @param bank bank in which new client need to be created
     * @return Result of the creation. Fail with error message or Success
     * **/
    ServiceOperationResult createClient(String phoneNumber, String firstName, String lastName, String address, String passportId, Bank bank);
    /**
     * Updates client's address. If only address is missing, then client's accounts stop being suspended
     * @param client client which address need to be updated
     * @param address new address
     * @return Result of update. Fail with error message or Success
     * **/
    ServiceOperationResult updateAddress(Client client, String address);
    /**
     * Updates client's passportID. If only passportID is missing, then client's accounts stop being suspended
     * @param client client which passportID need to be updated
     * @param passportID new address
     * @return Result of update. Fail with error message or Success
     * **/
    ServiceOperationResult updatePassportID(Client client, String passportID);
    /**
     * Finds client by its phone i.e. phone number is a unique client ID
     * @param bank bank to which belongs client
     * @param phoneNumber client's phone number
     * @return Optional<Client>
     * **/
    Optional<Client> getClientByPhone(Bank bank, String phoneNumber);
    /**
     * Finds all accounts that belong to client
     * @param bank bank to which belongs client
     * @param client client
     * @return List of accounts which belong to client
     * **/
    List<Account> getClientAccounts(Bank bank, Client client);
}
