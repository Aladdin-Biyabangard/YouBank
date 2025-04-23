package com.aladdin.youbank001.dao.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Data
@Table(name = "customers")
@Inheritance(strategy = InheritanceType.JOINED)
public class Customer extends User {

    @Column(name = "full_name", nullable = false, length = 30)
    private String fullName;

    @Column(name = "phone_number", length = 17)
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "fin_id")
    private String fin;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Account account;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    @JsonIgnore
    private BankBranch branch;

}
