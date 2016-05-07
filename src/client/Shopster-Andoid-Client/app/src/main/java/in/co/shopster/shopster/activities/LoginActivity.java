package in.co.shopster.shopster.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.journeyapps.barcodescanner.Util;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.co.shopster.shopster.Config;
import in.co.shopster.shopster.R;
import in.co.shopster.shopster.Utilities;
import in.co.shopster.shopster.rest.RestClient;
import in.co.shopster.shopster.rest.models.CustomerLogin;
import in.co.shopster.shopster.rest.models.LoginCredentials;
import in.co.shopster.shopster.rest.models.responses.ShopsterToken;
import in.co.shopster.shopster.rest.services.ShopsterService;
import retrofit.Retrofit;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

public class LoginActivity extends AppCompatActivity {
    String email1;

    @Bind(R.id.edit_email)
    EditText emailEdit;

    @Bind(R.id.edit_password)
    EditText passwordEdit;

    @Bind(R.id.btn_login)
    Button loginBtn;

    Retrofit retrofit;

    ShopsterService shopsterService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        // Rest Client

        RestClient.init(Config.getShopsterApiHost());

        this.retrofit = RestClient.getRetrofit();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(allInputsValid()) {
                    Utilities.writeDebugLog("Host : "+Config.getShopsterApiHost());
                    Utilities.showToast(
                            "Logging into Shopster, please wait",
                            LoginActivity.this,
                            false
                    );
                    Config.enableDebugLogs();
                    LoginActivity.this.shopsterService =   retrofit.create(ShopsterService.class);
                    final String email = emailEdit.getText().toString();
                    email1=email;
                    final String password = passwordEdit.getText().toString();

                    LoginCredentials userLoginCredentials = new LoginCredentials(email, password);

                    Call<ShopsterToken> fetchTokenCall = LoginActivity.this
                            .shopsterService.login(userLoginCredentials);

                    fetchTokenCall.enqueue(new Callback<ShopsterToken>() {
                        @Override
                        public void onResponse(Response<ShopsterToken> response, Retrofit retrofit) {
                            if(response.code() == 200) {
                                Utilities.writeDebugLog("User logged in successfully");
                                ShopsterToken st  = (ShopsterToken)response.body();
                                Utilities.writeDebugLog("User token : " + st.toString());
                                Utilities.showToast("Login successful !!!", LoginActivity.this.getApplicationContext(), true);
                                Utilities.setSharedPreference(LoginActivity.this.getApplicationContext(), Config.getShopsterTokenKey(), st.getToken());
                                    getUser();

                                Intent restartShopsterIntent = new Intent(LoginActivity.this, MainActivity.class);
                                restartShopsterIntent.putExtra("Email",email);
                                LoginActivity.this.startActivity(restartShopsterIntent);
                            } else if(response.code() == 400) {
                                Utilities.writeDebugLog("Email / password combination incorrect.");
                                Utilities.showToast("Email / password combination is incorrect.", LoginActivity.this.getApplicationContext(), false);
                            } else {
                                Utilities.writeDebugLog("Unexpected response from server : response code : "+response.code());
                                Utilities.showToast("Something went wrong, please contact Shopster support.", LoginActivity.this.getApplicationContext(), false);
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Utilities.writeDebugLog("User login failed : reason : "+t.toString());
                            Utilities.showToast("Login request failed.", LoginActivity.this.getApplicationContext(), false);
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



    void getUser()
    {
        Utilities.writeDebugLog("Email"+email1);
        RestClient.init(Config.getShopsterApiHost());
        ShopsterService shopsterService = RestClient.getRetrofit().create(ShopsterService.class);

        String authToken = "Token "+Utilities.getSharedPreference(
                this.getApplicationContext(), Config.getShopsterTokenKey());
        Utilities.writeDebugLog("auth Token"+authToken);
        retrofit.Call<CustomerLogin> getUserDetail = shopsterService.getUserDetails(authToken,email1);

        getUserDetail.enqueue(new retrofit.Callback<CustomerLogin>() {
            @Override
            public void onResponse(retrofit.Response<CustomerLogin> response, Retrofit retrofit) {
                int responseCode = response.code();
                Utilities.writeDebugLog("Get User : on response : response code "+responseCode);
                switch(responseCode) {
                    case 200:
                        // success

                        Utilities.writeDebugLog("Successfully access details");
                        long userId = response.body().getId();
                        String uhash = response.body().getUhash();
                        Utilities.writeDebugLog("Id : "+userId+" UHASH : "+uhash);
                        Utilities.setSharedPreference(LoginActivity.this.getApplicationContext(), Config.getShopsterUserId(),""+userId);
                        Utilities.setSharedPreference(LoginActivity.this.getApplicationContext(), Config.getSHOPSTER_USER_Hash(),uhash);
                        break;
                    default:
                        Utilities.writeDebugLog("Unexpected response code : "+responseCode);
                        break;


                }

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });


    }
}
