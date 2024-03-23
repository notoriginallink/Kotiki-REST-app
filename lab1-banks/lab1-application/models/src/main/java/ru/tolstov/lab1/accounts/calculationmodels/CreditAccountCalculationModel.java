package ru.tolstov.lab1.accounts.calculationmodels;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.tolstov.lab1.Money;

import java.util.Calendar;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class CreditAccountCalculationModel extends AccountCalculationModel {
    Money balance;
    public CreditAccountCalculationModel(Calendar date, Money balance) {
        super(date);
        this.balance = balance;
    }

    @Override
    public String toString() {
        return super.toString() + "Balance: %s\n".formatted(balance);
    }
}
