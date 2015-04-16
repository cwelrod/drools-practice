package org.drools.devguide.chapter02;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import org.drools.devguide.BaseTest;
import org.drools.devguide.eshop.model.Customer;
import org.drools.devguide.eshop.model.Order;
import org.drools.devguide.util.factories.ModelFactory;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.kie.api.runtime.KieSession;

/**
 *
 * @author salaboy
 */
public class SimpleOrderDiscountTest extends BaseTest {

    @Test
    public void simpleApplyDiscount() {
        KieSession kSession = createDefaultSession();

        Order o = ModelFactory.getOrderWithFiveHighRangeItems();
        
        kSession.insert(o.getCustomer());
        kSession.insert(o.getOrderLines().get(0));
        kSession.insert(o.getOrderLines().get(1));
        kSession.insert(o.getOrderLines().get(2));
        kSession.insert(o.getOrderLines().get(3));
        kSession.insert(o.getOrderLines().get(4));
        kSession.insert(o.getOrderLines().get(0).getItem());
        kSession.insert(o.getOrderLines().get(1).getItem());
        kSession.insert(o.getOrderLines().get(2).getItem());
        kSession.insert(o.getOrderLines().get(3).getItem());
        kSession.insert(o.getOrderLines().get(4).getItem());
        kSession.insert(o);
        
        int fired = kSession.fireAllRules();

        assertThat(4, is(fired));
        assertThat(o.getCustomer().getCategory(), is(Customer.Category.SILVER));
        assertThat(o.getDiscount(), not(nullValue()));
        assertThat(o.getDiscount().getPercentage(), is(10.0));

    }
}
