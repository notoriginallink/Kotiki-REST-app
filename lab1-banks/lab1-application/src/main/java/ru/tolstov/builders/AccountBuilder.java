package ru.tolstov.builders;

import ru.tolstov.Bank;
import ru.tolstov.Client;
import ru.tolstov.accounts.Account;
import ru.tolstov.accounts.AccountStatus;

public class AccountBuilder {
    private AccountFactory factory;
    private Client client;
    private Bank bank;
    public AccountBuilder setFactory(AccountFactory factory) {
        this.factory = factory;

        return this;
    }

    public AccountBuilder setClient(Client client) {
        this.client = client;

        return this;
    }

    public AccountBuilder setBank(Bank bank) {
        this.bank = bank;

        return this;
    }

    public Account build() {
        if (client == null)
            throw new RuntimeException("Client is not provided to account builder");

        if (bank == null)
            throw new RuntimeException("Bank is not provided to account builder");

        var account = factory.create(client, bank);

        if (client.getAddress() == null || client.getPassportID() == null)
            account.setStatus(AccountStatus.SUSPENDED);
        else
            account.setStatus(AccountStatus.FULL_ACCESS);

        client = null;
        bank = null;

        return account;
    }
}
