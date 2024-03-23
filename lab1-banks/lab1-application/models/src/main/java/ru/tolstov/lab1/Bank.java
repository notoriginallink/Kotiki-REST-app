package ru.tolstov.lab1;

import lombok.*;
import ru.tolstov.lab1.accounts.Account;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class Bank {
    @ToString.Include
    @EqualsAndHashCode.Include
    private final String name;
    private final List<Client> clients;
    private final List<Account> accounts;
    private Money suspendedLimit;
    private Money creditLimit;
    private double debitPaymentPercent;
    private Money creditCommissionValue;
    private final DepositPercentageManager depositPaymentPercents;
    private final List<Client> subscribers;
}
