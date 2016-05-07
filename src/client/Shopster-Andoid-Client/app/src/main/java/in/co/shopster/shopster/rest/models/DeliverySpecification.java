package in.co.shopster.shopster.rest.models;

/**
 * Created by vikram on 7/5/16.
 */
public class DeliverySpecification {

    long order_id;
    String delivery_type;

    public DeliverySpecification(long orderId, String deliveryType) {
        this.order_id = orderId;
        this.delivery_type = deliveryType;
    }

    public String toString() {
        return  "Delivery preference : "+"\n"+
                "Order ID : "+this.order_id+"\n"+
                "Delivery type : "+this.delivery_type+"\n";
    }
}
