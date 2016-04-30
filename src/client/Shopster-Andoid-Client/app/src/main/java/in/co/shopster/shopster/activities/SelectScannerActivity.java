package in.co.shopster.shopster.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.test.UiThreadTest;
import android.view.View;
import android.widget.Button;

import com.google.zxing.integration.android.IntentIntegrator;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.co.shopster.shopster.R;
import in.co.shopster.shopster.ShopsterNavigationDrawer;
import in.co.shopster.shopster.Utilities;
import in.co.shopster.shopster.fragments.CartFragment;

public class SelectScannerActivity extends AppCompatActivity {

    @Bind(R.id.btn_scan_nfc)
    Button scanNfcBtn;

    @Bind(R.id.btn_scan_qr)
    Button scanQrBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_scanner);

        ButterKnife.bind(this);

        final Context ctx = SelectScannerActivity.this.getApplicationContext();

        boolean isNfcSupported = getIntent().getBooleanExtra("isNfcSupported", false);


        if(!isNfcSupported) {
            this.scanQr(ctx);
            this.finish();
        }

        scanNfcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectScannerActivity.this.scanNfc(ctx);
            }
        });

        scanQrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectScannerActivity.this.scanQr(ctx);
            }
        });

    }

    private void scanNfc(Context ctx) {
        Utilities.showToast("NFC scan begins ...", ctx, false);
    }

    private void scanQr(Context ctx) {
        Utilities.showToast("QR scan begins ...", ctx, false);

        ShopsterNavigationDrawer.currentDrawer.scanQrCode();
    }

}
