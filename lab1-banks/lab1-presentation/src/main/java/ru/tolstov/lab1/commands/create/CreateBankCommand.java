package ru.tolstov.lab1.commands.create;

import picocli.CommandLine;
import ru.tolstov.lab1.ServiceOperationResult;
import ru.tolstov.lab1.banks.CentralBankService;

@CommandLine.Command(
        name = "bank",
        aliases = {"b"},
        description = "Creates new bank and registers it in Central Bank",
        mixinStandardHelpOptions = true
)
public class CreateBankCommand implements Runnable {
    private final CentralBankService centralBankService;
    @CommandLine.Option(
            names = {"-n", "--name"},
            required = true,
            description = "Name of the bank")
    private String name;

    @CommandLine.Option(
            names = {"-d", "--debit-percent"},
            required = true,
            description = "Size of monthly payment for debit accounts (in percents)"
    )
    private double debitPaymentPercent;

    @CommandLine.Option(
            names = {"-cc", "--credit-commission"},
            required = true,
            description = "Size of fixed commission for credit accounts"
    )
    private double creditCommissionValue;

    @CommandLine.Option(
            names = {"-cl", "--credit-limit"},
            required = true,
            description = "Size of limit for credit accounts"
    )
    private double creditLimit;


    @CommandLine.Option(
            names = {"-sl", "--suspended-limit"},
            required = true,
            description = "Size of withdraw and transfer limit for suspended clients"
    )
    private double suspendedLimit;

    public CreateBankCommand(CentralBankService centralBankService) {
        this.centralBankService = centralBankService;
    }

    @Override
    public void run() {
        var result = centralBankService.createBank(name, suspendedLimit, creditLimit, debitPaymentPercent, creditCommissionValue);

        if (result instanceof ServiceOperationResult.Fail fail)
            System.out.println(fail.getMessage());
        else
            System.out.printf("Bank %s created%n", name);
    }
}
