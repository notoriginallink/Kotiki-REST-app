package ru.tolstov.lab1.commands.clients;

import picocli.CommandLine;
import ru.tolstov.lab1.ServiceOperationResult;

@CommandLine.Command(
        name = "update",
        description = "Allows to update clients address or passport information",
        mixinStandardHelpOptions = true
)
public class ClientUpdateCommand implements Runnable {
    @CommandLine.ParentCommand
    private ClientCommand mainCmd;
    @CommandLine.Option(
            names = {"-a", "--address"},
            description = "Client's address"
    )
    private String address;
    @CommandLine.Option(
            names = {"-p", "--passport"},
            description = "Client's passportID"
    )
    private String passportID;

    @Override
    public void run() {
        if (address == null && passportID == null) {
            System.out.println("No data to update given. Type -h or --help for help");
            return;
        }

        int exitCode = mainCmd.call();
        if (exitCode != 0)
            return;

        var client = mainCmd.getClient();

        ServiceOperationResult result = null;
        if (address != null)
            result = mainCmd.getClientService().updateAddress(client, address);

        if (result instanceof ServiceOperationResult.Fail fail) {
            System.out.println(fail.getMessage());
            return;
        }

        if (passportID != null)
            result = mainCmd.getClientService().updatePassportID(client, passportID);

        if (result instanceof ServiceOperationResult.Fail fail) {
            System.out.println(fail.getMessage());
            return;
        }

        System.out.println("Data updated");
    }
}
