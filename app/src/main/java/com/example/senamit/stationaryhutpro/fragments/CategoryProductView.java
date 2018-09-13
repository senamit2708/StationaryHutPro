package com.example.senamit.stationaryhutpro.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.senamit.stationaryhutpro.R;
import com.example.senamit.stationaryhutpro.adapters.CategoryProductAdapter;
import com.example.senamit.stationaryhutpro.models.Product;
import com.example.senamit.stationaryhutpro.viewModels.ProductCartViewModel;
import com.example.senamit.stationaryhutpro.viewModels.ProductCategoryViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryProductView extends Fragment {

    private static final String TAG = CategoryProductView.class.getSimpleName();
    private static final String CATEGORY_KEY = "productCategory";

    private Context context;
    private String mUserId;
    private String productCategory;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private CategoryProductAdapter mAdapter;
    private ProductCategoryViewModel mViewModel;
    private ProductCartViewModel mCartViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productCategory = getArguments().getString(CATEGORY_KEY);
        Log.i(TAG, "the product category is "+productCategory);
        mCartViewModel = ViewModelProviders.of(getActivity()).get(ProductCartViewModel.class);
        mViewModel = ViewModelProviders.of(getActivity()).get(ProductCategoryViewModel.class);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = container.getContext();
        View view = inflater.inflate(R.layout.activity_category_product_view, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recycler_product);
        mLayoutManager = new GridLayoutManager(context, 2);
        mAdapter = new CategoryProductAdapter(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(10);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        mViewModel.getCategoryProduct(productCategory).observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                mAdapter.setProduct(products);
            }
        });

    }
}
