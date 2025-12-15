package com.example.demospring.entity.request;

import com.example.demospring.dto.OfficerDTO;
import com.example.demospring.entity.Officer;
import lombok.Data;

@Data
public class SaveCustomerRequest {
    private Long custId;
    private String address;
    private String city;
    private String custTypeCd;
    private String fedId;
    private String postalCode;
    private String state;
    private Officer officer;
}
