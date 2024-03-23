package ru.tolstov.services;

import ru.tolstov.Money;
import ru.tolstov.ServiceOperationResult;
import ru.tolstov.TransactionService;
import ru.tolstov.accounts.Account;
import ru.tolstov.repositories.TransactionRepository;
import ru.tolstov.transactions.*;

import java.util.*;

public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void createDepositTransaction(Calendar date, Account account, Money value, TransactionStatus status) {
        var transaction = new DepositTransaction(
                UUID.randomUUID(),
                (GregorianCalendar) date.clone(),
                status,
                account,
                new Money(value.getValue())
        );

        account.getTransactions().add(transaction);
        transactionRepository.registerTransaction(transaction);
    }

    @Override
    public void createWithdrawTransaction(Calendar date, Account account, Money value, TransactionStatus status) {
        var transaction = new WithdrawTransaction(
                UUID.randomUUID(),
                (GregorianCalendar) date.clone(),
                status,
                account,
                new Money(value.getValue())
        );

        account.getTransactions().add(transaction);
        transactionRepository.registerTransaction(transaction);
    }

    @Override
    public void createTransferTransaction(Calendar date, Account from, Account to, Money value, TransactionStatus status) {
        var transaction = new TransferTransaction(
                UUID.randomUUID(),
                (GregorianCalendar) date.clone(),
                status,
                from,
                to,
                new Money(value.getValue())
        );

        from.getTransactions().add(transaction);
        to.getTransactions().add(transaction);

        transactionRepository.registerTransaction(transaction);
    }

    @Override
    public List<Transaction> getAccountTransactions(Account account) {
        return account.getTransactions();
    }

    @Override
    public Optional<Transaction> getTransactionById(UUID id) {
        return transactionRepository.getTransactionById(id);
    }

    @Override
    public ServiceOperationResult cancelTransaction(Transaction transaction) {
        if (transaction.getStatus() != TransactionStatus.SUCCEED)
            return new ServiceOperationResult.Fail("Transaction can't be canceled, status: %s".formatted(transaction.getStatus()));

        transaction.cancel();
        transactionRepository.updateStatus(transaction, TransactionStatus.CANCELED);

        return new ServiceOperationResult.Success();
    }
}
