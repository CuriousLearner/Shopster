package in.co.shopster.shopster.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.co.shopster.shopster.R;
import in.co.shopster.shopster.Utilities;

/**
 * Created by vikram on 3/4/16.
 */
public class WalletFragment extends Fragment {

    private View view;


    @Bind(R.id.text_wallet_balance)
    TextView walletBalanceText;

    @Bind(R.id.edit_coupon_code)
    EditText couponCodeEdit;

    @Bind(R.id.btn_recharge)
    Button rechargeBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(R.layout.fragment_wallet, container, false);
            ButterKnife.bind(this, view);


            rechargeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String couponCode = couponCodeEdit.getText().toString();

                    if(couponCode.equals("")) {
                        Utilities.showToast("Please fill in a coupon code ...", WalletFragment.this.getContext(), false);
                        return;
                    }

                    Utilities.writeDebugLog("Calling recharge API : couponCode ==> "+couponCode);
                }
            });


        }
        return view;
    }
}
