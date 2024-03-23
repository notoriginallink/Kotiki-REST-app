package ru.tolstov.lab1.commands.transactions;

import picocli.CommandLine;

@CommandLine.Command(
        name = "info",
        description = "Prints information about transaction",
        mixinStandardHelpOptions = true
)
public class TransactionInfoCommand implements Runnable {
    @CommandLine.ParentCommand
    private TransactionCommand mainCmd;

    @Override
    public void run() {
        if (mainCmd.call() != 0)
            return;

        System.out.println(mainCmd.getTransaction());
    }
}
