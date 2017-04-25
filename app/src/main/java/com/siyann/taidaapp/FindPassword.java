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
import com.libhttp.subscribers.SubscriberListener;
import com.p2p.core.P2PSpecial.HttpErrorCode;
import com.p2p.core.P2PSpecial.HttpSend;

import utils.LogUtil;
import utils.MyApplication;
import widget.GetAccountByPhoneNOResult;

/**
 * 找回密码界面
 */
public class FindPassword extends Activity implements View.OnClickListener {

    private TextView mtitletext;
    private ImageView mImageView;
    private Context mContext;
    private String title = "";
    private Button findpwd_btn;
    private Button getcode;

    private EditText edt_phone; //电话号码
    private EditText edt_code;  //验证码


    private String phone;  //电话号码
    private String code;    //验证码

    SubscriberListener<HttpResult> subscriberListener;  //回调的监听
//    String VKey="d6a0d7a8b38f1371333c88c0377959606541459596276804345";    //鉴权码
    String VKey;
    String ID;      //ID

    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        mContext = FindPassword.this;
        init();
    }

    private void init() {
        /**
         * 从缓存中取出设备的昵称，ID，密码
         */
        sharedPreferences=getSharedPreferences("data", MODE_PRIVATE);
        ID=sharedPreferences.getString("LoginID", "");
        LogUtil.e("ID",ID);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");

        mtitletext = (TextView) findViewById(R.id.title_view);
        mImageView = (ImageView) findViewById(R.id.back);
        mImageView.setOnClickListener(this);
        mtitletext.setText(title);

        getcode = (Button) findViewById(R.id.getcode);
        getcode.setOnClickListener(this);

        findpwd_btn = (Button) findViewById(R.id.findpwd_btn);
        findpwd_btn.setOnClickListener(this);

        edt_phone = (EditText) findViewById(R.id.phone);     //电话号码
        edt_code = (EditText) findViewById(R.id.verificationcode);   //验证码

    }

    /**
     * 点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.getcode:  //获取验证码
                phone = edt_phone.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(mContext, "电话号码不能为空", Toast.LENGTH_SHORT).show();
                }
//                    subscriberListener = new SubscriberListener<HttpResult>() {
//                        @Override
//                        public void onStart() {
//
//                        }
//                        @Override
//                        public void onNext(HttpResult httpResult) {
//                            switch (httpResult.getError_code()) {
//                                case HttpErrorCode.ERROR_0:
//                                    //注册成功
//                                    Toast.makeText(mContext, "验证码发送成功", Toast.LENGTH_LONG).show();
//                                    break;
//                                case HttpErrorCode.ERROR_10901050:
//                                    Toast.makeText(mContext, "APP信息不正确", Toast.LENGTH_LONG).show();
//                                    break;
//                                case HttpErrorCode.ERROR_10902025:
//                                    Toast.makeText(mContext, "获取手机验证码太频繁", Toast.LENGTH_LONG).show();
//                                    break;
//                                case HttpErrorCode.ERROR_10902026:
//                                    Toast.makeText(mContext, "获取手机验证码已到达上限", Toast.LENGTH_LONG).show();
//                                    break;
//                                default:
//                                    //其它错误码需要用户自己实现
//                                    String msg = String.format("注册测试版(%s)", httpResult.getError_code());
//                                    Toast.makeText(MyApplication.app, msg, Toast.LENGTH_LONG).show();
//                                    break;
//                            }
//                        }
//                        @Override
//                        public void onError(String error_code, Throwable throwable) {
//
//                        }
//                    };
//                    HttpSend.getInstance().getAccountByPhoneNO("86", phone, subscriberListener);


                //获取验证码的监听事件
                SubscriberListener<GetAccountByPhoneNOResult> subscriberListener2=new SubscriberListener<GetAccountByPhoneNOResult>() {
                    @Override
                    public void onStart() {
                    }
                    @Override
                    public void onNext(GetAccountByPhoneNOResult getAccountByPhoneNOResult) {
                     switch(getAccountByPhoneNOResult.getError_code()){
                         case HttpErrorCode.ERROR_0:
                             Toast.makeText(mContext, "验证码发送成功", Toast.LENGTH_LONG).show();
                             VKey=getAccountByPhoneNOResult.getVKey();
                             LogUtil.e("VKey",VKey);
                            break;
                         case HttpErrorCode.ERROR_10901050:
                                    Toast.makeText(mContext, "APP信息不正确", Toast.LENGTH_LONG).show();
                                    break;
                                case HttpErrorCode.ERROR_10902025:
                                    Toast.makeText(mContext, "获取手机验证码太频繁", Toast.LENGTH_LONG).show();
                                    break;
                                case HttpErrorCode.ERROR_10902026:
                                    Toast.makeText(mContext, "获取手机验证码已到达上限", Toast.LENGTH_LONG).show();
                                    break;
                                default:
                                    //其它错误码需要用户自己实现
                                    String msg = String.format("注册测试版(%s)", getAccountByPhoneNOResult.getError_code());
                                    Toast.makeText(MyApplication.app, msg, Toast.LENGTH_LONG).show();
                                    break;
                        }
                    }
                    @Override
                    public void onError(String error_code, Throwable throwable) {
                    }
                };

                HttpSend.getInstance().getAccountByPhoneNO("86", phone, subscriberListener2);
                break;

            /**
             *
             * 找回密码先确认验证码
             * 确认验证码成功之后跳转到重置密码界面
             */
            case R.id.findpwd_btn:
                code = edt_code.getText().toString();
                phone = edt_phone.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(mContext, "电话号码不能为空", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(code)){
                    Toast.makeText(mContext, "验证码不能为空", Toast.LENGTH_SHORT).show();
                }else {
                    /**
                     * 确认验证码接口
                     */
                   SubscriberListener<HttpResult> subscriberListener = new SubscriberListener<HttpResult>() {
                        @Override
                        public void onStart() {
                        }

                       @Override
                       public void onNext(HttpResult httpResult) {
                           switch (httpResult.getError_code()) {
                               case HttpErrorCode.ERROR_0:
                                   Intent intent = new Intent(mContext, ChangePassword.class);
                                   intent.putExtra("title", "修改密码");
                                   intent.putExtra("VKey", VKey);
                                   startActivity(intent);
                                   break;
                               case HttpErrorCode.ERROR_10901020:
                                   Toast.makeText(mContext, "缺少输入参数", Toast.LENGTH_SHORT).show();
                                   break;
                               case HttpErrorCode.ERROR_41:
                                   Toast.makeText(mContext, "操作数已达上限", Toast.LENGTH_SHORT).show();
                                   break;
                               case HttpErrorCode.ERROR_10902014:
                                   Toast.makeText(mContext, "重置密码链接无效", Toast.LENGTH_SHORT).show();

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
                    LogUtil.e("ID",ID);
                    LogUtil.e("VKey",VKey);
                    LogUtil.e("phone",phone);
                    LogUtil.e("code",code);
                    HttpSend.getInstance().getCheckPhoneVKey(ID, VKey, "86", phone, code, subscriberListener);
                }
                break;
            default:
                break;
        }
    }


    /**
     * 找回密码先确认验证码
     * 确认验证码成功之后跳转到重置密码界面
     */
//    private void defindpwd() {
//        ID = checkPhoneVKeyResult.getID();
//        LogUtil.e("ID", ID);
//        VKey = checkPhoneVKeyResult.getVKey();
//        LogUtil.e("VKey", VKey);
//        /**
//         * 确认验证码接口
//         */
//        subscriberListener = new SubscriberListener<HttpResult>() {
//            @Override
//            public void onStart() {
//
//            }
//
//            @Override
//            public void onNext(HttpResult httpResult) {
//                switch (httpResult.getError_code()) {
//                    case HttpErrorCode.ERROR_0:
//                        Intent intent = new Intent(mContext, ChangePassword.class);
//                        intent.putExtra("title", "修改密码");
//                        intent.putExtra("VKey", VKey);
//                        startActivity(intent);
//                        break;
//                    case HttpErrorCode.ERROR_10901020:
//
//                        break;
//                    default:
//                        //其它错误码需要用户自己实现
//                        String msg = String.format("注册测试版(%s)", httpResult.getError_code());
//                        Toast.makeText(MyApplication.app, msg, Toast.LENGTH_LONG).show();
//                        break;
//                }
//            }
//
//            @Override
//            public void onError(String error_code, Throwable throwable) {
//
//            }
//        };
//        HttpSend.getInstance().getCheckPhoneVKey(ID, VKey, "86", phone, code, subscriberListener);
//    }

}
