package ru.tolstov.lab1.accounts;

import ru.tolstov.lab1.ServiceOperationResult;
import ru.tolstov.lab1.accounts.calculationmodels.CreditAccountCalculationModel;
import ru.tolstov.lab1.accounts.calculationmodels.DebitAccountCalculationModel;
import ru.tolstov.lab1.accounts.calculationmodels.DepositAccountCalculationModel;

public interface AccountOperationService {
    ServiceOperationResult deposit(Account account, double value);
    ServiceOperationResult withdraw(Account account, double value);
    ServiceOperationResult transfer(Account from, Account to, double value);
    void skipDays(Account account, int amount);
    DebitAccountCalculationModel peekDaysDebit(DebitAccount account, int amount);
    CreditAccountCalculationModel peekDaysCredit(CreditAccount account, int amount);
    DepositAccountCalculationModel peekDaysDeposit(DepositAccount account, int amount);

}
