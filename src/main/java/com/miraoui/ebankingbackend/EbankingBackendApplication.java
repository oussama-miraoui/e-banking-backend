package com.miraoui.ebankingbackend;

import com.miraoui.ebankingbackend.entities.*;
import com.miraoui.ebankingbackend.enums.AccountStatus;
import com.miraoui.ebankingbackend.enums.OperationType;
import com.miraoui.ebankingbackend.repositories.AccountOperationRepository;
import com.miraoui.ebankingbackend.repositories.BankAccountRepository;
import com.miraoui.ebankingbackend.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankingBackendApplication.class, args);
    }


    @Bean
    CommandLineRunner commandLineRunner(BankAccountRepository bankAccountRepository){
        return args -> {
            BankAccount bankAccount= bankAccountRepository.findById("eac49fdd-bc66-4080-9c99-c3f1d6c2f100").orElse(null);

            if(bankAccount != null) {
                System.out.println("***************");
                System.out.println(bankAccount.getId());
                System.out.println(bankAccount.getBalance());
                System.out.println(bankAccount.getStatus());
                System.out.println(bankAccount.getCreatedAt());
                System.out.println(bankAccount.getCustomer().getNom());
                System.out.println(bankAccount.getClass().getSimpleName());

                if (bankAccount instanceof CurrentAccount) {
                    System.out.println("Overdraft => " + ((CurrentAccount) bankAccount).getOverDraft());
                } else {
                    System.out.println("Interest Rate=> " + ((SavingAccount) bankAccount).getInterestRate());
                }

                bankAccount.getAccountOperations().forEach(op -> {
                    System.out.println("=====================");
                    System.out.println(op.getType());
                    System.out.println(op.getOperationDate());
                    System.out.println(op.getAmount());
                });
            }
        };
    }

    // @Bean
    CommandLineRunner start(BankAccountRepository bankAccountRepository,
                            CustomerRepository customerRepository,
                            AccountOperationRepository accountOperationRepository){
        return args -> {
            Stream.of("Oussama", "Amine","Rachid").forEach(name->{
                Customer customer = new Customer();
                customer.setNom(name);
                customer.setEmail(name+"gmail.com");
                customerRepository.save(customer);
            });

            customerRepository.findAll().forEach(customer -> {
                CurrentAccount currentAccount =  new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random()*320000);
                currentAccount.setCreatedAt(new Date());
                currentAccount.setOverDraft(89000);
                currentAccount.setCustomer(customer);
                currentAccount.setStatus(AccountStatus.CREATED);
                bankAccountRepository.save(currentAccount);

                SavingAccount savingAccount =  new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random()*320000);
                savingAccount.setCreatedAt(new Date());
                savingAccount.setCustomer(customer);
                savingAccount.setInterestRate(9.3);
                savingAccount.setStatus(AccountStatus.CREATED);
                bankAccountRepository.save(savingAccount);
            });

            bankAccountRepository.findAll().forEach(account->{
                for (int i = 0; i < 5; i++) {
                    AccountOperation operation = new AccountOperation();
                    operation.setAmount(Math.random()*55000);
                    operation.setOperationDate(new Date());
                    operation.setType(Math.random() > 0.5 ? OperationType.DEBIT : OperationType.CREDIT);
                    operation.setBankAccount(account);
                    accountOperationRepository.save(operation);
                }
            });
        };
    }

}
