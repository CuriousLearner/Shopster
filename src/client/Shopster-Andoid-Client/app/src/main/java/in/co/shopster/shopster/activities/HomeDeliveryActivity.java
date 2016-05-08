package in.co.shopster.shopster.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import in.co.shopster.shopster.Config;
import in.co.shopster.shopster.R;
import in.co.shopster.shopster.Utilities;

public class HomeDeliveryActivity extends AppCompatActivity {

    ImageView qr ;
    Button before_delivered;
    Button delivered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_delivery);
        qr =(ImageView) findViewById(R.id.uhash_qr);
        before_delivered=(Button) findViewById(R.id.delivery_1);
        delivered=(Button) findViewById(R.id.delivery);
        com.google.zxing.Writer writer = new QRCodeWriter();
        String uhash = Utilities.getSharedPreference(this.getApplicationContext(), Config.getSHOPSTER_USER_Hash());
        String uhashdata = Uri.encode(uhash, "utf-8");
        try {
            BitMatrix bm = writer.encode(uhashdata, BarcodeFormat.QR_CODE, 800, 800);
            Bitmap ImageBitmap = Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888);
            for (int i = 0; i < 800; i++) {//width
                for (int j = 0; j < 800; j++) {//height
                    ImageBitmap.setPixel(i, j, bm.get(i, j) ? Color.BLACK: Color.WHITE);
                }
            }

            if (ImageBitmap != null) {

                qr.setImageBitmap(ImageBitmap);
            } else {
            }
        }
        catch (Exception e)
        {
            Utilities.writeDebugLog("Exception : "+e);
        }

        before_delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delivered.setVisibility(View.VISIBLE);
                before_delivered.setVisibility(View.GONE);
            }
        });
        delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm();
            }
        });
    }
    void confirm()
    {

        Utilities.setSharedPreference(this.getApplicationContext(), Config.getShopsterDeliveryPrefKey(), "");
        Utilities.setSharedPreference(this.getApplicationContext(),Config.getShopsterOrderIdKey(),"");
        Utilities.setSharedPreference(this.getApplicationContext(),Config.getShopsterOrderPriceKey(),"");
        Utilities.setSharedPreference(this.getApplicationContext(),Config.getShopsterOrderStatusKey(),"");
        Intent restartIntent = new Intent(this.getApplicationContext(), MainActivity.class);
        startActivity(restartIntent);
    }
}
