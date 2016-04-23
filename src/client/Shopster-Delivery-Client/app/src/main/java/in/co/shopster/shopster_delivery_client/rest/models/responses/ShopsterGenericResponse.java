package in.co.shopster.shopster_delivery_client.rest.models.responses;

import in.co.shopster.shopster_delivery_client.rest.services.ShopsterService;

/**
 * Created by vikram on 23/4/16.
 */
public class ShopsterGenericResponse {

    String Message;

    public ShopsterGenericResponse(String message) {
        this.Message = message;
    }

    public String toString() {
        return "Message : "+this.Message;
    }

    public String getMessage() {
        return this.Message;
    }
}


