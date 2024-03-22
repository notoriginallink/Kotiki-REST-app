package ru.tolstov.commands.transactions;

import picocli.CommandLine;
import ru.tolstov.ServiceOperationResult;

@CommandLine.Command(
        name = "cancel",
        description = "Cancels transaction, i.e. reverts its action." +
                "If transaction is FAILED, SYSTEM or already CANCELED, command ignored",
        mixinStandardHelpOptions = true
)
public class TransactionCancelCommand implements Runnable {
    @CommandLine.ParentCommand
    private TransactionCommand mainCmd;

    @Override
    public void run() {
        if (mainCmd.call() != 0)
            return;

        var tx = mainCmd.getTransaction();

        var result = mainCmd.getTransactionService().cancelTransaction(tx);
        if (result instanceof ServiceOperationResult.Fail fail)
            System.out.println(fail.getMessage());
        else
            System.out.println("Transaction canceled successfully");
    }
}
