package ru.tolstov.lab1.commands.clients;

import picocli.CommandLine;

@CommandLine.Command(
        name = "accounts",
        aliases = {"accs"},
        description = "Lists all client's accounts",
        mixinStandardHelpOptions = true
)
public class ClientAccountsCommand implements Runnable {
    @CommandLine.ParentCommand
    private ClientCommand mainCmd;

    @Override
    public void run() {
        if (mainCmd.call() != 0)
            return;

        var bank = mainCmd.getBank();
        var client = mainCmd.getClient();

        mainCmd.getClientService().getClientAccounts(bank, client).forEach(System.out::println);
    }
}
