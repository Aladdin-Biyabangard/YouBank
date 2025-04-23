package com.aladdin.youbank001.services.interfaces;

import com.aladdin.youbank001.dao.entities.Customer;

public interface CustomerService {

    Customer findCustomerById(String customerId);

}
