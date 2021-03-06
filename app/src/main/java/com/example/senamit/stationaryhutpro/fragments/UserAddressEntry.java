package com.example.senamit.stationaryhutpro.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.senamit.stationaryhutpro.R;
import com.example.senamit.stationaryhutpro.activities.StationaryMainPage;
import com.example.senamit.stationaryhutpro.models.Address;
import com.example.senamit.stationaryhutpro.viewModels.UserAddressViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

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

    private UserAddressViewModel mViewModel;
    private Address editableAddress;
    private String firebaseKey;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(UserAddressViewModel.class);
        editableAddress = mViewModel.getSelectedAddressForEdit();


    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "inside onPause method");
        
    }

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
//        txtLandMark = view.findViewById(R.id.txtLandMark);
        btnSubmit = view.findViewById(R.id.btnSubmit);
         currentUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int check = validateData();
                if (check==0){
                firebaseAddressUpload(view);
                }
                ((StationaryMainPage)getActivity()).hideSoftKeyboard(view);

            }
        });

        if (editableAddress != null){
            firebaseKey = mViewModel.getSelectedAddressFirebaseKey();
            txtFullName.setText(editableAddress.getFullName());
            txtMobileNumber.setText(editableAddress.getMobileNumber());
            txtPincode.setText(editableAddress.getPincode());
            txtAddressPartOne.setText(editableAddress.getAddressPartOne());
            txtAddressPartTwo.setText(editableAddress.getAddressPartTwo());
            txtCity.setText(editableAddress.getCity());
            txtState.setText(editableAddress.getState());
        }

    }

    private void firebaseAddressUpload(final View view) {
        String key = null;

        String fullName = txtFullName.getText().toString();
         String mobileNumber= txtMobileNumber.getText().toString();
         String pincode=txtPincode.getText().toString();
         String addressPartOne=txtAddressPartOne.getText().toString();
         String addressPartTwo=txtAddressPartTwo.getText().toString();
//         String landMark=txtLandMark.getText().toString();
         String city=txtCity.getText().toString();
         String state=txtState.getText().toString();
         String date = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());

        int status=1;
        if (TextUtils.isEmpty(firebaseKey)){
             key = mDatabase.child("users").child(currentUser.getUid()).child("address").push().getKey();
        }else {
            key = firebaseKey;
        }

        Address address = new Address(fullName, mobileNumber, pincode, addressPartOne, addressPartTwo,
                null, city, state,status, date, key);
        Map<String, Object> addressValue = address.toMap();
        Map<String, Object> childUpdate = new HashMap<>();
//        childUpdate.put("/users/"+currentUser+"/address")
        childUpdate.put("/users/"+currentUser.getUid()+"/address/"+key, addressValue);
        mDatabase.updateChildren(childUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                Navigation.findNavController(view).popBackStack(R.id.userAddressEntry, true);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(context, "Unable to add address", Toast.LENGTH_SHORT).show();
            }
        });
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
