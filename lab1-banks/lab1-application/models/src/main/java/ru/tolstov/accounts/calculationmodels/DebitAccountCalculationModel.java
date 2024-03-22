package ru.tolstov.accounts.calculationmodels;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.tolstov.Money;

import java.util.Calendar;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DebitAccountCalculationModel extends AccountCalculationModel {
    Money balance;
    Money subtotalValue;
    public DebitAccountCalculationModel(Calendar date, Money balance, Money subtotalValue) {
        super(date);
        this.balance = balance;
        this.subtotalValue = subtotalValue;
    }

    @Override
    public String toString() {
        return super.toString() + "Balance: %s\nSubtotal value: %s\n".formatted(balance, subtotalValue);
    }
}
