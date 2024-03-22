package ru.tolstov.builders;

import ru.tolstov.Bank;
import ru.tolstov.Client;
import ru.tolstov.Money;
import ru.tolstov.accounts.Account;
import ru.tolstov.accounts.AccountStatus;
import ru.tolstov.accounts.DepositAccount;
import ru.tolstov.accounts.DepositAccountStatus;
import ru.tolstov.services.CentralBankServiceImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class DepositAccountFactory implements AccountFactory {
    private final int durationMonth;
    private final Money startBalance;
    public DepositAccountFactory(int durationMonth, Money startBalance) {
        this.durationMonth = durationMonth;
        this.startBalance = startBalance;
    }
    @Override
    public Account create(Client client, Bank bank) {
        var expirationDate = CentralBankServiceImpl.currentDate();
        expirationDate.add(Calendar.MONTH, durationMonth);

        var nextPaymentDate = CentralBankServiceImpl.currentDate();
        nextPaymentDate.add(Calendar.MONTH, 1);

        return new DepositAccount(
                UUID.randomUUID(),
                client,
                CentralBankServiceImpl.currentDate(),
                new ArrayList<>(),
                bank.getSuspendedLimit(),
                startBalance,
                AccountStatus.FULL_ACCESS,
                expirationDate,
                nextPaymentDate,
                bank.getDepositPaymentPercents().getPercentByValue(startBalance.getValue()),
                new Money(0),
                DepositAccountStatus.OPEN
        );
    }
}
