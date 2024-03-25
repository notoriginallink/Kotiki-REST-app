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
public class DepositAccount extends Account {
    @ToString.Exclude
    private final Calendar expirationDate;
    @ToString.Exclude
    private final Calendar nextPaymentDate;
    private double paymentPercent;
    private Money subtotalValue;
    private DepositAccountStatus depositStatus;
    public DepositAccount(
            UUID id,
            Client client,
            Calendar creationDate,
            List<Transaction> transactions,
            Money suspendedLimit,
            Money balance,
            AccountStatus accountStatus,
            Calendar expirationDate,
            Calendar nextPaymentDate,
            double paymentPercent,
            Money subtotalValue,
            DepositAccountStatus depositStatus) {
        super(id, client, creationDate, transactions, suspendedLimit, balance, accountStatus);

        this.expirationDate = expirationDate;
        this.nextPaymentDate = nextPaymentDate;
        this.paymentPercent = paymentPercent;
        this.subtotalValue = subtotalValue;
        this.depositStatus = depositStatus;
    }

    @Override
    public AccountOperationResult deposit(Money value) {
        balance.increase(value.getValue());

        return new AccountOperationResult.Success(this);
    }

    @Override
    public AccountOperationResult withdraw(Money value) {
        if (depositStatus.equals(DepositAccountStatus.OPEN))
            return new AccountOperationResult.Fail("Can't withdraw from account until %s".formatted(expirationDate.getTime()));

        if (balance.getValue() < value.getValue())
            return new AccountOperationResult.Fail("Can't withdraw more than have (Value: %s, Balance: %s)".formatted(value.getValue(), balance.getValue()));

        balance.decrease(value.getValue());

        return new AccountOperationResult.Success(this);
    }
}
