package ru.tolstov.banks;

import ru.tolstov.Bank;
import ru.tolstov.Client;
import ru.tolstov.ServiceOperationResult;

public interface BankService {
    ServiceOperationResult updateSuspendedLimit(Bank bank, double limit);
    ServiceOperationResult updateCreditConditions(Bank bank, double limit, double commission);
    ServiceOperationResult updateDebitConditions(Bank bank, double debitPaymentPercent);
    ServiceOperationResult addDepositPercent(Bank bank, double value, double percent);
    ServiceOperationResult removeDepositPercent(Bank bank, double value, double percent);
    ServiceOperationResult subscribeClient(Bank bank, Client client);
    ServiceOperationResult unsubscribeClient(Bank bank, Client client);
    boolean hasSubscription(Bank bank, Client client);
}
