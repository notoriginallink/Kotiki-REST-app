package ru.tolstov.lab1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.tolstov.lab1.*;
import ru.tolstov.lab1.accounts.AccountOperationService;
import ru.tolstov.lab1.banks.CentralBankService;
import ru.tolstov.lab1.clients.ClientService;
import ru.tolstov.lab1.repositories.AccountRepository;
import ru.tolstov.lab1.repositories.BankRepository;
import ru.tolstov.lab1.repositories.ClientRepository;
import ru.tolstov.lab1.repositories.TransactionRepository;
import ru.tolstov.lab1.services.AccountOperationServiceImpl;
import ru.tolstov.lab1.services.CentralBankServiceImpl;
import ru.tolstov.lab1.services.ClientServiceImpl;
import ru.tolstov.lab1.services.TransactionServiceImpl;

import static org.junit.jupiter.api.Assertions.*;

public class ClientServiceTests {
    private ClientService clientService;
    private Bank bank;
    private Client client;

    @BeforeEach
    public void init() {
        // REPOSITORIES
        BankRepository bankRepository = new InMemoryBankRepository();
        ClientRepository clientRepository = new InMemoryClientRepository();
        AccountRepository accountRepository = new InMemoryAccountRepository();
        TransactionRepository transactionRepository = new InMemoryTransactionRepository();

        // SERVICES
        TransactionService transactionService = new TransactionServiceImpl(transactionRepository);
        clientService = new ClientServiceImpl(clientRepository, bankRepository, accountRepository);
        AccountOperationService accountOperationService = new AccountOperationServiceImpl(accountRepository, transactionService);
        CentralBankService centralBankService = new CentralBankServiceImpl(bankRepository, transactionService, accountOperationService);

        centralBankService.createBank("Tinkoff", 111, 1111, 11, 11);
        bank = centralBankService.getBankByName("Tinkoff").get();

        clientService.createClient("98765432112", "ivan", "ivanov", null, null, bank);
        client = clientService.getClientByPhone(bank, "98765432112").get();
    }

    @Test
    void createClient_ShouldBeCreated() {
        var phone = "79998887766";
        var firstName = "aboba";
        var lastName = "amogusov";
        var address = "SPB";
        var passportID = "1337";

        clientService.createClient(phone, firstName, lastName, address, passportID, bank);
        var client = clientService.getClientByPhone(bank, phone);

        assertTrue(client.isPresent());
        assertEquals(phone, client.get().getPhoneNumber().getNumber());
    }

    @Test
    void updateAddress_ShouldBeUpdated() {
        var currentAddress = client.getAddress();
        var expectedAddress = "SPB";
        assertNotEquals(expectedAddress, currentAddress);

        clientService.updateAddress(client, expectedAddress);

        assertEquals(expectedAddress, client.getAddress());
    }

    @Test
    void updatePassportID_ShouldBeUpdated() {
        var currentPassportID = client.getPassportID();
        var expectedPassportID = "1337";
        assertNotEquals(expectedPassportID, currentPassportID);

        clientService.updatePassportID(client, expectedPassportID);

        assertEquals(expectedPassportID, client.getPassportID());
    }
}
