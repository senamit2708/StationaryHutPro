package com.example.senamit.stationaryhutpro.viewModels;

import android.app.Application;
import android.util.Log;

import com.example.senamit.stationaryhutpro.liveData.FirebaseQueryLiveData;
import com.example.senamit.stationaryhutpro.models.Address;
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

public class UserAddressViewModel extends AndroidViewModel {

    private static final String TAG = UserAddressViewModel.class.getSimpleName();

    private static DatabaseReference USER_ADDRESS_REF;
    private MediatorLiveData<List<Address>> addressList;
    private FirebaseQueryLiveData liveData;

    private Address address;



    public UserAddressViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Address>> getAddressList(String uId){
        if (addressList==null){
            Log.i(TAG, "address live data is null");
            addressList = new MediatorLiveData<>();
            USER_ADDRESS_REF = FirebaseDatabase.getInstance().getReference("/users/"+uId+"/address");
            liveData= new FirebaseQueryLiveData(USER_ADDRESS_REF);
            loadAddressLiveData();
        }
        return addressList;
    }

    private void loadAddressLiveData() {
        addressList.addSource(liveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                if (dataSnapshot !=null){
                    List<Address> addresses = new ArrayList<>();
                    for (DataSnapshot addressDataSnapshot : dataSnapshot.getChildren()){
                        Address address = addressDataSnapshot.getValue(Address.class);
                        addresses.add(address);
                        Log.i(TAG, "inside loadAddress live data"+address);
                    }
                    addressList.setValue(addresses);
                }else{
                    addressList.setValue(null);
                }
            }
        });
    }

    public void setPaymentAddress(Address address) {
        this.address = address;
        Log.i(TAG, "the address in viewmodel is "+this.address);
    }
}
