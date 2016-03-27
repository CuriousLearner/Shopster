package in.co.shopster.shopster.rest;

import com.google.gson.GsonBuilder;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by vikram on 13/3/16.
 */
public class RestClient {


    private static GsonConverterFactory gsonConverterFactory;

    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {
        return RestClient.retrofit;
    }

    public static void init(String baseUrl) {
        RestClient.gsonConverterFactory = GsonConverterFactory.create(new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create());


        RestClient.retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(RestClient.gsonConverterFactory)
                .build();
    }




}
