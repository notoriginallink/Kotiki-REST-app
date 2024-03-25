package ru.tolstov.lab1.transactions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.tolstov.lab1.Money;
import ru.tolstov.lab1.accounts.Account;

import java.util.Calendar;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DepositTransaction extends Transaction {
    private final Account account;
    private final Money value;
    public DepositTransaction(UUID id, Calendar date, TransactionStatus status, Account account, Money value) {
        super(id, date, status);
        this.account = account;
        this.value = value;
    }
    @Override
    public void cancel() {
        account.withdraw(value);
        status = TransactionStatus.CANCELED;
    }

    @Override
    public String toString() {
        return super.toString() + "ID: %s\nType: %s Value: %s\nAccountID: %s\n"
                .formatted(id, "DEPOSIT", value, account.getId());
    }
}
