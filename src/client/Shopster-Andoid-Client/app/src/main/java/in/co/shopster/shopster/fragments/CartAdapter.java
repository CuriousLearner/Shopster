package in.co.shopster.shopster.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.co.shopster.shopster.R;

/**
 * Created by ayush on 4/4/16.
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartInfo> mainList;
    public CartAdapter(List<CartInfo> mainList) {
        this.mainList = mainList;
    }

    @Override
    public int getItemCount() {
        return mainList.size();
    }

    @Override
    public void onBindViewHolder(final CartViewHolder mainViewHolder, final int i) {
        CartInfo ci = mainList.get(i);
        mainViewHolder.title.setText(ci.title);
        mainViewHolder.price.setText(ci.price);
        mainViewHolder.quantity.setText(ci.quantity);
    }
    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.cart_item_card_view, viewGroup, false);

        return new CartViewHolder(itemView);
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        protected TextView title;
        protected TextView price;
        protected TextView quantity;


        public CartViewHolder(View v) {
            super(v);
            title =  (TextView) v.findViewById(R.id.title);
            price =  (TextView) v.findViewById(R.id.price);
            quantity =  (TextView) v.findViewById(R.id.quantity);
        }
    }


}
