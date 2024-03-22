package ru.tolstov.accounts;

import lombok.*;
import ru.tolstov.Client;
import ru.tolstov.Money;
import ru.tolstov.transactions.Transaction;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
public abstract class Account {
    @EqualsAndHashCode.Include
    protected final UUID id;
    @ToString.Exclude
    protected final Client client;
    @ToString.Exclude
    protected final Calendar creationDate;
    @ToString.Exclude
    protected final List<Transaction> transactions;
    protected final Money suspendedLimit;
    protected Money balance;
    protected AccountStatus status;
    public abstract AccountOperationResult deposit(Money value);
    public abstract AccountOperationResult withdraw(Money value);
}
