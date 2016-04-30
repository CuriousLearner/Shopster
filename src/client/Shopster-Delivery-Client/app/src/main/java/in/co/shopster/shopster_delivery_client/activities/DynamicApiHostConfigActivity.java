package in.co.shopster.shopster_delivery_client.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import in.co.shopster.shopster_delivery_client.Utilities;
import in.co.shopster.shopster_delivery_client.rest.RestClient;

public class DynamicApiHostConfigActivity extends AppCompatActivity {

    @Bind(R.id.edit_api_host_address)
    EditText apiHostAddressEdit;

    @Bind(R.id.btn_configure_host)
    Button configureHostBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_api_host_config);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        final Context ctx = DynamicApiHostConfigActivity.this.getApplicationContext();

        configureHostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String apiHostAddress = apiHostAddressEdit.getText().toString();
                if(apiHostAddress.isEmpty()) {
                    Utilities.showToast("Host address cannot be empty !!!", ctx, false);
                } else {
                    apiHostAddress = "http://" + apiHostAddress;
                    Utilities.writeDebugLog("New host address : " + apiHostAddress);
                    Config.setShopsterApiHost(apiHostAddress);
                    RestClient.init(Config.getShopsterApiHost());
                    Utilities.showToast("API host configured ...", ctx, false);
                    Intent restartAppIntent = new Intent(DynamicApiHostConfigActivity.this, MainActivity.class);
                    DynamicApiHostConfigActivity.this.startActivity(restartAppIntent);
                }
            }
        });

    }

}
