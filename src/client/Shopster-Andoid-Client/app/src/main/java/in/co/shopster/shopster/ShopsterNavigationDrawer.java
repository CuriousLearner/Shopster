package in.co.shopster.shopster;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;

import in.co.shopster.shopster.fragments.CartFragment;
import in.co.shopster.shopster.fragments.HomeFragment;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;

/**
 * Created by vikram on 14/3/16.
 */
public class ShopsterNavigationDrawer extends MaterialNavigationDrawer {

    public static MaterialNavigationDrawer currentDrawer;

    @Override
    public void init(Bundle savedInstanceState) {
        MaterialSection homeSection  = newSection(
                "Account",
                new IconDrawable(this, Iconify.IconValue.fa_money),
                new HomeFragment()
        );

        MaterialSection cartSection = newSection(
                "Cart",
                new IconDrawable(this, Iconify.IconValue.fa_shopping_cart),
                new CartFragment()
        );

        this.addSection(homeSection);

        this.addSection(cartSection);

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
             CartFragment.getInstance().onQRCodeScanned(scanResult);
             // @TODO : Call get product details API here
        }
    }
}
