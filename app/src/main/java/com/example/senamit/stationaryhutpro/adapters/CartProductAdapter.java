package com.example.senamit.stationaryhutpro.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.senamit.stationaryhutpro.R;
import com.example.senamit.stationaryhutpro.models.Product;
import com.example.senamit.stationaryhutpro.models.UserCart;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.ViewHolder> {

    private static final String TAG = CartProductAdapter.class.getSimpleName();

    private Context context;
    private List<UserCart> cartProductList;
    private List<Product> productList;
    private ButtonClickInterface btnClickinterface;

    public CartProductAdapter(Context context) {
        this.context = context;
    }

    public CartProductAdapter(Context context, ButtonClickInterface btnClickinterface) {
        this.context = context;
        this.btnClickinterface = btnClickinterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.cardview_cart_product,
                parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (cartProductList!= null){
            Log.i(TAG, "inside onBindViewHolder "+cartProductList.get(position).getProductNumber());
            holder.txtProductNumber.setText(cartProductList.get(position).getProductNumber());
            holder.txtProductPrice.setText(cartProductList.get(position).getProductPrice());
        }

        else {
            Log.i(TAG, "the cartProductList is null");
            holder.txtProductNumber.setText("No Product found");
            holder.txtProductPrice.setText("price not loaded");
        }
    }

    @Override
    public int getItemCount() {
        if (cartProductList != null){
            return cartProductList.size();
        }else {
            return 0;
        }
    }

    public void setCartProduct(List<UserCart> cartProductList){
        this.cartProductList = cartProductList;
        notifyDataSetChanged();
        Log.i(TAG, "inside setCartProduct method");
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageProduct;
        TextView txtProductName;
        TextView txtProductNumber;
        TextView txtProductPrice;
        Button btnRemove;
        Button btnSaveForLater;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProduct = itemView.findViewById(R.id.imageProduct);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductNumber = itemView.findViewById(R.id.txtProductNumber);
            txtProductPrice = itemView.findViewById(R.id.txtProductPrice);
            btnRemove = itemView.findViewById(R.id.btnRemove);
            btnSaveForLater = itemView.findViewById(R.id.btnSaveForLater);
            btnRemove.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btnRemove:
                    Log.i(TAG, "inside button click ");
                    int position = getAdapterPosition();
                    String productNumber = cartProductList.get(position).getProductNumber();
                    btnClickinterface.funRemoveBtnClick(productNumber, position);
                    break;
                default:
                    Log.i(TAG, "select any other option");
            }
        }
    }
    public interface ButtonClickInterface{
        void funRemoveBtnClick(String productNumber, int position);
    }

}
