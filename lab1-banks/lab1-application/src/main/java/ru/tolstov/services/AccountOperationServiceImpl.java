package ru.tolstov.services;

import ru.tolstov.Money;
import ru.tolstov.ServiceOperationResult;
import ru.tolstov.TransactionService;
import ru.tolstov.accounts.*;
import ru.tolstov.accounts.calculationmodels.CreditAccountCalculationModel;
import ru.tolstov.accounts.calculationmodels.DebitAccountCalculationModel;
import ru.tolstov.accounts.calculationmodels.DepositAccountCalculationModel;
import ru.tolstov.repositories.AccountRepository;
import ru.tolstov.transactions.TransactionStatus;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static java.lang.Math.abs;

public class AccountOperationServiceImpl implements AccountOperationService {
    private final AccountRepository accountRepository;
    private final TransactionService transactionService;
    public AccountOperationServiceImpl(AccountRepository accountRepository, TransactionService transactionService) {
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
    }

    @Override
    public ServiceOperationResult deposit(Account account, double value) {
        var moneyToDeposit = new Money(value);
        var result = account.deposit(moneyToDeposit);

        if (result instanceof AccountOperationResult.Success success) {
            accountRepository.updateBalance(success.getAccount(), success.getAccount().getBalance());
            transactionService.createDepositTransaction(
                    CentralBankServiceImpl.currentDate(),
                    account,
                    moneyToDeposit,
                    TransactionStatus.SUCCEED);

            return new ServiceOperationResult.Success();
        }

        transactionService.createDepositTransaction(
                CentralBankServiceImpl.currentDate(),
                account,
                moneyToDeposit,
                TransactionStatus.FAILED);

        return new ServiceOperationResult.Fail(((AccountOperationResult.Fail) result).getMessage());
    }

    @Override
    public ServiceOperationResult withdraw(Account account, double value) {
        var moneyToWithdraw = new Money(value);
        if (account.getStatus().equals(AccountStatus.SUSPENDED) && value > account.getSuspendedLimit().getValue()) {
            transactionService.createWithdrawTransaction(
                    CentralBankServiceImpl.currentDate(),
                    account,
                    moneyToWithdraw,
                    TransactionStatus.FAILED);

            return new ServiceOperationResult.Fail("Can't withdraw more than %s from suspended account".formatted(value));
        }

        var result = account.withdraw(moneyToWithdraw);

        if (result instanceof AccountOperationResult.Success success) {
            accountRepository.updateBalance(success.getAccount(), success.getAccount().getBalance());
            transactionService.createWithdrawTransaction(
                    CentralBankServiceImpl.currentDate(),
                    account,
                    moneyToWithdraw,
                    TransactionStatus.SUCCEED);

            return new ServiceOperationResult.Success();
        }

        transactionService.createWithdrawTransaction(
                CentralBankServiceImpl.currentDate(),
                account,
                moneyToWithdraw,
                TransactionStatus.FAILED);

        return new ServiceOperationResult.Fail(((AccountOperationResult.Fail) result).getMessage());
    }

    @Override
    public ServiceOperationResult transfer(Account from, Account to, double value) {
        var moneyToTransfer = new Money(value);
        if (from.getStatus().equals(AccountStatus.SUSPENDED) && value > from.getSuspendedLimit().getValue()) {
            transactionService.createTransferTransaction(
                    CentralBankServiceImpl.currentDate(),
                    from,
                    to,
                    moneyToTransfer,
                    TransactionStatus.FAILED
            );
            return new ServiceOperationResult.Fail("Can't transfer more than %s from suspended account".formatted(value));
        }


        var result = from.withdraw(moneyToTransfer);
        if (result instanceof AccountOperationResult.Success successFrom) {
            result = to.deposit(moneyToTransfer);
            if (result instanceof AccountOperationResult.Success successTo) {
                accountRepository.updateBalance(successFrom.getAccount(), successFrom.getAccount().getBalance());
                accountRepository.updateBalance(successTo.getAccount(), successTo.getAccount().getBalance());

                transactionService.createTransferTransaction(
                        CentralBankServiceImpl.currentDate(),
                        from,
                        to,
                        moneyToTransfer,
                        TransactionStatus.SUCCEED);

                return new ServiceOperationResult.Success();
            } else
                from.deposit(moneyToTransfer);
        }

        transactionService.createTransferTransaction(
                CentralBankServiceImpl.currentDate(),
                from,
                to,
                moneyToTransfer,
                TransactionStatus.FAILED);

        return new ServiceOperationResult.Fail(((AccountOperationResult.Fail) result).getMessage());
    }

    @Override
    public void skipDays(Account account, int amount) {
        if (account instanceof DebitAccount debit)
            skipDaysDebit(debit, amount);
        else if (account instanceof  CreditAccount credit)
            skipDaysCredit(credit, amount);
        else if (account instanceof DepositAccount deposit)
            skipDaysDeposit(deposit, amount);
        else
            throw new IllegalArgumentException("Unexpected account type");
    }

