package com.jeff_jeong.shoppingcartapp.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeff_jeong.shoppingcartapp.IMainActivity;
import com.jeff_jeong.shoppingcartapp.R;
import com.jeff_jeong.shoppingcartapp.databinding.ProductItemBinding;
import com.jeff_jeong.shoppingcartapp.models.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductsAdapter extends  RecyclerView.Adapter<ProductsAdapter.BindingHolder>{

    private static final String TAG = "ProductsAdapter";

    private List<Product> mProducts = new ArrayList<>();
    private Context mContext;
    // 메인 액티비티 인터페이스


    public ProductsAdapter(Context context, List<Product> products) {
        mProducts = products;
        mContext = context;
    }

    public void refreshList(List<Product> products){
        mProducts.clear();
        mProducts.addAll(products);
        notifyDataSetChanged();
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        ViewDataBinding binding = DataBindingUtil.inflate(
//                LayoutInflater.from(mContext), R.layout.product_item, parent, false);
        ProductItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(mContext), R.layout.product_item, parent, false);

        return new BindingHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        final Product product = mProducts.get(position);
        //         <variable
//            name="product"
//            type="com.jeff_jeong.shoppingcartapp.models.Product"
//            />
        // 프로덕트 인스턴스가 들어가게 되고
        holder.binding.setProduct(product);

        // 이렇게 함으로써 메인액티비티 인터페이스를 뷰에 추가했다.
        holder.binding.setIMainActivity((IMainActivity) mContext);


//         <variable
//            name="testUrl"
//            type="String"
//            />
        // xml 파일에서 variable 을 정의한 이름으로 setTestUrl 메소드가 만들어진다.
        // url 테스트
//        holder.binding.setTestUrl("https://www.sound-crowd.com/soundCrowdMobile/uploads/images/artworks/20190329_5c9d9ae06c3b4_044 (여자)아이들 - Senorita.jpeg");

        // 일반적인 방법
        // ViewDataBinding binding 이것을 사용하면 아래 것을 써야함
//        holder.binding.setVariable(BR.product, product);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public class BindingHolder extends RecyclerView.ViewHolder{

        // 일반적인 방법
//        ViewDataBinding binding;
        // 커스텀 방법
        ProductItemBinding binding;

        public BindingHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }



}













