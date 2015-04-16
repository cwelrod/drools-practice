/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drools.devguide.util.factories;

import java.util.ArrayList;
import java.util.List;
import org.drools.devguide.eshop.model.Customer;
import org.drools.devguide.eshop.model.Item;
import org.drools.devguide.eshop.model.Order;
import org.drools.devguide.eshop.model.OrderLine;

/**
 *
 * @author salaboy
 */
public class ModelFactory {

    public static Order getOrderWithFiveHighRangeItems() {
        Order o = new Order();

        Item pA = new Item("A", 700.0, 800.0);
        Item pB = new Item("B", 800.0, 850.0);
        Item pC = new Item("C", 800.0, 850.0);
        Item pD = new Item("D", 800.0, 850.0);
        Item pE = new Item("E", 800.0, 850.0);
        
        List<OrderLine> items = new ArrayList<>();
        
        OrderLine item1 = new OrderLine();
        item1.setItem(pA);
        item1.setQuantity(1);
        items.add(item1);
        
        OrderLine item2 = new OrderLine();
        item2.setItem(pB);
        item2.setQuantity(2);
        items.add(item2);
        
        OrderLine item3 = new OrderLine();
        item3.setItem(pC);
        item3.setQuantity(3);
        items.add(item3);
        
        OrderLine item4 = new OrderLine();
        item4.setItem(pD);
        item4.setQuantity(4);
        items.add(item4);
        
        OrderLine item5 = new OrderLine();
        item5.setItem(pE);
        item5.setQuantity(5);
        items.add(item5);
        
        o.setItems(items);
        
        Customer c = new Customer();
        o.setCustomer(c);
        
        return o;
    }
}
