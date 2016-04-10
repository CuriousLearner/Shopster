package in.co.shopster.shopster.activities;

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
import in.co.shopster.shopster.Config;
import in.co.shopster.shopster.R;
import in.co.shopster.shopster.Utilities;
import in.co.shopster.shopster.rest.services.ShopsterService;

public class APIConfigActivity extends AppCompatActivity {

    @Bind(R.id.edit_api_host)
    EditText apiHostEdit;

    @Bind(R.id.btn_configure)
    Button configureBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apiconfig);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        configureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!apiHostEdit.getText().toString().isEmpty()) {
                    Config.setShopsterApiHost("http://"+apiHostEdit.getText().toString());
                    Utilities.showToast("API host has been setup !!!", APIConfigActivity.this.getApplicationContext(), false);
                } else {
                    Utilities.showToast("API host cannot be empty !!!", APIConfigActivity.this.getApplicationContext(), false);
                }
            }
        });

    }

}
