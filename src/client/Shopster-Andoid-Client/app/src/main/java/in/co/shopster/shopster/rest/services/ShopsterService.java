package in.co.shopster.shopster.rest.services;


import java.util.List;

import in.co.shopster.shopster.rest.models.Customer;
import in.co.shopster.shopster.rest.models.CustomerLogin;
import in.co.shopster.shopster.rest.models.DeliverySpecification;
import in.co.shopster.shopster.rest.models.LoginCredentials;
import in.co.shopster.shopster.rest.models.Order;
import in.co.shopster.shopster.rest.models.PaymentDetails;
import in.co.shopster.shopster.rest.models.Product;
import in.co.shopster.shopster.rest.models.Recharge;
import in.co.shopster.shopster.rest.models.responses.ShopsterGenericResponse;
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

    @POST("/api/checkout/")
    Call<Order> placeOrder(@Header("Authorization") String authToken, @Body Order order);

    @GET("/api/user/{email}")
    Call<CustomerLogin> getUserDetails(@Header("Authorization") String authToken, @Path("email") String email);

    @GET("/api/products/{hash}")
    Call<Product> getProductDetails(@Header("Authorization") String authToken, @Path("hash") String hash);

    @GET("/api/wallet/{userid}")
    Call<wallet> getAmount(@Header("Authorization") String authToken,@Path("userid") Long id);

    @POST("/api/recharge/")
    Call<Recharge> rechargeCounpon(@Header("Authorization") String authToken, @Body Recharge recharge );

    @GET("/api/orders/{order_id}")
    Call<Order> getOrderById(@Header("Authorization") String authToken, @Path("order_id") String orderId);

    @POST("/api/pay/")
    Call<ShopsterGenericResponse> payForOrder(@Header("Authorization") String authToken, @Body PaymentDetails paymentDetails);

    @POST("/api/delivery/")
    Call<ShopsterGenericResponse> setDeliveryPreference(@Header("Authorization") String authToken, @Body DeliverySpecification deliverySpecification);

}
