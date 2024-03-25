package ru.tolstov.lab1.repositories;

import ru.tolstov.lab1.Money;
import ru.tolstov.lab1.accounts.Account;
import ru.tolstov.lab1.accounts.AccountStatus;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {
    void addAccount(Account account);
    void updateBalance(Account account, Money value);
    void updateStatus(Account account, AccountStatus status);
    Optional<Account> getAccountById(UUID id);
}
