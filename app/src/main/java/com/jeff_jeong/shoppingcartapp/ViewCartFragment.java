package com.jeff_jeong.shoppingcartapp;


// Created by Jeff_Jeong on 2019. 5. 29.


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeff_jeong.shoppingcartapp.databinding.FragmentViewCartBinding;


public class ViewCartFragment extends Fragment {

    private static final String TAG = "ViewCartFragment";

    //data binding
    FragmentViewCartBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentViewCartBinding.inflate(inflater);
        mBinding.setIMainActivity((IMainActivity)getActivity());
        mBinding.getIMainActivity().setCartVisibility(true);


        return mBinding.getRoot();
    }

    @Override
    public void onDestroy() {
        mBinding.getIMainActivity().setCartVisibility(false);
        super.onDestroy();
    }

}