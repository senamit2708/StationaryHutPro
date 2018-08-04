package com.example.senamit.stationaryhutpro.viewModels;

import android.app.Application;
import android.util.Log;

import com.example.senamit.stationaryhutpro.liveData.FirebaseQueryLiveData;
import com.example.senamit.stationaryhutpro.models.UserCart;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

public class UsersAllOrdersViewModel extends AndroidViewModel {

    private static final String TAG = UsersAllOrdersViewModel.class.getSimpleName();

    private static DatabaseReference USER_ADDRESS_REF;
    private MediatorLiveData<List<UserCart>> orderList;
    private FirebaseQueryLiveData mLiveData;

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
                .getReference("/users/"+userId+"/order");
        mLiveData = new FirebaseQueryLiveData(USER_ADDRESS_REF);

        orderList.addSource(mLiveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                if (dataSnapshot!= null){
//                  long count =   dataSnapshot.getChildrenCount();
//                    Log.i(TAG, "the count is "+count);
//                  Log.i(TAG,  "childeres are "+ dataSnapshot.getChildren()) ;
//                  for (int i=0; i<count;i++){
//                      String key = dataSnapshot.getChildren().forEach();
//                      Log.i(TAG, "the key of datasnapshot is "+key);
//                  }
//
//                    UserCart order = dataSnapshot.getValue(UserCart.class);
//                    Log.i(TAG, "the order is "+order);
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

}
