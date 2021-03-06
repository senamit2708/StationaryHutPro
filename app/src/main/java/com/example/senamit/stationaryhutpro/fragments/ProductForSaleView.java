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
import android.widget.TextView;

import com.example.senamit.stationaryhutpro.CountDrawable;
import com.example.senamit.stationaryhutpro.R;
import com.example.senamit.stationaryhutpro.adapters.ProductForSaleAdapter;
import com.example.senamit.stationaryhutpro.models.Product;
import com.example.senamit.stationaryhutpro.models.UserCart;
import com.example.senamit.stationaryhutpro.viewModels.ProductCartViewModel;
import com.example.senamit.stationaryhutpro.viewModels.ProductForSaleViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProductForSaleView extends Fragment {

    private static final String TAG = ProductForSaleView.class.getSimpleName();

    private static final String PRODUCT_KEY = "product_key";
    private static final String PRODUCT_SEND = "product_Send";
    String key = null;

    private Context context;
    private String mUserId;

    private DatabaseReference mDatabase;

    TextView txtProductName;
    TextView txtProductNumber;
    Button mBtnTest;


    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProductForSaleAdapter mAdapter;
    private ProductForSaleViewModel mViewModel;
    private ProductCartViewModel mCartViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCartViewModel = ViewModelProviders.of(getActivity()).get(ProductCartViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = container.getContext();
        View view = inflater.inflate(R.layout.activity_product_for_sale_view, container, false);
        setHasOptionsMenu(true);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "the value of mViewModel is "+mViewModel);
        mViewModel = ViewModelProviders.of(this).get(ProductForSaleViewModel.class);
        Log.i(TAG, "the value of mViewModel is "+mViewModel);
        mRecyclerView = view.findViewById(R.id.recycler_product);
        mLayoutManager = new GridLayoutManager(context, 2);
        mAdapter = new ProductForSaleAdapter(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(10);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);




        mViewModel.getDataSnapshotLiveData().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(@Nullable List<Product> products) {
                if (products!= null){
//                    Collections.reverse(products);
                    Log.i(TAG, "inside the onchanged method, the size of product "+products.size());
                    mAdapter.setProduct(products);
                }
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
                mUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mCartViewModel.getCartProductCount(mUserId).observe(this, new Observer<List<UserCart>>() {
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
