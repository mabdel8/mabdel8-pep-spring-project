package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account registerAccount(Account account) throws Exception {
        if (account.getUsername() == null || account.getUsername().isBlank()){
            throw new Exception("The Username cannot be blank");
        }
        if (account.getPassword() == null || account.getPassword().length()<4){
            throw new Exception("Password must be atleast 4 characters long");
        }
        if (accountRepository.findByUsername(account.getUsername()) != null){
            throw new Exception("Username already exists");
        }
        return accountRepository.save(account);
    }

    public Account login(Account account) throws Exception {
        Account loggedAccount = accountRepository.findByUsername(account.getUsername());
        if(loggedAccount.getUsername() == null || !loggedAccount.getPassword().equals(account.getPassword())){
            throw new Exception("Invalid Login");
        }

        return loggedAccount;
    }

}
