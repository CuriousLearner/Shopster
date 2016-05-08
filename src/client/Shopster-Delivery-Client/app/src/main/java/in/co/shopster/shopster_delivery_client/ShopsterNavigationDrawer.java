package in.co.shopster.shopster_delivery_client;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;

import in.co.shopster.shopster_delivery_client.fragments.DeliveryManagerFragment;
import in.co.shopster.shopster_delivery_client.fragments.SettingsFragment;
import in.co.shopster.shopster_delivery_client.rest.models.Delivery;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;

/**
 * Created by vikram on 23/4/16.
 */
public class ShopsterNavigationDrawer extends MaterialNavigationDrawer {


    public static MaterialNavigationDrawer currentDrawer;

    @Override
    public void init(Bundle savedInstanceState) {

        // setup header
        View customHeaderView = LayoutInflater.from(this)
                .inflate(R.layout.custom_header_layout, null);
        this.setDrawerHeaderCustom(customHeaderView);


        MaterialSection deliveriesSection = newSection(
                "Deliveries",
                new IconDrawable(this, Iconify.IconValue.fa_money),
                new DeliveryManagerFragment()
        );

        MaterialSection settingsSection = newSection(
                "Settings",
                new IconDrawable(this, Iconify.IconValue.fa_cog),
                new SettingsFragment()
        );

        this.addSection(deliveriesSection);

        this.addSection(settingsSection);

        this.setDefaultSectionLoaded(0);
        disableLearningPattern();

        currentDrawer = this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utilities.writeDebugLog("In onActivity Result");
        IntentResult scanResult = IntentIntegrator.parseActivityResult(
                requestCode, resultCode, data);

        if(scanResult != null) {
            DeliveryManagerFragment.getInstance().onQRCodeScanned(scanResult);


        }
    }

}
