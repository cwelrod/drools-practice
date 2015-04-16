/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drools.devguide.chapter06;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.drools.devguide.BaseTest;
import static java.util.stream.Collectors.toList;
import org.drools.devguide.eshop.model.Customer;
import org.drools.devguide.eshop.model.Order;
import org.drools.devguide.eshop.model.OrderState;
import org.drools.devguide.eshop.model.SuspiciousOperation;
import org.drools.devguide.eshop.service.AuditService;
import org.drools.devguide.eshop.service.OrderService;
import org.drools.devguide.util.CustomerBuilder;
import org.drools.devguide.util.OrderBuilder;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.kie.api.runtime.KieSession;

/**
 *
 * @author esteban
 */
public class GlobalsTest extends BaseTest{

    @Test
    public void detectSuspiciousAmountOperationsWithFixedThresholdTest() {

        //Create a customer with PENDING orders for a value > 10000
        Customer customerA = new CustomerBuilder()
                .withId(1L)
                .newOrder()
                    .withSate(OrderState.PENDING)
                    .newItem()
                        .withQuantity(2)
                        .withItem()
                        .withSalePrice(5000.0)
                        .end()
                    .end()
                    .newItem()
                        .withQuantity(5)
                        .withItem()
                        .withSalePrice(800.0)
                    .end()
                .end()
            .end()
        .build();

        //Create a customer with PENDING orders for a value < 10000 
        Customer customerB = new CustomerBuilder()
                .withId(2L)
                .newOrder()
                    .withSate(OrderState.PENDING)
                    .newItem()
                        .withQuantity(1)
                        .withItem()
                        .withSalePrice(1000.0)
                        .end()
                    .end()
                .end()
        .build();

        //Create a session and insert the 2 patients.
        KieSession ksession = this.createSession("suspicious-operations-fixed");

        ksession.insert(customerA);
        ksession.insert(customerB);

        //Before we fire any activated rule, we check that the session doesn't
        //have any object of type SuspiciousOperation in it.
        Collection<SuspiciousOperation> suspiciousOperations
                = this.getFactsFromKieSession(ksession, SuspiciousOperation.class);
        assertThat(suspiciousOperations, hasSize(0));

        //Let's fire any activated rule now.
        ksession.fireAllRules();

        //After the rules are fired, a SuspiciousOperation object is now 
        //present. This object belongs to Customer "A".
        suspiciousOperations
                = this.getFactsFromKieSession(ksession, SuspiciousOperation.class);

        assertThat(suspiciousOperations, hasSize(1));
        assertThat(suspiciousOperations.iterator().next().getCustomer().getCustomerId(),
                is(1L));

        ksession.dispose();

    }

    @Test
    public void detectSuspiciousAmountOperationsWithVariableThresholdTest() {

        //Create a customer with PENDING orders for a value > 10000
        Customer customerA = new CustomerBuilder()
                .withId(1L)
                .newOrder()
                    .withSate(OrderState.PENDING)
                    .newItem()
                        .withQuantity(2)
                        .withItem()
                        .withSalePrice(5000.0)
                    .end()
                .end()
                    .newItem()
                        .withQuantity(5)
                        .withItem()
                        .withSalePrice(800.0)
                    .end()
                .end()
            .end()
        .build();

        //Create a customer with PENDING orders for a value < 10000 
        Customer customerB = new CustomerBuilder()
                .withId(2L)
                .newOrder()
                    .withSate(OrderState.PENDING)
                    .newItem()
                        .withQuantity(1)
                        .withItem()
                        .withSalePrice(1000.0)
                    .end()
                .end()
            .end()
        .build();

        //Create a session
        KieSession ksession = this.createSession("suspicious-operations-variable");
        
        //Before we insert any fact, we set the value of 'amountThreshold' global
        //to 500.0
        ksession.setGlobal("amountThreshold", 500.0);

        ksession.insert(customerA);
        ksession.insert(customerB);

        //Before we fire any activated rule, we check that the session doesn't
        //have any object of type SuspiciousOperation in it.
        Collection<SuspiciousOperation> suspiciousOperations
                = this.getFactsFromKieSession(ksession, SuspiciousOperation.class);
        assertThat(suspiciousOperations, hasSize(0));

        //Let's fire any activated rule now.
        ksession.fireAllRules();

        //After the rules are fired, 2 SuspiciousOperation objects are now 
        //present. These objects belong to Customer "A" and "B".
        suspiciousOperations
                = this.getFactsFromKieSession(ksession, SuspiciousOperation.class);

        assertThat(suspiciousOperations, hasSize(2));
        assertThat(
                suspiciousOperations.stream().map(so -> so.getCustomer().getCustomerId()).collect(toList())
                , containsInAnyOrder(1L, 2L)
        );
        
    }

