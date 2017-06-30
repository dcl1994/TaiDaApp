package com.siyann.taidaapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.p2p.core.P2PHandler;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import utils.LogUtil;
import utils.SettingListener;

/**
 * 添加传感器界面
 */
public class AddSensorActivity extends Activity implements View.OnClickListener{
    private Context mContext;
    private TextView mtextView;     //标题
    private ImageView mimageView;   //返回键

    private ImageView addsensor_img;
    private ImageView remotecontrol_img;
    private ImageView specialsensor_img;
    SweetAlertDialog pdialog;

    private String equipmentpwd="";
    private String equipmentid="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sensor);
        mContext = this;
        init();
    }
    /**
     * UI界面
     */
    private void init() {
        Intent intent=getIntent();
        mtextView = (TextView) findViewById(R.id.title_view);
        mtextView.setText(intent.getStringExtra("title"));

        mimageView = (ImageView) findViewById(R.id.back);
        mimageView.setOnClickListener(this);

        addsensor_img= (ImageView) findViewById(R.id.addsensor);
        addsensor_img.setOnClickListener(this);

        remotecontrol_img= (ImageView) findViewById(R.id.remotecontrol);
        remotecontrol_img.setOnClickListener(this);

        specialsensor_img= (ImageView) findViewById(R.id.specialsensor);
        specialsensor_img.setOnClickListener(this);
    }


    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        SharedPreferences pref=getSharedPreferences("equipment", MODE_PRIVATE);
        equipmentid= pref.getInt("equipid",0)+"";
        equipmentpwd=pref.getString("password","");

        LogUtil.e("equipmentpwd",equipmentpwd);
        LogUtil.e("equipmentid", equipmentid);
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.addsensor:    //传感器
                showdialogsensor();
                break;
            case R.id.remotecontrol: //遥控
                /**设置防区状态*/
                showdialogremotecontrol();
                break;
            case R.id.specialsensor: //特殊传感器
                showdialogspecial();
                break;
            default:
                break;
        }
    }
    /**
     * 遥控器的dialog
     */
    private void showdialogremotecontrol() {
        pdialog = new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE);
        pdialog.setContentText("点击确定，触发将要添加的遥控器")
                .setTitleText("")
                .setConfirmText("确定")
                .setCancelable(false);
        pdialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        //连接传感器
                        P2PHandler.getInstance().setDefenceAreaState(equipmentid, equipmentpwd, 0, 0, 0);
                        P2PHandler.getInstance().setDefenceAreaState(equipmentid, equipmentpwd, 0, 1, 0);
                        P2PHandler.getInstance().setDefenceAreaState(equipmentid, equipmentpwd, 0, 2, 0);
                        P2PHandler.getInstance().setDefenceAreaState(equipmentid, equipmentpwd, 0, 3, 0);
                        P2PHandler.getInstance().setDefenceAreaState(equipmentid, equipmentpwd, 0, 4, 0);
                        P2PHandler.getInstance().setDefenceAreaState(equipmentid, equipmentpwd, 0, 5, 0);
                        sDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        sDialog.setContentText("Loading...")
                                .setTitleText("")
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.PROGRESS_TYPE);


                        SettingListener listener=new SettingListener();
                        listener.vRetDefenceAreaResult(0,new ArrayList<int[]>(0),0,0);
                    }


                }).show();
    }

    /**
     * 普通传感器的dialog
     */
    private void showdialogsensor() {
        pdialog = new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE);
        pdialog.setContentText("点击确定，触发将要添加的传感器")
                .setTitleText("")
                .setConfirmText("确定")
                .setCancelable(false);
        pdialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        //连接传感器
                        P2PHandler.getInstance().setDefenceAreaState(equipmentid, equipmentpwd, 2, 0, 0);
                        P2PHandler.getInstance().setDefenceAreaState(equipmentid, equipmentpwd, 3, 1, 0);
                        P2PHandler.getInstance().setDefenceAreaState(equipmentid, equipmentpwd, 4, 2, 0);
                        P2PHandler.getInstance().setDefenceAreaState(equipmentid, equipmentpwd, 5, 3, 0);
                        P2PHandler.getInstance().setDefenceAreaState(equipmentid, equipmentpwd, 6, 4, 0);
                        P2PHandler.getInstance().setDefenceAreaState(equipmentid, equipmentpwd, 7, 0, 0);
                        P2PHandler.getInstance().setDefenceAreaState(equipmentid, equipmentpwd, 8, 1, 0);
                        sDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        sDialog.setContentText("Loading...")
                                .setTitleText("")
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
                    }
                }).show();
    }


    /**
     * 特殊传感器的dialog
     */
    private void showdialogspecial() {
        pdialog = new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE);
        pdialog.setContentText("点击确定，触发将要添加的传感器")
                .setTitleText("")
                .setConfirmText("确定")
                .setCancelable(false);
        pdialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        //连接传感器
                        P2PHandler.getInstance().setDefenceAreaState(equipmentid, equipmentpwd, 8, 1, 0);
                        P2PHandler.getInstance().setDefenceAreaState(equipmentid, equipmentpwd, 8, 2, 0);
                        P2PHandler.getInstance().setDefenceAreaState(equipmentid, equipmentpwd, 8, 3, 0);
                        P2PHandler.getInstance().setDefenceAreaState(equipmentid, equipmentpwd, 8, 4, 0);

                        sDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        sDialog.setContentText("Loading...")
                                .setTitleText("")
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
                    }
                }).show();
    }


}
