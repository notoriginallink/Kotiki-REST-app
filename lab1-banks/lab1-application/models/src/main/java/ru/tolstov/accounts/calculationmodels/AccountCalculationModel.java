package ru.tolstov.accounts.calculationmodels;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Calendar;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class AccountCalculationModel {
    Calendar date;
    @Override
    public String toString() {
        return "Date: %s\n".formatted(date.getTime());
    }
}
