package in.co.shopster.shopster_delivery_client.rest.models;

/**
 * Created by vikram on 23/4/16.
 */
public class Delivery {

    /**
     * {
     "queue_id": 1, *
     "delivery_type": "H", *
     "is_delivered": false, *
     "order_id": 3, *
     "delivered_by": 3 *
     }
     */

    long queue_id, order_id, delivered_by;
    char delivery_type;
    boolean is_delivered;

    public Delivery(long queueId, long orderId, long deliveredBy, char deliveryType, boolean isDelivered) {
        this.queue_id = queueId;
        this.order_id = orderId;
        this.delivered_by = deliveredBy;
        this.delivery_type = deliveryType;
        this.is_delivered = isDelivered;
    }

    public String toString() {
        return "Delivery : "+
                "\nqueue_id : " +this.queue_id+
                "\norder_id : "+this.order_id+
                "\ndelivered_by : "+this.delivered_by+
                "\ndelivery_type : "+this.delivery_type+
                "\nis_delivered : "+this.is_delivered;
    }

    public long getOrderId() { return this.order_id; }


    public long getQueueId() { return this.queue_id; }


    public long getDeliveredBy() { return this.delivered_by; }


    public char getDeliveryType() { return this.delivery_type; }

    public boolean isDelivered() { return this.is_delivered; }

}
