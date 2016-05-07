package in.co.shopster.shopster.rest.models.responses;

import in.co.shopster.shopster.rest.services.ShopsterService;

/**
 * Created by vikram on 7/5/16.
 */
public class ShopsterGenericResponse {

    public String Message;

    public ShopsterGenericResponse(String message) {
        this.Message = message;
    }

    public String getMessage() {
        return this.Message;
    }
}

