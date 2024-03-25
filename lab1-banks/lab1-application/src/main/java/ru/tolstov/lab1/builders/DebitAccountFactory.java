package ru.tolstov.lab1.builders;

import ru.tolstov.lab1.Bank;
import ru.tolstov.lab1.Client;
import ru.tolstov.lab1.Money;
import ru.tolstov.lab1.accounts.Account;
import ru.tolstov.lab1.accounts.AccountStatus;
import ru.tolstov.lab1.accounts.DebitAccount;
import ru.tolstov.lab1.services.CentralBankServiceImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class DebitAccountFactory implements AccountFactory {
    @Override
    public Account create(Client client, Bank bank) {
        var nextPaymentDate = CentralBankServiceImpl.currentDate();
        nextPaymentDate.add(Calendar.MONTH, 1);

        return new DebitAccount(
                UUID.randomUUID(),
                client,
                CentralBankServiceImpl.currentDate(),
                new ArrayList<>(),
                bank.getSuspendedLimit(),
                new Money(0d),
                AccountStatus.FULL_ACCESS,
                new Money(0d),
                nextPaymentDate,
                bank.getDebitPaymentPercent()
        );
    }
}
