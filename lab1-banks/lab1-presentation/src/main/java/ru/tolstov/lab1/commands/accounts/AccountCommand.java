package ru.tolstov.lab1.commands.accounts;

import lombok.Getter;
import picocli.CommandLine;
import ru.tolstov.lab1.accounts.Account;
import ru.tolstov.lab1.accounts.AccountOperationService;
import ru.tolstov.lab1.accounts.AccountService;

import java.util.UUID;
import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "account",
        aliases = {"acc"},
        description = "Provides interface to work with accounts",
        mixinStandardHelpOptions = true
)
@Getter
public class AccountCommand implements Callable<Integer> {
    private final AccountOperationService accountOperationService;
    private final AccountService accountService;

    @CommandLine.Parameters(
            paramLabel = "ID",
            description = "UUID of accounts"
    )
    private UUID id;
    private Account account;

    public AccountCommand(AccountOperationService accountOperationService, AccountService accountService) {
        this.accountOperationService = accountOperationService;
        this.accountService = accountService;
    }

    @Override
    public Integer call() {
        var account = accountService.getAccountById(id);
        if (account.isEmpty()) {
            System.out.println("Account with this ID does not exists");
            return 1;
        }

        this.account = account.get();

        return 0;
    }
}