    @Test
    public void detectSuspiciousAmountOperationsWithOrderServiceTest() {

        //Create 2 customers without any Order. Orders are going to be provided
        //by the OrderService.
        Customer customerA = new CustomerBuilder().withId(1L).build();
        Customer customerB = new CustomerBuilder().withId(2L).build();

        //Mock an instance of OrderService
        OrderService orderService = new OrderService() {

            @Override
            public Collection<Order> getOrdersByCustomer(Long customerId) {
                switch (customerId.toString()){
                    case "1":
                        return Arrays.asList(
                            new OrderBuilder(null)
                                    .withSate(OrderState.PENDING)
                                    .newItem()
                                        .withQuantity(2)
                                        .withItem()
                                        .withSalePrice(5000.0)
                                        .end()
                                    .end()
                                    .newItem()
                                        .withQuantity(5)
                                        .withItem()
                                        .withSalePrice(800.0)
                                        .end()
                                    .end()
                            .build()
                        );
                    case "2":
                        return Arrays.asList(
                            new OrderBuilder(null)
                                    .withSate(OrderState.PENDING)
                                    .newItem()
                                        .withQuantity(1)
                                        .withItem()
                                        .withSalePrice(1000.0)
                                    .end()
                                .end()
                            .build()
                        );
                    default:
                        return Collections.EMPTY_LIST;
                }
            }
        };
        
        //Create a session
        KieSession ksession = this.createSession("suspicious-operations-order-service");
        
        //Before we insert any fact, we set the value of 'amountThreshold' global
        //to 500.0 and the value of 'orderService' global to the mocked service
        //we have created.
        ksession.setGlobal("amountThreshold", 500.0);
        ksession.setGlobal("orderService", orderService);

        ksession.insert(customerA);
        ksession.insert(customerB);

        //Before we fire any activated rule, we check that the session doesn't
        //have any object of type SuspiciousOperation in it.
        Collection<SuspiciousOperation> suspiciousOperations
                = this.getFactsFromKieSession(ksession, SuspiciousOperation.class);
        assertThat(suspiciousOperations, hasSize(0));

        //Let's fire any activated rule now.
        ksession.fireAllRules();

        //After the rules are fired, 2 SuspiciousOperation objects are now 
        //present. These objects belong to Customer "A" and "B".
        suspiciousOperations
                = this.getFactsFromKieSession(ksession, SuspiciousOperation.class);

        assertThat(suspiciousOperations, hasSize(2));
        assertThat(
                suspiciousOperations.stream().map(so -> so.getCustomer().getCustomerId()).collect(toList())
                , containsInAnyOrder(1L, 2L)
        );
        
    }

