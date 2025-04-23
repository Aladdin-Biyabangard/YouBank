package com.aladdin.youbank001.dao.repositories;

import com.aladdin.youbank001.dao.entities.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, String> {
    Optional<Otp> findByCodeAndEmail(Integer code, String email);
}
