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

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.co.shopster.shopster_delivery_client.Config;
import in.co.shopster.shopster_delivery_client.R;
import in.co.shopster.shopster_delivery_client.Utilities;
import in.co.shopster.shopster_delivery_client.rest.RestClient;
import in.co.shopster.shopster_delivery_client.rest.models.Delivery;
import in.co.shopster.shopster_delivery_client.rest.models.requests.VerifyDeliveryRequest;
import in.co.shopster.shopster_delivery_client.rest.models.responses.ShopsterGenericResponse;
import in.co.shopster.shopster_delivery_client.rest.services.ShopsterService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class DeliveryManagerFragment extends Fragment {

    @Bind(R.id.btn_refresh)
    Button refreshBtn;

    @Bind(R.id.btn_verify_delivery)
    Button verifyDeliveryBtn;

    @Bind(R.id.text_delivery_status)
    TextView deliveryStatusText;

    private View view;

    ShopsterService shopsterService;

    static DeliveryManagerFragment currentFragment;


    public static DeliveryManagerFragment getInstance() {
        return currentFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null) {

            currentFragment = this;
            view = inflater.inflate(R.layout.fragment_delivery_manager, container, false);
            ButterKnife.bind(this, view);

            final Context ctx = DeliveryManagerFragment.this.getActivity().getApplicationContext();

            String assignedOrderId = Utilities.getSharedPreference(ctx, Config.getShopsterDeliveryObjOrderIdKey());
            if(!assignedOrderId.isEmpty()) {
                deliveryStatusText.setText("Assigned order ID : "+assignedOrderId);
                verifyDeliveryBtn.setVisibility(View.VISIBLE);
                refreshBtn.setVisibility(View.INVISIBLE);

                verifyDeliveryBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        IntentIntegrator intentIntegrator = new IntentIntegrator(
                                DeliveryManagerFragment.this.getActivity());
                        Utilities.writeDebugLog("Initiating QR Code scan");
                        intentIntegrator
                                .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
                                .setCameraId(0)
                                .setPrompt("Scan Customers' QR Code to verify delivery")
                                .setBeepEnabled(true)
                                .initiateScan();
                    }
                });
            }

            refreshBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    test();
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
                                    Utilities.writeDebugLog("Delivery json : "+response.raw());
                                    Utilities.writeDebugLog("Delivery object : " + delivery.toString());
                                    Utilities.showToast("You've been assigned a delivery", ctx, true);
                                    deliveryStatusText.setText("Delivery assigned : " + delivery.getOrderId());
                                    Utilities.setSharedPreference(ctx, Config.getShopsterDeliveryObjQueueIdKey(), delivery.getQueueId() + "");
                                    Utilities.setSharedPreference(ctx, Config.getShopsterDeliveryObjOrderIdKey(), delivery.getOrderId() + "");
                                    Utilities.setSharedPreference(ctx, Config.getShopsterDeliveryObjDeliveredByKey(), delivery.getDeliveredBy()+"");
                                    Utilities.setSharedPreference(ctx, Config.getShopsterDeliveryObjIsDeliveredKey(), delivery.isDelivered()+"");
                                    Utilities.setSharedPreference(ctx, Config.getShopsterDeliveryObjDeliveryTypeKey(), delivery.getDeliveryType()+"");
                                    verifyDeliveryBtn.setVisibility(View.VISIBLE);
                                    refreshBtn.setVisibility(View.GONE);
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

    private void test() {
        String jsonString = "{'a':'b', 'c':'d', 'e':'f'}";

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            Iterator iterator = jsonObject.keys();
            while(iterator.hasNext()) {
                String key = (String) iterator.next();
                Utilities.writeDebugLog("JSON ==> "+key+" : "+(String)jsonObject.get(key));
            }


        } catch(JSONException jsone) {
            Utilities.writeDebugLog("JSONException : "+jsone);
        }
    }

    public void onQRCodeScanned(IntentResult scanResult) {
        Utilities.writeDebugLog("On QR Code scanned : "+scanResult.getContents());
        Utilities.writeDebugLog("Will hit /delivery/verify/ here");

        final Context ctx = DeliveryManagerFragment.this.getActivity().getApplicationContext();

        String customerHashToken = scanResult.getContents();

        String deliveryPersonId = Utilities.getSharedPreference(ctx, Config.getShopsterUserIdKey());

        String orderId = Utilities.getSharedPreference(ctx, Config.getShopsterDeliveryObjOrderIdKey());


        VerifyDeliveryRequest verifyDeliveryRequest = new VerifyDeliveryRequest(
                Long.parseLong(orderId),
                Long.parseLong(deliveryPersonId),
                customerHashToken
        );

        RestClient.init(Config.getShopsterApiHost());

        ShopsterService shopsterService = RestClient.getRetrofit().create(ShopsterService.class);

        String authToken = "Token " + Utilities.getSharedPreference(ctx, Config.getShopsterTokenKey());

        final Call<ShopsterGenericResponse> verifyDeliveryCall =
                shopsterService.verifyDelivery(authToken, verifyDeliveryRequest);

        verifyDeliveryCall.enqueue(new Callback<ShopsterGenericResponse>() {
            @Override
            public void onResponse(Response<ShopsterGenericResponse> response, Retrofit retrofit) {
                int responseCode = response.code();
                Utilities.writeDebugLog("Verify delivery : response code : "+responseCode);
                switch(responseCode) {
                    case 200 :
                        ShopsterGenericResponse sgr = response.body();
                        String responseMessage = sgr.getMessage();
                        if(responseMessage.equals("Delivered")) {
                            Utilities.showToast("Verification successful", ctx, true);
                            Utilities.writeDebugLog("Verification succeeded");
                            // remove all delivery data
                            Utilities.removeSharedPreference(ctx, Config.getShopsterDeliveryObjIsDeliveredKey());
                            Utilities.removeSharedPreference(ctx, Config.getShopsterDeliveryObjDeliveryTypeKey());
                            Utilities.removeSharedPreference(ctx, Config.getShopsterDeliveryObjOrderIdKey());
                            Utilities.removeSharedPreference(ctx, Config.getShopsterDeliveryObjDeliveredByKey());
                            Utilities.removeSharedPreference(ctx, Config.getShopsterDeliveryObjQueueIdKey());
                            deliveryStatusText.setText("Delivery succeeded ...");
                            verifyDeliveryBtn.setVisibility(View.GONE);
                            refreshBtn.setVisibility(View.VISIBLE);


                        } else if(responseMessage.equals("Customer not matched")) {
                            Utilities.showToast("Verification failed : Customer ID mis-match", ctx, true);
                            Utilities.writeDebugLog("Customer hash mis-matched");
                        } else {
                            Utilities.writeDebugLog("Verify delivery : unexpected response message : "+responseMessage);
                            Utilities.showToast("Verification failed !!!", ctx, false);
                        }

                        break;
                    default :
                        Utilities.writeDebugLog("Verify delivery : unexpected response code");
                        Utilities.showToast("Verification failed !!!", ctx, false);
                        break;
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Utilities.writeDebugLog("Verify delivery : request failed : reason : "+t.toString());
                Utilities.showToast("Verification failed !!!", ctx, false);
            }
        });




    }
}
