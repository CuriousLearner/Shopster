package in.co.shopster.shopster.rest.models;

/**
 * Created by vikram on 7/5/16.
 */
public class PaymentDetails {

    long order_id, ordered_by;

    public PaymentDetails(long orderId, long orderedBy) {
        this.order_id = orderId;
        this.ordered_by = orderedBy;
    }

    public String toString() {
        return  "Payment : "+"\n"+
                "Order ID : "+this.order_id+"\n"+
                "Ordered By : "+this.ordered_by+"\n";
    }
}
