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
        banks.stream()
                .filter(b -> b.equals(bank))
                .findAny()
                .orElseThrow(() -> new RuntimeException("No such bank in repository"));
    }

    @Override
    public List<Account> getAllAccounts(Bank bank) {
        banks.stream()
                .filter(b -> b.equals(bank))
                .findAny()
                .orElseThrow(() -> new RuntimeException("No such bank in repository"));

        return bank.getAccounts();
    }

    @Override
    public List<Bank> getAllBanks() {
        return banks;
    }

    @Override
    public void updateSuspendedLimit(Bank bank, Money limit) {
        banks.stream()
                .filter(b -> b.equals(bank))
                .findAny()
                .orElseThrow(() -> new RuntimeException("No such bank in repository"));
    }

    @Override
    public void updateCreditConditions(Bank bank, Money limit, Money commission) {
        banks.stream()
                .filter(b -> b.equals(bank))
                .findAny()
                .orElseThrow(() -> new RuntimeException("No such bank in repository"));

    }

    @Override
    public void updateDebitConditions(Bank bank, double debitPaymentPercent) {
        banks.stream()
                .filter(b -> b.equals(bank))
                .findAny()
                .orElseThrow(() -> new RuntimeException("No such bank in repository"));
    }

    @Override
    public void addDepositPercent(Bank bank, Money value, double percent) {
        banks.stream()
                .filter(b -> b.equals(bank))
                .findAny()
                .orElseThrow(() -> new RuntimeException("No such bank in repository"));
    }

    @Override
    public void removeDepositPercent(Bank bank, Money value, double percent) {
        banks.stream()
                .filter(b -> b.equals(bank))
                .findAny()
                .orElseThrow(() -> new RuntimeException("No such bank in repository"));
    }

    @Override
    public void registerSubscriber(Bank bank, Client client) {
        banks.stream()
                .filter(b -> b.equals(bank))
                .findAny()
                .orElseThrow(() -> new RuntimeException("No such bank in repository"));
    }

    @Override
    public void deleteSubscriber(Bank bank, Client client) {
        banks.stream()
                .filter(b -> b.equals(bank))
                .findAny()
                .orElseThrow(() -> new RuntimeException("No such bank in repository"));
    }

    @Override
    public List<Client> getSubscribers(Bank bank) {
        banks.stream()
                .filter(b -> b.getName().equals(bank.getName()))
                .findAny()
                .orElseThrow(() -> new RuntimeException("No such bank in repository"));

        return bank.getSubscribers();
    }
}
