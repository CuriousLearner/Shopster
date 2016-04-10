package in.co.shopster.shopster.activities;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.journeyapps.barcodescanner.Util;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.co.shopster.shopster.Config;
import in.co.shopster.shopster.R;
import in.co.shopster.shopster.Utilities;
import in.co.shopster.shopster.rest.RestClient;
import in.co.shopster.shopster.rest.models.Address;
import in.co.shopster.shopster.rest.models.Customer;
import in.co.shopster.shopster.rest.services.ShopsterService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

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

    @Bind(R.id.spin_gender)
    Spinner genderSpinner;

    @Bind(R.id.edit_contact_number)
    EditText contactNumberEdit;

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

    ShopsterService shopsterService;

    private char gender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        String[] genderList = this.getResources().getStringArray(R.array.genders);

        genderSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, genderList));

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String gender = (String) adapterView.getItemAtPosition(i);
                RegistrationActivity.this.gender = (gender.equals("Male")) ? 'M' : 'F';
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allInputsValid()) {
                    Utilities.showToast(
                            "Registering on Shopster, please wait ...",
                            RegistrationActivity.this,
                            false
                    );


                    long zipCode = (zipCodeEdit.getText().toString().isEmpty()) ?
                            null : Long.parseLong(zipCodeEdit.getText().toString());
                    Address address = new Address(
                            lineOneEdit.getText().toString(),
                            lineTwoEdit.getText().toString(),
                            cityEdit.getText().toString(),
                            stateEdit.getText().toString(),
                            zipCode
                    );

                    byte age = (!ageEdit.getText().toString().isEmpty())  ?
                            Byte.parseByte(ageEdit.getText().toString()) : null;

                    long contactNumber = (!contactNumberEdit.getText().toString().isEmpty()) ?
                            Long.parseLong(contactNumberEdit.getText().toString()) : null;

                    Customer customer = new Customer(
                            firstNameEdit.getText().toString(),
                            lastNameEdit.getText().toString(),
                            emailEdit.getText().toString(),
                            passwordEdit.getText().toString(),
                            age,
                            RegistrationActivity.this.gender,
                            address,
                            contactNumber
                    );

                    Utilities.writeDebugLog("Customer object : \n" + customer.toString());

                    RestClient.init(Config.getDebugLogTag());
                    RegistrationActivity.this.shopsterService = RestClient.getRetrofit().create(ShopsterService.class);

                    Call registerCall = RegistrationActivity.this.shopsterService.register(customer);
                    registerCall.enqueue(new Callback() {
                        @Override
                        public void onResponse(Response response, Retrofit retrofit) {
                            if(response.code() == 201) {
                                Utilities.showToast("Registration successful, please log in.", RegistrationActivity.this.getApplicationContext(), true);
                                Intent loginIntent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                RegistrationActivity.this.startActivity(loginIntent);
                            } else if (response.code() == 400) {
                                Utilities.showToast("Registration failed !!!", RegistrationActivity.this.getApplicationContext(), false);
                                try {
                                    Utilities.writeDebugLog("Registration failed : reason : "+response.errorBody().string());
                                } catch(IOException ioe) {
                                    Utilities.writeDebugLog("Registration failed : reason unknown");
                                }

                            } else {
                                Utilities.showToast("Something went wrong please contact Shopster support", RegistrationActivity.this.getApplicationContext(), true);
                                Utilities.writeDebugLog("Unexpected response code : "+response.code()+"\nResponse Body : "+response.body().toString());
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Utilities.showToast("Registration failed !!!", RegistrationActivity.this.getApplicationContext(), false);
                            Utilities.writeDebugLog("Registeration failed : reason : "+t.toString());
                        }
                    });

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
                contactNumber = contactNumberEdit.getText().toString(),
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
            } else if(contactNumber.length() != 10) {
                Utilities.showToast("Invalid contact number, only 10 digit contact numbers are acceptable", ctx, true);
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
