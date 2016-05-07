package in.co.shopster.shopster.fragments;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.co.shopster.shopster.Config;
import in.co.shopster.shopster.R;
import in.co.shopster.shopster.Utilities;
import in.co.shopster.shopster.activities.SelectScannerActivity;
import in.co.shopster.shopster.rest.RestClient;
import in.co.shopster.shopster.rest.models.Order;
import in.co.shopster.shopster.rest.models.OrderItem;
import in.co.shopster.shopster.rest.models.Product;
import in.co.shopster.shopster.rest.services.ShopsterService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.POST;

/**
 * Created by vikram on 3/4/16.
 */
public class CartFragment extends Fragment {
    public static String[] Q = new String[50];
    private View view;
    RecyclerView recList;
    Button submit;
    CartAdapter ca;
    ArrayList<Product> products = new ArrayList<Product>();
    List<CartInfo> cartItems = new ArrayList();
    ArrayList<Product> orderItems = new ArrayList<>();
    String title,price,quantity;
    int i=0;
    @Bind(R.id.btn_add_to_cart)
    FloatingActionButton addToCartBtn;

    private static CartFragment currentCart;


    NfcAdapter mAdapter;
    PendingIntent mPendingIntent;

    private boolean isNfcSupported = false;


    public static CartFragment getInstance() { return currentCart; }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(R.layout.fragment_cart, container, false);
            ButterKnife.bind(this, view);

            addToCartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent openSelectorIntent = new Intent(CartFragment.this.getActivity(), SelectScannerActivity.class);
                    openSelectorIntent.putExtra("isNfcSupported", CartFragment.this.isNfcSupported);
                    CartFragment.this.getActivity().startActivity(openSelectorIntent);

//                    Utilities.showToast("Scan the product QR code", container.getContext(), false);
//                    IntentIntegrator intentIntegrator = new IntentIntegrator(
//                            CartFragment.this.getActivity());
//                    Utilities.writeDebugLog("Initiating QR Code scan");
//                    intentIntegrator
//                            .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
//                            .setCameraId(0)
//                            .setPrompt("Scan Product QR Code")
//                            .setBeepEnabled(true)
//                            .initiateScan();
                }
            });

            Context ctx = this.getActivity().getApplicationContext();

            mAdapter = NfcAdapter.getDefaultAdapter(this.getActivity().getApplicationContext());
            if (mAdapter == null) {
                //nfc not support your device.
                Utilities.showToast("NFC is not supported on this device", ctx, true);
                Utilities.writeDebugLog("NFC not supported");
            } else {
                Utilities.showToast("NFC is supported :) ", ctx, true);
                Utilities.writeDebugLog("NFC is supported :)");
                this.isNfcSupported = true;
            }
            mPendingIntent = PendingIntent.getActivity(this.getContext(), 0, new Intent(this.getContext(),
                    getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        }
        currentCart = this;
        return view;
    }

    public void onQRCodeScanned(IntentResult scanResult) {
        if(scanResult == null) {
            Utilities.writeDebugLog("Scan result is null");
            return;
        }


        Utilities.writeDebugLog("Scan Result : Error correction level : " + scanResult.getErrorCorrectionLevel());
        Utilities.writeDebugLog("Scan Result : Contents : " + scanResult.getContents());
        Utilities.writeDebugLog("Scan Result : Format name : " + scanResult.getFormatName());
        Utilities.writeDebugLog("Scan Result : Orientation : " + scanResult.getOrientation());
        Utilities.writeDebugLog("Scan Result : Raw bytes : " + scanResult.getRawBytes());
        String productTitle = scanResult.getContents();
        //@TODO: Continue from here, call API, add product to list

        RestClient.init(Config.getShopsterApiHost());

        ShopsterService shopsterService = RestClient.getRetrofit().create(ShopsterService.class);

        String userAuthToken = "Token "+Utilities.getSharedPreference(CartFragment.this.getContext(), Config.getShopsterTokenKey()),
               productHash = scanResult.getContents();

        Call<Product> productCall =  shopsterService.getProductDetails(userAuthToken, productHash);
        productCall.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Response<Product> response, Retrofit retrofit) {
                Utilities.showToast("Yoo !!!", CartFragment.this.getActivity().getApplicationContext(), true);
                Utilities.writeDebugLog("Product response code : "+response.code());
                if(response.code() == 200) {
                    Utilities.writeDebugLog("Product found : "+response.body());
                    Utilities.writeDebugLog("Product found : "+response.raw());
                    Utilities.writeDebugLog("Product found : "+response.message());
                    Utilities.writeDebugLog("Product found : "+response.hashCode());
                    orderItems.add(response.body());
                    products.add(response.body());
                    Utilities.writeDebugLog("List of products : " + orderItems.toString());
                    ca.notifyDataSetChanged();
                    Utilities.showToast("Product added successfully", CartFragment.this.getActivity().getApplicationContext(), false);
                } else {
                    Utilities.showToast("Invalid QR code !!!", CartFragment.this.getActivity().getApplicationContext(), false);
                    Utilities.writeDebugLog("Response : " + response.raw());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Utilities.showToast("Server did not respond, scan failed !!!", CartFragment.this.getContext(), true);
            }
        });


