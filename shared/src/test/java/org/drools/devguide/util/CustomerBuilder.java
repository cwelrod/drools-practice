/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drools.devguide.util;

import org.drools.devguide.eshop.model.Customer;

/**
 *
 * @author esteban
 */
public class CustomerBuilder {
    
    private final Customer instance;
    private static Long customerIdGenerator = 0L;
    public CustomerBuilder() {
        instance = new Customer();
        instance.setCustomerId(customerIdGenerator++);
    }
    
    public CustomerBuilder withId(Long id){
        instance.setCustomerId(id);
        return this;
    }
    
    public Customer build(){
        return instance;
    }

    public CustomerBuilder end() {
        return this;
    }
    
}
