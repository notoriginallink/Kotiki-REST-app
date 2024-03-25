package ru.tolstov.lab1.commands.clients;

import lombok.Getter;
import picocli.CommandLine;
import ru.tolstov.lab1.Bank;
import ru.tolstov.lab1.Client;
import ru.tolstov.lab1.banks.CentralBankService;
import ru.tolstov.lab1.clients.ClientService;

import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "client",
        description = "Provides interface to work with clients",
        mixinStandardHelpOptions = true
)
@Getter
public class ClientCommand implements Callable<Integer> {
    private final CentralBankService centralBankService;
    private final ClientService clientService;
    @CommandLine.Parameters(
            paramLabel = "Bank name",
            description = "Name of bank"
    )
    private String bankName;
    @CommandLine.Parameters(
            paramLabel = "phone number",
            description = "Client's phone number"
    )
    private String phoneNumber;
    private Bank bank;
    private Client client;

    public ClientCommand(CentralBankService centralBankService, ClientService clientService) {
        this.centralBankService = centralBankService;
        this.clientService = clientService;
    }

    @Override
    public Integer call() {
        var bank = centralBankService.getBankByName(bankName);
        if (bank.isEmpty()) {
            System.out.println("Bank with this name does not exists");
            return 1;
        }

        var client = clientService.getClientByPhone(bank.get(), phoneNumber);
        if (client.isEmpty()) {
            System.out.println("Client with this phone does not exists");
            return 1;
        }

        this.bank = bank.get();
        this.client = client.get();

        return 0;
    }
}
