package ru.tolstov.lab1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.tolstov.lab1.accounts.AccountOperationService;
import ru.tolstov.lab1.banks.BankService;
import ru.tolstov.lab1.banks.CentralBankService;
import ru.tolstov.lab1.clients.ClientService;
import ru.tolstov.lab1.repositories.AccountRepository;
import ru.tolstov.lab1.repositories.BankRepository;
import ru.tolstov.lab1.repositories.ClientRepository;
import ru.tolstov.lab1.repositories.TransactionRepository;
import ru.tolstov.lab1.services.*;

import static org.junit.jupiter.api.Assertions.*;

public class BankServiceTests {
    private BankService bankService;
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
        NotificationService notificationService = new NotificationServiceImpl();
        TransactionService transactionService = new TransactionServiceImpl(transactionRepository);
        ClientService clientService = new ClientServiceImpl(clientRepository, bankRepository, accountRepository);
        AccountOperationService accountOperationService = new AccountOperationServiceImpl(accountRepository, transactionService);
        CentralBankService centralBankService = new CentralBankServiceImpl(bankRepository, transactionService, accountOperationService);
        bankService = new BankServiceImpl(bankRepository, notificationService);

        centralBankService.createBank("Tinkoff", 200, 20000, 5, 100);
        bank = centralBankService.getBankByName("Tinkoff").get();

        clientService.createClient("12345678912", "aboba", "boba", null, null, bank);
        client = clientService.getClientByPhone(bank, "12345678912").get();
    }

    @Test
    void updateSuspendedLimit_ShouldBeUpdated() {
        var currentSuspendedLimit = bank.getSuspendedLimit().getValue();
        var expectedSuspendedLimit = 5000;
        assertNotEquals(expectedSuspendedLimit, currentSuspendedLimit);

        bankService.updateSuspendedLimit(bank, expectedSuspendedLimit);

        assertEquals(expectedSuspendedLimit, bank.getSuspendedLimit().getValue());
    }

    @Test
    void updateCreditLimit_ShouldBeUpdated() {
        var currentCreditLimit = bank.getSuspendedLimit().getValue();
        var expectedCreditLimit = 20000;
        assertNotEquals(expectedCreditLimit, currentCreditLimit);

        bankService.updateCreditConditions(bank, expectedCreditLimit, -1);

        assertEquals(expectedCreditLimit, bank.getCreditLimit().getValue());
    }

    @Test
    void updateCreditCommission_ShouldBeUpdated() {
        var currentCreditCommission = bank.getSuspendedLimit().getValue();
        var expectedCreditCommission = 400;
        assertNotEquals(expectedCreditCommission, currentCreditCommission);

        bankService.updateSuspendedLimit(bank, expectedCreditCommission);

        assertEquals(expectedCreditCommission, bank.getSuspendedLimit().getValue());
    }

    @Test
    void updateDebitConditions_ShouldBeUpdated() {
        var currentDebitPercent = bank.getDebitPaymentPercent();
        var expectedDebitPercent = 10;
        assertNotEquals(expectedDebitPercent, currentDebitPercent);

        bankService.updateDebitConditions(bank, expectedDebitPercent);

        assertEquals(expectedDebitPercent, bank.getDebitPaymentPercent());
    }

    @Test
    void subscribeClient_WhenNotSubscribed_ShouldBeSubscribed() {
        assertFalse(bankService.hasSubscription(bank, client));

        bankService.subscribeClient(bank, client);

        assertTrue(bankService.hasSubscription(bank, client));
    }

    @Test
    void unsubscribeClient_WhenSubscribed_ShouldBeUnsubscribed() {
        bankService.subscribeClient(bank, client);
        assertTrue(bankService.hasSubscription(bank, client));

        bankService.unsubscribeClient(bank, client);

        assertFalse(bankService.hasSubscription(bank, client));
    }

    @Test
    void addDepositCondition_WhenThereWasNot_ShouldBeAdded() {
        double value = 30000;
        double percent = 30;
        assertNotEquals(percent, bank.getDepositPaymentPercents().getPercentByValue(value));

        bankService.addDepositPercent(bank, value, percent);

        assertEquals(percent, bank.getDepositPaymentPercents().getPercentByValue(value));
    }

    @Test
    void removeDepositCondition_WhenThereWas_ShouldBeRemoved() {
        double value = 30000;
        double percent = 30;
        bankService.addDepositPercent(bank, value, percent);
        assertEquals(percent, bank.getDepositPaymentPercents().getPercentByValue(value));

        bankService.removeDepositPercent(bank, value, percent);

        assertNotEquals(percent, bank.getDepositPaymentPercents().getPercentByValue(value));
    }
}
