package ru.tolstov.lab1.commands.accounts;

import picocli.CommandLine;
import ru.tolstov.lab1.ServiceOperationResult;

@CommandLine.Command(
        name = "withdraw",
        aliases = {"dec", "with"},
        description = "Decreases account's balance by given value",
        mixinStandardHelpOptions = true
)
public class AccountWithdrawCommand implements Runnable {
    @CommandLine.ParentCommand
    private AccountCommand mainCmd;

    @CommandLine.Parameters(
            paramLabel = "Value",
            description = "Amount of money to withdraw"
    )
    private double value;

    @Override
    public void run() {
        if (mainCmd.call() != 0)
            return;

        var account = mainCmd.getAccount();
        var result = mainCmd.getAccountOperationService().withdraw(account, value);

        if (result instanceof ServiceOperationResult.Fail fail)
            System.out.println(fail.getMessage());
        else
            System.out.println("Withdraw success");
    }
}
