package com.jeff_jeong.shoppingcartapp;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeff_jeong.shoppingcartapp.models.Product;
import com.jeff_jeong.shoppingcartapp.viewmodel.ProductViewModel;
import com.jeff_jeong.shoppingcartapp.databinding.FragmentViewProductBinding;

// 제품 상세 페이지 프래그먼트
public class ViewProductFragment extends Fragment {

    private static final String TAG = "ViewProductFragment";

    // Data binding
    FragmentViewProductBinding mBinding;

    //vars
    private Product mProduct;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if(bundle != null) {
            mProduct = bundle.getParcelable(getString(R.string.intent_product));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentViewProductBinding.inflate(inflater);

        mBinding.setIMainActivity((IMainActivity)getActivity());

        // 뷰모델
        ProductViewModel productViewModel = new ProductViewModel();
        // 값 설정
        productViewModel.setProduct(mProduct);
        productViewModel.setQuantity(1);

        // 뷰모델을 설정
        mBinding.setProductViewModel(productViewModel);


        return mBinding.getRoot();
    }

}













