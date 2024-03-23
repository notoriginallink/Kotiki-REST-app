package ru.tolstov.lab1.commands.accounts;

import picocli.CommandLine;
import ru.tolstov.lab1.ServiceOperationResult;
import ru.tolstov.lab1.banks.CentralBankService;

import java.util.UUID;

@CommandLine.Command(
        name = "transfer",
        aliases = {"tr"},
        description = "Transfers money from one account to another. If accounts are in different banks, commission is taken",
        mixinStandardHelpOptions = true
)
public class AccountTransferCommand implements Runnable {
    private final CentralBankService centralBankService;
    @CommandLine.ParentCommand
    private AccountCommand mainCmd;
    @CommandLine.Parameters(
            paramLabel = "receiverID",
            description = "UUID of account which receives money"
    )
    private UUID receiverID;

    @CommandLine.Parameters(
            paramLabel = "Value",
            description = "Amount of money to deposit"
    )
    private double value;

    public AccountTransferCommand(CentralBankService centralBankService) {
        this.centralBankService = centralBankService;
    }

    @Override
    public void run() {
        if (mainCmd.call() != 0)
            return;

        var account = mainCmd.getAccount();

        var receiver = mainCmd.getAccountService().getAccountById(receiverID);
        if (receiver.isEmpty()) {
            System.out.printf("Account with this ID: %s does not exists%n", receiverID);
            return;
        }

        var senderBank = centralBankService.getBankOfAccount(account);
        var receiverBank = centralBankService.getBankOfAccount(receiver.get());

        var result = centralBankService.transferBetweenBanks(senderBank, account, receiverBank, receiver.get(), value);

        if (result instanceof ServiceOperationResult.Fail fail)
            System.out.println(fail.getMessage());
        else
            System.out.println("Transfer success");
    }
}
