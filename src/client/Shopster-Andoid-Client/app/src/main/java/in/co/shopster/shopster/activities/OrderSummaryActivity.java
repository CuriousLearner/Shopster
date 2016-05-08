package in.co.shopster.shopster.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.journeyapps.barcodescanner.Util;

import org.w3c.dom.Text;

import java.io.OptionalDataException;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.co.shopster.shopster.Config;
import in.co.shopster.shopster.R;
import in.co.shopster.shopster.ShopsterNavigationDrawer;
import in.co.shopster.shopster.Utilities;
import in.co.shopster.shopster.rest.RestClient;
import in.co.shopster.shopster.rest.models.DeliverySpecification;
import in.co.shopster.shopster.rest.models.Order;
import in.co.shopster.shopster.rest.models.PaymentDetails;
import in.co.shopster.shopster.rest.models.Product;
import in.co.shopster.shopster.rest.models.Recharge;
import in.co.shopster.shopster.rest.models.responses.ShopsterGenericResponse;
import in.co.shopster.shopster.rest.services.ShopsterService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class OrderSummaryActivity extends AppCompatActivity {

    @Bind(R.id.text_order_id)
    TextView orderIdText;

    @Bind(R.id.text_order_price)
    TextView orderPriceText;

    @Bind(R.id.text_order_status)
    TextView orderStatusText;

    @Bind(R.id.btn_refresh)
    Button refreshBtn;

    @Bind(R.id.btn_pay)
    Button payBtn;

    @Bind(R.id.btn_take)
    Button takePackageBtn;

    @Bind(R.id.btn_deliver)
    Button deliverPackageBtn;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);
        ButterKnife.bind(this);

        final Context ctx = this.getApplicationContext();

        initViews(ctx);


        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilities.showToast("Refreshing order status", ctx, false);
                String orderId = Utilities.getSharedPreference(ctx, Config.getShopsterOrderIdKey());
                String authToken = Utilities.getSharedPreference(ctx, Config.getShopsterTokenKey());
                Utilities.writeDebugLog("Using api key  : " + authToken);
                RestClient.init(Config.getShopsterApiHost());
                ShopsterService shopsterService = RestClient.getRetrofit().create(ShopsterService.class);

                Call<Order> getOrderCall = shopsterService.getOrderById("Token " + authToken, orderId);
                progressDialog = ProgressDialog.show(OrderSummaryActivity.this, "Refeshing order", "Contacting server ...", false);
                getOrderCall.enqueue(new Callback<Order>() {
                    @Override
                    public void onResponse(Response<Order> response, Retrofit retrofit) {
                        progressDialog.dismiss();
                        Utilities.writeDebugLog("On response : " + response.code());
                        int statusCode = response.code();
                        switch (statusCode) {
                            case 200:
                                // success
                                Order order = response.body();
                                Utilities.setSharedPreference(ctx, Config.getShopsterOrderIdKey(), order.getOrderId() + "");
                                Utilities.setSharedPreference(ctx, Config.getShopsterOrderPriceKey(), order.getPrice() + "");
                                Utilities.setSharedPreference(ctx, Config.getShopsterOrderStatusKey(), order.getStatus() + "");
                                initViews(ctx);
                                checkStatus(ctx);
                                Utilities.showToast("Order refreshed successfully", ctx, false);
                                break;
                            default:
                                Utilities.showToast("Unexpected response from server !!!", ctx, false);
                                Utilities.writeDebugLog("Unexpected response code : " + statusCode);
                                break;
                        }


                    }

                    @Override
                    public void onFailure(Throwable t) {
                        progressDialog.dismiss();
                        Utilities.showToast("Refresh request failed", ctx, false);
                        Utilities.writeDebugLog("Refresh request failed : reason : " + t.toString());
                    }
                });


            }
        });

        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String userId = Utilities.getSharedPreference(ctx, Config.getShopsterUserId());
                String orderId = Utilities.getSharedPreference(ctx, Config.getShopsterOrderIdKey());

                String authToken = Utilities.getSharedPreference(ctx, Config.getShopsterTokenKey());

                try {

                    PaymentDetails paymentDetails =
                            new PaymentDetails(Long.parseLong(orderId), Long.parseLong(userId));
                    RestClient.init(Config.getShopsterApiHost());
                    ShopsterService shopsterService = RestClient.getRetrofit().create(ShopsterService.class);

                    Call<ShopsterGenericResponse> paymentCall = shopsterService.payForOrder("Token "+authToken, paymentDetails);
                    progressDialog = ProgressDialog.show(OrderSummaryActivity.this, "Prrocessing payment", "Contacting server ...", false);
                    paymentCall.enqueue(new Callback<ShopsterGenericResponse>() {
                        @Override
                        public void onResponse(Response<ShopsterGenericResponse> response, Retrofit retrofit) {
                            progressDialog.dismiss();
                            String message = response.body().getMessage();
                            Utilities.writeDebugLog("onResponse : message : "+message);
                            if(message.contains("deducted")) {
                                // success
                                Utilities.showToast("Payment successful !!!", ctx, false);
                                payBtn.setVisibility(View.GONE);
                                //takePackageBtn.setVisibility(View.VISIBLE);
                                //deliverPackageBtn.setVisibility(View.VISIBLE);
                                refreshBtn.setVisibility(View.VISIBLE);

                            } else if(message.contains("re-charge")) {
                                // needs recharge
                                Utilities.showToast("Wallet balance is low. Please recharge.", ctx, false);

                                rechargeDialog();



                            }

                        }

                        @Override
                        public void onFailure(Throwable t) {
                            progressDialog.dismiss();
                            Utilities.showToast("Could not contact server", ctx, false);
                            Utilities.writeDebugLog("onFailure : reason : "+t.toString());
                        }
                    });






                } catch (NumberFormatException nfe) {
                    Utilities.writeDebugLog("Exception : nfe : "+nfe.toString());
                }



            }
        });

        takePackageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Utilities.showToast("Please wait for the products to be packaged, you can then take the package from the counter", ctx, true);
