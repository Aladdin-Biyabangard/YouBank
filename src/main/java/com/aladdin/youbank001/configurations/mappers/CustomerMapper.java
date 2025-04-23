package com.aladdin.youbank001.configurations.mappers;

import com.aladdin.youbank001.dao.entities.Customer;
import com.aladdin.youbank001.model.dtos.response.customer.ResponseCustomerDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerMapper {


    public ResponseCustomerDto toResponse(Customer customer) {
        return new ResponseCustomerDto(
                customer.getFullName(),
                customer.getPhoneNumber(),
                customer.getAddress(),
                customer.getFin()
        );
    }

    public List<ResponseCustomerDto> toResponse(List<Customer> customers) {
        return customers.stream().map(this::toResponse).toList();
    }

}
