package ru.tolstov.commands.create;

import picocli.CommandLine;
import ru.tolstov.accounts.Account;
import ru.tolstov.accounts.AccountService;
import ru.tolstov.banks.CentralBankService;
import ru.tolstov.clients.ClientService;

@CommandLine.Command(
        name = "account",
        aliases = {"acc", "a"},
        description = "Creates new account for client",
        mixinStandardHelpOptions = true
)
public class CreateAccountCommand implements Runnable {
    private final CentralBankService centralBankService;
    private final AccountService accountService;
    private final ClientService clientService;
    @CommandLine.Parameters(
            paramLabel = "account type",
            description = "Accepts one of three options: ${COMPLETION-CANDIDATES}"
    )
    private AccountType type;
    @CommandLine.Option(
            names = {"-b", "--bank"},
            description = "Name of the bank",
            required = true
    )
    private String bankName;

    @CommandLine.Option(
            names = {"-p", "--phone-number"},
            description = "Client's phone number",
            required = true
    )
    private String phoneNumber;

    @CommandLine.ArgGroup(exclusive = false)
    DepositCreateOptions depositCreateOptions;

    public CreateAccountCommand(CentralBankService centralBankService, AccountService accountService, ClientService clientService) {
        this.centralBankService = centralBankService;
        this.accountService = accountService;
        this.clientService = clientService;
    }

    @Override
    public void run() {
        var bank = centralBankService.getBankByName(bankName);
        if (bank.isEmpty()) {
            System.out.println("Bank with this name does not exists");
            return;
        }

        var client = clientService.getClientByPhone(bank.get(), phoneNumber);
        if (client.isEmpty()) {
            System.out.println("Client with this phone does not exists");
            return;
        }

        Account account = null;
        switch (type) {
            case DEBIT -> account = accountService.createDebitAccount(bank.get(), client.get());
            case CREDIT -> account = accountService.createCreditAccount(bank.get(), client.get());
            case DEPOSIT -> account = accountService.createDepositAccount(bank.get(), client.get(), depositCreateOptions.durationMonth, depositCreateOptions.startBalance);
            default -> System.out.println("Unknown account type");
        }
        if (account != null)
            System.out.printf("%s account ID: %s created successfully%n", type, account.getId());
    }

    private enum AccountType {
        DEBIT,
        CREDIT,
        DEPOSIT
    }

    static class DepositCreateOptions {
        @CommandLine.Option(
                names = {"-sb", "--start-balance"},
                description = "Start balance of DEPOSIT account. If passed and account's type is not DEPOSIT, does nothing",
                defaultValue = "10000",
                required = true
        )
        private double startBalance;
        @CommandLine.Option(
                names = {"-dm", "--duration-months"},
                description = "Duration of DEPOSIT account. If passed abd account's type is not DEPOSIT, does nothing",
                defaultValue = "3",
                required = true
        )
        private int durationMonth;
    }
}
