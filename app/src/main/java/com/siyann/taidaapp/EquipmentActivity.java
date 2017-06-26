package com.siyann.taidaapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.p2p.core.P2PHandler;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import adapter.EquipmentAdapter;
import widget.Equipment;
import widget.PopupMenu;

/**
 * 设备列表界面
 */
public class EquipmentActivity extends AppCompatActivity {
    private Context mContext;
    private ImageView mImageView;   //返回按钮
    private LinearLayout toopbar_layout;    //添加设备
    private PopupMenu mPopupMenu;
    private List<Equipment> equipmentList = new ArrayList<>();

    private String returnedData = "";

    private String equipmentname = "";    //设备昵称
    private String equipmentid = "";      //设备ID
    private String equipmentpwd = "";         //设备密码


    private TextView mtextView; //提示信息

    private RecyclerView recyclerView;
    private EquipmentAdapter adapter;

    private LinearLayout lineequipmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment);
        mContext =this;

        mtextView = (TextView) findViewById(R.id.hint_view);

        lineequipmentList= (LinearLayout) findViewById(R.id.equipment_list);
        /**
         * 设置数据
         */
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        /**
         *返回上一个页面
         */
        mImageView = (ImageView) findViewById(R.id.back);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 结束时关闭
                 */
                P2PHandler.getInstance().p2pDisconnect();
                Intent intent=new Intent(mContext,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        /**
         * 显示toolbar
         */
        toopbar_layout = (LinearLayout) findViewById(R.id.toolbar_menu);
        toopbar_layout.setVisibility(View.VISIBLE);

        //初始化 PopupMenu并设置它的宽度
        View menuLayout = getLayoutInflater().inflate(R.layout.toolbar_menu, null);
        menuLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        mPopupMenu = new PopupMenu((ViewGroup) menuLayout);
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
                        Intent intent = new Intent(mContext, AddEquipmentActivity.class);
                        startActivityForResult(intent, 1);
                        break;

                    case R.id.menu02:
                        Intent intent1 = new Intent(mContext, SmartLink_PlanActivity.class);
                        intent1.putExtra("title", "智能联机提示");
                        startActivity(intent1);
                        break;
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
     * 重写返回键
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
            /**
             * 结束时关闭
             */
            P2PHandler.getInstance().p2pDisconnect();
            Intent intent=new Intent(mContext,MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 一进来就查询数据库
     */
    @Override
    protected void onStart() {
        super.onStart();
        List<Equipment> equipments = DataSupport.findAll(Equipment.class);

        equipmentList = equipments;


        adapter = new EquipmentAdapter(mContext, equipmentList);
        /**
         * 如果没有设备，显示没有设备页面
         */
        if (equipmentList == null || equipmentList.size() == 0) {
            lineequipmentList.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setAdapter(adapter);
            lineequipmentList.setVisibility(View.GONE);
        }
    }
}
