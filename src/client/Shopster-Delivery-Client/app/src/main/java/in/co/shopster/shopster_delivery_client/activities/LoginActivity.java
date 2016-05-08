package in.co.shopster.shopster_delivery_client.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.journeyapps.barcodescanner.Util;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.co.shopster.shopster_delivery_client.Config;
import in.co.shopster.shopster_delivery_client.R;
import in.co.shopster.shopster_delivery_client.ShopsterNavigationDrawer;
import in.co.shopster.shopster_delivery_client.Utilities;
import in.co.shopster.shopster_delivery_client.rest.RestClient;
import in.co.shopster.shopster_delivery_client.rest.models.Customer;
import in.co.shopster.shopster_delivery_client.rest.models.LoginCredentials;
import in.co.shopster.shopster_delivery_client.rest.models.responses.ShopsterToken;
import in.co.shopster.shopster_delivery_client.rest.services.ShopsterService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.edit_email)
    EditText emailEdit;

    @Bind(R.id.edit_password)
    EditText passwordEdit;


    @Bind(R.id.btn_login)
    Button loginBtn;

    ShopsterService shopsterService;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        RestClient.init(Config.getShopsterApiHost());
        shopsterService = RestClient.getRetrofit().create(ShopsterService.class);

        ButterKnife.bind(this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(allInputsValid()) {
                    Utilities.writeDebugLog("All inputs are valid");
                    Call<ShopsterToken> loginCall = shopsterService.login(new LoginCredentials(
                            emailEdit.getText().toString(),
                            passwordEdit.getText().toString()
                    ));

                    final Context ctx = LoginActivity.this.getApplicationContext();
                    progressDialog = ProgressDialog.show(LoginActivity.this, "Logging in", "Contacting server ...", false);
                    loginCall.enqueue(new Callback<ShopsterToken>() {
                        @Override
                        public void onResponse(Response<ShopsterToken> response, Retrofit retrofit) {
                            Utilities.writeDebugLog("Login response code : "+response.code());
                            ShopsterToken shopsterToken = response.body();
                            if(response.code() == 200  && shopsterToken != null) {
                                String apiToken = response.body().getToken();
                                Utilities.setSharedPreference(
                                        LoginActivity.this.getApplicationContext(),
                                        Config.getShopsterTokenKey(),
                                        apiToken
                                );
                                Utilities.writeDebugLog("API token received : " + apiToken);
                                Utilities.writeDebugLog("Calling : getUserByEmail API");
                                String shopsterApiToken = Utilities.getSharedPreference(ctx, Config.getShopsterTokenKey());

                                Call<Customer> userCall = shopsterService.getUserByEmail("Token "+shopsterApiToken,emailEdit.getText().toString());
                                userCall.enqueue(new Callback<Customer>() {
                                    @Override
                                    public void onResponse(Response<Customer> response, Retrofit retrofit) {
                                        progressDialog.dismiss();
                                        Utilities.writeDebugLog("Get user by email : onResponse");
                                        Customer customer = response.body();
                                        Utilities.writeDebugLog("Get user by email : response code : " + response.code());
                                        Utilities.writeDebugLog("Customer object : \n" + customer.toString());

                                        if (response.code() == 200) {
                                            String  userHash = customer.getUserHash(),
                                                    userId = customer.getId()+"";

                                            Utilities.writeDebugLog("User ID received : "+userId);
                                            Utilities.writeDebugLog("User hash received : "+userHash);
                                            Utilities.setSharedPreference(ctx, Config.getShopsterUserIdKey(), userId);
                                            Utilities.setSharedPreference(ctx, Config.getShopsterUserHashKey(), userHash);

                                            Utilities.showToast("Welcome to Shopster Delivery Manager", LoginActivity.this.getApplicationContext(), true);
                                            Intent openNavDrawer = new Intent(LoginActivity.this, ShopsterNavigationDrawer.class);
                                            LoginActivity.this.startActivity(openNavDrawer);


                                        } else {
                                            Utilities.writeDebugLog("Unexpected getUserByEmail API response : " + response.code());
                                            Utilities.showToast("Login failed", ctx, false);
                                        }

                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                        progressDialog.dismiss();
                                        Utilities.writeDebugLog("Get user by email : failed : reason : \n" + t.toString());
                                        Utilities.showToast("Login failed", ctx, false);
                                    }
                                });




//                            } else if(response.body() == null) {
//                                Utilities.writeDebugLog("Login : response is null");
                            } else if(response.code() == 400) {
                                progressDialog.dismiss();
                                Utilities.showToast("Invalid email/password combination.", LoginActivity.this.getApplicationContext(), true);
                            } else {
                                progressDialog.dismiss();
                                Utilities.writeDebugLog("Login : unexpected response code : "+response.code());

                                Utilities.showToast("Login failed", ctx, false);
                            }

                        }

                        @Override
                        public void onFailure(Throwable t) {
                            progressDialog.dismiss();
                            Utilities.showToast("Login failed", ctx, true);
                            Utilities.writeDebugLog("Login : request failed : \n"+t.toString());
                        }
                    });
                }
            }
        });




    }

    private boolean allInputsValid() {

        String email = emailEdit.getText().toString();

        Context ctx = LoginActivity.this.getApplicationContext();
        if(email.isEmpty()) {
            Utilities.showToast("Email cannot be empty ...", ctx, false);
            return false;
        } else if(email.length() < 5 || !email.contains("@") || !email.contains(".")) {
            Utilities.showToast("Invalid email ...", ctx, false);
            return false;
        }

        return true;
    }

}