    @Test
    public void detectSuspiciousAmountOperationsCollectInGlobalList() {

        //Create 2 customers without any Order. Orders are going to be provided
        //by the OrderService.
        Customer customerA = new CustomerBuilder().withId(1L).build();
        Customer customerB = new CustomerBuilder().withId(2L).build();

        //Mock an instance of OrderService
        OrderService orderService = new OrderService() {

            @Override
            public Collection<Order> getOrdersByCustomer(Long customerId) {
                switch (customerId.toString()){
                    case "1":
                        return Arrays.asList(
                            new OrderBuilder(null)
                                    .withSate(OrderState.PENDING)
                                    .newItem()
                                        .withQuantity(2)
                                        .withItem()
                                        .withSalePrice(5000.0)
                                        .end()
                                    .end()
                                    .newItem()
                                        .withQuantity(5)
                                        .withItem()
                                        .withSalePrice(800.0)
                                        .end()
                                    .end()
                            .build()
                        );
                    case "2":
                        return Arrays.asList(
                            new OrderBuilder(null)
                                    .withSate(OrderState.PENDING)
                                    .newItem()
                                        .withQuantity(1)
                                        .withItem()
                                        .withSalePrice(1000.0)
                                    .end()
                                .end()
                            .build()
                        );
                    default:
                        return Collections.EMPTY_LIST;
                }
            }
        };
        
        //Create a session
        KieSession ksession = this.createSession("suspicious-operations-global-list");
        
        //Before we insert any fact, we set the value of 'amountThreshold' global
        //to 500.0 and the value of 'orderService' global to the mocked service
        //we have created.
        ksession.setGlobal("amountThreshold", 500.0);
        ksession.setGlobal("orderService", orderService);
        
        //We will also set a global Set to collect all the SuspiciousOperation
        //objects generated in the session.
        Set<SuspiciousOperation> results = new HashSet<>();
        ksession.setGlobal("results", results);

        ksession.insert(customerA);
        ksession.insert(customerB);

        //Let's fire any activated rule now.
        ksession.fireAllRules();

        //After the rules are fired, 2 SuspiciousOperation objects are now 
        //present in the 'results' Set.
        assertThat(results, hasSize(2));
        assertThat(
                results.stream().map(so -> so.getCustomer().getCustomerId()).collect(toList())
                , containsInAnyOrder(1L, 2L)
        );
        
    }
    
    @Test
    public void detectSuspiciousAmountOperationsNotifyAuditService() {

        //Create 2 customers without any Order. Orders are going to be provided
        //by the OrderService.
        Customer customerA = new CustomerBuilder().withId(1L).build();
        Customer customerB = new CustomerBuilder().withId(2L).build();

        //Mock an instance of OrderService
        OrderService orderService = new OrderService() {

            @Override
            public Collection<Order> getOrdersByCustomer(Long customerId) {
                switch (customerId.toString()){
                    case "1":
                        return Arrays.asList(
                            new OrderBuilder(null)
                                    .withSate(OrderState.PENDING)
                                    .newItem()
                                        .withQuantity(2)
                                        .withItem()
                                        .withSalePrice(5000.0)
                                        .end()
                                    .end()
                                    .newItem()
                                        .withQuantity(5)
                                        .withItem()
                                        .withSalePrice(800.0)
                                        .end()
                                    .end()
                            .build()
                        );
                    case "2":
                        return Arrays.asList(
                            new OrderBuilder(null)
                                    .withSate(OrderState.PENDING)
                                    .newItem()
                                        .withQuantity(1)
                                        .withItem()
                                        .withSalePrice(1000.0)
                                    .end()
                                .end()
                            .build()
                        );
                    default:
                        return Collections.EMPTY_LIST;
                }
            }
        };
        
        //Create a mocked implementation of AuditService that collects its results
        //into a set
        final Set<SuspiciousOperation> results = new HashSet<>();
        AuditService auditService = new AuditService() {

            @Override
            public void notifySuspiciousOperation(SuspiciousOperation operation) {
                results.add(operation);
            }
        };
        
        //Create a session
        KieSession ksession = this.createSession("suspicious-operations-audit-service");
        
        //Before we insert any fact, we set the value of 'amountThreshold' global
        //to 500.0, the value of 'orderService' global to the mocked service
        //we have created and the value of 'auditService' global to the mocked
        //service we have created.
        ksession.setGlobal("amountThreshold", 500.0);
        ksession.setGlobal("orderService", orderService);
        ksession.setGlobal("auditService", auditService);
        
        ksession.insert(customerA);
        ksession.insert(customerB);

        //Let's fire any activated rule now.
        ksession.fireAllRules();

        //After the rules are fired, 2 SuspiciousOperation objects are now 
        //present in the 'results' Set.
        assertThat(results, hasSize(2));
        assertThat(
                results.stream().map(so -> so.getCustomer().getCustomerId()).collect(toList())
                , containsInAnyOrder(1L, 2L)
        );
        
    }
    
}
