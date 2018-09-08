package com.example.senamit.stationaryhutpro.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.senamit.stationaryhutpro.R;
import com.example.senamit.stationaryhutpro.adapters.CartProductAdapter;
import com.example.senamit.stationaryhutpro.models.UserCart;
import com.example.senamit.stationaryhutpro.viewModels.ProductCartViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
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

    private Button btnPayment;
    private Button btnStartBuying;
    private TextView txtTotalPrice;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private CartProductAdapter mAdapter;
    List<UserCart> userCartProduct;
    private ConstraintLayout mConstraint;
    private ConstraintLayout mEmptyConstraint;

    private ProductCartViewModel mViewModel;

    private FirebaseUser mFirebaseUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(ProductCartViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context= container.getContext();
        View view = inflater.inflate(R.layout.activity_cart_product, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserId = mFirebaseUser.getUid();

        btnPayment =  view.findViewById(R.id.btnPayment);
        btnStartBuying = view.findViewById(R.id.btnStartBuying);
        txtTotalPrice = view.findViewById(R.id.txtTotalPrice);
        mConstraint = view.findViewById(R.id.view_coordinate);
        mEmptyConstraint = view.findViewById(R.id.emptyView);
        mEmptyConstraint.setVisibility(View.GONE);

        mRecyclerView = view.findViewById(R.id.recycler_cart);
        mLayoutManager = new LinearLayoutManager(context);
        mAdapter = new CartProductAdapter(context, this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);



        mViewModel.getCartData(mUserId).observe(this, new Observer<List<UserCart>>() {
            @Override
            public void onChanged(@Nullable List<UserCart> userCarts) {
                if (userCarts!= null){
                    if (userCarts.size()>0){
                        mEmptyConstraint.setVisibility(View.GONE);
                        mConstraint.setVisibility(View.VISIBLE);

                        userCartProduct = new ArrayList<>();
                        userCartProduct.addAll(userCarts);
                        Log.i(TAG, "the size of cart is "+userCarts.size());
                        mAdapter.setCartProduct(userCarts);
                        int size  = userCartProduct.size();
                        int totalPrice =0;
                        for (int i=0;i<size; i++){
                            int quantity = userCartProduct.get(i).getQuantity();
                            int productPrice = Integer.parseInt(userCartProduct.get(i).getProductPrice());
                            int price = quantity * productPrice;
                            totalPrice= totalPrice+price;
                        }
                        txtTotalPrice.setText(String.valueOf(totalPrice));
                    }else {
                        Log.i(TAG,"inside else statement of user cart");
                        mConstraint.setVisibility(View.GONE);
                        mEmptyConstraint.setVisibility(View.VISIBLE);
                    }

                }
                else{


                }
            }
        });

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.setOrderedProduct(userCartProduct);
                Navigation.findNavController(view).navigate(R.id.action_cartProduct_to_orderDelivery);
            }
        });

        btnStartBuying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_cartProduct_to_productForSaleView);
            }
        });
    }

    @Override
    public void funRemoveBtnClick(String productNumber, int position) {
        Log.i(TAG, "the product number fo product is  "+productNumber +"  position is "+ position);
        mViewModel.removeProductFromCart(productNumber);
    }

    @Override
    public void funAddProductQuantity(String productNumber, int quantity, int price) {
        Log.i(TAG, "inside funaddproductqunaity "+ productNumber +"quantity"+quantity);
        mViewModel.addProductQuantityToCart(productNumber, quantity, price);

    }






}
