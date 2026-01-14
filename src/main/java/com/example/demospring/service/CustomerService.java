package com.example.demospring.service;

import com.example.demospring.entity.Customer;
import com.example.demospring.entity.request.SaveCustomerRequest;
import com.example.demospring.repository.CustomerRepository;
import com.example.demospring.repository.OfficerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final OfficerRepository officerRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public boolean SaveCustomerRequest(SaveCustomerRequest request){
        var customer = new Customer();;
        customer.setCustId(request.getCustId());
        customer.setAddress(request.getAddress());
        customer.setCity(request.getCity());
        customer.setCustTypeCd(request.getCustTypeCd());
        customer.setFedId(request.getFedId());;
//        customerRepository.save(customer);
//        officerRepository.save(request.getOfficer());
        kafkaTemplate.send("customer-topic", customer);
        return true;
    }

    private boolean saveCustomer(Customer customer){
        customerRepository.save(customer);
        return true;
    };
}
