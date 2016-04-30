package in.co.shopster.shopster.rest.services;


import in.co.shopster.shopster.rest.models.Customer;
import in.co.shopster.shopster.rest.models.LoginCredentials;
import in.co.shopster.shopster.rest.models.Product;
import in.co.shopster.shopster.rest.models.Recharge;
import in.co.shopster.shopster.rest.models.responses.ShopsterToken;
import in.co.shopster.shopster.rest.models.wallet;
import retrofit.http.Body;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by vikram on 7/4/16.
 */
public interface ShopsterService {

    //@TODO: Make this final again
    public String BASE_URL = "http://shopster-shopsterr.rhcloud.com";
    @POST("/api/login/")
    Call<ShopsterToken> login(@Body LoginCredentials loginCredentials);

    @POST("/api/user/")
    Call<Customer> register(@Body Customer customer);

    @GET("/api/products/{hash}")
    Call<Product> getProductDetails(@Header("Authorization") String authToken, @Path("hash") String hash);

    @GET("/api/wallet/4/")
    Call<wallet> getAmount(@Header("Authorization") String authToken);

    @POST("/api/recharge/")
    Call<Recharge> rechargeCounpon(@Header("Authorization") String authToken, @Body Recharge recharge );
}
