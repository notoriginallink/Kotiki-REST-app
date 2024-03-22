package ru.tolstov;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.tolstov.accounts.AccountOperationService;
import ru.tolstov.banks.CentralBankService;
import ru.tolstov.repositories.AccountRepository;
import ru.tolstov.repositories.BankRepository;
import ru.tolstov.repositories.TransactionRepository;
import ru.tolstov.services.AccountOperationServiceImpl;
import ru.tolstov.services.CentralBankServiceImpl;
import ru.tolstov.services.TransactionServiceImpl;

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
