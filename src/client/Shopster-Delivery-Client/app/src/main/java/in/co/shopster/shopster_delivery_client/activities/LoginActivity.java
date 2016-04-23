package in.co.shopster.shopster_delivery_client.activities;

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

import butterknife.Bind;
import butterknife.ButterKnife;
import in.co.shopster.shopster_delivery_client.Config;
import in.co.shopster.shopster_delivery_client.R;
import in.co.shopster.shopster_delivery_client.ShopsterNavigationDrawer;
import in.co.shopster.shopster_delivery_client.Utilities;
import in.co.shopster.shopster_delivery_client.rest.RestClient;
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
                                Utilities.showToast("Welcome to Shopster Delivery Manager", LoginActivity.this.getApplicationContext(), true);
                                Intent openNavDrawer = new Intent(LoginActivity.this, ShopsterNavigationDrawer.class);
                                LoginActivity.this.startActivity(openNavDrawer);

//                            } else if(response.body() == null) {
//                                Utilities.writeDebugLog("Login : response is null");
                            } else if(response.code() == 400) {
                                Utilities.showToast("Invalid email/password combination.", LoginActivity.this.getApplicationContext(), true);
                            } else {
                                Utilities.writeDebugLog("Login : unexpected response code : "+response.code());
                            }

                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Utilities.showToast("Login failed", LoginActivity.this.getApplicationContext(), true);
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
