package ru.tolstov.builders;

import ru.tolstov.Bank;
import ru.tolstov.Client;
import ru.tolstov.accounts.Account;

public interface AccountFactory {
    Account create(Client client, Bank bank);
}
