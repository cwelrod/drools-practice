package org.drools.devguide.eshop.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Order implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Customer customer;
	private Date date;
	private List<OrderLine> items;
	private OrderState state;
        private Discount discount;
	
	public Order() {
	}

        public Customer getCustomer() {
            return customer;
        }

        public void setCustomer(Customer customer) {
            this.customer = customer;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

	public List<OrderLine> getItems() {
		return items;
	}

	public void setItems(List<OrderLine> items) {
		this.items = items;
	}
        
        
	
	public OrderState getState() {
		return state;
	}
	
	public void setState(OrderState state) {
		this.state = state;
	}
        
        public double getTotal(){
            return this.getItems().stream()
                    .mapToDouble(item -> item.getItem().getSalePrice() * item.getQuantity())
                    .sum();
        }

        public Discount getDiscount() {
            return discount;
        }

        public void setDiscount(Discount discount) {
            this.discount = discount;
        }
        
        

        @Override
        public int hashCode() {
                int hash = 5;
                return hash;
        }

        @Override
        public boolean equals(Object obj) {
                if (obj == null) {
                    return false;
                }
                if (getClass() != obj.getClass()) {
                    return false;
                }
                final Order other = (Order) obj;
                if (!Objects.equals(this.customer, other.customer)) {
                    return false;
                }
                if (!Objects.equals(this.date, other.date)) {
                    return false;
                }
                if (!Objects.equals(this.items, other.items)) {
                    return false;
                }
                if (this.state != other.state) {
                    return false;
                }
                return true;
        }

        @Override
        public String toString() {
                return "Order [" + "customer=" + customer + ", date=" + date + ", items=" + items + ", state=" + state + ']';
        }
}