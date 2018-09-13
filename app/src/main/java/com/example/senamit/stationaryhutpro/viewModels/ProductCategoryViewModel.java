package com.example.senamit.stationaryhutpro.viewModels;

import android.app.Application;
import android.util.Log;

import com.example.senamit.stationaryhutpro.liveData.FirebaseQueryLiveData;
import com.example.senamit.stationaryhutpro.models.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

public class ProductCategoryViewModel extends AndroidViewModel {

    private static final String TAG = ProductCategoryViewModel.class.getSimpleName();

    private static Query PRODUCT_FOR_SALE ;
    private static  DatabaseReference PRODUCT_FOR_SEARCH ;
    private FirebaseQueryLiveData liveData ;
//    private FirebaseQueryLiveData productDescriptionLiveData;
    private MediatorLiveData<List<Product>> productLiveData;

    public ProductCategoryViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Product>> getCategoryProduct(String productCategory) {
       loadProductLiveData(productCategory);
        return productLiveData;
    }

    private void loadProductLiveData(String productCategory) {
        PRODUCT_FOR_SALE = FirebaseDatabase
                .getInstance().getReference("/products").orderByChild("category").equalTo(productCategory);
        Log.i(TAG, "PRODUCT_FOR_SALE "+PRODUCT_FOR_SALE);
        productLiveData = new MediatorLiveData<>();
        liveData = new FirebaseQueryLiveData(PRODUCT_FOR_SALE);
        productLiveData.addSource(liveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                if (dataSnapshot!=null){
                    List<Product> productList = new ArrayList<>();
                    for (DataSnapshot productDataSnapshot : dataSnapshot.getChildren()){
                        Product product = productDataSnapshot.getValue(Product.class);
                        productList.add(product);
                        Log.i(TAG, "inside loadproduct live data"+product);

                    }
                    Collections.reverse(productList);
                    productLiveData.setValue(productList);
                }
                else {
                    productLiveData.setValue(null);
                }
            }
        });
    }

//    public void getSortTypeProduct(int sortType) {
//        if (productLiveData!= null){
//
//        }
//    }
}
