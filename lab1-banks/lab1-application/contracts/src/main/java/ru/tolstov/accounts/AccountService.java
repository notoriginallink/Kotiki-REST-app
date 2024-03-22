package ru.tolstov.accounts;

import ru.tolstov.Bank;
import ru.tolstov.Client;

import java.util.Optional;
import java.util.UUID;

public interface AccountService {
    DebitAccount createDebitAccount(Bank bank, Client client);
    CreditAccount createCreditAccount(Bank bank, Client client);
    DepositAccount createDepositAccount(Bank bank, Client client, int durationMonth, double startsBalance);
    Optional<Account> getAccountById(UUID id);
}
