package com.example.demospring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private Long custId;
    private String address;
    private String city;
    private String custTypeCd;
    private String fedId;
    private String postalCode;
    private String state;
}
