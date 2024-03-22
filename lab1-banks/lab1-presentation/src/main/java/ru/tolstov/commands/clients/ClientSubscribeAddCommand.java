package ru.tolstov.commands.clients;

import picocli.CommandLine;
import ru.tolstov.ServiceOperationResult;

@CommandLine.Command(
        name = "add",
        description = "Subscribes client to bank notifications",
        mixinStandardHelpOptions = true
)
public class ClientSubscribeAddCommand implements Runnable {
    @CommandLine.ParentCommand
    private ClientSubscribeCommand parentSubCmd;

    @Override
    public void run() {
        var mainCmd = parentSubCmd.getMainCmd();
        if (mainCmd.call() != 0)
            return;

        var bank = mainCmd.getBank();
        var client = mainCmd.getClient();

        if (parentSubCmd.getBankService().hasSubscription(bank, client)) {
            System.out.println("Client is already subscribed");
            return;
        }

        var result = parentSubCmd.getBankService().subscribeClient(bank, client);
        if (result instanceof ServiceOperationResult.Fail fail)
            System.out.println(fail.getMessage());
        else
            System.out.println("Subscribed successfully");
    }
}
