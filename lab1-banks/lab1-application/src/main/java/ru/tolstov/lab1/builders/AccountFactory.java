package ru.tolstov.lab1.builders;

import ru.tolstov.lab1.Bank;
import ru.tolstov.lab1.Client;
import ru.tolstov.lab1.accounts.Account;

public interface AccountFactory {
    Account create(Client client, Bank bank);
}
