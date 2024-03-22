package ru.tolstov.repositories;

import ru.tolstov.Money;
import ru.tolstov.accounts.Account;
import ru.tolstov.accounts.AccountStatus;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {
    void addAccount(Account account);
    void updateBalance(Account account, Money value);
    void updateStatus(Account account, AccountStatus status);
    Optional<Account> getAccountById(UUID id);
}
