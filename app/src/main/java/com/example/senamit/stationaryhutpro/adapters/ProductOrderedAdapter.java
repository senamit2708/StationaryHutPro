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
        holder.txtProductName.setText(mOrderList.get(position).getProductNumber());
        holder.txtProductPrice.setText(mOrderList.get(position).getProductPrice());

    }

    @Override
    public int getItemCount() {
        if (mOrderList!= null){

            Log.i(TAG, "inside getItemcount "+mOrderList.size());
            return mOrderList.size();
        }else {
            return 0;
        }
    }

    public void setOrderedProduct(List<UserCart> orderList) {
        if (orderList!= null){
            Log.i(TAG, "the orderList is "+orderList.size());
            mOrderList = orderList;
            notifyDataSetChanged();
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
