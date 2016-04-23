package in.co.shopster.shopster_delivery_client.rest.models.requests;

/**
 * Created by vikram on 23/4/16.
 */
public class VerifyDeliveryRequest {
    long order_id, delivery_person;
    String uhash_token;

    public VerifyDeliveryRequest(long orderId, long deliveryPersonId, String customerHashToken) {
        this.order_id = orderId;
        this.delivery_person = deliveryPersonId;
        this.uhash_token = customerHashToken;
    }
}
