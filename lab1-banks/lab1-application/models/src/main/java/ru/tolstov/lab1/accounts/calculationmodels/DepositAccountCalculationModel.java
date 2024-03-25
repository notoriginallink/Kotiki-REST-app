package ru.tolstov.lab1.accounts.calculationmodels;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.tolstov.lab1.Money;
import ru.tolstov.lab1.accounts.DepositAccountStatus;

import java.util.Calendar;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DepositAccountCalculationModel extends AccountCalculationModel {
    Money balance;
    Money subtotalValue;
    DepositAccountStatus status;
    public DepositAccountCalculationModel(Calendar date, Money balance, Money subtotalValue, DepositAccountStatus status) {
        super(date);
        this.balance = balance;
        this.subtotalValue = subtotalValue;
        this.status = status;
    }

    @Override
    public String toString() {
        return super.toString() + "Balance: %s\nSubtotal value: %s\nDeposit status: %s%n".formatted(balance, subtotalValue, status);
    }
}
