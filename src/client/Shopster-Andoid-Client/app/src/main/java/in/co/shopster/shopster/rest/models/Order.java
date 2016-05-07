package in.co.shopster.shopster.rest.models;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ayush on 7/5/16.
 */
public class Order {


    /**
     *
     order_id
     price
     ordered_on
     is_completed
     status
     */
    public ArrayList<Product> products;
    public String ordered_by;
    public long order_id, price;
    public Date ordered_on;
    public boolean is_completed;
    public char status;


    public Order(
            String orderedBy,
            ArrayList<Product> products,
            long orderId,
            long price,
            Date orderedOn,
            boolean isCompleted,
            char status) {
        this.ordered_by = orderedBy;
        this.products = products;
        this.order_id = orderId;
        this.price = price;
        this.ordered_on = orderedOn;
        this.is_completed = isCompleted;
        this.status = status;
    }
    public long getPrice() { return this.price; }
    public long getOrderId(){ return this.order_id; }
    public char getStatus() { return this.status; }
}



