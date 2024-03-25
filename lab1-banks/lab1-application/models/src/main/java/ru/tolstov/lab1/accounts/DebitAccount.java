package ru.tolstov.lab1.accounts;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.tolstov.lab1.Client;
import ru.tolstov.lab1.Money;
import ru.tolstov.lab1.transactions.Transaction;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DebitAccount extends Account {
    private Money subtotalValue;
    @ToString.Exclude
    private final Calendar nextPaymentDate;
    private double debitPaymentPercent;
    public DebitAccount(
            UUID id,
            Client client,
            Calendar creationDate,
            List<Transaction> transactions,
            Money suspendedLimit,
            Money balance,
            AccountStatus status,
            Money subtotalValue,
            Calendar nextPaymentDate,
            double debitPaymentPercent) {
        super(id, client, creationDate, transactions, suspendedLimit, balance, status);

        this.subtotalValue = subtotalValue;
        this.nextPaymentDate = nextPaymentDate;
        this.debitPaymentPercent = debitPaymentPercent;
    }

    @Override
    public AccountOperationResult deposit(Money value) {
        var current = balance.getValue();
        balance.setValue(current + value.getValue());

        return new AccountOperationResult.Success(this);
    }

    @Override
    public AccountOperationResult withdraw(Money value) {
        var current = balance.getValue();

        if (value.getValue() > current)
            return new AccountOperationResult.Fail("Can't withdraw more than have (Value: %s, Balance: %s)".formatted(value.getValue(), current));

        balance.setValue(current - value.getValue());

        return new AccountOperationResult.Success(this);
    }
}
