package com.aladdin.youbank001.services.impl;

import com.aladdin.youbank001.dao.entities.Account;
import com.aladdin.youbank001.dao.entities.Customer;
import com.aladdin.youbank001.dao.entities.User;
import com.aladdin.youbank001.dao.repositories.AccountRepository;
import com.aladdin.youbank001.dao.repositories.CustomerRepository;
import com.aladdin.youbank001.dao.repositories.UserRepository;
import com.aladdin.youbank001.model.dtos.request.AuthRequestDto;
import com.aladdin.youbank001.model.dtos.security.RegisterRequest;
import com.aladdin.youbank001.services.interfaces.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final OtpServiceImpl otpServiceImpl;


    @Override
    public void register(RegisterRequest request) {
        Customer customer = createCustomer(request);
        Account account = Account.builder()
                .customer(customer)
                .build();
        customerRepository.save(customer);
        accountRepository.save(account);
        otpServiceImpl.sendOtp(customer);
    }

    @Override
    public void login(AuthRequestDto authRequestDto) {
        Optional<User> userOptional = userRepository
                .findByEmailAndPassword(authRequestDto.getEmail(), authRequestDto.getPassword());
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("Password or email incorrect!");
        }
    }


    private Customer createCustomer(RegisterRequest request) {
        return Customer.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .fullName(request.getFullName())
                .phoneNumber(null)
                .address(null)
                .fin(null)
                .build();

    }
}
