package in.co.shopster.shopster_delivery_client.fragments;

import android.app.UiAutomation;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.journeyapps.barcodescanner.Util;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.co.shopster.shopster_delivery_client.Config;
import in.co.shopster.shopster_delivery_client.R;
import in.co.shopster.shopster_delivery_client.Utilities;
import in.co.shopster.shopster_delivery_client.activities.LoginActivity;
import in.co.shopster.shopster_delivery_client.activities.MainActivity;
import in.co.shopster.shopster_delivery_client.rest.models.Delivery;

public class SettingsFragment extends Fragment {


    @Bind(R.id.btn_logout)
    Button logoutBtn;

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(R.layout.fragment_settings, container, false);

            ButterKnife.bind(this, view);

            logStoredData(SettingsFragment.this.getActivity().getApplicationContext());

            logoutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context ctx = SettingsFragment.this.getActivity().getApplicationContext();
                    Utilities.clearAllSharedPreferences(ctx);
                    Utilities.showToast("Successfully logged out of Shopster Delivery Manager ...", ctx, true);
                    Intent restartAppIntent = new Intent(SettingsFragment.this.getActivity(), MainActivity.class);
                    SettingsFragment.this.startActivity(restartAppIntent);
                }
            });

        }
        return view;
    }


    private void logStoredData(Context ctx) {
        String shopsterApiToken = Utilities.getSharedPreference(ctx, Config.getShopsterTokenKey());
        String userId = Utilities.getSharedPreference(ctx, Config.getShopsterUserIdKey());
        String userHash = Utilities.getSharedPreference(ctx, Config.getShopsterUserHashKey());
        String queueId = Utilities.getSharedPreference(ctx, Config.getShopsterDeliveryObjQueueIdKey());
        String orderId = Utilities.getSharedPreference(ctx, Config.getShopsterDeliveryObjOrderIdKey());
        String isDelivered = Utilities.getSharedPreference(ctx, Config.getShopsterDeliveryObjIsDeliveredKey());
        String deliveredBy = Utilities.getSharedPreference(ctx, Config.getShopsterDeliveryObjDeliveredByKey());
        String deliveryType = Utilities.getSharedPreference(ctx, Config.getShopsterDeliveryObjDeliveryTypeKey());

        Utilities.writeDebugLog("User data : \nAPI key : "+shopsterApiToken+
                "\nUser ID : "+userId+
                "\nUser hash : "+userHash+
                "\nQueue ID : "+queueId+
                "\nOrder ID : "+orderId+
                "\nIs delivered : "+isDelivered+
                "\nDelivered by : "+deliveredBy+
                "\nDelivery type : "+deliveryType
        );
    }

}
