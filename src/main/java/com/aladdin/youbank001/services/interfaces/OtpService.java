package com.aladdin.youbank001.services.interfaces;


import com.aladdin.youbank001.dao.entities.Customer;
import com.aladdin.youbank001.model.dtos.security.OtpRequest;

public interface OtpService {
    void sendOtp(Customer user);

    void verifyOtp(OtpRequest otpRequest);
}
