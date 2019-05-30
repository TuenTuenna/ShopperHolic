package com.jeff_jeong.shoppingcartapp;


// Created by Jeff_Jeong on 2019. 5. 29.


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeff_jeong.shoppingcartapp.databinding.FragmentViewCartBinding;
import com.jeff_jeong.shoppingcartapp.models.CartItem;
import com.jeff_jeong.shoppingcartapp.util.PreferenceKeys;
import com.jeff_jeong.shoppingcartapp.util.Products;
import com.jeff_jeong.shoppingcartapp.viewmodel.CartViewModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ViewCartFragment extends Fragment {

    private static final String TAG = "ViewCartFragment";

    //data binding
    FragmentViewCartBinding mBinding;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentViewCartBinding.inflate(inflater);

        // 장바구니 프래그 먼트가 보이도록 설정한다.
        mBinding.setIMainActivity((IMainActivity)getActivity());
        mBinding.getIMainActivity().setCartVisibility(true);

        // 장바구니 목록을 가져온다.
        getShoppingCartList();

        return mBinding.getRoot();
    }

    // 장바구니 목록을 가져오는 메소드
    private void getShoppingCartList(){
        // 쉐어드를 가져온다. - 프래그먼트니까 getActivity() 로 콘텍스트를 가져온다.
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Set<String> serialNumbers = preferences.getStringSet(PreferenceKeys.shopping_cart, new HashSet<String>());

        // 제품들 가져와서 넣기
        Products products = new Products();
        // 장바구니 아이템 어레이 리스트
        List<CartItem> cartItems = new ArrayList<>();

        // 해쉬셋 만큼 반복한다.
        for(String serialNumber : serialNumbers){
            // 시리얼 넘버 제품의 갯수를 가져온다.
            int quantity = preferences.getInt(serialNumber,0);
            // 장바구니 아이템 어레이 리스트에 추가한다.
            cartItems.add(new CartItem(products.PRODUCT_MAP.get(serialNumber), quantity));
        }

        // 장바구니 뷰 모델 인스턴스화
        CartViewModel cartViewModel = new CartViewModel();
        // 장바구니 뷰 모델의 값을 설정한다.
        cartViewModel.setCart(cartItems);
        // xml 의 ui를 설정한다.
        mBinding.setCartView(cartViewModel);
    }

    public void updateCartItems(){
        getShoppingCartList();
    }

    @Override
    public void onDestroy() {
        mBinding.getIMainActivity().setCartVisibility(false);
        super.onDestroy();
    }

}