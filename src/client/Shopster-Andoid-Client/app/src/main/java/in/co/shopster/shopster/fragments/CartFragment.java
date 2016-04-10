package in.co.shopster.shopster.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.co.shopster.shopster.R;
import in.co.shopster.shopster.Utilities;

/**
 * Created by vikram on 3/4/16.
 */
public class CartFragment extends Fragment {

    private View view;
    RecyclerView recList;
    Button add;
    CartAdapter ca;
    List<CartInfo> cartItems = new ArrayList();
    String title,price,quantity;
    int i=0;
    @Bind(R.id.btn_add_to_cart)
    FloatingActionButton addToCartBtn;

    private static CartFragment currentCart;


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
                    Utilities.showToast("Hello", container.getContext(), false);
                    IntentIntegrator intentIntegrator = new IntentIntegrator(
                            CartFragment.this.getActivity());
                    Utilities.writeDebugLog("Initiating QR Code scan");
                    intentIntegrator
                            .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
                            .setCameraId(0)
                            .setPrompt("Scan Customer's QR Code")
                            .setBeepEnabled(true)
                            .initiateScan();
                }
            });

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
        if(productTitle != null && !productTitle.isEmpty()) {
            CartInfo ci = new CartInfo();
            ci.price = "100";
            ci.quantity = "1";
            ci.title = scanResult.getContents();
            cartItems.add(ci);
            ca.notifyDataSetChanged();
            Utilities.showToast("Item added to cart !!!", CartFragment.this.getContext(), false);
        } else {
            Utilities.showToast("Scan failed !!!", CartFragment.this.getContext(), false);
        }


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        starting();
    }

    private List<CartInfo> createList() {

        return cartItems;
    }

    public void starting()
    {
        Log.e("Message", "super1");
        recList = (RecyclerView) getActivity().findViewById(R.id.cart);
        add=(Button)getActivity().findViewById(R.id.add);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        ca = new CartAdapter(createList());
        recList.setAdapter(ca);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = "Title" + i;
                price = "" + i;
                quantity = "" + i;
                CartInfo ci = new CartInfo();
                ci.title = title;
                ci.price = price;
                ci.quantity = quantity;
                cartItems.add(ci);
                ca.notifyDataSetChanged();
                i++;
                Utilities.writeDebugLog("Result : " + String.valueOf(cartItems));
            }
        });
    }



}
