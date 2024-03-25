package ru.tolstov.lab1;

import ru.tolstov.lab1.*;
import ru.tolstov.lab1.accounts.AccountOperationService;
import ru.tolstov.lab1.accounts.AccountService;
import ru.tolstov.lab1.banks.BankService;
import ru.tolstov.lab1.banks.CentralBankService;
import ru.tolstov.lab1.clients.ClientService;
import ru.tolstov.lab1.repositories.AccountRepository;
import ru.tolstov.lab1.repositories.BankRepository;
import ru.tolstov.lab1.repositories.ClientRepository;
import ru.tolstov.lab1.repositories.TransactionRepository;
import ru.tolstov.lab1.services.*;

public class Main {
    public static void main(String[] args) {
        // REPOSITORIES
        BankRepository bankRepository = new InMemoryBankRepository();
        ClientRepository clientRepository = new InMemoryClientRepository();
        AccountRepository accountRepository = new InMemoryAccountRepository();
        TransactionRepository transactionRepository = new InMemoryTransactionRepository();

        // SERVICES
        NotificationService notificationService = new NotificationServiceImpl();
        TransactionService transactionService = new TransactionServiceImpl(transactionRepository);
        ClientService clientService = new ClientServiceImpl(clientRepository, bankRepository, accountRepository);
        AccountService accountService = new AccountServiceImpl(accountRepository, bankRepository, transactionService);
        AccountOperationService accountOperationService = new AccountOperationServiceImpl(accountRepository, transactionService);
        CentralBankService centralBankService = new CentralBankServiceImpl(bankRepository, transactionService, accountOperationService);
        BankService bankService = new BankServiceImpl(bankRepository, notificationService);

        // RUNNER
        var runner = Runner.builder()
                .notificationService(notificationService)
                .transactionService(transactionService)
                .clientService(clientService)
                .accountService(accountService)
                .accountOperationService(accountOperationService)
                .centralBankService(centralBankService)
                .bankService(bankService)
                .build();

        runner.init();
        runner.start();
    }
}