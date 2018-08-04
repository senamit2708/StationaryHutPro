package com.example.senamit.stationaryhutpro.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.senamit.stationaryhutpro.R;
import com.example.senamit.stationaryhutpro.adapters.ProductOrderedAdapter;
import com.example.senamit.stationaryhutpro.models.Address;
import com.example.senamit.stationaryhutpro.models.UserCart;
import com.example.senamit.stationaryhutpro.viewModels.OrderedProductViewModel;
import com.example.senamit.stationaryhutpro.viewModels.ProductCartViewModel;
import com.example.senamit.stationaryhutpro.viewModels.UserAddressViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NewOrders extends Fragment {

    private static final String TAG = NewOrders.class.getSimpleName();

    private Context context;
    private UserAddressViewModel mAddressViewModel;
    private ProductCartViewModel mProductCardViewModel;
    private OrderedProductViewModel mOrderedProductViewModel;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProductOrderedAdapter mAdapter;

    private Address address;
    private List<UserCart> orderedProduct;
    private List<String> keyList;

    private DatabaseReference mDatabase;
    String userId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       mAddressViewModel = ViewModelProviders.of(getActivity()).get(UserAddressViewModel.class);
       mProductCardViewModel = ViewModelProviders.of(getActivity()).get(ProductCartViewModel.class);
       mOrderedProductViewModel = ViewModelProviders.of(getActivity()).get(OrderedProductViewModel.class);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_new_order_details, container, false);
        context = container.getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
         userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

       address= mAddressViewModel.getAddress().getValue();
       orderedProduct = mProductCardViewModel.getOrderedProduct().getValue();

         writeNewPost(orderedProduct);
//       if (check>0){
//           Log.i(TAG, "the size of check is"+check);
//       }
//        Log.i(TAG, "the size of check is"+check);

        mRecyclerView = view.findViewById(R.id.recycler_order);
        mLayoutManager = new LinearLayoutManager(context);
        mAdapter = new ProductOrderedAdapter(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }



    private void writeNewPost(List<UserCart> orderedProduct) {
        final int count = orderedProduct.size();
         keyList = new ArrayList<>();


        Map<String, Object> addressValue = address.toMap();
        for (int i=0; i<count; i++){
            final int total = i+1;
            UserCart userCart = orderedProduct.get(i);
            Map<String, Object> productValues = userCart.toMap();
            Map<String, Object> childUpdate = new HashMap<>();
            final Map<String, Object> childUpdateAddress = new HashMap<>();
            final String keyOrder = FirebaseDatabase.getInstance().getReference("/users/"+userId+"/order").push().getKey();
            childUpdate.put("/users/"+userId+"/order/"+keyOrder+"/product", productValues);
            childUpdateAddress.put("/users/"+userId+"/order/"+keyOrder+"/address", addressValue);
            Log.i(TAG, "inside the writeNewPost for loop "+i);

            mDatabase.updateChildren(childUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(Task<Void> task) {
                    mDatabase.updateChildren(childUpdateAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            Log.i(TAG, "inside the onComplete method of updatechildren "+keyOrder);

                            keyList.add(keyOrder);
                            Log.i(TAG, "the size of keyList is "+keyList.size());
                            Log.i(TAG, "count is "+count);
                            Log.i(TAG, "total is "+total);
                            if (total==count){
                                Log.i(TAG, "the size of final keyList is "+keyList.size());
                                productDetails(keyList);
                            }




                        }
                    });

                }
            });


        }
    }

    private void productDetails(List<String> keyList) {
        mDatabase.child("users").child(userId).child("cart").removeValue();
        Log.i(TAG, "inside product details list "+keyList.size());
        mOrderedProductViewModel.getOrderedProduct(keyList, userId).observe(this, new Observer<List<UserCart>>() {
            @Override
            public void onChanged(List<UserCart> orderList) {
                if (orderList.size()>0){
                   mAdapter.setOrderedProduct(orderList);
                }
                else{
                    Log.i(TAG, "userCarts is empty");
                }

            }
        });
    }
}
