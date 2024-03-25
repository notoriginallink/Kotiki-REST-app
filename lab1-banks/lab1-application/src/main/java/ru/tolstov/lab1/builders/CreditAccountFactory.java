package ru.tolstov.lab1.builders;

import ru.tolstov.lab1.Bank;
import ru.tolstov.lab1.Client;
import ru.tolstov.lab1.Money;
import ru.tolstov.lab1.accounts.Account;
import ru.tolstov.lab1.accounts.AccountStatus;
import ru.tolstov.lab1.accounts.CreditAccount;
import ru.tolstov.lab1.services.CentralBankServiceImpl;

import java.util.ArrayList;
import java.util.UUID;

public class CreditAccountFactory implements AccountFactory {
    @Override
    public Account create(Client client, Bank bank) {
        return new CreditAccount(
                UUID.randomUUID(),
                client,
                CentralBankServiceImpl.currentDate(),
                new ArrayList<>(),
                bank.getSuspendedLimit(),
                new Money(0),
                AccountStatus.FULL_ACCESS,
                bank.getCreditLimit(),
                bank.getCreditCommissionValue()
        );
    }
}
