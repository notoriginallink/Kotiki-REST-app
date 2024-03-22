package ru.tolstov.commands.accounts;

import picocli.CommandLine;
import ru.tolstov.TransactionService;

@CommandLine.Command(
        name = "txls",
        description = "Prints information about account's transactions",
        mixinStandardHelpOptions = true
)
public class AccountTransactionListCommand implements Runnable {
    private final TransactionService transactionService;
    @CommandLine.ParentCommand
    private AccountCommand mainCmd;

    @CommandLine.Option(
            names = {"-n", "--number"},
            description = "Amount of transactions to print",
            defaultValue = "10"
    )
    private int number;

    public AccountTransactionListCommand(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public void run() {
        if (mainCmd.call() != 0)
            return;

        var account = mainCmd.getAccount();

        transactionService.getAccountTransactions(account).stream()
                .limit(number)
                .forEach(System.out::println);
    }
}
