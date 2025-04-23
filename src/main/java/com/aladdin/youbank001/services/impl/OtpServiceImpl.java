package com.aladdin.youbank001.services.impl;

import com.aladdin.youbank001.dao.entities.Card;
import com.aladdin.youbank001.dao.entities.Customer;
import com.aladdin.youbank001.dao.entities.Otp;
import com.aladdin.youbank001.dao.repositories.OtpRepository;
import com.aladdin.youbank001.exceptions.ResourceNotFoundException;
import com.aladdin.youbank001.mail.EmailServiceImpl;
import com.aladdin.youbank001.mail.EmailTemplate;
import com.aladdin.youbank001.model.dtos.security.OtpRequest;
import com.aladdin.youbank001.services.interfaces.OtpService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OtpServiceImpl implements OtpService {
    OtpRepository otpRepository;
    EmailServiceImpl emailService;

    public void resetPinCode(Card card) {
        String email = card.getAccount().getCustomer().getEmail();
        Map<String, String> placeholders = Map.of("userName", card.getAccount().getCustomer().getFullName(),
                "PIN", card.getPin());
        emailService.sendSimpleEmail(email, EmailTemplate.RESET_PIN, placeholders);
    }


    @Override
    public void sendOtp(Customer user) {
        log.info("Operation of sending otp started for user with email {}", user.getEmail());
        String code = generateOtp(user.getEmail());
        Map<String, String> placeholders = Map.of("userName", user.getFullName(), "code", code);
        emailService.sendSimpleEmail(user.getEmail(), EmailTemplate.VERIFICATION, placeholders);
        log.info("Otp code sent to user email {}", user.getEmail());
    }


    @Override
    public void verifyOtp(OtpRequest otpRequest) {
        Otp otp = otpRepository.findByCodeAndEmail(otpRequest.getOtpCode(),
                otpRequest.getEmail()).orElseThrow(() -> new ResourceNotFoundException("OTP_NOT_FOUND"));
        if (otp.getExpirationTime().isBefore(LocalDateTime.now())) {
            log.error("Otp is expired for user with email {}", otpRequest.getEmail());
            throw new IllegalArgumentException("OTP_EXPIRED");
        }
    }

    private String generateOtp(String email) {
        log.info("Operation of generating otp started for user {}", email);
        SecureRandom random = new SecureRandom();
        Integer code = 100_000 + random.nextInt(900_000);
        Otp otp = Otp.builder()
                .email(email)
                .code(code)
                .expirationTime(LocalDateTime.now().plusMinutes(15))
                .build();
        otpRepository.save(otp);
        log.info("Otp generated for user with email {}", email);
        return String.valueOf(code);
    }
}
