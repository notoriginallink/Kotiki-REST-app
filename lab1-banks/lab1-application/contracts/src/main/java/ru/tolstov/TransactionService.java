package ru.tolstov;

import ru.tolstov.accounts.Account;
import ru.tolstov.transactions.*;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionService {
    void createDepositTransaction(Calendar date, Account account, Money value, TransactionStatus status);
    void createWithdrawTransaction(Calendar date, Account account, Money value, TransactionStatus status);
    void createTransferTransaction(Calendar date, Account from, Account to, Money value, TransactionStatus status);
    List<Transaction> getAccountTransactions(Account account);
    Optional<Transaction> getTransactionById(UUID id);
    ServiceOperationResult cancelTransaction(Transaction transaction);
}
