package com.example.senamit.stationaryhutpro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.senamit.stationaryhutpro.R;
import com.example.senamit.stationaryhutpro.models.Address;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserAddressAdapter extends RecyclerView.Adapter<UserAddressAdapter.ViewHolder> {

    private List<Address> addressList = new ArrayList<>();
    private Context context;

    public UserAddressAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_user_address, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (addressList!=null){
            holder.txtFullName.setText(addressList.get(position).getFullName());
            holder.txtMobileNumber.setText(addressList.get(position).getMobileNumber());
            holder.txtAddressPartOne.setText(addressList.get(position).getAddressPartOne());
            holder.txtAddressPartTwo.setText(addressList.get(position).getAddressPartTwo());
            holder.txtCity.setText(addressList.get(position).getCity());
            holder.txtState.setText(addressList.get(position).getState());
            holder.txtPincode.setText(addressList.get(position).getPincode());
        }

    }

    @Override
    public int getItemCount() {
        if (addressList!= null){
            return addressList.size();
        }else {
            return 0;
        }
    }

    public void setAddress(List<Address> addressList){
        if (addressList!= null){
            this.addressList = addressList;
            notifyDataSetChanged();
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtFullName;
        TextView txtMobileNumber;
        TextView txtAddressPartOne;
        TextView txtAddressPartTwo;
        TextView txtCity;
        TextView txtState;
        TextView txtPincode;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFullName = itemView.findViewById(R.id.txtFullName);
            txtMobileNumber = itemView.findViewById(R.id.txtMobileNumber);
            txtAddressPartOne = itemView.findViewById(R.id.txtAddressPartOne);
            txtAddressPartTwo = itemView.findViewById(R.id.txtAddressPartTwo);
            txtCity = itemView.findViewById(R.id.txtCity);
            txtState = itemView.findViewById(R.id.txtState);
            txtPincode = itemView.findViewById(R.id.txtPincode);
        }
    }
}
