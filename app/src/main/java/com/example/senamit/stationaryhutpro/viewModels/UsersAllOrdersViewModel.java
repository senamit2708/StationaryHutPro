package com.example.senamit.stationaryhutpro.viewModels;

import android.app.Application;
import android.util.Log;

import com.example.senamit.stationaryhutpro.liveData.FirebaseQueryLiveData;
import com.example.senamit.stationaryhutpro.models.UserCart;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

public class UsersAllOrdersViewModel extends AndroidViewModel {

    private static final String TAG = UsersAllOrdersViewModel.class.getSimpleName();

    private static Query USER_ADDRESS_REF;
    private static Query USER_NEW_ORDERS_REF;
    private MediatorLiveData<List<UserCart>> orderList;
    private MediatorLiveData<List<UserCart>> newOrderList;
    private FirebaseQueryLiveData mLiveData;
    private FirebaseQueryLiveData mNewOrderLiveData;

    public UsersAllOrdersViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<UserCart>>getAllOrders(String userId){
        if (orderList==null){
            orderList = new MediatorLiveData<>();
            loadOrders(userId);
        }
        return orderList;
    }

    private void loadOrders(String userId) {
        USER_ADDRESS_REF = FirebaseDatabase.getInstance()
                .getReference("/users/"+userId+"/order").orderByKey();
        mLiveData = new FirebaseQueryLiveData(USER_ADDRESS_REF);

        orderList.addSource(mLiveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                if (dataSnapshot!= null){
                    List<UserCart> orders = new ArrayList<>();
                    for (DataSnapshot child : dataSnapshot.getChildren()){
                        Log.i(TAG, "the key of children is "+child.getKey());
                        UserCart order = child.child("product").getValue(UserCart.class);
                        orders.add(order);
                    }

                    orderList.setValue(orders);
                    Log.i(TAG, "the orderlist is "+orders.size());
                }else {
                    orderList.setValue(null);
                }

            }
        });
    }

    public LiveData<List<UserCart>> getNewOrders(String userId){
        if(newOrderList ==null){
            newOrderList = new MediatorLiveData<>();
            loadNewOrders(userId);
        }
        return newOrderList;
    }

    private void loadNewOrders(String userId) {
        USER_NEW_ORDERS_REF = FirebaseDatabase.getInstance()
                .getReference("/users/"+userId+"/order").orderByChild("orderConfirmation").equalTo(1);
        mNewOrderLiveData = new FirebaseQueryLiveData(USER_NEW_ORDERS_REF);
        newOrderList.addSource(mNewOrderLiveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                if (dataSnapshot!= null){
                    List<UserCart> orders = new ArrayList<>();
                    for (DataSnapshot child : dataSnapshot.getChildren()){
                        Log.i(TAG, "the key of children is "+child.getKey());
                        UserCart order = child.child("product").getValue(UserCart.class);
                        orders.add(order);
                    }

                    newOrderList.setValue(orders);
                    Log.i(TAG, "the orderlist is "+orders.size());
                }else {
                    newOrderList.setValue(null);
                }
            }
        });
    }

}
