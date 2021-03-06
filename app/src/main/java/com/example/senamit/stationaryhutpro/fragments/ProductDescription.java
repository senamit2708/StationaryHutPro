package com.example.senamit.stationaryhutpro.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.senamit.stationaryhutpro.CountDrawable;
import com.example.senamit.stationaryhutpro.R;
import com.example.senamit.stationaryhutpro.models.Product;
import com.example.senamit.stationaryhutpro.models.UserCart;
import com.example.senamit.stationaryhutpro.viewModels.ProductCartViewModel;
import com.example.senamit.stationaryhutpro.viewModels.ProductForSaleViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

public class ProductDescription extends Fragment implements View.OnClickListener {

    private static final String TAG = ProductDescription.class.getSimpleName();
    private static final String PRODUCT_KEY = "product_key";
    private static final String PRODUCT_INDEX = "product_index";

    private Context context;
    private String mProductNumber;
    private String mProductName;
    private String mProductPrice;
    private String mImageUrl;
    private int clickedItemIndex;
    private Product product;
    private String userId;
    private String date;

    private TextView mTxtProductName;
    private TextView mTxtProductPrice;
    private TextView mTxtProductNumber;
    private ImageView mProductImage;
    private Button mBtnAddToCart;
    private Button mBtnBuyNow;

    private ProductForSaleViewModel mViewModel;
    private ProductCartViewModel mCartViewModel;
    private DatabaseReference mDatabase;
    private DatabaseReference mUserDatabase;
    private LiveData<DataSnapshot> liveData;
    private FirebaseUser mFirebaseUser;

    private boolean showToast = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProductNumber = getArguments().getString(PRODUCT_KEY);
        clickedItemIndex = getArguments().getInt(PRODUCT_INDEX);
        Log.i(TAG, "inside oncreate product description "+mProductNumber);
        Log.i(TAG, "inside oncreate product description "+clickedItemIndex);

        mCartViewModel = ViewModelProviders.of(getActivity()).get(ProductCartViewModel.class);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = container.getContext();
        View view = inflater.inflate(R.layout.activity_product_description, container, false);
        setHasOptionsMenu(true);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTxtProductName = view.findViewById(R.id.txtProductName);
        mTxtProductPrice = view.findViewById(R.id.txtProductPrice);
        mTxtProductNumber = view.findViewById(R.id.txtProductNumber);
        mProductImage = view.findViewById(R.id.imageProduct);
        mBtnAddToCart = view.findViewById(R.id.btnAddToCart);
        mBtnBuyNow = view.findViewById(R.id.btnBuyNow);

        mBtnBuyNow.setOnClickListener(this);
        mBtnAddToCart.setOnClickListener(this);

         date = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = mFirebaseUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mViewModel = ViewModelProviders.of(this).get(ProductForSaleViewModel.class);

        liveData= mViewModel.getProductMutableLiveData(mProductNumber);

        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                if (dataSnapshot!=null){
                    Log.i(TAG, "inside onChanged method of livedata observer of product desc");
                    product= dataSnapshot.getValue(Product.class);
                    Log.i(TAG, "the product is "+product);
                    mProductName = product.getProductName();
                    mProductPrice = product.getProductPrice();
                    mImageUrl = product.getImageUrl();
                    mTxtProductName.setText(product.getProductName());
                    mTxtProductPrice.setText(product.getProductPrice());
                    mTxtProductNumber.setText(product.getProductNumber());

                    Picasso.with(context).load(product.getImageUrl()).into(mProductImage);

                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnAddToCart:
                showToast = true;
                pushProductToCart();
                break;
            case R.id.btnBuyNow:
                pushProductToCart();
                Navigation.findNavController(view).navigate(R.id.action_productDescription_to_cartProduct);

            default:
                Log.i(TAG, "Select any other click option");
        }
    }
    private void pushProductToCart() {
        Log.i(TAG, "inside pushproducttocart method ");
        Query mdataRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(userId).child("cart").child(mProductNumber);

        mdataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "inside ondatachange method "+dataSnapshot.getKey());
                if (dataSnapshot.exists()){
                    Log.i(TAG, "inside the if true statement "+mProductNumber);
                    if (showToast==true){
                        Toast.makeText(context, "Product is already in the cart",Toast.LENGTH_SHORT).show();
                        showToast=false;
                    }
                }
                else {
                    Log.i(TAG, "product is not avaiable, trying to load the product");
                    UserCart cart = new UserCart(mProductNumber, date, mProductPrice, mProductName, mImageUrl);
                    cart.setQuantity(1);
                    Map<String, Object> cartValue = cart.toMap();
                    Map<String, Object> childUpdate = new HashMap<>();
                    Log.i(TAG, "username is "+mFirebaseUser.getUid());
                    mUserDatabase = mDatabase.child("users").child(userId).child("cart");
                    childUpdate.put("/"+mProductNumber+"/", cartValue);
                    mUserDatabase.updateChildren(childUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            if (showToast==true){
                                Toast.makeText(context, "Product added to the cart", Toast.LENGTH_SHORT).show();
                                showToast=false;
                            }
                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Some error occured", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        Log.i(TAG, "inside oncreate option menu in cartproduct");
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem menuItem = menu.findItem(R.id.cartProduct);
        LayerDrawable icon = (LayerDrawable) menuItem.getIcon();

        final CountDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_group_count);
        if (reuse != null && reuse instanceof CountDrawable) {
            badge = (CountDrawable) reuse;
        } else {
            badge = new CountDrawable(context);
        }
        mCartViewModel.getCartProductCount(userId).observe(this, new Observer<List<UserCart>>() {
            @Override
            public void onChanged(List<UserCart> userCarts) {
                if (userCarts!=null){
                    int size= userCarts.size();
                    badge.setCount(Integer.toString(size));
                }
            }
        });

        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_group_count, badge);
    }


}
