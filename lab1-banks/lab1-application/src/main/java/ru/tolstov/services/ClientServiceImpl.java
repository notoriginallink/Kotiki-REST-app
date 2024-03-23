package ru.tolstov.services;

import ru.tolstov.Bank;
import ru.tolstov.Client;
import ru.tolstov.Phone;
import ru.tolstov.ServiceOperationResult;
import ru.tolstov.accounts.Account;
import ru.tolstov.accounts.AccountStatus;
import ru.tolstov.clients.ClientService;
import ru.tolstov.repositories.AccountRepository;
import ru.tolstov.repositories.BankRepository;
import ru.tolstov.repositories.ClientRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final BankRepository bankRepository;
    private final AccountRepository accountRepository;
    private final static int VALID_PHONE_NUMBER_LENGTH = 11;
    public ClientServiceImpl(ClientRepository clientRepository, BankRepository bankRepository, AccountRepository accountRepository) {
        this.clientRepository = clientRepository;
        this.bankRepository = bankRepository;
        this.accountRepository = accountRepository;
    }
    @Override
    public ServiceOperationResult createClient(String phoneNumber, String firstName, String lastName, String address, String passportId, Bank bank) {
        if (phoneNumber.length() != VALID_PHONE_NUMBER_LENGTH)
            return new ServiceOperationResult.Fail("Invalid phone number format, 11 digits required");

        Client client = new Client(new Phone(phoneNumber), firstName, lastName, address, passportId, bank, new ArrayList<>());
        bank.getClients().add(client);
        clientRepository.registerClient(client);

        return new ServiceOperationResult.Success();
    }

    @Override
    public ServiceOperationResult updateAddress(Client client, String address) {
        checkBankPresence(client);

        if (client.getAddress() == null && client.getPassportID() != null) {
            var clientAccounts = bankRepository.getAllAccounts(client.getBank()).stream()
                    .filter(a -> a.getClient().equals(client))
                    .toList();

            for (var account : clientAccounts) {
                account.setStatus(AccountStatus.FULL_ACCESS);
                accountRepository.updateStatus(account, AccountStatus.FULL_ACCESS);
            }
        }

        clientRepository.updateAddress(client, address);

        return new ServiceOperationResult.Success();
    }

    @Override
    public ServiceOperationResult updatePassportID(Client client, String passportID) {
        checkBankPresence(client);

        if (client.getAddress() != null && client.getPassportID() == null) {
            var clientAccounts = bankRepository.getAllAccounts(client.getBank()).stream()
                    .filter(a -> a.getClient().equals(client))
                    .toList();

            for (var account : clientAccounts) {
                account.setStatus(AccountStatus.FULL_ACCESS);
                accountRepository.updateStatus(account, AccountStatus.FULL_ACCESS);
            }
        }

        clientRepository.updatePassportID(client, passportID);

        return new ServiceOperationResult.Success();
    }

    @Override
    public Optional<Client> getClientByPhone(Bank bank, String phoneNumber) {
        return clientRepository.getClientByPhoneNumber(bank, new Phone(phoneNumber));
    }

    @Override
    public List<Account> getClientAccounts(Bank bank, Client client) {
        return bankRepository.getAllAccounts(bank).stream()
                .filter(c -> c.getClient().equals(client))
                .toList();
    }

    private void checkBankPresence(Client client) {
        var bank = bankRepository.findBankByName(client.getBank().getName());
        if (bank.isEmpty())
            throw new RuntimeException("Client's bank does not exists");
    }
}
