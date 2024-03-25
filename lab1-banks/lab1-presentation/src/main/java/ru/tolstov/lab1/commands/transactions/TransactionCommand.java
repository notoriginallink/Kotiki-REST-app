package ru.tolstov.lab1.commands.transactions;

import lombok.Getter;
import picocli.CommandLine;
import ru.tolstov.lab1.TransactionService;
import ru.tolstov.lab1.transactions.Transaction;

import java.util.UUID;
import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "transaction",
        aliases = {"tx"},
        description = "Provides interface to work with transactions",
        mixinStandardHelpOptions = true
)
@Getter
public class TransactionCommand implements Callable<Integer> {
    private final TransactionService transactionService;

    @CommandLine.Parameters(
            paramLabel = "ID",
            description = "Transaction's UUID"
    )
    private UUID id;

    private Transaction transaction;

    public TransactionCommand(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public Integer call() {
        var tx = transactionService.getTransactionById(id);
        if (tx.isEmpty()) {
            System.out.println("Transaction with this ID not found");
            return 1;
        }
        transaction = tx.get();

        return 0;
    }
}
