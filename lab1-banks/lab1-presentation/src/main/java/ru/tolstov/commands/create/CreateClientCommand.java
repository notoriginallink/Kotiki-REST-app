package ru.tolstov.commands.create;

import picocli.CommandLine;
import ru.tolstov.ServiceOperationResult;
import ru.tolstov.banks.CentralBankService;
import ru.tolstov.clients.ClientService;

@CommandLine.Command(
        name = "client",
        aliases = {"cl"},
        description = "Creates a new client in bank",
        mixinStandardHelpOptions = true
)
public class CreateClientCommand implements Runnable {
    private final ClientService clientService;
    private final CentralBankService centralBankService;

    @CommandLine.Parameters(
            description = "Name of bank",
            paramLabel = "Bank name"
    )
    private String bankName;
    @CommandLine.Parameters(
            paramLabel = "first name",
            description = "Clients first name"
    )
    private String firstName;
    @CommandLine.Parameters(
            paramLabel = "last name",
            description = "Clients last name"
    )
    private String lastName;
    @CommandLine.Parameters(
            paramLabel = "phone number",
            description = "Unique phone number"
    )
    private String phoneNumber;
    @CommandLine.Option(
            names = {"-a", "--address"},
            description = "Clients address, optional parameter.\n If not filled, " +
                    "all account of the client will be suspended until address information filled"
    )
    private String address;
    @CommandLine.Option(
            names = {"-p", "--passport"},
            description = "Clients passport, optional parameter. If not filled, " +
                    "all accounts of the client will be suspended until passport information filled"
    )
    private String passportID;

    public CreateClientCommand(ClientService clientService, CentralBankService centralBankService) {
        this.clientService = clientService;
        this.centralBankService = centralBankService;
    }

    @Override
    public void run() {
        var bank = centralBankService.getBankByName(bankName);
        if (bank.isEmpty()) {
            System.out.println("Bank with this name does not exist");
            return;
        }

        var result = clientService.createClient(phoneNumber, firstName, lastName, address, passportID, bank.get());
        if (result instanceof ServiceOperationResult.Fail fail)
            System.out.println(fail.getMessage());
        else
            System.out.printf("Client %s %s with phone %s was successfully registered in bank %s%n",
                    firstName, lastName, phoneNumber,  bankName);
    }
}
