package ru.tolstov.commands.banks;

import picocli.CommandLine;

@CommandLine.Command(
        name = "info",
        description = "Prints information about bank",
        mixinStandardHelpOptions = true
)
public class BankInfoCommand implements Runnable {
    @CommandLine.ParentCommand
    private BankCommand mainCmd;

    @Override
    public void run() {
        if (mainCmd.call() != 0)
            return;

        var bank = mainCmd.getBank();
        System.out.printf("Name: %s%nSuspended limit: %s%nCredit limit: %s Credit commission value: %s%nDebit Monthly Percent: %s%n",
                bank.getName(), bank.getSuspendedLimit(), bank.getCreditLimit(), bank.getCreditCommissionValue(), bank.getDebitPaymentPercent());
        System.out.println("Deposit Percents:");
        System.out.println(bank.getDepositPaymentPercents());
    }
}
