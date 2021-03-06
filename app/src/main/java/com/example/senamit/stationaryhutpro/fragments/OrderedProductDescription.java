package com.example.senamit.stationaryhutpro.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.senamit.stationaryhutpro.R;
import com.example.senamit.stationaryhutpro.models.Address;
import com.example.senamit.stationaryhutpro.models.UserCart;
import com.example.senamit.stationaryhutpro.viewModels.OrderedProductViewModel;
import com.example.senamit.stationaryhutpro.viewModels.UsersAllOrdersViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class OrderedProductDescription extends Fragment {

    private static final String TAG = OrderedProductViewModel.class.getSimpleName();

    private Context context;
    private UsersAllOrdersViewModel mViewModel;
    private UserCart cartProduct;
    private String userId;

    private TextView txtProductName;
    private TextView txtProductNumber;
    private TextView txtTotalPrice;
    private TextView txtOrderNumber;
    private TextView txtOrderStatus;
    private TextView txtOrderDate;
    private TextView txtQuantity;
    private TextView txtTotalQuantity;
    private ImageView imageProduct;
    private TextView txtAddress;
    private TextView txtPaymentMode;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(UsersAllOrdersViewModel.class);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.activity_ordered_product_description, container, false);
       context = container.getContext();
       return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        txtProductName = view.findViewById(R.id.txtProductName);
        txtProductNumber = view.findViewById(R.id.txtProductNumber);
        txtOrderNumber = view.findViewById(R.id.txtOrderNumber);
        txtOrderStatus = view.findViewById(R.id.txtOrderStatus);
        txtOrderDate = view.findViewById(R.id.txtOrderDate);
        txtQuantity = view.findViewById(R.id.txtQuantity);
        txtTotalPrice = view.findViewById(R.id.txtTotalPrice);
        txtTotalQuantity = view.findViewById(R.id.txtTotalQuantity);
        imageProduct = view.findViewById(R.id.imageProduct);
        txtAddress = view.findViewById(R.id.txtAddress);
        txtPaymentMode = view.findViewById(R.id.txtPaymentMode);

        mViewModel.getSelectedCartProductForDesc(userId).observe(this, new Observer<UserCart>() {
         @Override
         public void onChanged(UserCart userCart) {
             Log.i(TAG, "userCart is null");
             if (userCart!=null){
                 Log.i(TAG, "userCart is not null");
                 setProductDetails(userCart);
             }
         }
     });

    mViewModel.getAddressDelivery().observe(this, new Observer<Address>() {
        @Override
        public void onChanged(Address address) {
            Log.i(TAG, "the address is "+address.getMobileNumber());
            setAddress(address);
        }
    });

    }

    private void setAddress(Address address) {
        String name = address.getFullName();
        String mobileNo = address.getMobileNumber();
        String addressPartOne = address.getAddressPartOne();
        String addressPartTwo = address.getAddressPartTwo();
        String city = address.getCity();
        String state = address.getState();
        String pincode = address.getPincode();

        String finalAddress = name +"\n"+addressPartOne+"\n"+addressPartTwo+"\n"+city+","+state+" "+pincode+"\n"+"India"+"\n"+"Phone number: "+mobileNo;
        txtAddress.setText(finalAddress);
    }

    private void setProductDetails(UserCart userCart) {
        Log.i(TAG, "teh product name is "+userCart.getProductName());
        Picasso.with(context).load(userCart.getImageUrl()).fit().into(imageProduct);

        txtProductName.setText(userCart.getProductName());
        txtProductNumber.setText(userCart.getProductNumber());
        txtQuantity.setText(Integer.toString(userCart.getQuantity()));
        txtOrderNumber.setText(userCart.getOrderNumber());
        txtOrderStatus.setText(userCart.getOrderStatus());
        txtOrderDate.setText(userCart.getDate());
        txtPaymentMode.setText(userCart.getPaymentMode());
//        txtProductTotalPrice.setText(userCart.getProductPrice());
        int quantity = userCart.getQuantity();
        int price = Integer.parseInt(userCart.getProductPrice());
        int totalPrice = (quantity * price);
        Log.i(TAG, "total price of product is "+totalPrice);
        Log.i(TAG,"the total price is "+totalPrice);
        txtTotalQuantity.setText("Total price of ("+quantity+" items)");
        txtTotalPrice.setText(Integer.toString(totalPrice));
    }


}
