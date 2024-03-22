package ru.tolstov.services;

import ru.tolstov.Bank;
import ru.tolstov.Client;
import ru.tolstov.Money;
import ru.tolstov.TransactionService;
import ru.tolstov.accounts.*;
import ru.tolstov.builders.AccountBuilder;
import ru.tolstov.builders.CreditAccountFactory;
import ru.tolstov.builders.DebitAccountFactory;
import ru.tolstov.builders.DepositAccountFactory;
import ru.tolstov.repositories.AccountRepository;
import ru.tolstov.repositories.BankRepository;
import ru.tolstov.transactions.TransactionStatus;

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
