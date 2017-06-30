package com.siyann.taidaapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.p2p.core.P2PHandler;
import com.suke.widget.SwitchButton;

import utils.LogUtil;

/**
 * 报警设置页面
 */
public class AlarmActivity extends Activity implements View.OnClickListener {
    private Context mcontext;
    private TextView mtextView;
    private ImageView mimageView;
    private RelativeLayout alarmemail_rel;  //报警邮箱

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        mcontext=AlarmActivity.this;
        init();
    }
    private void init() {
        Intent intent=getIntent();
        mtextView= (TextView) findViewById(R.id.title_view);
        mtextView.setText(intent.getStringExtra("title"));
        mimageView= (ImageView) findViewById(R.id.back);
        mimageView.setOnClickListener(this);

        alarmemail_rel= (RelativeLayout) findViewById(R.id.alarm_email);
        alarmemail_rel.setOnClickListener(this);

        /**
         * 从缓存中获取设备ID和密码
         */
        SharedPreferences pref=getSharedPreferences("equipment", MODE_PRIVATE);
        final String equipmentid=pref.getInt("equipid",0)+"";
        LogUtil.e("equipid",equipmentid);
         final String equipmentpwd=pref.getString("password","");
        LogUtil.e("password",equipmentpwd);

        /**
         * 设置蜂鸣器
         */
        P2PHandler.getInstance().setBuzzer(equipmentid, equipmentpwd,1);

        /**
         * 接收报警推送
         */
        final com.suke.widget.SwitchButton switchButton = (com.suke.widget.SwitchButton)findViewById(R.id.switch_button);
        switchButton.setChecked(false);
        switchButton.isChecked();
        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(mcontext, "接收报警推送", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mcontext, "关闭报警推送", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /**
         * 移动侦测
         */
        com.suke.widget.SwitchButton removeswitchButton = (com.suke.widget.SwitchButton)findViewById(R.id.removeswitch_button);
        removeswitchButton.setChecked(false);
        removeswitchButton.isChecked();
        /**
         * 设置移动侦测默认是开启状态
         */
        P2PHandler.getInstance().setMotion(equipmentid, equipmentpwd, 0);

        removeswitchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(mcontext,"开启移动侦测",Toast.LENGTH_SHORT).show();
                    P2PHandler.getInstance().setMotion(equipmentid, equipmentpwd, 1);
                }else{
                    P2PHandler.getInstance().setMotion(equipmentid, equipmentpwd, 0);
                    Toast.makeText(mcontext,"关闭移动侦测",Toast.LENGTH_SHORT).show();
                }
            }
        });
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
            case R.id.alarm_email:
                Toast.makeText(mcontext,"邮箱绑定被点击了",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
