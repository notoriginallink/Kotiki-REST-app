package ru.tolstov.commands.accounts;

import picocli.CommandLine;

@CommandLine.Command(
        name = "balance",
        description = "Prints balance of the account",
        mixinStandardHelpOptions = true
)
public class AccountBalanceCommand implements Runnable {
    @CommandLine.ParentCommand
    private AccountCommand mainCmd;

    @Override
    public void run() {
        if (mainCmd.call() != 0)
            return;

        var account = mainCmd.getAccount();

        System.out.println(account.getBalance());
    }
}
