package com.siyann.taidaapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adapter.CabbageAdapter;
import widget.Shop;

/**
 * 分类的fragment
 */
public class ClassifyFragment extends Fragment implements View.OnClickListener{
    //五个LinearLayout
    private LinearLayout ll_pag01;
    private LinearLayout ll_pag02;
    private LinearLayout ll_pag03;
    private LinearLayout ll_pag04;
    private LinearLayout ll_pag05;

    //五个textview
    private TextView tv_pag01;
    private TextView tv_pag02;
    private TextView tv_pag03;
    private TextView tv_pag04;
    private TextView tv_pag05;

    RecyclerView recyclerView;
    //蔬菜的adapter
    private CabbageAdapter adapter;

    private List<Shop> shopList=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.page_02,container,false);
        return view;
    }

    /**
     * 初始化UI界面
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ll_pag01= (LinearLayout) getActivity().findViewById(R.id.ll_pag01);
        ll_pag02= (LinearLayout) getActivity().findViewById(R.id.ll_pag02);
        ll_pag03= (LinearLayout) getActivity().findViewById(R.id.ll_pag03);
        ll_pag04= (LinearLayout) getActivity().findViewById(R.id.ll_pag04);
        ll_pag05= (LinearLayout) getActivity().findViewById(R.id.ll_pag05);


        ll_pag01.setOnClickListener(this);
        ll_pag02.setOnClickListener(this);
        ll_pag03.setOnClickListener(this);
        ll_pag04.setOnClickListener(this);
        ll_pag05.setOnClickListener(this);



        tv_pag01= (TextView) getActivity().findViewById(R.id.tv_pag01);
        tv_pag02= (TextView) getActivity().findViewById(R.id.tv_pag02);
        tv_pag03= (TextView) getActivity().findViewById(R.id.tv_pag03);
        tv_pag04= (TextView) getActivity().findViewById(R.id.tv_pag04);
        tv_pag05= (TextView) getActivity().findViewById(R.id.tv_pag05);

        /**
         * 获取cabbageadapter并且初始化数据
          */
        initShop();

        recyclerView= (RecyclerView) getActivity().findViewById(R.id.coment_recycler);
        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);

        adapter=new CabbageAdapter(getActivity(),shopList);
        recyclerView.setAdapter(adapter);
    }


    /**
     * 初始化数据
     */
    private void initShop() {
        Shop cabbage=new
                Shop("https://img11.360buyimg.com/n7/jfs/t4048/252/288662110/127744/931a566e/58462cd6N653980df.jpg",
                "白菜","农家人自钟，环保健康","5.99","320");
        shopList.add(cabbage);

        Shop cabbage1=new
                Shop("https://img13.360buyimg.com/n7/jfs/t4237/72/338884735/326271/fabe0f8d/58b3c666N4d3ff37e.jpg",
                "西红柿","绿鲜知西红柿约1100g，安全放心","14.5","230");
        shopList.add(cabbage1);

        Shop cabbage2=new
                Shop("https://img11.360buyimg.com/n7/jfs/t4378/2/2051979024/264490/a1674250/58ca35deN9d611dae.jpg",
                "水果玉米","农场直供，每日新鲜","22.80","320");
        shopList.add(cabbage2);

        Shop cabbage3=new
                Shop("https://img13.360buyimg.com/n7/jfs/t3196/267/7220503854/427592/e7328e70/58b3c7dbNb15dbd79.jpg",
                "紫薯","农场直供，每日新鲜","15.90","278");
        shopList.add(cabbage3);

        Shop  cabbage4=new
                Shop("https://img13.360buyimg.com/n7/jfs/t3526/310/2014375487/500788/99978c40/583d31d0N4b3c83e0.jpg",
                "西蓝花","农场直供，每日新鲜","8.80","260");
        shopList.add(cabbage4);

        Shop cabbage5=new
                Shop("https://img10.360buyimg.com/n7/jfs/t3460/229/2260622301/57805/cdbdc052/58490b43N01fe973a.jpg",
                "新鲜土豆","农场直供，每日新鲜","11.80","350");
        shopList.add(cabbage5);
    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        restartButton();
        switch (v.getId()){
            case R.id.ll_pag01:
                    ll_pag01.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
                    tv_pag01.setTextColor(ContextCompat.getColor(getActivity(), R.color.blackmin));
                    break;
            case R.id.ll_pag02:
                    ll_pag02.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
                    tv_pag02.setTextColor(ContextCompat.getColor(getActivity(), R.color.blackmin));
                    break;
            case R.id.ll_pag03:
                    ll_pag03.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
                    tv_pag03.setTextColor(ContextCompat.getColor(getActivity(), R.color.blackmin));
                break;
            case R.id.ll_pag04:
                    ll_pag04.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
                    tv_pag04.setTextColor(ContextCompat.getColor(getActivity(), R.color.blackmin));
                break;
            case R.id.ll_pag05:
                    ll_pag05.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
                    tv_pag05.setTextColor(ContextCompat.getColor(getActivity(), R.color.blackmin));
                break;

            default:
                break;
        }
    }


    /**
     * 初始化
     */
    private void restartButton() {

        ll_pag01.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.green_listview));
        tv_pag01.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

        ll_pag02.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.green_listview));
        tv_pag02.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));

        ll_pag03.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.green_listview));
        tv_pag03.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));

        ll_pag04.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.green_listview));
        tv_pag04.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));

        ll_pag05.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.green_listview));
        tv_pag05.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));

    }
}
