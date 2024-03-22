package ru.tolstov.banks;

import ru.tolstov.Bank;
import ru.tolstov.ServiceOperationResult;
import ru.tolstov.accounts.Account;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
/**
 * Service for bank management. It allows creation and registration of new banks.
 * Controls banks and provides functionality for banks to interact with each other.
 * (For example transfer operations must be realised with its help)
 * It also provides method to skip time, and notifies all banks that it's time to make payments
 * or take commissions.
 * **/
public interface CentralBankService {
    /**
     * Creates new bank and registers it
     * @param name name of the bank being created. Is unique ID
     * @param suspendedLimit maximum value that can be withdrawn from suspended account
     * @param debitPaymentPercent value of monthly payment for devit accounts (in percents)
     * @param creditLimit maximum value that can be loaned without paying commission
     * @param creditCommissionValue size of a commission for credit accounts
     * @return result of bank creation, which is Fail and contains message of error or is Success
     * **/
    ServiceOperationResult createBank(String name, double suspendedLimit, double creditLimit, double debitPaymentPercent, double creditCommissionValue);
    /**
     * Transfers money from one account to another, it necessary to call this method even if accounts are from the same bank
     * @param fromBank bank of the sender account
     * @param fromAccount sender account
     * @param toBank bank of the receiver account
     * @param toAccount receiver account
     * @param value value to be transferred from sender to receiver
     * @return result of the transfer operation, which is Fail and contains message of error or is Success
     * **/
    ServiceOperationResult transferBetweenBanks(Bank fromBank, Account fromAccount, Bank toBank, Account toAccount, double value);
    /**
     * Find the bank to which belongs account
     * @param account account which bank is needed to get
     * @return Bank
     * **/
    Bank getBankOfAccount(Account account);
    /**
     * Gets all banks that were created and registered
     * @return returns List<> of all registered banks
     * **/
    List<Bank> getBanks();
    /**
     * Finds bank by its name, i.e. name is an unique ID
     * @param name name of the bank
     * @return Optional<Bank> with this name, which is empty if bank does not exist
     * **/
    Optional<Bank> getBankByName(String name);
    /**
     * Changes the current date by skipping some days. All banks that a registered receives notification in order to
     * make payment or take commissions if its needed
     * @param amount amount of days to be skipped
     * **/
    void skipDays(int amount);
    /**
     * Returns current date
     * @return Calendar instance
     * **/
    Calendar getCurrentDate();
}
