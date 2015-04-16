/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drools.devguide.util;

import java.util.Optional;
import org.drools.devguide.eshop.model.Customer;
import org.drools.devguide.eshop.model.Order;

/**
 *
 * @author esteban
 */
public class CustomerBuilder {
    
    private final Customer instance;
    private Optional<OrderBuilder> orderBuilder = Optional.empty();
    private static Long customerIdGenerator = 0L;
    public CustomerBuilder() {
        instance = new Customer();
        instance.setCustomerId(customerIdGenerator++);
    }
    
    public CustomerBuilder withId(Long id){
        instance.setCustomerId(id);
        return this;
    }
    
    public OrderBuilder newOrder(){
        if (this.orderBuilder.isPresent()){
            Order order = this.orderBuilder.get().build();
            order.setCustomer(this.instance);
        }
        this.orderBuilder = Optional.of(new OrderBuilder(this));
        return this.orderBuilder.get();
    }
    
    public Customer build(){
        if (this.orderBuilder.isPresent()){
            Order order = this.orderBuilder.get().build();
            order.setCustomer(this.instance);
        }
        return instance;
    }

    public CustomerBuilder end() {
        return this;
    }
    
}
