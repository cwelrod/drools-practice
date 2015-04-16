/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drools.devguide.eshop.model;

/**
 *
 * @author salaboy
 */
public class Coupon {
    public enum CouponType{DISCOUNT, TWOFORONE, POINTS };
    
    private Customer customer;
    private Order order;
    private CouponType type;

    public Coupon() {
    }

    public Coupon(Customer customer, Order order, CouponType type) {
        this.customer = customer;
        this.order = order;
        this.type = type;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public CouponType getType() {
        return type;
    }

    public void setType(CouponType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Coupon{" + "customer=" + customer + ", order=" + order + ", type=" + type + '}';
    }
    
    
    
    
    
}
