package org.drools.devguide.chapter02;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.List;
import org.drools.devguide.BaseTest;
import org.drools.devguide.eshop.model.Customer;
import org.drools.devguide.eshop.model.Order;
import org.drools.devguide.eshop.model.OrderLine;
import org.drools.devguide.eshop.model.Item;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.kie.api.runtime.KieSession;

/**
 *
 * @author salaboy
 */
public class SimpleOrderDiscountTest extends BaseTest{

    @Test
    public void simpleApplyDiscount() {
        KieSession kSession = createDefaultSession();
        Order o = new Order();
        
        Item pA = new Item("A", 700.0,800.0);
        Item pB = new Item("B", 800.0,850.0);
        List<OrderLine> items = new ArrayList<>();
        OrderLine item1 = new OrderLine();
        item1.setItem(pA);
        item1.setQuantity(2);
        items.add(item1);
        OrderLine item2 = new OrderLine();
        item1.setItem(pB);
        item1.setQuantity(1);
        items.add(item2);
        o.setItems(items);
        Customer c = new Customer();
        o.setCustomer(c);
        
        kSession.insert(pA);
        kSession.insert(pB);
        kSession.insert(item1);
        kSession.insert(item2);
        kSession.insert(o);
        
        int fired = kSession.fireAllRules();
        assertEquals(3, fired);
        assertThat(o.getDiscount(), not(nullValue()));
        assertThat(o.getDiscount().getPercentage(), is(10.0));
        
    }
}
