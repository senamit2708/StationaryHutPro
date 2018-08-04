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

public class OrderedProductViewModel extends AndroidViewModel {

    private static final String TAG = OrderedProductViewModel.class.getSimpleName();

    private static DatabaseReference ORDERED_PRODUCT;
    private DatabaseReference mDatabaseRef;
    private FirebaseQueryLiveData mFirebaseQueryLiveData;

    private MediatorLiveData<List<UserCart>> orderedProduct;



    public OrderedProductViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<UserCart>> getOrderedProduct(List<String> keyList, String userId){
        if (orderedProduct==null){
            orderedProduct = new MediatorLiveData<>();
            loadOrderedProductFirebase(keyList, userId);
        }
        return orderedProduct;
    }

    private void loadOrderedProductFirebase(List<String> keyList, String userId) {
        Log.i(TAG, "the sizze of retrived product will be"+keyList.size());
        if (keyList.size()>0){
            final List<UserCart> orderedProductList = new ArrayList<>();
            for (int i=0; i<keyList.size(); i++){

                String productKey = keyList.get(i);
                Log.i(TAG, "the product key is "+productKey);
                ORDERED_PRODUCT = FirebaseDatabase.getInstance().getReference("/users/"+userId+"/order/"+productKey+"/product");
                mFirebaseQueryLiveData = new FirebaseQueryLiveData(ORDERED_PRODUCT);

                orderedProduct.addSource(mFirebaseQueryLiveData, new Observer<DataSnapshot>() {
                    @Override
                    public void onChanged(DataSnapshot dataSnapshot) {
                        if (dataSnapshot!= null){
                            UserCart order = dataSnapshot.getValue(UserCart.class);
                            Log.i(TAG, "the ordered product is "+order.getProductNumber());
                            orderedProductList.add(order);
                        }
                        orderedProduct.setValue(orderedProductList);

                    }
                });

            }
        }
    }

}
