package in.co.shopster.shopster;

import android.content.Intent;
import android.os.Bundle;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;

import in.co.shopster.shopster.fragments.CartFragment;
import in.co.shopster.shopster.fragments.AccountFragment;
import in.co.shopster.shopster.fragments.WalletFragment;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;

/**
 * Created by vikram on 14/3/16.
 */
public class ShopsterNavigationDrawer extends MaterialNavigationDrawer {

    public static ShopsterNavigationDrawer currentDrawer;

    @Override
    public void init(Bundle savedInstanceState) {
        MaterialSection accountSection = newSection(
                "Account",
                new IconDrawable(this, Iconify.IconValue.fa_money),
                new AccountFragment()
        );

        MaterialSection cartSection = newSection(
                "Cart",
                new IconDrawable(this, Iconify.IconValue.fa_shopping_cart),
                new CartFragment()
        );

        MaterialSection walletSection = newSection(
                "Wallet",
                new IconDrawable(this, Iconify.IconValue.fa_money),
                new WalletFragment()
        );


        this.addSection(cartSection);

        this.addSection(walletSection);

        this.addSection(accountSection);

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

        }
    }

    public void scanQrCode() {
        Utilities.showToast("Scan the product QR code", this, false);
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        Utilities.writeDebugLog("Initiating QR Code scan");
        intentIntegrator
                .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
                .setCameraId(0)
                .setPrompt("Scan Product QR Code")
                .setBeepEnabled(true)
                .initiateScan();
    }
}
