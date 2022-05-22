package com.miraoui.ebankingbackend.services;

import com.miraoui.ebankingbackend.entities.BankAccount;
import com.miraoui.ebankingbackend.entities.CurrentAccount;
import com.miraoui.ebankingbackend.entities.Customer;
import com.miraoui.ebankingbackend.entities.SavingAccount;
import com.miraoui.ebankingbackend.exceptions.BalanceNotSufficientException;
import com.miraoui.ebankingbackend.exceptions.BankAccountNotFoundException;
import com.miraoui.ebankingbackend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    Customer saveCustomer(Customer customer);
    CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    List<Customer> listCustomers();
    List<BankAccount> bankAccountList();
    BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException;
    void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;
    void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;
}
