package ru.tolstov.lab1.repositories;

import ru.tolstov.lab1.transactions.Transaction;
import ru.tolstov.lab1.transactions.TransactionStatus;

import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository {
    void registerTransaction(Transaction transaction);
    Optional<Transaction> getTransactionById(UUID id);
    void updateStatus(Transaction transaction, TransactionStatus status);
}
