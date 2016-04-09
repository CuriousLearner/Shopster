package in.co.shopster.shopster.fragments;


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

/**
 * Created by vikram on 14/3/16.
 */
public class AccountFragment extends Fragment {

    private View view;

    @Bind(R.id.btn_hello)
    public Button helloBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(R.layout.fragment_account, container, false);
            ButterKnife.bind(this, view);

            helloBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(
                            AccountFragment.this.getContext(),
                            "Hello fragment",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });

        }
        return view;
    }
}
