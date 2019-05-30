package com.jeff_jeong.shoppingcartapp.viewmodel;


// Created by Jeff_Jeong on 2019. 5. 30.

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.jeff_jeong.shoppingcartapp.BR;
import com.jeff_jeong.shoppingcartapp.IMainActivity;
import com.jeff_jeong.shoppingcartapp.models.CartItem;

// 장바구니 아이템 뷰 모델 클래스
public class CartItemViewModel extends BaseObservable {

    private static final String TAG = "CartItemViewModel";

    // 장바구기 아이템
    private CartItem cartItem;

    @Bindable
    public CartItem getCartItem() {
        return cartItem;
    }


    public void setCartItem(CartItem cartItem) {
        this.cartItem = cartItem;
        notifyPropertyChanged(BR.cartItem);
    }

    // 상품의 갯수를 가져오는 메소드
    public String getQuantityString(CartItem cartItem){
        return ("수량: " + String.valueOf(cartItem.getQuantity()));
    }

    // 상품의 수량을 증기시키는 메소드
    public void increaseQuantity(Context context){
        // 장바구니 아이템 가져오기
        CartItem cartItem = getCartItem();
        // 모델클래스의 상품수량 증가
        cartItem.setQuantity(cartItem.getQuantity() + 1);
        // xml ui 변경
        setCartItem(cartItem);
        // 메인액티비티 인터페이스를 인스턴스화 한다.
        IMainActivity iMainActivity = (IMainActivity)context;
        // 메인액티비티 인터페이스에 수량이 변경되었다고 알린다.
        iMainActivity.updateQuantity(cartItem.getProduct(), 1);
    }

    // 상품의 수량을 감소시키는 메소드
    public void decreaseQuantity(Context context){
        // 장바구니 아이템 가져오기
        CartItem cartItem = getCartItem();
        // 메인액티비티 인터페이스를 인스턴스화 한다.
        IMainActivity iMainActivity = (IMainActivity)context;
        // 언더플로우 방지
        if(cartItem.getQuantity() > 1){
            // 모델클래스의 상품수량 설정
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            // xml ui 변경
            setCartItem(cartItem);

            // 메인액티비티 인터페이스에 수량이 변경되었다고 알린다.
            iMainActivity.updateQuantity(cartItem.getProduct(), -1);
        }
        else if(cartItem.getQuantity() == 1){ // 상품의 수량이 0이면
            // 모델클래스 상품수량 설정
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            // xml ui 변경
            setCartItem(cartItem);
            // 메인액티비티에 장바구니 아이템을 제거한다.
            iMainActivity.removeCartItem(cartItem);
        }
    }




}
