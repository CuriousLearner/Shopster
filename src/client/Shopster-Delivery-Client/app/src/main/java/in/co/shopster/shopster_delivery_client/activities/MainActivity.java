package in.co.shopster.shopster_delivery_client.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.co.shopster.shopster_delivery_client.Config;
import in.co.shopster.shopster_delivery_client.R;
import in.co.shopster.shopster_delivery_client.ShopsterNavigationDrawer;
import in.co.shopster.shopster_delivery_client.Utilities;
import in.co.shopster.shopster_delivery_client.rest.RestClient;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.btn_login_to_shopster)
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // debug mode
        Config.enableDebugLogs();


        String shopsterApiKey = Utilities.getSharedPreference(MainActivity.this, Config.getShopsterTokenKey());
        Context ctx = MainActivity.this.getApplicationContext();

        if(!shopsterApiKey.isEmpty()) {
            Utilities.writeDebugLog("Shopster API Key : " + shopsterApiKey);
            Utilities.showToast("API key present, re-directing to navigation drawer", ctx, false);
            Intent openNavDrawerIntent = new Intent(MainActivity.this, ShopsterNavigationDrawer.class);
            this.startActivity(openNavDrawerIntent);
        } else {
            Utilities.showToast("Hello, delivery person, please login", ctx, false);
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                MainActivity.this.startActivity(loginIntent);
            }
        });


    }
}
