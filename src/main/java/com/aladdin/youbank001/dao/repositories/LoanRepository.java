package com.aladdin.youbank001.dao.repositories;

import com.aladdin.youbank001.dao.entities.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, String> {
}
