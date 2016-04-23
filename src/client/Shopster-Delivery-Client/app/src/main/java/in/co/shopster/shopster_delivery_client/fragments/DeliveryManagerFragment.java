package in.co.shopster.shopster_delivery_client.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.journeyapps.barcodescanner.Util;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.co.shopster.shopster_delivery_client.Config;
import in.co.shopster.shopster_delivery_client.R;
import in.co.shopster.shopster_delivery_client.Utilities;
import in.co.shopster.shopster_delivery_client.rest.RestClient;
import in.co.shopster.shopster_delivery_client.rest.models.Delivery;
import in.co.shopster.shopster_delivery_client.rest.services.ShopsterService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class DeliveryManagerFragment extends Fragment {

    @Bind(R.id.btn_refresh)
    Button refreshBtn;

    @Bind(R.id.text_delivery_status)
    TextView deliveryStatusText;

    private View view;

    ShopsterService shopsterService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(R.layout.fragment_delivery_manager, container, false);
            ButterKnife.bind(this, view);

            final Context ctx = DeliveryManagerFragment.this.getActivity().getApplicationContext();

            refreshBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(RestClient.getRetrofit() == null) {
                        RestClient.init(Config.getShopsterApiHost());
                    }
                    shopsterService = RestClient.getRetrofit().create(ShopsterService.class);

                    String authToken = "Token "+ Utilities.getSharedPreference(ctx, Config.getShopsterTokenKey());
                    String userId = Utilities.getSharedPreference(ctx, Config.getShopsterUserIdKey());

                    Call<Delivery> deliveryCheckCall =
                            shopsterService.checkDeliveryAssignment(authToken, userId);

                    deliveryCheckCall.enqueue(new Callback<Delivery>() {

                        @Override
                        public void onResponse(Response<Delivery> response, Retrofit retrofit) {
                            int responseCode = response.code();
                            Utilities.writeDebugLog(
                                    "Delivery check : response code : "+responseCode);
                            switch(responseCode) {
                                case 200 :
                                    Utilities.writeDebugLog("delivery check : 200 OK");
                                    Delivery delivery = response.body();
                                    Utilities.writeDebugLog("Delivery object : " + delivery.toString());
                                    Utilities.showToast("You've been assigned a delivery", ctx, true);
                                    deliveryStatusText.setText("Delivery assigned : "+delivery.getOrderId());
                                    break;
                                case 404 :
                                    Utilities.writeDebugLog("delivery check : 404 NOT FOUND");
                                    Utilities.showToast("No, deliveries assigned to you yet", ctx, true);
                                    deliveryStatusText.setText("Idle : no deliveries assigned");
                                    break;
                                default :
                                    Utilities.writeDebugLog(
                                            "deliver check : unexpected response code : "+responseCode);
                                    break;
                            }


                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Utilities.writeDebugLog(
                                    "Check delivery : request failed : reason : "+t.toString());
                        }
                    });



                }
            });

        }

        return view;
    }
}
