package com.jeff_jeong.shoppingcartapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeff_jeong.shoppingcartapp.adapters.ProductsAdapter;
import com.jeff_jeong.shoppingcartapp.databinding.FragmentMainBinding;
import com.jeff_jeong.shoppingcartapp.models.Product;
import com.jeff_jeong.shoppingcartapp.util.Products;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = "MainFragment";

    // Data binding
    FragmentMainBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMainBinding.inflate(inflater);
        mBinding.swipeRefreshLayout.setOnRefreshListener(this);

        // 상품목록을
        setupProductsList();

        return mBinding.getRoot();
    }

    @Override
    public void onRefresh() {

        Products products = new Products();
        List<Product> productList = new ArrayList<>();
        productList.addAll(Arrays.asList(products.PRODUCTS));
        ((ProductsAdapter)mBinding.recyclervView.getAdapter()).refreshList(productList);

        // 아이템 로드가 완료되면
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        (mBinding.recyclervView.getAdapter()).notifyDataSetChanged();
        mBinding.swipeRefreshLayout.setRefreshing(false);
    }

    // 리사이클러뷰 설정
    private void setupProductsList(){
        Products products = new Products();
        List<Product> productList = new ArrayList<>();
        productList.addAll(Arrays.asList(products.PRODUCTS));
        mBinding.setProducts(productList);
    }

}














