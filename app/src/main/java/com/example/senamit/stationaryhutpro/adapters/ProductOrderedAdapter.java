package com.example.senamit.stationaryhutpro.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.senamit.stationaryhutpro.R;
import com.example.senamit.stationaryhutpro.models.UserCart;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductOrderedAdapter extends RecyclerView.Adapter<ProductOrderedAdapter.ViewHolder> {

    private static final String TAG = ProductOrderedAdapter.class.getSimpleName();
    private Context context;
    private List<UserCart> mOrderList = new ArrayList<>();

    public ProductOrderedAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i(TAG, "inside oncreateViewHolder method");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_recent_order, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i(TAG, "inside onbind view holder "+mOrderList.get(position).getProductNumber());
        Picasso.with(context).load(mOrderList.get(position).getImageUrl()).into(holder.imageProduct);
        holder.txtProductName.setText(mOrderList.get(position).getProductName());
        holder.txtProductPrice.setText(mOrderList.get(position).getProductPrice());
        holder.txtProductNumber.setText(mOrderList.get(position).getProductNumber());
        holder.txtProductQuantity.setText(Integer.toString(mOrderList.get(position).getQuantity()));
        holder.txtProductOrderStatus.setText(mOrderList.get(position).getOrderStatus());
        holder.txtOrderNumber.setText(mOrderList.get(position).getOrderNumber());
        holder.txtProductOrderedDate.setText(mOrderList.get(position).getDate());
        int quantity = mOrderList.get(position).getQuantity();
        int price = Integer.parseInt(mOrderList.get(position).getProductPrice());
        int totalPrice = (quantity * price);
        Log.i(TAG,"the total price is "+totalPrice);
        holder.txtTotalQuantity.setText("Total price of ("+quantity+" items)");
        holder.txtTotalPrice.setText(Integer.toString(totalPrice));

    }

    @Override
    public int getItemCount() {
        if (mOrderList!= null){

            Log.i(TAG, "inside getItemcount inside adapter is  "+mOrderList.size());
            return mOrderList.size();
        }else {
            return 0;
        }
    }

    public void setOrderedProduct(List<UserCart> orderList) {
        if (orderList!= null){
            Log.i(TAG, "the orderList inside adapter is "+orderList.size());
            mOrderList = orderList;
            notifyDataSetChanged();
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtProductName;
        TextView txtProductNumber;
        TextView txtProductPrice;
        TextView txtProductQuantity;
        TextView txtProductOrderStatus;
        TextView txtProductOrderedDate;
        ImageView imageProduct;
        TextView txtTotalPrice;
        TextView txtTotalQuantity;
        TextView txtOrderNumber;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductNumber = itemView.findViewById(R.id.txtProductNumber);
            txtProductPrice = itemView.findViewById(R.id.txtProductPrice);
            txtProductQuantity = itemView.findViewById(R.id.txtQuantity);
            txtProductOrderStatus = itemView.findViewById(R.id.txtOrderedProductStatus);
            txtProductOrderedDate = itemView.findViewById(R.id.txtProductOrderDate);
            imageProduct = itemView.findViewById(R.id.imageProduct);
            txtTotalPrice = itemView.findViewById(R.id.txtTotalPrice);
            txtTotalQuantity = itemView.findViewById(R.id.txtTotalQuantity);
            txtOrderNumber = itemView.findViewById(R.id.txtOrderNumber);

        }
    }
}
