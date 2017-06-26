package com.siyann.taidaapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import widget.Equipment;

/**
 * 添加设备界面
 */
public class AddEquipmentActivity extends Activity {


    @Bind(R.id.equipment_name)
    EditText equipmentName;
    @Bind(R.id.equipment_id)
    EditText equipmentId;
    @Bind(R.id.equipment_pwd)
    EditText equipmentPwd;
    @Bind(R.id.add_equipment)
    Button addEquipment;
    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.back)
    ImageView back;
    private Context mContext;


    String id = "";
    String name = "";
    String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_equipment);
        mContext = this;
        ButterKnife.bind(this);

        Intent intent = getIntent();

        titleView.setText(intent.getStringExtra("title"));
        equipmentId.setText(intent.getStringExtra("deviceId"));
    }

    @OnClick({R.id.back, R.id.add_equipment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back: //返回
                finish();
                break;
            case R.id.add_equipment://添加设备
                id = equipmentId.getText().toString();
                name = equipmentName.getText().toString();
                password = equipmentPwd.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(mContext, "设备昵称不能为空", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(id)) {
                    Toast.makeText(mContext, "设备ID不能为空", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(mContext, "密码不能为空", Toast.LENGTH_SHORT).show();
                }
                /**
                 * 拿到设备的昵称,id,密码，存数据库
                 */
                else {
                    int equipid = Integer.parseInt(id);
                    Equipment equipment = new Equipment(name, equipid, password);
                    equipment.save();
                    finish();
                }
                break;
            default:
                break;
        }
    }
}


