package ru.tolstov.services;

import ru.tolstov.*;
import ru.tolstov.accounts.Account;
import ru.tolstov.accounts.AccountOperationService;
import ru.tolstov.banks.CentralBankService;
import ru.tolstov.repositories.BankRepository;
import ru.tolstov.transactions.TransactionStatus;

import java.util.*;

public class CentralBankServiceImpl implements CentralBankService {
    private final BankRepository bankRepository;
    private final TransactionService transactionService;
    private final AccountOperationService accountOperationService;
    private final static Calendar date = new GregorianCalendar(2023, Calendar.MARCH,1);
    private final Money transferCommission = new Money(10);

    public CentralBankServiceImpl(BankRepository bankRepository, TransactionService transactionService, AccountOperationService accountOperationService) {
        this.bankRepository = bankRepository;
        this.transactionService = transactionService;
        this.accountOperationService = accountOperationService;
    }

    public static GregorianCalendar currentDate() {
        return (GregorianCalendar) date.clone();
    }
    @Override
    public Calendar getCurrentDate() {
        return CentralBankServiceImpl.currentDate();
    }

    @Override
    public ServiceOperationResult createBank(String name, double suspendedLimit, double creditLimit, double debitPaymentPercent, double creditCommissionValue) {
        var bankInRepository = bankRepository.getAllBanks().stream()
                .filter(b -> b.getName().equals(name))
                .findAny();

        if (bankInRepository.isPresent())
            return new ServiceOperationResult.Fail("Bank with this name already exists");

        Bank bank = new Bank(
                name,
                new ArrayList<>(),
                new ArrayList<>(),
                new Money(suspendedLimit),
                new Money(creditLimit),
                debitPaymentPercent,
                new Money(creditCommissionValue),
                new DepositPercentageManager(),
                new ArrayList<>()
        );

        bankRepository.registerBank(bank);

        return new ServiceOperationResult.Success();
    }

    @Override
    public ServiceOperationResult transferBetweenBanks(Bank fromBank, Account fromAccount, Bank toBank, Account toAccount, double value) {
        var result = accountOperationService.transfer(fromAccount, toAccount, value);

        if (result instanceof ServiceOperationResult.Fail)
            return result;

        if (!fromBank.equals(toBank)) {
            fromAccount.getBalance().decrease(transferCommission);
            transactionService.createWithdrawTransaction(getCurrentDate(), fromAccount, transferCommission, TransactionStatus.SYSTEM);
        }

        return result;
    }

    @Override
    public Bank getBankOfAccount(Account account) {
        return bankRepository.getAllBanks().stream()
                .filter(b -> b.getAccounts().contains(account))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Account %s does not belong to any bank".formatted(account.getId())));
    }

    @Override
    public List<Bank> getBanks() {
        return bankRepository.getAllBanks();
    }

    @Override
    public Optional<Bank> getBankByName(String name) {
        return bankRepository.findBankByName(name);
    }

    @Override
    public void skipDays(int amount) {
        if (amount < 1)
            return;

        for (var bank : bankRepository.getAllBanks())
            for (var account : bankRepository.getAllAccounts(bank))
                accountOperationService.skipDays(account, amount);

        date.add(Calendar.DAY_OF_MONTH, amount);
    }
}
