package com.example.senamit.stationaryhutpro.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.senamit.stationaryhutpro.R;
import com.example.senamit.stationaryhutpro.models.Address;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class UserAddressEntry extends Fragment {

    private static final String TAG = UserAddressEntry.class.getSimpleName();

    private Context context;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private EditText txtFullName;
    private EditText txtMobileNumber;
    private EditText txtPincode;
    private EditText txtAddressPartOne;
    private EditText txtAddressPartTwo;
    private EditText txtLandMark;
    private EditText txtCity;
    private EditText txtState;
    private Button btnSubmit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = container.getContext();
        View view = inflater.inflate(R.layout.activity_user_newaddress_entry, container, false);
        mFirebaseAuth = FirebaseAuth.getInstance();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtFullName= view.findViewById(R.id.txtFullName);
        txtMobileNumber = view.findViewById(R.id.txtMobileNumber);
        txtPincode = view.findViewById(R.id.txtPincode);
        txtAddressPartOne= view.findViewById(R.id.txtAddressPartOne);
        txtAddressPartTwo = view.findViewById(R.id.txtAddressPartTwo);
        txtCity = view.findViewById(R.id.txtCity);
        txtState = view.findViewById(R.id.txtState);
        txtLandMark = view.findViewById(R.id.txtLandMark);
        btnSubmit = view.findViewById(R.id.btnSubmit);
         currentUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int check = validateData();
                if (check==0){
                    firebaseAddressUpload();
                }
            }
        });

    }

    private void firebaseAddressUpload() {

        String fullName = txtFullName.getText().toString();
         String mobileNumber= txtMobileNumber.getText().toString();
         String pincode=txtPincode.getText().toString();
         String addressPartOne=txtAddressPartOne.getText().toString();
         String addressPartTwo=txtAddressPartTwo.getText().toString();
         String landMark=txtLandMark.getText().toString();
         String city=txtCity.getText().toString();
         String state=txtState.getText().toString();
         int status=1;

        Address address = new Address(fullName, mobileNumber, pincode, addressPartOne, addressPartTwo,
                landMark, city, state,status);
        Map<String, Object> addressValue = address.toMap();
        Map<String, Object> childUpdate = new HashMap<>();
//        childUpdate.put("/users/"+currentUser+"/address")
        String key = mDatabase.child("users").child(currentUser.getUid()).child("address").push().getKey();
        childUpdate.put("/users/"+currentUser.getUid()+"/address/"+key, addressValue);
        mDatabase.updateChildren(childUpdate);
    }

    private int validateData() {
        int check= 0;
        if (TextUtils.isEmpty(txtFullName.getText())){
            check=1;
            txtFullName.setError("Enter valid full name");
        }
        if (TextUtils.isEmpty(txtMobileNumber.getText())){
            check=1;
            txtMobileNumber.setError("Enter mobile number");
        }
        if (TextUtils.isEmpty(txtPincode.getText())){
            check=1;
            txtPincode.setError("Enter correct pincode");
        }
        if (TextUtils.isEmpty(txtAddressPartOne.getText())){
            check=1;
            txtAddressPartOne.setError("missing");
        }
        if (TextUtils.isEmpty(txtCity.getText())){
            check=1;
            txtCity.setError("missing");
        }
        if (TextUtils.isEmpty(txtState.getText())){
            check=1;
            txtState.setError("missing");
        }
        return check;

    }
}
