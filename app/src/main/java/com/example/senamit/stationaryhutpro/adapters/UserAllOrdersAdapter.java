package com.example.senamit.stationaryhutpro.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.senamit.stationaryhutpro.R;
import com.example.senamit.stationaryhutpro.models.UserCart;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserAllOrdersAdapter extends RecyclerView.Adapter<UserAllOrdersAdapter.ViewHolder> {

    private static final String TAG = UserAllOrdersAdapter.class.getSimpleName();
    private Context context;
    private List<UserCart> orderList = new ArrayList<>();

    public UserAllOrdersAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_all_orders, parent, false);
        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (orderList!=null){
            holder.txtProductNumber.setText(orderList.get(position).getProductNumber());
            holder.txtProductPrice.setText(orderList.get(position).getProductPrice());
        }
    }

    @Override
    public int getItemCount() {
        if (orderList!= null){
            return orderList.size();
        }else {
            return 0;
        }

    }

    public void setOrderList(List<UserCart> userCarts) {
        if (userCarts!= null){
            Log.i(TAG, "orderlist in adapter is not null");
            orderList = userCarts;
            notifyDataSetChanged();
        }else {
            Log.i(TAG, "orderlist in adapter is null");
        }

    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtProductName;
        TextView txtProductNumber;
        TextView txtProductPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductNumber = itemView.findViewById(R.id.txtProductNumber);
            txtProductPrice = itemView.findViewById(R.id.txtProductPrice);
        }
    }
}