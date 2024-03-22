package ru.tolstov.repositories;

import ru.tolstov.Bank;
import ru.tolstov.Client;
import ru.tolstov.Money;
import ru.tolstov.accounts.Account;

import java.util.List;
import java.util.Optional;

public interface BankRepository {
    void registerBank(Bank bank);
    Optional<Bank> findBankByName(String name);
    void addAccountToBank(Bank bank, Account account);
    List<Account> getAllAccounts(Bank bank);
    List<Bank> getAllBanks();
    void updateSuspendedLimit(Bank bank, Money limit);
    void updateCreditConditions(Bank bank, Money limit, Money commission);
    void updateDebitConditions(Bank bank, double debitPaymentPercent);
    void addDepositPercent(Bank bank, Money value, double percent);
    void removeDepositPercent(Bank bank, Money value, double percent);
    void registerSubscriber(Bank bank, Client client);
    void deleteSubscriber(Bank bank, Client client);
    List<Client> getSubscribers(Bank bank);
}
