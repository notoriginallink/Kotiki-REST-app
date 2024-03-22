package ru.tolstov;

import ru.tolstov.accounts.AccountOperationService;
import ru.tolstov.accounts.AccountService;
import ru.tolstov.banks.BankService;
import ru.tolstov.banks.CentralBankService;
import ru.tolstov.clients.ClientService;
import ru.tolstov.repositories.AccountRepository;
import ru.tolstov.repositories.BankRepository;
import ru.tolstov.repositories.ClientRepository;
import ru.tolstov.repositories.TransactionRepository;
import ru.tolstov.services.*;

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