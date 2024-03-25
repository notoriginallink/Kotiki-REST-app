package ru.tolstov.lab1.transactions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
public abstract class Transaction {
    protected final UUID id;
    protected final Calendar date;
    protected TransactionStatus status;

    public Transaction(UUID id, Calendar date, TransactionStatus status) {
        this.id = id;
        this.date = date;
        this.status = status;
    }
    public abstract void cancel();
    @Override
    public String toString() {
        return "Date: %s\nStatus: %s\n".formatted(date.getTime(), status);
    }
}
