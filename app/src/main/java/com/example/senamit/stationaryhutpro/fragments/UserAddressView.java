package com.example.senamit.stationaryhutpro.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.senamit.stationaryhutpro.R;
import com.example.senamit.stationaryhutpro.adapters.UserAddressAdapter;
import com.example.senamit.stationaryhutpro.models.Address;
import com.example.senamit.stationaryhutpro.viewModels.UserAddressViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UserAddressView extends Fragment {
    private static final String TAG = UserAddressView.class.getSimpleName();
    private UserAddressViewModel mViewModel;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser currentUser;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private UserAddressAdapter mAdapter;

    private Context context;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_user_address_view, container,false);
        context= container.getContext();
        mFirebaseAuth = FirebaseAuth.getInstance();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentUser = mFirebaseAuth.getCurrentUser();
        mRecyclerView = view.findViewById(R.id.recycler_address);
        mLayoutManager = new LinearLayoutManager(context);
        mAdapter = new UserAddressAdapter(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        mViewModel= ViewModelProviders.of(this).get(UserAddressViewModel.class);

        mViewModel.getAddressList(currentUser.getUid()).observe(this, new Observer<List<Address>>() {
            @Override
            public void onChanged(List<Address> addresses) {
                if (addresses!=null){
                    Log.i(TAG, "inside viewmodel of not null address");
                    mAdapter.setAddress(addresses);
                }
            }
        });
    }
}
