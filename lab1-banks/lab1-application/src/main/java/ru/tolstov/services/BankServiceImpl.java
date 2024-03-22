package ru.tolstov.services;

import ru.tolstov.*;
import ru.tolstov.accounts.Account;
import ru.tolstov.accounts.CreditAccount;
import ru.tolstov.accounts.DebitAccount;
import ru.tolstov.banks.BankService;
import ru.tolstov.repositories.BankRepository;

import java.util.Optional;

public class BankServiceImpl implements BankService {
    private final BankRepository bankRepository;
    private final NotificationService notificationService;
    public BankServiceImpl(BankRepository bankRepository,  NotificationService notificationService) {
        this.bankRepository = bankRepository;
        this.notificationService = notificationService;
    }

    @Override
    public ServiceOperationResult updateSuspendedLimit(Bank bank, double limit) {
        if (limit < 0)
            return new ServiceOperationResult.Fail("Suspended limit can't be less 0");

        bank.getSuspendedLimit().setValue(limit);
        bankRepository.updateSuspendedLimit(bank, new Money(limit));

        var notification = notificationService
                .createNotification("Limits for suspended accounts were updated. Current limit: %s".formatted(limit));

        bankRepository.getSubscribers(bank).stream()
                .filter(x -> x.getAddress() == null || x.getPassportID() == null)
                .forEach(s -> notificationService.sendNotification(s, notification));

        return new ServiceOperationResult.Success();
    }

    @Override
    public ServiceOperationResult updateCreditConditions(Bank bank, double limit, double commission) {
        if (limit < 0)
            return new ServiceOperationResult.Fail("Credit limit can't be less 0");
        else
            bank.getCreditLimit().setValue(limit);

        if (commission < 0)
            return new ServiceOperationResult.Fail("Credit commission can't be less 0");
        else
            bank.getCreditCommissionValue().setValue(commission);

        var notification = notificationService
                .createNotification("Credit account conditions were updated. Current conditions: limit=%s, commission=%s"
                        .formatted(bank.getCreditLimit(), bank.getCreditCommissionValue()));

        var accounts = bankRepository.getAllAccounts(bank);
        for (var subscriber : bankRepository.getSubscribers(bank)) {
            Optional<Account> creditAccount = accounts.stream()
                    .filter(a -> a.getClient().equals(subscriber) && a instanceof CreditAccount)
                    .findAny();

            creditAccount.ifPresent(account -> notificationService.sendNotification(account.getClient(), notification));
        }

        bankRepository.updateCreditConditions(bank, new Money(limit), new Money(commission));

        return new ServiceOperationResult.Success();
    }

    @Override
    public ServiceOperationResult updateDebitConditions(Bank bank, double debitPaymentPercent) {
        bank.setDebitPaymentPercent(debitPaymentPercent);

        var notification = notificationService
                .createNotification("Debit payment percent was updated. Current percent: %s".formatted(debitPaymentPercent));

        var accounts = bankRepository.getAllAccounts(bank);
        for (var subscriber : bankRepository.getSubscribers(bank)) {
            Optional<Account> debitAccount = accounts.stream()
                    .filter(a -> a.getClient().equals(subscriber) && a instanceof DebitAccount)
                    .findAny();

            debitAccount.ifPresent(account -> notificationService.sendNotification(account.getClient(), notification));
        }

        bankRepository.updateDebitConditions(bank, debitPaymentPercent);

        return new ServiceOperationResult.Success();
    }

    @Override
    public ServiceOperationResult addDepositPercent(Bank bank, double value, double percent) {
        if (value < 0)
            return new ServiceOperationResult.Fail("Value can't be less 0");

        if (percent < 0)
            return new ServiceOperationResult.Fail("Percent can't be less 0");

        bank.getDepositPaymentPercents().add(value, percent);
        bankRepository.addDepositPercent(bank, new Money(value), percent);

        return new ServiceOperationResult.Success();
    }

    @Override
    public ServiceOperationResult removeDepositPercent(Bank bank, double value, double percent) {
        if (value < 0)
            return new ServiceOperationResult.Fail("Value can't be less 0");

        if (percent < 0)
            return new ServiceOperationResult.Fail("Percent can't be less 0");

        if (!bank.getDepositPaymentPercents().remove(value))
            return new ServiceOperationResult.Fail("These conditions weren't found");

        bankRepository.removeDepositPercent(bank, new Money(value), percent);

        return new ServiceOperationResult.Success();
    }

    @Override
    public ServiceOperationResult subscribeClient(Bank bank, Client client) {
        bank.getSubscribers().add(client);
        bankRepository.registerSubscriber(bank, client);

        return new ServiceOperationResult.Success();
    }

    @Override
    public ServiceOperationResult unsubscribeClient(Bank bank, Client client) {
        bank.getSubscribers().remove(client);
        bankRepository.deleteSubscriber(bank, client);

        return new ServiceOperationResult.Success();
    }

    @Override
    public boolean hasSubscription(Bank bank, Client client) {
        return bankRepository.getSubscribers(bank).contains(client);
    }
}
