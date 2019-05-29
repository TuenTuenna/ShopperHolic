package com.jeff_jeong.shoppingcartapp.viewmodel;


import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;

import com.jeff_jeong.shoppingcartapp.BR;
import com.jeff_jeong.shoppingcartapp.models.CartItem;
import com.jeff_jeong.shoppingcartapp.util.BigDecimalUtil;
import com.jeff_jeong.shoppingcartapp.util.Prices;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// Created by Jeff_Jeong on 2019. 5. 29.

// 장바구니 뷰모델 클래스
public class CartViewModel extends BaseObservable {

    private static final String TAG = "장바구니 뷰모델";

    // 장바구니 리스트
    private List<CartItem> cart = new ArrayList<>();

    // 장바구니 보임 여부
    private boolean isCartVisible;

    @Bindable
    public List<CartItem> getCart() {
        return cart;
    }

    @Bindable
    public boolean isCartVisible() {
        return isCartVisible;
    }


    public void setCart(List<CartItem> cart) {
        this.cart = cart;
        notifyPropertyChanged(BR.cart);
    }


    public void setCartVisible(boolean cartVisible) {
        isCartVisible = cartVisible;
        notifyPropertyChanged(BR.cartVisible);
    }

    // 제품수량을 가져오는 메소드
    public String getProductQuantitiesString(){
        int totalItems = 0;

        for(CartItem cartItem : cart){
            totalItems += cartItem.getQuantity();
        }

        String s = "";

        if(totalItems > 1){
            s = "items";
        }else {
            s = "item";
        }
        return ("(" + String.valueOf(totalItems) + " " + s + ")");
    }

    // 총 금액을 가져오는 메소드
    public String getTotalCostString(){
        double totalCost = 0;

        for(CartItem cartItem : cart){
            int productQuantity = cartItem.getQuantity();
            //
            double cost = productQuantity * (Prices.getPrices().get(String.valueOf(cartItem.getProduct().getSerial_number()))).doubleValue();
            totalCost += cost;
        }
        Log.d(TAG, "getTotalCostString: totalCost : " + totalCost);
        return "$" + BigDecimalUtil.getValue(new BigDecimal(totalCost));
    }

}
