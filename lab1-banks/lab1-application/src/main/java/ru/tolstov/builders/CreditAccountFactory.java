package ru.tolstov.builders;

import ru.tolstov.Bank;
import ru.tolstov.Client;
import ru.tolstov.Money;
import ru.tolstov.accounts.Account;
import ru.tolstov.accounts.AccountStatus;
import ru.tolstov.accounts.CreditAccount;
import ru.tolstov.services.CentralBankServiceImpl;

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
