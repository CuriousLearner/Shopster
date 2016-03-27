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

import com.journeyapps.barcodescanner.Util;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.co.shopster.shopster.R;
import in.co.shopster.shopster.Utilities;

public class RegistrationActivity extends AppCompatActivity {

    @Bind(R.id.edit_email)
    EditText emailEdit;

    @Bind(R.id.edit_password)
    EditText passwordEdit;

    @Bind(R.id.edit_first_name)
    EditText firstNameEdit;

    @Bind(R.id.edit_last_name)
    EditText lastNameEdit;

    @Bind(R.id.edit_age)
    EditText ageEdit;

    @Bind(R.id.edit_line_1)
    EditText lineOneEdit;

    @Bind(R.id.edit_line_2)
    EditText lineTwoEdit;

    @Bind(R.id.edit_city)
    EditText cityEdit;

    @Bind(R.id.edit_state)
    EditText stateEdit;

    @Bind(R.id.edit_zip_code)
    EditText zipCodeEdit;

    @Bind(R.id.btn_register)
    Button registerBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allInputsValid()) {
                    Utilities.showToast(
                            "Registering on Shopster, please wait ...",
                            RegistrationActivity.this,
                            false
                    );
                }
            }
        });



    }

    private boolean allInputsValid() {
        String  email = emailEdit.getText().toString(),
                password = passwordEdit.getText().toString(),
                firstName = firstNameEdit.getText().toString(),
                lineOne = lineOneEdit.getText().toString(),
                city = cityEdit.getText().toString(),
                state = stateEdit.getText().toString(),
                age = ageEdit.getText().toString(),
                zipCode = zipCodeEdit.getText().toString();


        Context ctx = RegistrationActivity.this.getApplicationContext();


        try {
            if (email.length() < 5 || !email.contains("@") || !email.contains(".")) {
                Utilities.showToast("Invalid email ...", ctx, false);
                return false;
            } else if (password.length() < 8) {
                Utilities.showToast("Selected password is too short ( min 8 chars ) ...", ctx, false);
                return false;
            } else if (firstName.isEmpty()) {
                Utilities.showToast("First name cannot be empty ...", ctx, false);
                return false;
            } else if(age.isEmpty()) {
                Utilities.showToast("Age cannot be empty ...", ctx, false);
                return false;
            } else if (Integer.parseInt(age) < 18) {
                Utilities.showToast("Persons under the age of 18 cannot use Shopster ...", ctx, false);
                return false;
            } else if (Integer.parseInt(age) > 150) {
                Utilities.showToast("Invalid age ...", ctx, false);
                return false;
            } else if (lineOne.isEmpty()) {
                Utilities.showToast("Invalid address : line one cannot be empty ...", ctx, false);
                return false;
            } else if (city.isEmpty()) {
                Utilities.showToast("Invalid address : city cannot be empty ...", ctx, false);
                return false;
            } else if (state.isEmpty()) {
                Utilities.showToast("Invalid address : state cannot be empty ...", ctx, false);
                return false;
            } else if(zipCode.isEmpty()) {
                Utilities.showToast("Invalid address : zip code cannot be empty ...", ctx, false);
                return false;
            } else if (Integer.parseInt(zipCode) < 99999 || Integer.parseInt(zipCode) > 999999) {
                Utilities.showToast("Invalid address : invalid zip code", ctx, false);
                return false;
            }
        } catch(NumberFormatException nfe) {
            Log.d("Shopster", "Caught nfe : "+ nfe.toString());
            return false;
        }



        return true;
    }

}
