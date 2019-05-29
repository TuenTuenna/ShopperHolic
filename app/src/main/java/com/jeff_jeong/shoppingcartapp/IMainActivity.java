package com.jeff_jeong.shoppingcartapp;

import com.jeff_jeong.shoppingcartapp.models.Product;

// 프래그먼트끼리는 직접적으로 통신하면 안된다.
// 메인액티비티 인터페이스를 통해 통신하자
public interface IMainActivity {


    // 제품상세패이지 프래그먼트를 보여주는 메소드
    void inflateViewProductFragment(Product product);

    // 수량을 보여주는 다이얼로그 메소드
    void showQuantityDialog();

    // 수량을 설정하는 메소드
    void setQuantity(int quantity);

    // 장바구니에 담는 메소드
    void addToCart(Product product, int quantity);

//     장바구니 프래그먼트를 보여주는 메소드
    void inflateViewCartFragment();

    // 장바구니 보임여부를 설정하는 메소드
    void setCartVisibility(boolean visibility);


}
