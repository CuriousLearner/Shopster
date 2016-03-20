package in.co.shopster.shopster.rest.services;

import java.util.List;

import in.co.shopster.shopster.rest.models.ExampleUser;
import retrofit.http.GET;
import retrofit.Call;

/**
 * Created by vikram on 14/3/16.
 */
public interface ExampleService {

    public final String BASE_URL = "http://www.google.co.in";

    @GET("/users")
    Call<List<ExampleUser>> getAllUsers();
}
