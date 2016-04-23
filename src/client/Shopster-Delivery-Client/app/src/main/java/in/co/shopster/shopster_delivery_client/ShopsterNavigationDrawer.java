package in.co.shopster.shopster_delivery_client;

import android.graphics.drawable.Icon;
import android.os.Bundle;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;

import in.co.shopster.shopster_delivery_client.fragments.DeliveryManagerFragment;
import in.co.shopster.shopster_delivery_client.fragments.SettingsFragment;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;

/**
 * Created by vikram on 23/4/16.
 */
public class ShopsterNavigationDrawer extends MaterialNavigationDrawer {


    public static MaterialNavigationDrawer currentDrawer;

    @Override
    public void init(Bundle savedInstanceState) {
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

}
