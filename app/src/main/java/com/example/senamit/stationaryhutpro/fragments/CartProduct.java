package com.example.senamit.stationaryhutpro.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.senamit.stationaryhutpro.R;
import com.example.senamit.stationaryhutpro.adapters.CartProductAdapter;
import com.example.senamit.stationaryhutpro.models.UserCart;
import com.example.senamit.stationaryhutpro.viewModels.ProductCartViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CartProduct extends Fragment implements CartProductAdapter.ButtonClickInterface {

    private static final String TAG = CartProduct.class.getSimpleName();

    private Context context;
    private String mUserId;
    //    private UserCart userCart;
    private List<UserCart> userCart;
    private Button btnPayment;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private CartProductAdapter mAdapter;

    private ProductCartViewModel mViewModel;

    private FirebaseUser mFirebaseUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context= container.getContext();
        View view = inflater.inflate(R.layout.activity_cart_product, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserId = mFirebaseUser.getUid();

        btnPayment =  view.findViewById(R.id.btnPayment);
        mViewModel = ViewModelProviders.of(this).get(ProductCartViewModel.class);
        mRecyclerView = view.findViewById(R.id.recycler_cart);
        mLayoutManager = new LinearLayoutManager(context);
        mAdapter = new CartProductAdapter(context, this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        mViewModel.getCartData(mUserId).observe(this, new Observer<List<UserCart>>() {
            @Override
            public void onChanged(@Nullable List<UserCart> userCarts) {
                if (userCarts!= null){
                    mAdapter.setCartProduct(userCarts);
                }
            }
        });

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_cartProduct_to_userAddressView);
            }
        });
    }

    @Override
    public void funRemoveBtnClick(String productNumber, int position) {
        Log.i(TAG, "the product number fo product is  "+productNumber +"  position is "+ position);
        mViewModel.removeProductFromCart(productNumber);
    }
}
