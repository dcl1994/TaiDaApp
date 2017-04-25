package com.siyann.taidaapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adapter.SettingAdapter;
import widget.Setting;

/**
 * 设备的设置页面
 */
public class SettingActivity extends Activity  implements View.OnClickListener{
    private Context mContext;
    private TextView mtextView;
    private ImageView mimageView;

    private List<Setting> settingList=new ArrayList<>();
    private SettingAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mContext=SettingActivity.this;
        init();

        RecyclerView recyclerView= (RecyclerView) findViewById(R.id.setting_recycler);
        LinearLayoutManager manager=new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(manager);
        adapter=new SettingAdapter(settingList);
        recyclerView.setAdapter(adapter);
    }
    /**
     * 初始化UI
     */
    private void init() {
        //设置标题
        Intent intent=getIntent();
        mtextView= (TextView) findViewById(R.id.title_view);
        mtextView.setText(intent.getStringExtra("title"));

        mimageView= (ImageView) findViewById(R.id.back);
        mimageView.setOnClickListener(this);

        /**
         * 初始化setting中的数据
         */

        Setting equipmentname=new Setting("设备昵称",R.drawable.equipment_info);

        settingList.add(equipmentname);

        Setting networking=new Setting("网络设置",R.drawable.networksetting);
        settingList.add(networking);

        Setting alarmsetting=new Setting("报警设置",R.drawable.alarmsetting);
        settingList.add(alarmsetting);

        Setting recordsetting=new Setting("录像设置",R.drawable.recordsetting);
        settingList.add(recordsetting);

        Setting sensors=new Setting("添加传感器",R.drawable.addsensors);
        settingList.add(sensors);
    }
    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
        }
    }
}
