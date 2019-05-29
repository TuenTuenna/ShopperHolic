package com.jeff_jeong.shoppingcartapp.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.jeff_jeong.shoppingcartapp.BR;
import com.jeff_jeong.shoppingcartapp.models.Product;

// Created by Jeff_Jeong on 2019. 5. 29.

// 제품 뷰 모델 클래스
// 베이스 옵저버블 클래스를 상속하자.
public class ProductViewModel extends BaseObservable {

    private static final String TAG = "ProductViewModel";

    private Product product;
    private int quantity;

    // 이미지 로딩 다됬는지 여부
    private boolean imageVisibility = false;

    // 게터 세터
    // 게터 바인딩은 바인더블 노테이션만 추가하면 된다.
    @Bindable
    public Product getProduct() {
        return product;
    }

    // 게터 바인딩은 바인더블 노테이션만 추가하면 된다.
    @Bindable
    public int getQuantity() {
        return quantity;
    }

    @Bindable
    public boolean isImageVisibility() {
        return imageVisibility;
    }


    public void setProduct(Product product) {
        this.product = product;
        // 세터 메소드는 BR 클래스에게 데이터가 변경되었다고 알려주기만 하면 된다.
        notifyPropertyChanged(BR.product);
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        // 세터 메소드는 BR 클래스에게 데이터가 변경되었다고 알려주기만 하면 된다.
        notifyPropertyChanged(BR.quantity);
    }

    public void setImageVisibility(boolean mImageVisibility) {
        imageVisibility = mImageVisibility;
        Log.d(TAG, "setImageVisibility: " +  imageVisibility);
        notifyPropertyChanged(BR.imageVisibility);
    }

    // app:requestListener="@{productViewModel.customRequestListener}"
    // 해당 customRequestListener를 참조하게 된다 자동으로
    // 글라이드 이미지 리스너
    public RequestListener getCustomRequestListener(){
        return new RequestListener() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                return false;
            }

            // 이미지가 로딩 됬을때
            @Override
            public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {

                // 이미지 로딩 여부 트루
                setImageVisibility(true);
                Log.d(TAG, "onResourceReady: 이미지 다운로드 완료");
                return false;
            }
        };
    }


}
