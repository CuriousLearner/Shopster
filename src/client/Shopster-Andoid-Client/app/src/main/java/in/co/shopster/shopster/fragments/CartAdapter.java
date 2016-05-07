package in.co.shopster.shopster.fragments;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.co.shopster.shopster.R;
import in.co.shopster.shopster.rest.models.Product;

/**
 * Created by ayush on 4/4/16.
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartInfo> mainList;
    public static String[] quant ;
    private List<Product> orderItems;

    public CartAdapter(List<Product> orderItems,String[] a) {
        this.orderItems = orderItems;
        this.quant = a;
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    @Override
    public void onBindViewHolder(final CartViewHolder mainViewHolder, final int i) {
        Product pro = orderItems.get(i);
        String title = pro.name;
        String price = String.valueOf(pro.price);
        Log.e("title",title);
       // mainViewHolder.title.setText(orderItems.get(i).getName());
        //mainViewHolder.price.setText(orderItems.get(i).getPrice()+"");
        mainViewHolder.title.setText(title);
        mainViewHolder.price.setText(price);
        mainViewHolder.quantity.setText("1");
        quant[i]="1";
        mainViewHolder.quantity.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                quant[i]=s.toString();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                quant[i]=s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                quant[i]=s.toString();
            }
        });
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

    String[] allquant()
    {
        return quant;
    }

}
