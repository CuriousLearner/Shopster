package in.co.shopster.shopster_delivery_client.rest.services;

import in.co.shopster.shopster_delivery_client.rest.models.LoginCredentials;
import in.co.shopster.shopster_delivery_client.rest.models.responses.ShopsterToken;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by vikram on 23/4/16.
 */
public interface ShopsterService {

    @POST("/api/login/")
    Call<ShopsterToken> login(@Body LoginCredentials loginCredentials);


}
