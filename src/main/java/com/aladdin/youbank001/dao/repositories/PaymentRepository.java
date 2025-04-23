package com.aladdin.youbank001.dao.repositories;

import com.aladdin.youbank001.dao.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,String> {
}
