package ru.tolstov.lab1.commands.banks;

import picocli.CommandLine;
import ru.tolstov.lab1.ServiceOperationResult;
import ru.tolstov.lab1.banks.BankService;

@CommandLine.Command(
        name = "update",
        description = "Allows to update credit and debit account conditions and notifies clients about it",
        mixinStandardHelpOptions = true
)
public class BankUpdateCommand implements Runnable {
    private final BankService bankService;
    @CommandLine.ParentCommand
    private BankCommand mainCmd;

    @CommandLine.Option(
            names = {"-sl", "--suspended-limit"},
            description = "Sets new value for limit of suspended accounts",
            defaultValue = "-1"
    )
    private double suspendedLimit;

    @CommandLine.Option(
            names = {"-cr", "--credit-limit"},
            description = "Sets new value for credit limit",
            defaultValue = "-1"
    )
    private double creditLimit;

    @CommandLine.Option(
            names = {"-cc", "--credit-commission"},
            description = "Sets new value for credit commissions",
            defaultValue = "-1"
    )
    private double creditCommission;

    @CommandLine.Option(
            names = {"-dp", "--debit-percent"},
            description = "Sets new value for debit monthly payment percent. (Input value in percents)",
            defaultValue = "-1"
    )
    private double debitPaymentPercent;

    @CommandLine.ArgGroup(
            exclusive = false,
            multiplicity = "0..*"
    )
    DepositConditions[] depositConditions;

    public BankUpdateCommand(BankService bankService) {
        this.bankService = bankService;
    }

    @Override
    public void run() {
        if (mainCmd.call() != 0)
            return;

        var bank = mainCmd.getBank();
        ServiceOperationResult result = new ServiceOperationResult.Success();

        if (suspendedLimit != -1) {
            result = bankService.updateSuspendedLimit(bank, suspendedLimit);
            if (result instanceof ServiceOperationResult.Fail fail)
                System.out.println(fail.getMessage());
            else
                System.out.println("Limit for suspended accounts updated");
        }

        if (creditLimit != -1 || creditCommission != -1) {
            result = bankService.updateCreditConditions(bank, creditLimit, creditCommission);
            if (result instanceof ServiceOperationResult.Fail fail)
                System.out.println(fail.getMessage());
            else
                System.out.println("Credit account conditions were updated");
        }

        if (debitPaymentPercent != -1) {
            result = bankService.updateDebitConditions(bank, debitPaymentPercent);
            if (result instanceof ServiceOperationResult.Fail fail)
                System.out.println(fail.getMessage());
            else
                System.out.println("Debit account conditions were updated");
        }

        if (depositConditions == null)
            return;

        boolean success = true;
        for (var condition : depositConditions) {
            if (condition.type.equals(DepositConditions.DepositConditionsOperation.ADD))
                result = bankService.addDepositPercent(bank, condition.value, condition.percent);
            else if (condition.type.equals(DepositConditions.DepositConditionsOperation.REMOVE))
                result = bankService.removeDepositPercent(bank, condition.value, condition.percent);

            if (result instanceof ServiceOperationResult.Fail fail) {
                System.out.println(fail.getMessage());
                success = false;
                break;
            }
        }
        if (success)
            System.out.println("Deposit condition's were updated");
    }
    static class DepositConditions {
        @CommandLine.Parameters(
                paramLabel = "Type",
                description = "Type of operation. ADD or REMOVE"
        )
        DepositConditionsOperation type;
        @CommandLine.Parameters(paramLabel = "Value", description = "Double value")
        double value;
        @CommandLine.Parameters(paramLabel = "Percent", description = "Double value")
        double percent;
        public enum DepositConditionsOperation {
            ADD,
            REMOVE
        }
    }
}
