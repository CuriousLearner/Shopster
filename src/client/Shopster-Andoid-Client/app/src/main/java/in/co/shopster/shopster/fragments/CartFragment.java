package in.co.shopster.shopster.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.co.shopster.shopster.R;
import in.co.shopster.shopster.Utilities;

/**
 * Created by vikram on 3/4/16.
 */
public class CartFragment extends Fragment {

    private View view;

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



    }
}
