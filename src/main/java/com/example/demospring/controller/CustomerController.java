package com.example.demospring.controller;

import com.example.demospring.dto.OfficerDTO;
import com.example.demospring.entity.request.SaveCustomerRequest;
import com.example.demospring.service.CustomerService;
import com.example.demospring.service.OfficerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
@Tag(name = "Officer Management", description = "APIs for managing officers")
public class CustomerController {
    private final OfficerService officerService;
    private final CustomerService customerService;


    @PostMapping("save")
    public ResponseEntity<String> saveCustomer(@RequestBody SaveCustomerRequest request) {
        customerService.SaveCustomerRequest(request);
        return ResponseEntity.ok("Save Customer Success");
    }

    public ResponseEntity<String> saveCustomer2(@RequestBody SaveCustomerRequest request) {
        customerService.SaveCustomerRequest(request);
        return ResponseEntity.ok("Save Customer Success");
    }
}