//        if(productTitle != null && !productTitle.isEmpty()) {
//            CartInfo ci = new CartInfo();
//            ci.price = "100";
//            ci.quantity = "1";
//            ci.title = scanResult.getContents();
//            cartItems.add(ci);
//            ca.notifyDataSetChanged();
//            Utilities.showToast("Item added to cart !!!", CartFragment.this.getContext(), false);
//        } else {
//            Utilities.showToast("Scan failed !!!", CartFragment.this.getContext(), false);
//        }


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        starting();
    }

    private List<Product> createList() {
          return orderItems;
//        return cartItems;
    }

    public void starting()
    {
        Log.e("Message", "super1");
        recList = (RecyclerView) getActivity().findViewById(R.id.cart);
        submit=(Button)getActivity().findViewById(R.id.submit);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        ca = new CartAdapter(createList(),Q);
        recList.setAdapter(ca);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitOrder();
            }
        });
    }

    public void submitOrder()
    {

        String[] quantities = ca.allquant();
        int l = ca.getItemCount();
        for(int i=0;i<l;i++) {
            Log.e("allQuantities", quantities[i]);
            long k = Long.parseLong(quantities[i]);
            Product prod = products.get(i);
            prod.setQuantity(k);
            prod.status = 'O';
        }
        Log.e("order", products.toString());
        Order order = new Order("2", products, 0, 0, null, false, 'O');
        Utilities.writeDebugLog("Order created : "+order.toString());

        RestClient.init(Config.getShopsterApiHost());
        ShopsterService shopsterService = RestClient.getRetrofit().create(ShopsterService.class);

        String authToken = "Token "+Utilities.getSharedPreference(
                this.getActivity().getApplicationContext(), Config.getShopsterTokenKey());




        Call<Order> placeOrderCall = shopsterService.placeOrder(authToken, order);



        placeOrderCall.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Response<Order> response, Retrofit retrofit) {

                int responseCode = response.code();
                Utilities.writeDebugLog("Place order : on response : response code "+responseCode);
                switch(responseCode) {
                    case 200:
                        // success
                        break;
                    case 201:
                        Utilities.writeDebugLog("Order Created ");
                        char status = response.body().getStatus();
                        long order_id = response.body().getOrderId();
                        long price = response.body().getPrice();
                        Utilities.writeDebugLog("Status : "+status+" Order Id : "+order_id+ " price : "+price);
                        break;
                    default:
                        Utilities.writeDebugLog("Unexpected response code : "+responseCode);
                        break;
                }

            }

            @Override
            public void onFailure(Throwable t) {
                Utilities.writeDebugLog("Place order failed : reason : "+t.toString());
            }
        });
    }


    void balanceDeduction()
    {
        
    }


}
