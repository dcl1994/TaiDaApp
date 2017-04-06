package com.siyann.taidaapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import adapter.AddEquipmentAdapter;
import utils.LogUtil;
import widget.Equipment;
import widget.PopupMenu;

/**
 * 设备列表界面
 */
public class EquipmentActivity extends AppCompatActivity{
    private Context mContext;
    private ImageView mImageView;   //返回按钮
    private LinearLayout toopbar_layout;    //添加设备
    private PopupMenu mPopupMenu;
    private List<Equipment> equipmentList=new ArrayList<>();

    private String returnedData="";

    private String equipmentname="";    //设备昵称
    private String equipmentid="";      //设备ID
    private String equipmentpwd="";         //设备密码


    private  RecyclerView recyclerView;
    private AddEquipmentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment);
        mContext = EquipmentActivity.this;
        /**
         * 设置数据
         */
        recyclerView= (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        mImageView = (ImageView) findViewById(R.id.back);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /**
         * 显示toolbar
         */
        toopbar_layout= (LinearLayout) findViewById(R.id.toolbar_menu);
        toopbar_layout.setVisibility(View.VISIBLE);

        //初始化 PopupMenu并设置它的宽度
        View menuLayout = getLayoutInflater().inflate(R.layout.toolbar_menu, null);
        menuLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        mPopupMenu = new PopupMenu((ViewGroup)menuLayout);
        mPopupMenu.setMenuItemBackgroundColor(0xffb1df83);
        mPopupMenu.setMenuItemHoverBackgroundColor(0x22000000);

        /**
         * 子菜单的点击事件
         */
        mPopupMenu.setOnMenuItemSelectedListener(new PopupMenu.OnMenuItemSelectedListener() {
            @Override
            public void onMenuItemSelected(View menuItem) {
                switch (menuItem.getId()) {
                    case R.id.menu01:
                        Intent intent=new Intent(mContext,AddEquipmentActivity.class);
                        startActivityForResult(intent,1);
                        break;
//                    case R.id.menu02:
//                        Toast.makeText(mContext, "智能联机", Toast.LENGTH_SHORT).show();
//                        break;
                }
            }
        });
        // 显示menu并添加点击事件
        final float offsetX = 0;
        final float offsetY = 0;
        final float menuWidth = menuLayout.getMeasuredWidth();
        final View menu = findViewById(R.id.toolbar_menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupMenu.isShowing()) {
                    mPopupMenu.dismiss();
                } else {
                    // based on bottom-left, need take menu width and menu icon width into account
                    mPopupMenu.show(menu, (int) (menu.getWidth() - offsetX - menuWidth), (int) offsetY);
                }
            }
        });
    }

    /**
     * 重写onActivityResult来获得返回的结果值
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode==RESULT_OK){
                    equipmentname=data.getStringExtra("equipmentname");
                    LogUtil.e("设备昵称", equipmentname);

                    equipmentid=data.getStringExtra("equipmentid");
                    LogUtil.e("设备ID", equipmentid);

                    equipmentpwd=data.getStringExtra("equipmentpwd");
                    LogUtil.e("设备密码", equipmentpwd);

                    /**
                     * 将返回值存到缓存中
                     */
                    SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();
                    editor.putString("equipmentname",equipmentname);
                    editor.putString("equipmentid",equipmentid);
                    editor.putString("equipmentpwd",equipmentpwd);
                    editor.apply();
                    if (!TextUtils.isEmpty(equipmentname)){
                        Equipment equipment=new Equipment(equipmentname,R.drawable.header_icon);
                        equipmentList.add(equipment);
                        adapter=new AddEquipmentAdapter(equipmentList);
                        recyclerView.setAdapter(adapter);
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 一进入页面就从缓存中读数据，读到了数据就显示adapter
     */
    @Override
    protected void onStart() {
        SharedPreferences pref=getSharedPreferences("data",MODE_PRIVATE);
        String equipment=pref.getString("equipmentname","");
        if (!TextUtils.isEmpty(equipment)){
            Equipment equipment1=new Equipment(equipment,R.drawable.header_icon);
            equipmentList.add(equipment1);

            AddEquipmentAdapter adapter=new AddEquipmentAdapter(equipmentList);
            recyclerView.setAdapter(adapter);
        }
        super.onStart();
    }
}
