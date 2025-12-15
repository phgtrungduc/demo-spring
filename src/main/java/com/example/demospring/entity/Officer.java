package com.example.demospring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "OFFICER", schema = "DUC_PT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Officer {
    
    @Id
    @Column(name = "OFFICER_ID")
    private Long officerId;
    
    @Column(name = "FIRST_NAME", nullable = false, length = 30)
    private String firstName;
    
    @Column(name = "LAST_NAME", nullable = false, length = 30)
    private String lastName;
    
    @Column(name = "TITLE", length = 20)
    private String title;
    
    @Column(name = "START_DATE", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date startDate;
    
    @Column(name = "END_DATE")
    @Temporal(TemporalType.DATE)
    private Date endDate;
    
    @Column(name = "CUST_ID")
    private Long custId;
}

