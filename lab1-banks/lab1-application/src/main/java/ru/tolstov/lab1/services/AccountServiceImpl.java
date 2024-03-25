package ru.tolstov.lab1.services;

import ru.tolstov.lab1.Bank;
import ru.tolstov.lab1.Client;
import ru.tolstov.lab1.Money;
import ru.tolstov.lab1.TransactionService;
import ru.tolstov.lab1.accounts.*;
import ru.tolstov.lab1.builders.AccountBuilder;
import ru.tolstov.lab1.builders.CreditAccountFactory;
import ru.tolstov.lab1.builders.DebitAccountFactory;
import ru.tolstov.lab1.builders.DepositAccountFactory;
import ru.tolstov.lab1.repositories.AccountRepository;
import ru.tolstov.lab1.repositories.BankRepository;
import ru.tolstov.lab1.transactions.TransactionStatus;

import java.util.Optional;
import java.util.UUID;

public class AccountServiceImpl implements AccountService {
    private final AccountBuilder builder;
    private final AccountRepository accountRepository;
    private final BankRepository bankRepository;
    private final TransactionService transactionService;
    public AccountServiceImpl(AccountRepository accountRepository, BankRepository bankRepository, TransactionService transactionService) {
        builder = new AccountBuilder();
        this.accountRepository = accountRepository;
        this.bankRepository = bankRepository;
        this.transactionService = transactionService;
    }

    @Override
    public DebitAccount createDebitAccount(Bank bank, Client client) {
        var account = builder
                .setFactory(new DebitAccountFactory())
                .setBank(bank)
                .setClient(client)
                .build();

        bank.getAccounts().add(account);
        accountRepository.addAccount(account);
        bankRepository.addAccountToBank(bank, account);

        return (DebitAccount) account;
    }

    @Override
    public CreditAccount createCreditAccount(Bank bank, Client client) {
        var account = builder
                .setFactory(new CreditAccountFactory())
                .setBank(bank)
                .setClient(client)
                .build();

        bank.getAccounts().add(account);
        accountRepository.addAccount(account);
        bankRepository.addAccountToBank(bank, account);

        return (CreditAccount) account;
    }

    @Override
    public DepositAccount createDepositAccount(Bank bank, Client client, int durationMonth, double startsBalance) {
        var balance = new Money(startsBalance);
        var account = builder
                .setFactory(new DepositAccountFactory(durationMonth, balance))
                .setBank(bank)
                .setClient(client)
                .build();

        bank.getAccounts().add(account);
        accountRepository.addAccount(account);
        transactionService.createDepositTransaction(CentralBankServiceImpl.currentDate(), account, balance, TransactionStatus.SYSTEM);
        bankRepository.addAccountToBank(bank, account);

        return (DepositAccount) account;
    }

    @Override
    public Optional<Account> getAccountById(UUID id) {
        return accountRepository.getAccountById(id);
    }

}
