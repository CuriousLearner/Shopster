package in.co.shopster.shopster.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.co.shopster.shopster.R;
import in.co.shopster.shopster.Utilities;
import in.co.shopster.shopster.activities.MainActivity;

/**
 * Created by vikram on 14/3/16.
 */
public class AccountFragment extends Fragment {

    private View view;

    @Bind(R.id.btn_logout)
    public Button logoutBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(R.layout.fragment_account, container, false);
            ButterKnife.bind(this, view);

            logoutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utilities.clearAllSharedPreferences(AccountFragment.this.getContext());
                    Utilities.showToast("Logged out of Shopster ...", AccountFragment.this.getContext(), true);
                    Intent restartShopsterIntent = new Intent(AccountFragment.this.getContext(), MainActivity.class);
                    AccountFragment.this.startActivity(restartShopsterIntent);
                }
            });

        }
        return view;
    }
}
