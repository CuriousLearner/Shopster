package in.co.shopster.shopster_delivery_client.rest;

import com.google.gson.GsonBuilder;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by vikram on 23/4/16.
 */
public class RestClient {


    private static GsonConverterFactory gsonConverterFactory;

    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {
        return RestClient.retrofit;
    }

    public static void init(String baseUrl) {
        if(gsonConverterFactory == null) {
            gsonConverterFactory = GsonConverterFactory.create(new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create());
        }


        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(gsonConverterFactory)
                .build();
    }

}