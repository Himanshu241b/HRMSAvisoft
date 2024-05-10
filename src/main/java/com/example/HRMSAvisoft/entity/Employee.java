package com.example.HRMSAvisoft.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long employeeId;

    @Column(unique = true)
    private String employeeCode;

    private String firstName;

    private String lastName;

    private String contact;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<EmergencyContact> emergencyContacts = new ArrayList<EmergencyContact>();
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Address> addresses = new ArrayList<Address>();

    @Enumerated(EnumType.STRING)
    private Position position;

    private String joinDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String adhaarNumber;

    private String panNumber;

    private String eanNumber;

    private String profileImage;

    private String dateOfBirth;

    private BigDecimal salary;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "accountId")
    private Account account;



}


