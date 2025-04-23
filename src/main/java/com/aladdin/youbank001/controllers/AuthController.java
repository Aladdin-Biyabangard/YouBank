package com.aladdin.youbank001.controllers;

import com.aladdin.youbank001.model.dtos.security.RegisterRequest;
import com.aladdin.youbank001.services.interfaces.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authServiceImpl;


    @PostMapping(path = "register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        authServiceImpl.register(request);
        return ResponseEntity.ok("Register successfully!");
    }
}
