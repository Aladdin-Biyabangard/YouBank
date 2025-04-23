package com.aladdin.youbank001.dao.repositories;

import com.aladdin.youbank001.dao.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, String> {
}
