package in.co.shopster.shopster.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.co.shopster.shopster.Config;
import in.co.shopster.shopster.R;
import in.co.shopster.shopster.Utilities;
import in.co.shopster.shopster.activities.LoginActivity;
import in.co.shopster.shopster.rest.RestClient;
import in.co.shopster.shopster.rest.models.Customer;
import in.co.shopster.shopster.rest.models.Product;
import in.co.shopster.shopster.rest.models.Recharge;
import in.co.shopster.shopster.rest.models.wallet;
import in.co.shopster.shopster.rest.services.ShopsterService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by vikram on 3/4/16.
 */
public class WalletFragment extends Fragment {

    private View view;

    ShopsterService shopsterService;
    @Bind(R.id.text_wallet_balance)
    TextView walletBalanceText;

    @Bind(R.id.edit_coupon_code)
    EditText couponCodeEdit;

    @Bind(R.id.btn_recharge)
    Button rechargeBtn;

    @Nullable


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        amount();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(R.layout.fragment_wallet, container, false);
            ButterKnife.bind(this, view);

            RestClient.init(Config.getShopsterApiHost());
            rechargeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String couponCode = couponCodeEdit.getText().toString();

                    if (couponCode.equals("")) {
                        Utilities.showToast("Please fill in a coupon code ...", WalletFragment.this.getContext(), false);
                        return;
                    }
                    String userAuthToken = "Token "+Utilities.getSharedPreference(WalletFragment.this.getContext(), Config.getShopsterTokenKey());
                    WalletFragment.this.shopsterService = RestClient.getRetrofit().create(ShopsterService.class);
                    String userid = Utilities.getSharedPreference(WalletFragment.this.getContext(), Config.getShopsterUserId());
                    Recharge recharge = new Recharge(
                            userid,
                            couponCodeEdit.getText().toString()
                    );
                    Log.e("Couponcode", couponCodeEdit.getText().toString());
                    Call rechargeCall = WalletFragment.this.shopsterService.rechargeCounpon(userAuthToken,recharge);
                    rechargeCall.enqueue(new Callback() {
                                             @Override
                                             public void onResponse(Response response, Retrofit retrofit) {
                                                 if (response.code() == 200) {
                                                     Utilities.showToast("Recharge Successfull", WalletFragment.this.getActivity(), true);
                                                     Utilities.writeDebugLog("Unexpected response code : " + response.code() + "\nResponse Body : " + response.body().toString());

                                                 } else if (response.code() == 400) {
                                                     Utilities.showToast("Recharge failed !!!", WalletFragment.this.getActivity(), false);

                                                 } else {
                                                     Utilities.showToast("Something went wrong please contact Shopster support", WalletFragment.this.getActivity(), true);
                                                     Utilities.writeDebugLog("Unexpected response code : " + response.code() + "\nResponse Body : " + response.body().toString());
                                                 }
                                             }

                                             @Override
                                             public void onFailure(Throwable t) {
                                                 Utilities.showToast("Recharge failed !!!", WalletFragment.this.getActivity(), false);
                                                 Utilities.writeDebugLog("Recharge failed : reason : " + t.toString());
                                             }
                                         });

                            Utilities.writeDebugLog("Calling recharge API : couponCode ==> " + couponCode);
                    amount();
                }

            });


        }
        return view;
    }
    public void amount() {
        RestClient.init(Config.getShopsterApiHost());
        ShopsterService shopsterService = RestClient.getRetrofit().create(ShopsterService.class);
        String userAuthToken = "Token " + Utilities.getSharedPreference(WalletFragment.this.getContext(), Config.getShopsterTokenKey());
        String userid = Utilities.getSharedPreference(WalletFragment.this.getContext(), Config.getShopsterUserId());
        Utilities.writeDebugLog("UserId : "+userid);
        Call<wallet> walletCall = shopsterService.getAmount(userAuthToken,Long.parseLong(userid));
        walletCall.enqueue(new Callback<wallet>() {
            @Override
            public void onResponse(Response<wallet> response, Retrofit retrofit) {
                Utilities.showToast("Yoo !!!", WalletFragment.this.getActivity().getApplicationContext(), true);
                Utilities.writeDebugLog("Wallet response code : " + response.code());
                if (response.code() == 200) {
                    Utilities.writeDebugLog("Product found : " + response.body());
                    Utilities.writeDebugLog("Product found : " + response.raw());
                    Utilities.writeDebugLog("Product found : " + response.message());
                    Utilities.writeDebugLog("Product found : " + response.hashCode());

                    String amount1 = response.body().getBalance();
                    Utilities.writeDebugLog("Product found : " + amount1);
                    walletBalanceText.setText(amount1);
                    Utilities.showToast("Product added successfully", WalletFragment.this.getActivity().getApplicationContext(), false);
                } else {
                    Utilities.showToast("Invalid QR code !!!", WalletFragment.this.getActivity().getApplicationContext(), false);
                    Utilities.writeDebugLog("Response : " + response.raw());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Utilities.showToast("Server did not respond, scan failed !!!", WalletFragment.this.getContext(), true);
            }
        });


    }


}
