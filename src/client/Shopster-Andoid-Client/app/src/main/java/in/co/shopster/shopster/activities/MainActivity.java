package in.co.shopster.shopster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.journeyapps.barcodescanner.Util;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.co.shopster.shopster.Config;
import in.co.shopster.shopster.R;
import in.co.shopster.shopster.ShopsterNavigationDrawer;
import in.co.shopster.shopster.Utilities;
import in.co.shopster.shopster.rest.RestClient;
import in.co.shopster.shopster.rest.models.CustomerLogin;
import in.co.shopster.shopster.rest.models.ExampleUser;
import in.co.shopster.shopster.rest.models.Order;
import in.co.shopster.shopster.rest.services.ExampleService;
import in.co.shopster.shopster.rest.services.ShopsterService;
import retrofit.Retrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.btn_login)
    Button loginBtn;

    @Bind(R.id.btn_register)
    Button registerBtn;

    @Bind(R.id.btn_test)
    Button testBtn;

    @Bind(R.id.btn_api_host_config)
    Button configureApiHostBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);


        // Enabled debug logs
        Config.enableDebugLogs();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                MainActivity.this.startActivity(loginIntent);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registrationIntent = new Intent(MainActivity.this, RegistrationActivity.class);
                MainActivity.this.startActivity(registrationIntent);
            }
        });

        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openNavigationDrawer = new Intent(MainActivity.this, ShopsterNavigationDrawer.class);
                MainActivity.this.startActivity(openNavigationDrawer);
            }
        });

        configureApiHostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent configureApiHostIntent = new Intent(MainActivity.this, APIConfigActivity.class);
                MainActivity.this.startActivity(configureApiHostIntent);
            }
        });

        // check login
        redirectIfLoggedIn();
    }

    public void redirectIfLoggedIn() {
        String userApiToken = Utilities.getSharedPreference(this.getApplicationContext(), Config.getShopsterTokenKey());
        if(!userApiToken.isEmpty()) {
            Utilities.writeDebugLog("User is logged in : API Key : "+userApiToken);
            Intent openNavigationDrawerIntent = new Intent(this, ShopsterNavigationDrawer.class);
            this.startActivity(openNavigationDrawerIntent);
            Utilities.showToast("Welcome to Shopster !!!", this.getApplicationContext(), true);
        } else {
            Utilities.writeDebugLog("User is not logged in.");
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        redirectIfLoggedIn();
    }
}
