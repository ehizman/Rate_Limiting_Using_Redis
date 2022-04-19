package com.ehizman.mmr_application.security;

import com.ehizman.mmr_application.models.Account;
import com.ehizman.mmr_application.repositories.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Slf4j
public class AuthServiceImpl implements UserDetailsService {
    @Autowired
    AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return loadAUserByUsername(username);
    }

    private UserDetails loadAUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format("No account found with this username %s", username))
                );
        return new org.springframework.security.core.userdetails.User(account.getUsername(), account.getAuthId(), new ArrayList<>());
    }

    public Account internalFindUserByUsername(String username) {
        log.info("Account --> {}", accountRepository.findByUsername(username));
        return accountRepository.findByUsername(username).orElse(null);
    }
}
