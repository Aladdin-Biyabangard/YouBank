package com.aladdin.youbank001.services.impl;

import com.aladdin.youbank001.configurations.mappers.CustomerMapper;
import com.aladdin.youbank001.dao.entities.Customer;
import com.aladdin.youbank001.dao.repositories.CustomerRepository;
import com.aladdin.youbank001.exceptions.ResourceNotFoundException;
import com.aladdin.youbank001.model.dtos.request.CustomerDto;
import com.aladdin.youbank001.model.dtos.response.customer.ResponseCustomerDto;
import com.aladdin.youbank001.services.interfaces.CustomerService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final CustomerMapper customerMapper;

    public ResponseCustomerDto updateCustomerInfo(String customerId, CustomerDto customerDto) {
        Customer customer = findCustomerById(customerId);
        customer.setFullName(customerDto.getFullName());
        customer.setPhoneNumber(customerDto.getPhoneNumber());
        customer.setAddress(customerDto.getAddress());
        customer.setFin(customerDto.getFin());
        return modelMapper.map(customer, ResponseCustomerDto.class);
    }



    public List<ResponseCustomerDto> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customerMapper.toResponse(customers);
    }


    @Override
    public Customer findCustomerById(String customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("CUSTOMER_NOT_FOUND"));
    }
}
