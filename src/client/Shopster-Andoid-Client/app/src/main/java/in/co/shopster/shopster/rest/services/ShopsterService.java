package in.co.shopster.shopster.rest.services;


import in.co.shopster.shopster.rest.models.Customer;
import in.co.shopster.shopster.rest.models.LoginCredentials;
import in.co.shopster.shopster.rest.models.responses.ShopsterToken;
import retrofit.http.Body;
import retrofit.Call;
import retrofit.http.POST;

/**
 * Created by vikram on 7/4/16.
 */
public interface ShopsterService {

    public final String BASE_URL = "http://shopster-shopsterr.rhcloud.com";

    @POST("/api/login/")
    Call<ShopsterToken> login(@Body LoginCredentials loginCredentials);

    @POST("/api/user/")
    Call<Customer> register(@Body Customer customer);

}
