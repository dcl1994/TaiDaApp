package com.siyann.taidaapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.libhttp.entity.HttpResult;
import com.libhttp.subscribers.SubscriberListener;
import com.p2p.core.P2PSpecial.HttpErrorCode;
import com.p2p.core.P2PSpecial.HttpSend;

import utils.LogUtil;
import utils.MyApplication;

/**
 * 注册的activity
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private Context mContext;

    private ProgressBar registerprogress;

    private EditText phone;

    private EditText code_deittext;

    private EditText password_deittext;

    private Button registerbtn;

    private Button getcode; //获取验证码


    private String phonenumber="";  //电话
    private String code="";         //验证码
    private String password="";     //密码

    SubscriberListener<HttpResult> subscriberListener;  //回调监听

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContext=RegisterActivity.this;
        init();
    }

    /**
     * 初始化UI界面
     */
    private void init() {

        getcode= (Button) findViewById(R.id.getcode);
        getcode.setOnClickListener(this);

        registerprogress= (ProgressBar) findViewById(R.id.register_progress);

        code_deittext= (EditText) findViewById(R.id.code);  //验证码edittext

        phone= (EditText) findViewById(R.id.phone_edit);

        password_deittext= (EditText) findViewById(R.id.password_edit);

        registerbtn= (Button) findViewById(R.id.register_btn);
        registerbtn.setOnClickListener(this);
    }

    /**
     * 进行判断的方法
     * @return
     */
    private boolean checkData() {
        phonenumber = phone.getText().toString();
        LogUtil.e("电话号码", phonenumber);

        code = code_deittext.getText().toString();      //获取验证码
        LogUtil.e("验证码", code);

        password= password_deittext.getText().toString();    //获取密码
        LogUtil.e("密码", password);

        if (TextUtils.isEmpty(phonenumber)) {
            Toast.makeText(mContext, "请输入正确的电话号码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(mContext, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /**
             * 获取验证码
             */
            case R.id.getcode:
                if (checkData()){
                     subscriberListener = new SubscriberListener<HttpResult>() {
                        @Override
                        public void onStart() {
                        }
                        @Override
                        public void onNext(HttpResult registerResult) {
                            switch (registerResult.getError_code()) {
                                case HttpErrorCode.ERROR_0:
                                    Toast.makeText(RegisterActivity.this, "验证码发送成功", Toast.LENGTH_LONG).show();
                                    break;
                                case HttpErrorCode.ERROR_10901050:
                                    Toast.makeText(mContext, "APP信息不正确", Toast.LENGTH_LONG).show();
                                    break;
                                case HttpErrorCode.ERROR_10902020:
                                    Toast.makeText(mContext,"用户已注册",Toast.LENGTH_SHORT).show();
                                    break;
                                case HttpErrorCode.ERROR_10902025:
                                    Toast.makeText(mContext,"获取手机验证码太频繁",Toast.LENGTH_SHORT).show();
                                    break;
                                case HttpErrorCode.ERROR_10902009:
                                    Toast.makeText(mContext,"手机验证码不正确",Toast.LENGTH_SHORT).show();
                                    break;
                                case HttpErrorCode.ERROR_10902010:
                                    Toast.makeText(mContext,"获取手机验证码超时",Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    //其它错误码需要用户自己实现
                                    String msg = String.format("注册测试版(%s)", registerResult.getError_code());
                                    Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                        @Override
                        public void onError(String error_code, Throwable throwable) {
                            Toast.makeText(mContext,error_code,Toast.LENGTH_SHORT).show();
                        }
                    };
                    HttpSend.getInstance().getPhoneCode("86",phonenumber,subscriberListener);
                }
                break;

            /**
             * 注册方法的调用
             */
            case R.id.register_btn:
                if(checkData()){
                    /**
                     * 验证手机验证码
                     */
                  subscriberListener=new SubscriberListener<HttpResult>() {
                      @Override
                      public void onStart() {
                      }
                      @Override
                      public void onNext(HttpResult httpResult) {
                        switch (httpResult.getError_code()){
                            case HttpErrorCode.ERROR_0:
                                break;
                            case HttpErrorCode.ERROR_10902009:
                                Toast.makeText(mContext,"手机验证码不正确",Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                //其它错误码需要用户自己实现
                                String msg = String.format("注册测试版(%s)", httpResult.getError_code());
                                Toast.makeText(MyApplication.app, msg, Toast.LENGTH_LONG).show();
                                break;
                        }
                      }
                      @Override
                      public void onError(String error_code, Throwable throwable) {
                      }
                  };
                    HttpSend.getInstance().verifyPhoneCode("86",phonenumber,code,subscriberListener);


                    /**
                     * 注册账号
                     */
                subscriberListener=new SubscriberListener<HttpResult>() {
                    @Override
                    public void onStart() {
                    }
                    @Override
                    public void onNext(HttpResult httpResult) {
                        switch (httpResult.getError_code()){
                            case HttpErrorCode.ERROR_0:
                                Toast.makeText(mContext,"注册成功",Toast.LENGTH_SHORT).show();
                                Intent intent = getIntent();
                                intent.putExtra("phonenumber",phonenumber);
                                intent.putExtra("password",password);
                                setResult(2, intent);
                                finish();
                                break;
                            case HttpErrorCode.ERROR_10902020:
                                Toast.makeText(mContext,"手机号已被使用",Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                //其它错误码需要用户自己实现
                                String msg = String.format("注册测试版(%s)", httpResult.getError_code());
                                Toast.makeText(MyApplication.app, msg, Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                    @Override
                    public void onError(String error_code, Throwable throwable) {

                    }
                };
                    HttpSend.getInstance().register("1","","86",phonenumber,password,password,code,"1",subscriberListener);
                }
                break;
        }
    }
}
