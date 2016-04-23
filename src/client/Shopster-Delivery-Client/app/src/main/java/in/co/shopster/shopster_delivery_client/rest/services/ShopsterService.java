package in.co.shopster.shopster_delivery_client.rest.services;

import in.co.shopster.shopster_delivery_client.rest.models.Customer;
import in.co.shopster.shopster_delivery_client.rest.models.Delivery;
import in.co.shopster.shopster_delivery_client.rest.models.LoginCredentials;
import in.co.shopster.shopster_delivery_client.rest.models.responses.ShopsterToken;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.HEAD;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by vikram on 23/4/16.
 */
public interface ShopsterService {

    @POST("/api/login/")
    Call<ShopsterToken> login(@Body LoginCredentials loginCredentials);


    @GET("/api/user/{user_email}/")
    Call<Customer> getUserByEmail(@Header("Authorization") String authToken, @Path("user_email") String userEmail);


    @GET("/api/deliveryrequest/{user_id}/")
    Call<Delivery> checkDeliveryAssignment(@Header("Authorization") String authToken, @Path("user_id") String userId);

}
