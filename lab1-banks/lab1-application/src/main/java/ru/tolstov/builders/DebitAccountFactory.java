package ru.tolstov.builders;

import ru.tolstov.Bank;
import ru.tolstov.Client;
import ru.tolstov.Money;
import ru.tolstov.accounts.Account;
import ru.tolstov.accounts.AccountStatus;
import ru.tolstov.accounts.DebitAccount;
import ru.tolstov.services.CentralBankServiceImpl;

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
