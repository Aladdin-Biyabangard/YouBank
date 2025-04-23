package com.aladdin.youbank001.dao.entities;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class BankBranch {
    @Id
    @Column(unique = true, nullable = false, length = 12)
    private String id;

    private String branchName;
    private String location;

    @OneToMany(mappedBy = "branch")
    private List<Customer> customers;

    @PrePersist
    public void generatedId() {
        if (this.id == null) {
            this.id = NanoIdUtils.randomNanoId().substring(0, 12);
        }
    }
}
