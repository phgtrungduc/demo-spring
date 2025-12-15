package com.example.demospring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CUSTOMER", schema = "DUC_PT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @Column(name = "CUST_ID", nullable = false)
    private Long custId;

    @Column(name = "ADDRESS", length = 30)
    private String address;

    @Column(name = "CITY", length = 20)
    private String city;

    @Column(name = "CUST_TYPE_CD", nullable = false, length = 1)
    private String custTypeCd;

    @Column(name = "FED_ID", nullable = false, length = 12)
    private String fedId;

    @Column(name = "POSTAL_CODE", length = 10)
    private String postalCode;

    @Column(name = "STATE", length = 20)
    private String state;
}
