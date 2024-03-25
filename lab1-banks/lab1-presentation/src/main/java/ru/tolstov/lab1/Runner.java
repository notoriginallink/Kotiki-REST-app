package ru.tolstov.lab1;

import lombok.Builder;
import picocli.CommandLine;
import ru.tolstov.lab1.accounts.AccountOperationService;
import ru.tolstov.lab1.accounts.AccountService;
import ru.tolstov.lab1.banks.BankService;
import ru.tolstov.lab1.banks.CentralBankService;
import ru.tolstov.lab1.clients.ClientService;
import ru.tolstov.lab1.commands.ExitCommand;
import ru.tolstov.lab1.commands.MainCommand;
import ru.tolstov.lab1.commands.accounts.*;
import ru.tolstov.lab1.commands.banks.BankCommand;
import ru.tolstov.lab1.commands.banks.BankInfoCommand;
import ru.tolstov.lab1.commands.banks.BankListCommand;
import ru.tolstov.lab1.commands.banks.BankUpdateCommand;
import ru.tolstov.lab1.commands.clients.*;
import ru.tolstov.lab1.commands.create.CreateAccountCommand;
import ru.tolstov.lab1.commands.create.CreateBankCommand;
import ru.tolstov.lab1.commands.create.CreateClientCommand;
import ru.tolstov.lab1.commands.create.CreateCommand;
import ru.tolstov.lab1.commands.time.TimeCommand;
import ru.tolstov.lab1.commands.time.TimeCurrentCommand;
import ru.tolstov.lab1.commands.time.TimeSkipCommand;
import ru.tolstov.lab1.commands.transactions.TransactionCancelCommand;
import ru.tolstov.lab1.commands.transactions.TransactionCommand;
import ru.tolstov.lab1.commands.transactions.TransactionInfoCommand;

import java.util.Scanner;

@Builder
public class Runner {
    private final NotificationService notificationService;
    private final TransactionService transactionService;
    private final ClientService clientService;
    private final AccountService accountService;
    private final AccountOperationService accountOperationService;
    private final CentralBankService centralBankService;
    private final BankService bankService;
    private CommandLine mainCmd;

    public void start() {
        var scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            if (!s.isEmpty())
                mainCmd.execute(s.split(" "));
        }
    }
    public void init() {
        var createCmd = new CommandLine(new CreateCommand())
                .addSubcommand(new CreateBankCommand(centralBankService))
                .addSubcommand(new CreateClientCommand(clientService, centralBankService))
                .addSubcommand(new CreateAccountCommand(centralBankService, accountService, clientService));

        var bankCmd = new CommandLine(new BankCommand(centralBankService))
                .addSubcommand(new BankListCommand())
                .addSubcommand(new BankInfoCommand())
                .addSubcommand(new BankUpdateCommand(bankService));

        var subscribeCmd = new CommandLine(new ClientSubscribeCommand(bankService))
                .addSubcommand(new ClientSubscribeAddCommand())
                .addSubcommand(new ClientSubscribeCancelCommand());

        var clientCmd = new CommandLine(new ClientCommand(centralBankService, clientService))
                .addSubcommand(new ClientUpdateCommand())
                .addSubcommand(subscribeCmd)
                .addSubcommand(new ClientAccountsCommand());

        var transactionCmd = new CommandLine(new TransactionCommand(transactionService))
                .addSubcommand(new TransactionInfoCommand())
                .addSubcommand(new TransactionCancelCommand());

        var accountCmd = new CommandLine(new AccountCommand(accountOperationService, accountService))
                .addSubcommand(new AccountBalanceCommand())
                .addSubcommand(new AccountDepositCommand())
                .addSubcommand(new AccountWithdrawCommand())
                .addSubcommand(new AccountTransferCommand(centralBankService))
                .addSubcommand(new AccountTransactionListCommand(transactionService))
                .addSubcommand(new AccountPeekCommand());

        var timeCmd = new CommandLine(new TimeCommand(centralBankService))
                .addSubcommand(new TimeCurrentCommand())
                .addSubcommand(new TimeSkipCommand());

        var exitCmd = new CommandLine(new ExitCommand());

        mainCmd = new CommandLine(new MainCommand())
                .addSubcommand(createCmd)
                .addSubcommand(bankCmd)
                .addSubcommand(clientCmd)
                .addSubcommand(accountCmd)
                .addSubcommand(transactionCmd)
                .addSubcommand(timeCmd)
                .addSubcommand(exitCmd);
    }

    public void execute(String prompt) {
        mainCmd.execute(prompt.split(" "));
    }
}