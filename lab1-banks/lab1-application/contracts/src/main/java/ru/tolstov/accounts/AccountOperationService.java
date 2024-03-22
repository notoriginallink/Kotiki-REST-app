package ru.tolstov.accounts;

import ru.tolstov.ServiceOperationResult;
import ru.tolstov.accounts.calculationmodels.CreditAccountCalculationModel;
import ru.tolstov.accounts.calculationmodels.DebitAccountCalculationModel;
import ru.tolstov.accounts.calculationmodels.DepositAccountCalculationModel;

public interface AccountOperationService {
    ServiceOperationResult deposit(Account account, double value);
    ServiceOperationResult withdraw(Account account, double value);
    ServiceOperationResult transfer(Account from, Account to, double value);
    void skipDays(Account account, int amount);
    DebitAccountCalculationModel peekDaysDebit(DebitAccount account, int amount);
    CreditAccountCalculationModel peekDaysCredit(CreditAccount account, int amount);
    DepositAccountCalculationModel peekDaysDeposit(DepositAccount account, int amount);

}
