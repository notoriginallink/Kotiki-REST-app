package ru.tolstov.lab1.commands.banks;

import picocli.CommandLine;

@CommandLine.Command(
        name = "list",
        aliases = {"ls", "show"},
        mixinStandardHelpOptions = true,
        description = "Prints list of all banks"
)
public class BankListCommand implements Runnable {
    @CommandLine.ParentCommand
    private BankCommand mainCmd;

    @Override
    public void run() {
        mainCmd.getCentralBankService().getBanks().forEach(System.out::println);
    }
}
