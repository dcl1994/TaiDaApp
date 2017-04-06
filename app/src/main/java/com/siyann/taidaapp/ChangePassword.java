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
import android.widget.TextView;
import android.widget.Toast;

import com.libhttp.entity.HttpResult;
import com.libhttp.http.HttpMethods;
import com.libhttp.subscribers.SubscriberListener;
import com.p2p.core.P2PSpecial.HttpErrorCode;

/**
 * 修改密码界面
 */
public class ChangePassword extends Activity implements View.OnClickListener {
    private Context mContext;
    private ImageView mbackimg;
    private TextView mtitle;
    private String phone;   //电话号码

    private EditText myoldedittext; //旧密码
    private EditText mynewpassword; //新密码
    private EditText myagainpassword; //确认新密码


    private String oldPwd;
    private String newPwd;
    private String againPwd;

    private Button mychangepwd_btn; //修改密码
    private String VKey;    //鉴权码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        mContext = ChangePassword.this;
        init();
    }

    private void init() {
        mychangepwd_btn = (Button) findViewById(R.id.changepwd_btn);
        mychangepwd_btn.setOnClickListener(this);

        myoldedittext = (EditText) findViewById(R.id.oldpassword);
        mynewpassword = (EditText) findViewById(R.id.newpassword);
        myagainpassword = (EditText) findViewById(R.id.againpassword);


        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        VKey = intent.getStringExtra("VKey");             //获取鉴权码

        mbackimg = (ImageView) findViewById(R.id.back);
        mbackimg.setOnClickListener(this);
        //设置标题
        mtitle = (TextView) findViewById(R.id.title_view);
        mtitle.setText(title);
    }


    private boolean checkData() {
        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        phone = sharedPreferences.getString("phone", "");  //获取电话号码

        oldPwd = myoldedittext.getText().toString();
        newPwd = mynewpassword.getText().toString();
        againPwd = myagainpassword.getText().toString();
        if (TextUtils.isEmpty(oldPwd)) {
            Toast.makeText(mContext, "旧密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(newPwd)) {
            Toast.makeText(mContext, "新密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(againPwd)) {
            Toast.makeText(mContext, "确认新密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回的点击事件
            case R.id.back:
                finish();
                break;
            case R.id.changepwd_btn:
                dochangepwd();
                break;
            default:
                break;
        }
    }


    /**
     * 重置密码的方法
     */
    private void dochangepwd() {
        if (checkData()) {
            SubscriberListener<HttpResult> subscriberListener = new SubscriberListener<HttpResult>() {
                @Override
                public void onStart() {

                }

                @Override
                public void onNext(HttpResult httpResult) {
                    switch (httpResult.getError_code()) {
                        case HttpErrorCode.ERROR_0:
                            Toast.makeText(mContext, "修改密码成功", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(mContext, LoginActivity.class);
                            startActivity(intent);
                            finish();
                            break;
                    }
                }
                @Override
                public void onError(String error_code, Throwable throwable) {

                }
            };
            HttpMethods.getInstance().resetPwd(phone, VKey, newPwd, againPwd, subscriberListener);
        }
    }
}
