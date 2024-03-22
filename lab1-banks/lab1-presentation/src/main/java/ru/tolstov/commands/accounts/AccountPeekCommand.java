package ru.tolstov.commands.accounts;

import picocli.CommandLine;
import ru.tolstov.accounts.CreditAccount;
import ru.tolstov.accounts.DebitAccount;
import ru.tolstov.accounts.DepositAccount;

@CommandLine.Command(
        name = "peek",
        description = "Shows information about account after period of time",
        mixinStandardHelpOptions = true
)
public class AccountPeekCommand implements Runnable {
    @CommandLine.ParentCommand
    private AccountCommand mainCmd;

    @CommandLine.Option(
            names = {"-d", "--days"},
            description = "Amount of days to skip and calculate information about account",
            required = true
    )
    private int days;

    @Override
    public void run() {
        if (mainCmd.call() != 0)
            return;

        var account = mainCmd.getAccount();

        if (account instanceof DebitAccount debit)
            System.out.println(mainCmd.getAccountOperationService().peekDaysDebit(debit, days));
        else if (account instanceof CreditAccount credit)
            System.out.println(mainCmd.getAccountOperationService().peekDaysCredit(credit, days));
        else if (account instanceof DepositAccount deposit)
            System.out.println(mainCmd.getAccountOperationService().peekDaysDeposit(deposit, days));
        else
            System.out.println("Unexpected error, couldn't peek days");
    }
}
