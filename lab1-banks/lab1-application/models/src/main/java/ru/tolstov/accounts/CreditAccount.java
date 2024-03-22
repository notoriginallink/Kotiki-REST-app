package ru.tolstov.accounts;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.tolstov.Client;
import ru.tolstov.Money;
import ru.tolstov.transactions.Transaction;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CreditAccount extends Account {
    private Money creditLimit;
    private Money creditCommissionValue;
    public CreditAccount(
            UUID id,
            Client client,
            Calendar creationDate,
            List<Transaction> transactions,
            Money suspendedLimit,
            Money balance,
            AccountStatus status,
            Money creditLimit,
            Money creditCommissionValue) {
        super(id, client, creationDate, transactions, suspendedLimit, balance, status);

        this.creditLimit = creditLimit;
        this.creditCommissionValue = creditCommissionValue;
    }

    @Override
    public AccountOperationResult deposit(Money value) {
        balance.setValue(balance.getValue() + value.getValue());

        return new AccountOperationResult.Success(this);
    }

    @Override
    public AccountOperationResult withdraw(Money value) {
        balance.setValue(balance.getValue() - value.getValue());

        return new AccountOperationResult.Success(this);
    }
}