//                refreshBtn.setVisibility(View.GONE);
//                deliverPackageBtn.setVisibility(View.GONE);
//                takePackageBtn.setVisibility(View.GONE);
                //@TODO: /api/delivery
                String orderId = Utilities.getSharedPreference(ctx, Config.getShopsterOrderIdKey());
                final String deliveryPreference = "I";
                String authToken =  Utilities.getSharedPreference(ctx, Config.getShopsterTokenKey());

                RestClient.init(Config.getShopsterApiHost());
                ShopsterService shopsterService = RestClient.getRetrofit().create(ShopsterService.class);

                DeliverySpecification deliverySpecification = new DeliverySpecification(Long.parseLong(orderId), deliveryPreference);

                Call<ShopsterGenericResponse> deliverySpecCall = shopsterService.setDeliveryPreference("Token "+authToken, deliverySpecification);

                progressDialog = ProgressDialog.show(OrderSummaryActivity.this, "Setting delivery preference : IN-HAND ", "Contacting server ...", false);
                deliverySpecCall.enqueue(new Callback<ShopsterGenericResponse>() {
                    @Override
                    public void onResponse(Response<ShopsterGenericResponse> response, Retrofit retrofit) {
                        progressDialog.dismiss();
                        String message = "---";
                        if(response.body() != null)
                            message = response.body().getMessage();
                        Utilities.writeDebugLog("Message : "+message);
                        int responseCode = response.code();
                        Utilities.writeDebugLog("Response code : "+responseCode);
                        switch(responseCode) {
                            case 200:
                                Utilities.showToast("Please take the package, from the counter", ctx, false);
                                Intent restartIntent = new Intent(OrderSummaryActivity.this, MainActivity.class);
                                Utilities.setSharedPreference(OrderSummaryActivity.this, Config.getShopsterDeliveryPrefKey(), "");
                                Utilities.setSharedPreference(OrderSummaryActivity.this,Config.getShopsterOrderIdKey(),"");
                                Utilities.setSharedPreference(OrderSummaryActivity.this,Config.getShopsterOrderPriceKey(),"");
                                Utilities.setSharedPreference(OrderSummaryActivity.this,Config.getShopsterOrderStatusKey(),"");
                                OrderSummaryActivity.this.startActivity(restartIntent);
                                OrderSummaryActivity.this.finish();
                                break;
                            case 403:
                                Utilities.showToast("Order is not package yet, please wait ...", ctx, false);
                                break;
                            default:
                                Utilities.showToast("Unexpected server response", ctx, false);
                                Utilities.writeDebugLog("Unexpected response code : "+responseCode);
                                break;
                        }

                    }

                    @Override
                    public void onFailure(Throwable t) {
                        progressDialog.dismiss();
                        Utilities.showToast("Could not contact sever, delivery specification failed ...", ctx, true);
                        Utilities.writeDebugLog("Delivery spec failure : reason : "+t.toString());
                    }
                });

                Utilities.setSharedPreference(ctx, Config.getShopsterDeliveryPrefKey(), "I");

            }
        });

        deliverPackageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Utilities.showToast("Your package will be delivered to your home", ctx, false);
