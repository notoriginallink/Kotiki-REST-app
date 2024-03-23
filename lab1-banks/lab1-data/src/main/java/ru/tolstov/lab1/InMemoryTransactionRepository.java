package ru.tolstov.lab1;

import ru.tolstov.lab1.repositories.TransactionRepository;
import ru.tolstov.lab1.transactions.Transaction;
import ru.tolstov.lab1.transactions.TransactionStatus;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;

public class InMemoryTransactionRepository implements TransactionRepository {
    private final Map<UUID, Transaction> transactions;
    public InMemoryTransactionRepository() {
        transactions = new TreeMap<>();
    }
    @Override
    public void registerTransaction(Transaction transaction) {
        if (transactions.containsKey(transaction.getId()))
            throw new RuntimeException("Transaction with this ID %s exists".formatted(transaction.getId()));

        transactions.put(transaction.getId(), transaction);
    }

    @Override
    public Optional<Transaction> getTransactionById(UUID id) {
        return Optional.ofNullable(transactions.get(id));
    }

    @Override
    public void updateStatus(Transaction transaction, TransactionStatus status) {
        var id = transaction.getId();
        if (transactions.get(id) == null)
            throw new RuntimeException("No such transaction in repository");
    }
}
