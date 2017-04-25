package com.siyann.taidaapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

/**
 * 添加设备界面
 */
public class AddEquipmentActivity extends Activity implements View.OnClickListener {

    private Context mContext;

    private ImageView mImageView;   //返回按钮

    private Button mbutton;       //添加设备

    private EditText mequipment_name;   //设备昵称

    private EditText mequipment_id;    //设备ID

    private EditText mequipment_pwd;    //设备连接密码

    private String equipmentname;   //设备昵称

    private String equipmentid;     //设备ID

    private String equipmentpwd;   //连接密码

    List<String> equipment_strlist; //  存放设备的list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_equipment);
        mContext = AddEquipmentActivity.this;
        init();
    }

    private void init() {
        mequipment_name = (EditText) findViewById(R.id.equipment_name);

        mequipment_id = (EditText) findViewById(R.id.equipment_id);

        mequipment_pwd = (EditText) findViewById(R.id.equipment_pwd);

        mImageView = (ImageView) findViewById(R.id.back);

        mImageView.setOnClickListener(this);

        mbutton = (Button) findViewById(R.id.add_equp);

        mbutton.setOnClickListener(this);

        Intent intent = getIntent();
        mequipment_id.setText(intent.getStringExtra("deviceId"));

    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        equipmentname = mequipment_name.getText().toString(); //拿到用户输入的昵称
        equipmentid = mequipment_id.getText().toString();     //拿到设备id
        equipmentpwd = mequipment_pwd.getText().toString();   //拿到设备密码
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            /**
             * 点击添加设备按钮将设备昵称显示在设备列表界面上
             */
            case R.id.add_equp:
                if (TextUtils.isEmpty(equipmentname)) {
                    Toast.makeText(mContext, "设备昵称不能为空", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(equipmentid)) {
                    Toast.makeText(mContext, "设备id不能为空", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(equipmentpwd)) {
                    Toast.makeText(mContext, "设备连接密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    /**
                     * 存缓存
                     */
                    SharedPreferences.Editor editor=getSharedPreferences("data", MODE_PRIVATE).edit();
                    editor.putString("equipmentname", equipmentname);
                    editor.putString("equipmentid", equipmentid);
                    editor.putString("equipmentpwd", equipmentpwd);
                    editor.apply();

                    Intent intent=new Intent(mContext,EquipmentActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
            default:
                break;
        }
    }
}


