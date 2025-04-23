package com.aladdin.youbank001.dao.repositories;

import com.aladdin.youbank001.dao.entities.BankBranch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankBranchRepository extends JpaRepository<BankBranch, String> {
}
