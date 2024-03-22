package ru.tolstov.commands.banks;

import lombok.Getter;
import picocli.CommandLine;
import ru.tolstov.Bank;
import ru.tolstov.banks.CentralBankService;

import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "bank",
        description = "Provides interface to work with banks",
        mixinStandardHelpOptions = true
)
@Getter
public class BankCommand implements Callable<Integer> {
    private final CentralBankService centralBankService;

    @CommandLine.Parameters(
            paramLabel = "Name",
            description = "Name of the bank"
    )
    private String name;
    private Bank bank;

    public BankCommand(CentralBankService centralBankService) {
        this.centralBankService = centralBankService;
    }

    @Override
    public Integer call() {
        var bankOptional = centralBankService.getBankByName(name);
        if (bankOptional.isEmpty()) {
            System.out.printf("Bank %s does not found%n", name);
            return 1;
        }
        bank = bankOptional.get();

        return 0;
    }
}
