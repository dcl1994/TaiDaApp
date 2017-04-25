package com.siyann.taidaapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * 我的信息fragment
 */
public class MyFragment extends Fragment implements View.OnClickListener{
    private LinearLayout ll_username;
    private LinearLayout ll_phone;
    private LinearLayout ll_address;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View view=inflater.inflate(R.layout.page_04,container,false);
        return view;
    }

    /**
     * 初始化UI界面
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ll_username= (LinearLayout) getActivity().findViewById(R.id.ll_username);
        ll_phone= (LinearLayout) getActivity().findViewById(R.id.ll_phone);
        ll_address= (LinearLayout) getActivity().findViewById(R.id.ll_address);

        ll_username.setOnClickListener(this);
        ll_phone.setOnClickListener(this);
        ll_address.setOnClickListener(this);
    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_username:
                Intent intent=new Intent(getActivity(),AddUsernameActivity.class);
                intent.putExtra("title", "名字");
                startActivity(intent);
                break;
            case R.id.ll_phone:
                Intent intent1=new Intent(getActivity(),AddPhoneActivity.class);
                intent1.putExtra("title", "电话号码");
                startActivity(intent1);
                break;

            case R.id.ll_address:
                Intent intent2=new Intent(getActivity(),AddAddressActivity.class);
                intent2.putExtra("title", "收货地址");
                startActivity(intent2);
                break;
            default:
                break;
        }

    }
}
