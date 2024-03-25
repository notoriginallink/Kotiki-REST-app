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
public class TransferTransaction extends Transaction {
    private final Account from;
    private final Account to;
    private Money value;
    public TransferTransaction(UUID id, Calendar date, TransactionStatus status, Account from, Account to, Money value) {
        super(id, date, status);
        this.from = from;
        this.to = to;
        this.value = value;
    }

    @Override
    public void cancel() {
        from.deposit(value);
        to.withdraw(value);
        status = TransactionStatus.CANCELED;
    }

    @Override
    public String toString() {
        return super.toString() + "ID: %s\nType: %s Value: %s\nSenderBank: %s SenderAccountID: %s\nReceiverBank: %s ReceiverAccountID: %s\n"
                .formatted(id,
                        "TRANSFER",
                        value,
                        from.getClient().getBank().getName(),
                        from.getId(),
                        to.getClient().getBank().getName(),
                        to.getId());
    }
}
