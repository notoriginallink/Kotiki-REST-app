package ru.tolstov;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.tolstov.accounts.AccountOperationService;
import ru.tolstov.accounts.AccountService;
import ru.tolstov.accounts.DepositAccount;
import ru.tolstov.accounts.DepositAccountStatus;
import ru.tolstov.banks.CentralBankService;
import ru.tolstov.clients.ClientService;
import ru.tolstov.repositories.AccountRepository;
import ru.tolstov.repositories.BankRepository;
import ru.tolstov.repositories.ClientRepository;
import ru.tolstov.repositories.TransactionRepository;
import ru.tolstov.services.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class AccountOperationServiceTests {
    private AccountOperationService aos;
    private AccountService as;
    private Bank bank;
    private Client normalClient;
    private Client suspendedClient;

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
        aos = new AccountOperationServiceImpl(accountRepository, transactionService);
        CentralBankService centralBankService = new CentralBankServiceImpl(bankRepository, transactionService, aos);
        as = new AccountServiceImpl(accountRepository, bankRepository, transactionService);

        centralBankService.createBank("Tinkoff", 1000, 1111, 11, 11);
        bank = centralBankService.getBankByName("Tinkoff").get();

        clientService.createClient("98765432112", "amogus", "aboba", null, null, bank);
        suspendedClient = clientService.getClientByPhone(bank, "98765432112").get();

        clientService.createClient("11111111111", "aboba", "amogusov", "SPB", "1337", bank);
        normalClient = clientService.getClientByPhone(bank, "11111111111").get();
    }

    @Test
    void DepositWithdraw_FromNormalDebitAccount_ShouldBeSuccessful() {
        var startValue = 0;
        var value = 1000;

        var account = as.createDebitAccount(bank, normalClient);
        assertEquals(startValue, account.getBalance().getValue());

        var result = aos.deposit(account, value);
        assertInstanceOf(ServiceOperationResult.Success.class, result);
        assertEquals(value, account.getBalance().getValue());

        result = aos.withdraw(account, value);
        assertInstanceOf(ServiceOperationResult.Success.class, result);
        assertEquals(startValue, account.getBalance().getValue());
    }

    @Test
    void DepositWithdraw_FromSuspendedDebitAccountWhenLessThanLimit_ShouldBeSuccessful() {
        var startValue = 0;
        var value = 1000;

        var account = as.createDebitAccount(bank, suspendedClient);
        assertEquals(startValue, account.getBalance().getValue());

        var result = aos.deposit(account, value);
        assertInstanceOf(ServiceOperationResult.Success.class, result);
        assertEquals(value, account.getBalance().getValue());

        result = aos.withdraw(account, value);
        assertInstanceOf(ServiceOperationResult.Success.class, result);
        assertEquals(startValue, account.getBalance().getValue());
    }

    @Test
    void DepositWithdraw_FromSuspendedDebitAccountWhenMoreThanLimit_SuccessFail() {
        var startValue = 0;
        var value = 1500;

        var account = as.createDebitAccount(bank, suspendedClient);
        assertEquals(startValue, account.getBalance().getValue());

        var result = aos.deposit(account, value);
        assertInstanceOf(ServiceOperationResult.Success.class, result);
        assertEquals(value, account.getBalance().getValue());

        result = aos.withdraw(account, value);
        assertInstanceOf(ServiceOperationResult.Fail.class, result);
        assertEquals(value, account.getBalance().getValue());
    }

    @Test
    void Withdraw_FromCreditAccountMoreThanHave_ShouldBeSuccess() {
        var startValue = 0;
        var value = 1500;
        var expectedValue = -1500;

        var account = as.createCreditAccount(bank, normalClient);
        assertEquals(startValue, account.getBalance().getValue());

        var result = aos.withdraw(account, value);
        assertInstanceOf(ServiceOperationResult.Success.class, result);
        assertEquals(expectedValue, account.getBalance().getValue());
    }

    @Test
    void DepositWithdraw_DepositAccountWhenIsOpen_SuccessFail() {
        var startValue = 10000;
        var value = 1000;
        var duration = 1;

        DepositAccount account = as.createDepositAccount(bank, normalClient, duration, startValue);
        assertEquals(DepositAccountStatus.OPEN, account.getDepositStatus());
        assertEquals(startValue, account.getBalance().getValue());

        var result = aos.deposit(account, value);
        assertInstanceOf(ServiceOperationResult.Success.class, result);
        assertEquals(startValue + value, account.getBalance().getValue());

        result = aos.withdraw(account, value);
        assertInstanceOf(ServiceOperationResult.Fail.class, result);
        assertEquals(startValue + value, account.getBalance().getValue());
    }
}
