package ru.tolstov.transactions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.tolstov.Money;
import ru.tolstov.accounts.Account;

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
    }

    @Override
    public String toString() {
        return super.toString() + "ID: %s\nType: %s Value: %s\nAccountID: %s\n"
                .formatted(id, "DEPOSIT", value, account.getId());
    }
}
