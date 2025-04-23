package com.aladdin.youbank001.services.interfaces;

import com.aladdin.youbank001.model.dtos.request.AuthRequestDto;
import com.aladdin.youbank001.model.dtos.security.RegisterRequest;

public interface AuthService {

    void register(RegisterRequest request);

    void login(AuthRequestDto authRequestDto);
//
//    AuthResponseDto verifyAndGetToken(OtpRequest request);
//
//    void requestPasswordReset(String email);
//
//    void resetPassword(String token, RecoveryPassword recoveryPassword);
//
//    AuthResponseDto refreshAccessToken(RefreshTokenRequest request);
}
