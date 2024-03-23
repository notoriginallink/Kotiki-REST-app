package ru.tolstov;

import ru.tolstov.accounts.Account;
import ru.tolstov.accounts.AccountStatus;
import ru.tolstov.repositories.AccountRepository;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;

public class InMemoryAccountRepository implements AccountRepository {
    private final Map<UUID, Account> accounts;
    public InMemoryAccountRepository() {
        accounts = new TreeMap<>();
    }

    @Override
    public void addAccount(Account account) {
        var id = account.getId();
        if (accounts.containsKey(id))
            throw new RuntimeException("Account with this id %s already exists".formatted(id));

        accounts.put(id, account);
    }

    @Override
    public void updateBalance(Account account, Money value) {
        var id = account.getId();
        Account accountInMemory = accounts.get(id);

        if (accountInMemory == null)
            throw new RuntimeException("No such account in repository");
    }

    @Override
    public void updateStatus(Account account, AccountStatus status) {
        var id = account.getId();
        Account accountInMemory = accounts.get(id);

        if (accountInMemory == null)
            throw new RuntimeException("No such account in repository");
    }

    @Override
    public Optional<Account> getAccountById(UUID id) {
        return Optional.ofNullable(accounts.get(id));
    }
}
