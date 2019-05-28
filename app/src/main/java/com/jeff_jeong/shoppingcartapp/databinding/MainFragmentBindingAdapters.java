package com.jeff_jeong.shoppingcartapp.databinding;

import android.databinding.BindingAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jeff_jeong.shoppingcartapp.adapters.ProductsAdapter;
import com.jeff_jeong.shoppingcartapp.models.Product;

import java.util.List;

public class MainFragmentBindingAdapters {

    private static final int NUM_COLUMNS = 2;

    // 노테이션을 함으로써 xml 파일에서 사용가능하다.
    @BindingAdapter("productsList")
    public static void setProductsList(RecyclerView view, List<Product> products){

        if(products == null){
            return;
        }

        // 레이아웃 매니저를 가져온다.
        RecyclerView.LayoutManager layoutManager = view.getLayoutManager();
        if(layoutManager == null){
            view.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        }

        ProductsAdapter adapter = (ProductsAdapter) view.getAdapter();
        // 널 체크
        if(adapter == null){
            adapter = new ProductsAdapter(view.getContext(), products);
            view.setAdapter(adapter);
        }

    }
}
