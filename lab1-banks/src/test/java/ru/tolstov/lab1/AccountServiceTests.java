package ru.tolstov.lab1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.tolstov.lab1.accounts.AccountOperationService;
import ru.tolstov.lab1.accounts.AccountService;
import ru.tolstov.lab1.banks.CentralBankService;
import ru.tolstov.lab1.clients.ClientService;
import ru.tolstov.lab1.repositories.AccountRepository;
import ru.tolstov.lab1.repositories.BankRepository;
import ru.tolstov.lab1.repositories.ClientRepository;
import ru.tolstov.lab1.repositories.TransactionRepository;
import ru.tolstov.lab1.services.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountServiceTests {
    private AccountService accountService;
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
        ClientService clientService = new ClientServiceImpl(clientRepository, bankRepository, accountRepository);
        AccountOperationService accountOperationService = new AccountOperationServiceImpl(accountRepository, transactionService);
        CentralBankService centralBankService = new CentralBankServiceImpl(bankRepository, transactionService, accountOperationService);
        accountService = new AccountServiceImpl(accountRepository, bankRepository, transactionService);

        centralBankService.createBank("Tinkoff", 111, 1111, 11, 11);
        bank = centralBankService.getBankByName("Tinkoff").get();

        clientService.createClient("98765432112", "amogus", "aboba", null, null, bank);
        client = clientService.getClientByPhone(bank, "98765432112").get();
    }

    @Test
    void CreateDebitAccount_ShouldBeCreated() {
        assertEquals(0, bank.getAccounts().size());

        var account = accountService.createDebitAccount(bank, client);

        assertEquals(1, bank.getAccounts().size());

        assertEquals(client, account.getClient());
    }

    @Test
    void CreateCreditAccount_ShouldBeCreated() {
        assertEquals(0, bank.getAccounts().size());

        var account = accountService.createCreditAccount(bank, client);

        assertEquals(1, bank.getAccounts().size());

        assertEquals(client, account.getClient());
    }

    @Test
    void CreateDepositAccount_ShouldBeCreated() {
        var duration = 3;
        var startBalance = 100000;
        assertEquals(0, bank.getAccounts().size());

        var account = accountService.createDepositAccount(bank, client, duration, startBalance);

        assertEquals(1, bank.getAccounts().size());

        assertEquals(client, account.getClient());

        assertEquals(startBalance, account.getBalance().getValue());
    }
}
