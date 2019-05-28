package com.jeff_jeong.shoppingcartapp.databinding;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jeff_jeong.shoppingcartapp.R;

// 글라이드가 편한 이유가 이미지 소스를 설정해 줄때
// url 이나 uri 나 아무거나 넣어도 알아서 이미지를 설정해주기 때문
// 글라이드 바인딩 어답터
public class GlideBindingAdapters {

//    //<ImageView
//    //                app:imageUrl="@{product.image}"
//    //                />
//    // 위와 같이 imageUrl 로 활용한다.
    @BindingAdapter("imageUrl")
    public static void setImage(ImageView imageView, int imageUrl){
        // 이미지 설정을 위한 글라이드
        Context context = imageView.getContext();

        // 기본 이미지 설정
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background);

        Glide.with(context)
                .setDefaultRequestOptions(options)
                .load(imageUrl)
                .into(imageView);

    }

//    @BindingAdapter("imageUrl")
//    public static void setImage(ImageView imageView, String imageUrl){
//        // 이미지 설정을 위한 글라이드
//        Context context = imageView.getContext();
//
//        // 기본 이미지 설정
//        RequestOptions options = new RequestOptions()
//                .placeholder(R.drawable.ic_launcher_background)
//                .error(R.drawable.ic_launcher_background);
//
//        Glide.with(context)
//                .setDefaultRequestOptions(options)
//                .load(imageUrl)
//                .into(imageView);
//
//    }

}