//                refreshBtn.setVisibility(View.VISIBLE);
//                deliverPackageBtn.setVisibility(View.GONE);
//                takePackageBtn.setVisibility(View.GONE);
                //@TODO: /api/delivery
                String orderId = Utilities.getSharedPreference(ctx, Config.getShopsterOrderIdKey());
                final String deliveryPreference = "H";
                String authToken =  Utilities.getSharedPreference(ctx, Config.getShopsterTokenKey());

                RestClient.init(Config.getShopsterApiHost());
                ShopsterService shopsterService = RestClient.getRetrofit().create(ShopsterService.class);

                DeliverySpecification deliverySpecification = new DeliverySpecification(Long.parseLong(orderId), deliveryPreference);

                Call<ShopsterGenericResponse> deliverySpecCall = shopsterService.setDeliveryPreference("Token "+authToken, deliverySpecification);

                progressDialog = ProgressDialog.show(OrderSummaryActivity.this, "Setting delivery preference : HOME-DELIVERY", "Contacting server ...", false);
                deliverySpecCall.enqueue(new Callback<ShopsterGenericResponse>() {
                    @Override
                    public void onResponse(Response<ShopsterGenericResponse> response, Retrofit retrofit) {
                        progressDialog.dismiss();

                        String message = "---";
                        if(response.body() != null)
                            message = response.body().getMessage();
                        Utilities.writeDebugLog("Message : " + message);
                        int responseCode = response.code();
                        Utilities.writeDebugLog("Response code : " + responseCode);
                        switch (responseCode) {
                            case 200:
                                Utilities.showToast("Your order will be delivered to your home.", ctx, false);
                               // Intent restartIntent = new Intent(OrderSummaryActivity.this, MainActivity.class);
                                Intent restartIntent = new Intent(OrderSummaryActivity.this, HomeDeliveryActivity.class);
                                OrderSummaryActivity.this.startActivity(restartIntent);
                                OrderSummaryActivity.this.finish();
                                refreshBtn.setVisibility(View.VISIBLE);
                                deliverPackageBtn.setVisibility(View.GONE);
                                takePackageBtn.setVisibility(View.GONE);
                                break;
                            case 403:
                                Utilities.showToast("Order is not package yet, please wait ...", ctx, false);
                                break;
                            default:
                                Utilities.showToast("Unexpected server response", ctx, false);
                                Utilities.writeDebugLog("Unexpected response code : " + responseCode);
                                break;
                        }

                    }

                    @Override
                    public void onFailure(Throwable t) {
                        progressDialog.dismiss();
                        Utilities.showToast("Could not contact sever, delivery specification failed ...", ctx, true);
                        Utilities.writeDebugLog("Delivery spec failure : reason : " + t.toString());
                    }
                });




                Utilities.setSharedPreference(ctx, Config.getShopsterDeliveryPrefKey(), "H");



                Utilities.setSharedPreference(ctx, Config.getShopsterDeliveryPrefKey(), "H");
            }
        });


    }

    private void initViews(Context ctx) {
        String orderId = Utilities.getSharedPreference(ctx, Config.getShopsterOrderIdKey());
        String orderPrice = Utilities.getSharedPreference(ctx, Config.getShopsterOrderPriceKey());
        String orderStatus = Utilities.getSharedPreference(ctx, Config.getShopsterOrderStatusKey());
        orderIdText.setText(orderId);
        orderPriceText.setText(orderPrice);
        orderStatusText.setText(orderStatus);
    }

    private void checkStatus(Context ctx) {
        String orderStatus = Utilities.getSharedPreference(ctx, Config.getShopsterOrderStatusKey());
        switch(orderStatus) {
            case "O" :
                Utilities.showToast("Order not packaged yet !!!", ctx, false);
                break;
            case "P" :
                Utilities.showToast("Order has been packaged !!!", ctx, false);
                Utilities.showToast("Please select a delivery type !!!", ctx, false);
                takePackageBtn.setVisibility(View.VISIBLE);
                deliverPackageBtn.setVisibility(View.VISIBLE);
                break;
            case "T" :
                Utilities.showToast("Order is in transit !!!", ctx, false);
                break;
        }

    }

    void rechargeDialog()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderSummaryActivity.this);

        alertDialog.setTitle("LOW BALANCE: RECHARGE");

        alertDialog.setMessage("Enter Coupon Code");

        final EditText input = new EditText(OrderSummaryActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
       // final String couponCode = input.getText().toString();

        alertDialog.setPositiveButton("ENTER",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        final String couponCode = input.getText().toString();
                        Utilities.writeDebugLog("Coupon Code : "+couponCode);
                        recharge(couponCode);
                    }
                });

        alertDialog.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }


    void recharge(String couponCode)
    {
        if (couponCode.equals("")) {
            Utilities.showToast("Please fill in a coupon code ...", this.getApplicationContext(), false);
            return;
        }
        String userAuthToken = "Token "+Utilities.getSharedPreference(this.getApplicationContext(), Config.getShopsterTokenKey());
        ShopsterService shopsterService = RestClient.getRetrofit().create(ShopsterService.class);
        String userid = Utilities.getSharedPreference(this.getApplicationContext(), Config.getShopsterUserId());
        Recharge recharge = new Recharge(
                userid,
                couponCode
        );

        Call rechargeCall = shopsterService.rechargeCounpon(userAuthToken,recharge);
        rechargeCall.enqueue(new Callback() {
            @Override
            public void onResponse(Response response, Retrofit retrofit) {
                if (response.code() == 200) {
                    Utilities.showToast("Recharge Successfull", OrderSummaryActivity.this, true);
                    Utilities.writeDebugLog("Unexpected response code : " + response.code() + "\nResponse Body : " + response.body().toString());

                } else if (response.code() == 400) {
                    Utilities.showToast("Recharge failed !!!",OrderSummaryActivity.this, false);

                } else {
                    Utilities.showToast("Something went wrong please contact Shopster support", OrderSummaryActivity.this, true);
                    Utilities.writeDebugLog("Unexpected response code : " + response.code() + "\nResponse Body : " + response.body().toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Utilities.showToast("Recharge failed !!!", OrderSummaryActivity.this, false);
                Utilities.writeDebugLog("Recharge failed : reason : " + t.toString());
            }
        });

        Utilities.writeDebugLog("Calling recharge API : couponCode ==> " + couponCode);
    }


}