    public DebitAccountCalculationModel peekDaysDebit(DebitAccount account, int amount) {
        var model = new DebitAccountCalculationModel(
                CentralBankServiceImpl.currentDate(),
                new Money(account.getBalance()),
                new Money(account.getSubtotalValue()));
        model.getDate().add(Calendar.DAY_OF_MONTH, amount);

        var date = CentralBankServiceImpl.currentDate();
        var paymentDate = (GregorianCalendar) account.getNextPaymentDate().clone();
        double percentPerDay = account.getDebitPaymentPercent() * 12 / 365 / 100;

        while (amount > 0) {
            amount -= 1;
            date.add(Calendar.DAY_OF_MONTH, 1);

            if (date.equals(paymentDate)) {
                model.getBalance().increase(model.getSubtotalValue());

                paymentDate.add(Calendar.MONTH, 1);
                model.getSubtotalValue().setValue(0d);
                continue;
            }

            double dailyPayment = account.getBalance().getValue() * percentPerDay;
            model.getSubtotalValue().increase(dailyPayment);
        }

        return model;
    }

    public CreditAccountCalculationModel peekDaysCredit(CreditAccount account, int amount) {
        var model = new CreditAccountCalculationModel(
                CentralBankServiceImpl.currentDate(),
                new Money(account.getBalance())
        );
        model.getDate().add(Calendar.DAY_OF_MONTH, amount);

        if (model.getBalance().getValue() < 0 && abs(model.getBalance().getValue()) > account.getCreditLimit().getValue()){
            double commission = account.getCreditCommissionValue().getValue() * amount;
            model.getBalance().decrease(commission);
        }

        return model;
    }

    public DepositAccountCalculationModel peekDaysDeposit(DepositAccount account, int amount) {
        var model = new DepositAccountCalculationModel(
                CentralBankServiceImpl.currentDate(),
                new Money(account.getBalance()),
                new Money(account.getSubtotalValue()),
                account.getDepositStatus()
        );
        model.getDate().add(Calendar.DAY_OF_MONTH, amount);

        if (account.getDepositStatus().equals(DepositAccountStatus.CLOSED))
            return model;

        var date = CentralBankServiceImpl.currentDate();
        var nextPaymentDate = (GregorianCalendar) account.getNextPaymentDate().clone();
        double percentPerDay = account.getPaymentPercent() * 12 / 365 / 100;

        while (amount > 0 && model.getStatus().equals(DepositAccountStatus.OPEN)) {
            amount -= 1;
            date.add(Calendar.DAY_OF_MONTH, 1);

            if (date.equals(nextPaymentDate)) {
                model.getBalance().increase(model.getSubtotalValue());

                nextPaymentDate.add(Calendar.MONTH, 1);
                model.getSubtotalValue().setValue(0d);

                if (date.equals(account.getExpirationDate())) {
                    model.setStatus(DepositAccountStatus.CLOSED);
                    return model;
                }

                continue;
            }

            double dailyPayment = account.getBalance().getValue() * percentPerDay;
            model.getSubtotalValue().increase(dailyPayment);
        }

        return model;
    }

    private void skipDaysDebit(DebitAccount account, int amount) {
        var date = CentralBankServiceImpl.currentDate();
        double percentPerDay = account.getDebitPaymentPercent() * 12 / 365 / 100;

        while (amount > 0) {
            amount -= 1;
            date.add(Calendar.DAY_OF_MONTH, 1);

            if (date.equals(account.getNextPaymentDate())) {
                account.deposit(account.getSubtotalValue());
                transactionService.createDepositTransaction(date, account, account.getSubtotalValue(), TransactionStatus.SYSTEM);

                account.getNextPaymentDate().add(Calendar.MONTH, 1);
                account.getSubtotalValue().setValue(0d);
                continue;
            }

            double dailyPayment = account.getBalance().getValue() * percentPerDay;
            account.getSubtotalValue().increase(dailyPayment);
        }

        accountRepository.updateBalance(account, account.getBalance());
    }

    private void skipDaysCredit(CreditAccount account, int amount) {
        var currentBalance = account.getBalance().getValue();
        if (currentBalance < 0 && abs(currentBalance) > account.getCreditLimit().getValue()){
            Money commission = new Money(account.getCreditCommissionValue().getValue() * amount);
            Calendar date = CentralBankServiceImpl.currentDate();
            date.add(Calendar.DAY_OF_MONTH, amount);
            account.withdraw(commission);

            transactionService.createDepositTransaction(date, account, commission, TransactionStatus.SYSTEM);
        }

        accountRepository.updateBalance(account, account.getBalance());
    }

    private void skipDaysDeposit(DepositAccount account, int amount) {
        if (account.getDepositStatus().equals(DepositAccountStatus.CLOSED))
            return;

        var date = CentralBankServiceImpl.currentDate();
        double percentPerDay = account.getPaymentPercent() * 12 / 365 / 100;

        while (amount > 0 && account.getDepositStatus().equals(DepositAccountStatus.OPEN)) {
            amount -= 1;
            date.add(Calendar.DAY_OF_MONTH, 1);

            if (date.equals(account.getNextPaymentDate())) {
                account.deposit(account.getSubtotalValue());
                transactionService.createDepositTransaction(date, account, account.getSubtotalValue(), TransactionStatus.SYSTEM);

                account.getNextPaymentDate().add(Calendar.MONTH, 1);
                account.getSubtotalValue().setValue(0d);

                if (date.equals(account.getExpirationDate())) {
                    account.setDepositStatus(DepositAccountStatus.CLOSED);
                    return;
                }

                continue;
            }

            double dailyPayment = account.getBalance().getValue() * percentPerDay;
            account.getSubtotalValue().increase(dailyPayment);
        }

        accountRepository.updateBalance(account, account.getBalance());
    }
}
