package ru.tolstov;

import ru.tolstov.accounts.Account;
import ru.tolstov.repositories.BankRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryBankRepository implements BankRepository {
    private final List<Bank> banks;
    public InMemoryBankRepository() {
        banks = new ArrayList<>();
    }

    @Override
    public void registerBank(Bank bank) {
        banks.add(bank);
    }

    @Override
    public Optional<Bank> findBankByName(String name) {
        return banks.stream()
                .filter(b -> b.getName().equals(name))
                .findAny();
    }

    @Override
    public void addAccountToBank(Bank bank, Account account) {
        Bank bank1 = banks.stream()
                .filter(b -> b.equals(bank))
                .findAny()
                .orElseThrow(() -> new RuntimeException("No such bank in repository"));

        bank1.getAccounts().add(account);
    }

    @Override
    public List<Account> getAllAccounts(Bank bank) {
        Bank bank1 = banks.stream()
                .filter(b -> b.equals(bank))
                .findAny()
                .orElseThrow(() -> new RuntimeException("No such bank in repository"));

        return bank1.getAccounts();
    }

    @Override
    public List<Bank> getAllBanks() {
        return banks;
    }

    @Override
    public void updateSuspendedLimit(Bank bank, Money limit) {
        Bank bankInMemory = banks.stream()
                .filter(b -> b.equals(bank))
                .findAny()
                .orElseThrow(() -> new RuntimeException("No such bank in repository"));

        bankInMemory.getSuspendedLimit().setValue(limit.getValue());

    }

    @Override
    public void updateCreditConditions(Bank bank, Money limit, Money commission) {
        Bank bankInMemory = banks.stream()
                .filter(b -> b.equals(bank))
                .findAny()
                .orElseThrow(() -> new RuntimeException("No such bank in repository"));

        if (limit != null)
            bankInMemory.getCreditLimit().setValue(limit.getValue());

        if (commission != null)
            bankInMemory.getCreditCommissionValue().setValue(commission.getValue());
    }

    @Override
    public void updateDebitConditions(Bank bank, double debitPaymentPercent) {
        Bank bankInMemory = banks.stream()
                .filter(b -> b.equals(bank))
                .findAny()
                .orElseThrow(() -> new RuntimeException("No such bank in repository"));

        bankInMemory.setDebitPaymentPercent(debitPaymentPercent);
    }

    @Override
    public void addDepositPercent(Bank bank, Money value, double percent) {
        Bank bankInMemory = banks.stream()
                .filter(b -> b.equals(bank))
                .findAny()
                .orElseThrow(() -> new RuntimeException("No such bank in repository"));

        bankInMemory.getDepositPaymentPercents().add(value.getValue(), percent);
    }

    @Override
    public void removeDepositPercent(Bank bank, Money value, double percent) {
        Bank bankInMemory = banks.stream()
                .filter(b -> b.equals(bank))
                .findAny()
                .orElseThrow(() -> new RuntimeException("No such bank in repository"));

        bankInMemory.getDepositPaymentPercents().remove(value.getValue());
    }

    @Override
    public void registerSubscriber(Bank bank, Client client) {
        Bank bankInMemory = banks.stream()
                .filter(b -> b.equals(bank))
                .findAny()
                .orElseThrow(() -> new RuntimeException("No such bank in repository"));

        bankInMemory.getSubscribers().add(client);
    }

    @Override
    public void deleteSubscriber(Bank bank, Client client) {
        Bank bankInMemory = banks.stream()
                .filter(b -> b.equals(bank))
                .findAny()
                .orElseThrow(() -> new RuntimeException("No such bank in repository"));

        bankInMemory.getSubscribers().remove(client);
    }

    @Override
    public List<Client> getSubscribers(Bank bank) {
        Bank bankInMemory = banks.stream()
                .filter(b -> b.getName().equals(bank.getName()))
                .findAny()
                .orElseThrow(() -> new RuntimeException("No such bank in repository"));

        return bankInMemory.getSubscribers();
    }
}
