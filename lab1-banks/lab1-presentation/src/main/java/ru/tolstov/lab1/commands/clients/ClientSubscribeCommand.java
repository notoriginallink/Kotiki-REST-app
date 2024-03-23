package ru.tolstov.lab1.commands.clients;

import lombok.Getter;
import picocli.CommandLine;
import ru.tolstov.lab1.banks.BankService;

@CommandLine.Command(
        name = "subscribe",
        aliases = {"sub"},
        description = "returns status of subscription",
        mixinStandardHelpOptions = true
)
@Getter
public class ClientSubscribeCommand implements Runnable {
    private final BankService bankService;

    @CommandLine.ParentCommand
    private ClientCommand mainCmd;

    public ClientSubscribeCommand(BankService bankService) {
        this.bankService = bankService;
    }

    @Override
    public void run() {
        if (mainCmd.call() != 0)
            return;

        var bank = mainCmd.getBank();
        var client = mainCmd.getClient();

        if (bankService.hasSubscription(bank, client))
            System.out.println("Client is subscribed to notifications");
        else
            System.out.println("Client is NOT subscribed to notification");
    }
}
