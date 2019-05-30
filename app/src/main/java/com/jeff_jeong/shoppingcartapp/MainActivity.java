package com.jeff_jeong.shoppingcartapp;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import com.jeff_jeong.shoppingcartapp.databinding.ActivityMainBinding;
import com.jeff_jeong.shoppingcartapp.models.CartItem;
import com.jeff_jeong.shoppingcartapp.models.Product;
import com.jeff_jeong.shoppingcartapp.util.PreferenceKeys;
import com.jeff_jeong.shoppingcartapp.util.Products;
import com.jeff_jeong.shoppingcartapp.viewmodel.CartViewModel;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements IMainActivity {

    private static final String TAG = "MainActivity!";

    //data binding
    ActivityMainBinding mBinding;

    // vars
    private boolean mClickToExit = false;
    private Handler mCheckoutHandler;
    private int mCheckoutTimer = 0;
    private Runnable mCheckoutRunnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // 바인딩 인스턴스의 장바구니에 터치 리스너를 설정한다.
        mBinding.cart.setOnTouchListener(new CartTouchListener());

        // 결제 버튼에 클릭 리스너를 설정한다.
        // android:id="@+id/proceed_to_checkout"
        mBinding.proceedToCheckout.setOnClickListener(mCheckoutListener);

        // 장바구니를 가져온다.
        getShoppingCart();
        // 메인프레그먼트 시작
        init();

    }

    // 메인 프래그먼트 시작 메소드
    private void init(){
        MainFragment fragment = new MainFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, fragment, getString(R.string.fragment_main));
        transaction.commit();
    }

    // 장바구니를 가져오는 메소드
    // 쉐어드에 있는 장바구니 정보를 가져와서 뷰에 적용시킨다.
    private void getShoppingCart() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> serialNumbers = preferences.getStringSet(PreferenceKeys.shopping_cart, new HashSet<String>());

        // 카트 아이템 목록을 가져온다.
        Products products = new Products();
        List<CartItem> cartItems = new ArrayList<>();
        for(String serialNumber : serialNumbers){
            // 쉐어드에서 해당 시리얼 번호에 해당하는 제품수량을 가져온다.
            int quantity = preferences.getInt(serialNumber, 0);
            // 장바구니 목록에 장바구니 아이템을 만들어 넣는다.
            cartItems.add(new CartItem(products.PRODUCT_MAP.get(serialNumber), quantity));
        }

        // 장바구니 뷰모델
        CartViewModel cartViewModel = new CartViewModel();
        cartViewModel.setCart(cartItems);
        // 카트 아이템이 null 일수도 있으니까 트라이캐치 getCartView()
        try{
            // 장바구니 뷰모델 보임여부 설정
            cartViewModel.setCartVisible(mBinding.getCartViewModel().isCartVisible());
        } catch (NullPointerException e) {
            Log.e(TAG, "getShoppingCart: NullPointerException : " + e.getMessage());
        }
        // 장바구니 뷰 모델을 설정한다.
        mBinding.setCartViewModel(cartViewModel);
    }


    // 제품상세 페이지 프래그먼트가 그려질때
    @Override
    public void inflateViewProductFragment(Product product) {

        // 프래그먼트를 인스턴스화한다.
        ViewProductFragment fragment = new ViewProductFragment();
        // 데이터를 번들에 넣는다.
        Bundle bundle = new Bundle();
        bundle.putParcelable(getString(R.string.intent_product), product);
        // 프래그먼트에 매개변수를 설정한다.
        fragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, fragment, getString(R.string.fragment_view_product));
        // 백스택에 포함 - 뒤로가기 버튼눌렀을때 쌓여있어야 하니까
        transaction.addToBackStack(getString(R.string.fragment_view_product));

        transaction.commit();
    }

    // 수량을 보여주는 다이얼로그 메소드가 실행될떄
    @Override
    public void showQuantityDialog() {
        Log.d(TAG, "showQuantityDialog: called");

        ChooseQuantityDialog dialog = new ChooseQuantityDialog();
        dialog.show(getSupportFragmentManager(), getString(R.string.dialog_choose_quantity));

    }

    // 만약 데이터 바인딩을 사용하지 않는다면
    // 각각의 프래그먼트 뷰에 위젯을 일일히 설정해야할 것이다.
    // 제거하고 다시 그리고 데이터를 번들로 보내고 해야 할것이다.
    // 데이터 바인딩을 이러한 과정들을 없애준다.

    // 수량을 설정메소드가 실행될때
    @Override
    public void setQuantity(int quantity) {
        Log.d(TAG, "setQuantity: 선택된 제품수량 : " + quantity);


        // 제품상세 프래그먼트를 가져온다.
        ViewProductFragment fragment = (ViewProductFragment)getSupportFragmentManager().findFragmentByTag(getString(R.string.fragment_view_product));
        // 널 체크
        if(fragment != null){
            // 데이터 바인딩의 정말 좋은 장점이다.
            // 바인딩을 참조함으로서 ui 를 손쉽게 변경할수 있다.
            // 제품상세 페이지 프래그먼트의 수량을 설정한다. ui 변경
            fragment.mBinding.getProductViewModel().setQuantity(quantity);
        }

    }

    // 장바구니가 추가되었을때
    @Override
    public void addToCart(Product product, int quantity) {

        // 쉐어드에 수량을 저장하자
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        // 시리얼 넘버 해쉬셋을 가져온다.
        Set<String> serialNumbers = preferences.getStringSet(PreferenceKeys.shopping_cart, new HashSet<String>());
        // 시리얼 넘버 해쉬셋에 제품의 시리얼 넘버를 추가한다.
        serialNumbers.add(String.valueOf(product.getSerial_number()));

        editor.putStringSet(PreferenceKeys.shopping_cart, serialNumbers);
        editor.commit();

        // 제품의 시실얼 넘버에 들어있는 기존수량을 가져온다.
        int currentQuantity = preferences.getInt(String.valueOf(product.getSerial_number()), 0);

        // 기존 수량에 추가된 수량을 넣는다.
        editor.putInt(String.valueOf(product.getSerial_number()), (currentQuantity + quantity));
        editor.commit();

        // 수량 리셋
        setQuantity(1);

        Toast.makeText(this,"장바구니에 담겼습니다.", Toast.LENGTH_SHORT).show();


        // 장바구니를 가져온다.
        getShoppingCart();
    }

    // 장바구니 프래그먼트가 그려질때
    @Override
    public void inflateViewCartFragment() {
        // 장바구니 프래그먼트를 가져온다.
        ViewCartFragment fragment = (ViewCartFragment)getSupportFragmentManager().findFragmentByTag(getString(R.string.fragment_view_cart));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // 널체크
        // 프래그먼트가 비어있으면
        if(fragment == null){
            // 새로 인스턴스
            fragment = new ViewCartFragment();
            transaction.replace(R.id.main_container, fragment, getString(R.string.fragment_view_cart));
            // 백스택에 추가
            transaction.addToBackStack(getString(R.string.fragment_view_cart));
            transaction.commit();
        }


    }

    // 장바구니 프래그먼트 보임여부가 설정되었을때
    @Override
    public void setCartVisibility(boolean visibility) {
        // 바인딩 인스턴스에 장바구니 보임여부를 설정한다.
        mBinding.getCartViewModel().setCartVisible(visibility);
    }

    // 수량이 갱신되어질때
    @Override
    public void updateQuantity(Product product, int quantity) {
        // 쉐어드에서
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        // 현재 재품수량을 가져온다.
        int currentQuantity = preferences.getInt(String.valueOf(product.getSerial_number()),0);

        // 현재의 재품수량에 추가된 제품수량을 계산한다.
        editor.putInt(String.valueOf(product.getSerial_number()), (currentQuantity + quantity));
        // 쉐어드에 변경사항을 저장한다.
        editor.commit();

        // 장바구니를 가져온다.
        getShoppingCart();

    }

    // 장바구니 아이템 제거 메소드가 발동될때
    @Override
    public void removeCartItem(CartItem cartItem) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        // 해당 장바구니 아이템을 쉐어드에서 제거한다.
        editor.remove(String.valueOf(cartItem.getProduct().getSerial_number()));
        editor.commit();

        // 쉐어드에서 제품 시리얼 넘버 해쉬셋을 가져온다.
        Set<String> serialNumbers = preferences.getStringSet(PreferenceKeys.shopping_cart, new HashSet<String>());

        // 제품시리얼 넘버가 1개 이면
        if(serialNumbers.size() == 1){
            // 장바구니 쉐어드 자체를 지운다.
            editor.remove(PreferenceKeys.shopping_cart);
            editor.commit();
        }
        else { // 제품시리얼 넘버가 여러개 이면
            // 제품시리얼 넘버 해쉬셋에서 해당 아이템 만 제거한다.
            serialNumbers.remove(String.valueOf(cartItem.getProduct().getSerial_number()));
            editor.putStringSet(PreferenceKeys.shopping_cart, serialNumbers);
            editor.commit();
        }

        // 장바구니를 가져온다.
        getShoppingCart();

        // 장바구니 프래그먼트를 가져와서
        ViewCartFragment fragment = (ViewCartFragment)getSupportFragmentManager().findFragmentByTag(getString(R.string.fragment_view_cart));
        // 보여지고 있는 상태이면
        if(fragment != null){
            // 장바구니 프래그먼트에서 장바구니 아이템들을 갱신한다.
            fragment.updateCartItems();
        }


    }

    // 결제 메소드
    public void checkout(){
        // 프로그래스바를 보여준다.
        mBinding.progressBar.setVisibility(View.VISIBLE);

        // 핸들러 인스턴스화
        mCheckoutHandler = new Handler();
        mCheckoutRunnable = new Runnable() {
            @Override
            public void run() {
                mCheckoutHandler.postDelayed(mCheckoutRunnable, 200);
                mCheckoutTimer += 200;
                if(mCheckoutTimer >= 1600){
                    // 장바구니를 비운다.
                    emptyCart();
                    // 프로그래스바를 숨긴다.
                    mBinding.progressBar.setVisibility(View.GONE);
                    // 콜백제거
                    mCheckoutHandler.removeCallbacks(mCheckoutRunnable);
                    // 결제타이머 리셋
                    mCheckoutTimer = 0;
                }
            }
        };
        // 핸들러 돌리기
        mCheckoutRunnable.run();
    }

    // 장바구니를 비우는 메소드
    public void emptyCart(){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        // 시리얼넘버 해시셋 가져오기
        Set<String> serialNumbers = preferences.getStringSet(PreferenceKeys.shopping_cart, new HashSet<String>());

        for(String serialNumber : serialNumbers){
            editor.remove(serialNumber);
            editor.commit();
        }

        //
        editor.remove(PreferenceKeys.shopping_cart);
        editor.commit();

        Toast.makeText(this, "이용해 주셔서 감사합니다.", Toast.LENGTH_SHORT).show();

        removeViewCartFragment();
        // 장바구니를 가져온다.
        getShoppingCart();

    }


    // 장바구니 프래그먼트를 제거하는 메소드
    public void removeViewCartFragment(){
        getSupportFragmentManager().popBackStack();
        ViewCartFragment fragment = (ViewCartFragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.fragment_view_cart));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(fragment != null){
            transaction.remove(fragment);
            transaction.commit();
        }
    }


    // 클릭 리스너
    public View.OnClickListener mCheckoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 결제를 진행한다.
            checkout();
        }
    };


    // 장바구니 터치 리스너 클래스
    public static class CartTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            // 화면에서 손을 땠다면
            if(event.getAction() == MotionEvent.ACTION_UP){
                // 배경색 설정
                v.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.blue4));
                // 클릭
                v.performClick();

                // 장바구니 프래그먼트 그리기
                IMainActivity iMainActivity = (IMainActivity)v.getContext();
                iMainActivity.inflateViewCartFragment();


            }
            else if(event.getAction() == MotionEvent.ACTION_DOWN) { // 화면에 손을 댓다면
                // 배경색 설정
                v.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.blue6));
            }

            // false 를 반환하게 되면 처음 받은 제스쳐 액션 을 제외하고 동작하지 않는다.
            // true 로 바꿔야 ACIONT_UP 제스쳐도 받는다.
            return true;

        }

    }


}