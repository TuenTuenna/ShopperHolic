package com.jeff_jeong.shoppingcartapp.databinding;


// Created by Jeff_Jeong on 2019. 5. 30.


import android.databinding.BindingAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jeff_jeong.shoppingcartapp.adapters.CartItemAdapter;
import com.jeff_jeong.shoppingcartapp.models.CartItem;

import java.util.List;

// 장바구니 프래그먼트뷰 리사이클러뷰 바인딩 어답터
public class ViewCartFragmentBindingAdapters {

    private static final String TAG = "ViewCartFragmentBinding";

    // app:cartItems="@{cartView.cart}"
    @BindingAdapter("cartItems")
    public static void setCartItems(RecyclerView view, List<CartItem> cartItems){
        if(cartItems == null){
            return;
        }
        RecyclerView.LayoutManager layoutManager = view.getLayoutManager();
        if(layoutManager == null){
            view.setLayoutManager(new LinearLayoutManager(view.getContext()));
        }
        CartItemAdapter adapter = (CartItemAdapter) view.getAdapter();

        // 장바구니 아이템 어답터가 없으면
        if(adapter == null){
            // 장바구니 아이템 어답터를 인스턴스화 한다.
            adapter = new CartItemAdapter(view.getContext(), cartItems);
            view.setAdapter(adapter);
        }
        else{
            adapter.updateCartItems(cartItems);
        }
    }

}
