package in.co.shopster.shopster.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.co.shopster.shopster.R;
import in.co.shopster.shopster.Utilities;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.edit_email)
    EditText emailEdit;

    @Bind(R.id.edit_password)
    EditText passwordEdit;

    @Bind(R.id.btn_login)
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(allInputsValid()) {
                    Utilities.showToast(
                            "Logging into Shopster, please wait",
                            LoginActivity.this,
                            false
                    );
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
