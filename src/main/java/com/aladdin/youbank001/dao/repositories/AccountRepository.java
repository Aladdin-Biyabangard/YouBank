package com.aladdin.youbank001.dao.repositories;

import com.aladdin.youbank001.dao.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}
