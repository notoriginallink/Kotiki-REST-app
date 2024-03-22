package ru.tolstov.repositories;

import ru.tolstov.transactions.Transaction;
import ru.tolstov.transactions.TransactionStatus;

import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository {
    void registerTransaction(Transaction transaction);
    Optional<Transaction> getTransactionById(UUID id);
    void updateStatus(Transaction transaction, TransactionStatus status);
}
