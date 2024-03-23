package ru.tolstov.lab1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.tolstov.lab1.InMemoryAccountRepository;
import ru.tolstov.lab1.InMemoryBankRepository;
import ru.tolstov.lab1.InMemoryTransactionRepository;
import ru.tolstov.lab1.TransactionService;
import ru.tolstov.lab1.accounts.AccountOperationService;
import ru.tolstov.lab1.banks.CentralBankService;
import ru.tolstov.lab1.repositories.AccountRepository;
import ru.tolstov.lab1.repositories.BankRepository;
import ru.tolstov.lab1.repositories.TransactionRepository;
import ru.tolstov.lab1.services.AccountOperationServiceImpl;
import ru.tolstov.lab1.services.CentralBankServiceImpl;
import ru.tolstov.lab1.services.TransactionServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CentralBankServiceTests {
    private CentralBankService centralBankService;

    @BeforeEach
    public void init() {
        // REPOSITORIES
        BankRepository bankRepository = new InMemoryBankRepository();
        AccountRepository accountRepository = new InMemoryAccountRepository();
        TransactionRepository transactionRepository = new InMemoryTransactionRepository();

        // SERVICES
        TransactionService transactionService = new TransactionServiceImpl(transactionRepository);
        AccountOperationService accountOperationService = new AccountOperationServiceImpl(accountRepository, transactionService);
        centralBankService = new CentralBankServiceImpl(bankRepository, transactionService, accountOperationService);
    }

    @Test
    void CreateBank_ShouldBeCreated() {
        var name = "Tinkoff";
        var suspendedLimit = 2000;
        var creditLimit = 20000;
        var debitPaymentPercent = 2;
        var creditCommissionValue = 100;

        centralBankService.createBank(name, suspendedLimit, creditLimit, debitPaymentPercent, creditCommissionValue);

        var bank = centralBankService.getBankByName(name);

        assertTrue(bank.isPresent());
        assertEquals(bank.get().getName(), name);
    }
}
