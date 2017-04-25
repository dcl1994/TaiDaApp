package com.siyann.taidaapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 一米菜园界面
 */
public class GardenActivity extends FragmentActivity implements View.OnClickListener {
    private Context mContext;
    private TextView mtextView;
    private ImageView mimageView;

    //底部菜单的四个linearlayout
    private LinearLayout ll_home;
    private LinearLayout ll_classify;
    private LinearLayout ll_shcart;
    private LinearLayout ll_my;

    //底部菜单的四个imageview
    private ImageView iv_home;
    private ImageView iv_classify;
    private ImageView iv_shcart;
    private ImageView iv_my;

    //底部菜单的四个textview
    private TextView tv_home;
    private TextView tv_classify;
    private TextView tv_shcart;
    private TextView tv_my;

    //四个fragment
    private Fragment homeFragment;
    private Fragment classifyFragment;
    private Fragment shoppingcartFragment;
    private Fragment myFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garden);
        mContext=this;
        init();
        //初始化并设置当前fragment
        initFragment(0);

    }

    /**
     * 初始化UI
     */
    private void init() {
        Intent intent=getIntent();
        mtextView= (TextView) findViewById(R.id.tv_ameter_garden);
        mtextView.setText(intent.getStringExtra("title"));

        mimageView= (ImageView) findViewById(R.id.leftback);
        mimageView.setOnClickListener(this);


        /**
         * 初始化底部菜单的四个linearlayout
         */
        ll_home= (LinearLayout) findViewById(R.id.ll_home);
        ll_classify= (LinearLayout) findViewById(R.id.ll_classify);
        ll_shcart= (LinearLayout) findViewById(R.id.ll_shcart);
        ll_my= (LinearLayout) findViewById(R.id.ll_my);

        ll_home.setOnClickListener(this);
        ll_classify.setOnClickListener(this);
        ll_shcart.setOnClickListener(this);
        ll_my.setOnClickListener(this);

        /**
         * 初始化底部菜单的四个imageview
         */
        iv_home= (ImageView) findViewById(R.id.iv_home);
        iv_classify= (ImageView) findViewById(R.id.iv_classify);
        iv_shcart= (ImageView) findViewById(R.id.iv_shcart);
        iv_my= (ImageView) findViewById(R.id.iv_my);


        /**
         * 初始化底部菜单的四个textview
         */
        tv_home= (TextView) findViewById(R.id.tv_home);
        tv_classify= (TextView) findViewById(R.id.tv_classify);
        tv_shcart= (TextView) findViewById(R.id.tv_shcart);
        tv_my= (TextView) findViewById(R.id.tv_my);
    }


    /**
     * 初始化fragment
     * @param i
     */
    private void initFragment(int i) {
        FragmentManager fragmentManager=getSupportFragmentManager();
        //开启事物
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        //隐藏所有fragment
        hindFragment(transaction);
        switch (i){
            case 0:
                if (homeFragment==null){
                    homeFragment=new HomeFragment();
                    transaction.add(R.id.fl_content,homeFragment);
                }else {
                    transaction.show(homeFragment);
                }
                break;

            case 1:
                if (classifyFragment==null){
                    classifyFragment=new ClassifyFragment();
                    transaction.add(R.id.fl_content,classifyFragment);
                }else{
                    transaction.show(classifyFragment);
                }
                break;
            case 2:
                if (shoppingcartFragment==null){
                    shoppingcartFragment=new ShoppingCartFragment();
                    transaction.add(R.id.fl_content,shoppingcartFragment);
                }else{
                    transaction.show(shoppingcartFragment);
                }
                break;

            case 3:
                if (myFragment==null){
                    myFragment=new MyFragment();
                    transaction.add(R.id.fl_content,myFragment);
                }else{
                    transaction.show(myFragment);
                }
                break;
            default:
                break;
        }
        // 提交事务
        transaction.commit();

    }


    /**
     * 隐藏所有fragment
     * @param transaction
     */
    private void hindFragment(FragmentTransaction transaction) {
        if (homeFragment!=null){
            transaction.hide(homeFragment);
        }
        if (classifyFragment!=null){
            transaction.hide(classifyFragment);
        }
        if (shoppingcartFragment!=null){
            transaction.hide(shoppingcartFragment);
        }
        if (myFragment!=null){
            transaction.hide(myFragment);
        }

    }


    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {

        restartButton();
        switch (v.getId()){
            //返回
            case R.id.leftback:
                finish();
                break;
            /**
             * 点击事件
              */
            case R.id.ll_home:
                iv_home.setImageResource(R.drawable.home_press);
                tv_home.setTextColor(ContextCompat.getColor(mContext, R.color.green_listview));
                mtextView.setText("一米菜园");
                initFragment(0);
                break;
            case R.id.ll_classify:
                iv_classify.setImageResource(R.drawable.classify_press);
                tv_classify.setTextColor(ContextCompat.getColor(mContext, R.color.green_listview));
                mtextView.setText("分类");
                initFragment(1);
                break;
            case R.id.ll_shcart:
                iv_shcart.setImageResource(R.drawable.shoppcar_press);
                tv_shcart.setTextColor(ContextCompat.getColor(mContext, R.color.green_listview));
                mtextView.setText("购物车");
                initFragment(2);
                break;
            case R.id.ll_my:
                iv_my.setImageResource(R.drawable.my_press);
                tv_my.setTextColor(ContextCompat.getColor(mContext, R.color.green_listview));
                mtextView.setText("我的信息");
                initFragment(3);
                break;
            default:
                break;
        }
    }

    /**
     * 初始化底部栏的按钮的颜色
     */
    private void restartButton() {

        //imageview设置为灰色
        iv_home.setImageResource(R.drawable.home);
        iv_classify.setImageResource(R.drawable.classify);
        iv_shcart.setImageResource(R.drawable.shoppcar);
        iv_my.setImageResource(R.drawable.my);

        //字体颜色保持默认就好
        tv_home.setTextColor(ContextCompat.getColor(mContext,R.color.blackmin));
        tv_classify.setTextColor(ContextCompat.getColor(mContext,R.color.blackmin));
        tv_shcart.setTextColor(ContextCompat.getColor(mContext,R.color.blackmin));
        tv_my.setTextColor(ContextCompat.getColor(mContext,R.color.blackmin));
    }
}
