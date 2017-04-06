package com.siyann.taidaapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.libhttp.entity.HttpResult;
import com.libhttp.http.HttpMethods;
import com.libhttp.subscribers.SubscriberListener;
import com.p2p.core.P2PSpecial.HttpErrorCode;
import com.p2p.core.P2PSpecial.HttpSend;
import com.p2p.core.P2PSpecial.WebConfig;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import utils.LogUtil;
import utils.MyApplication;


/**
 * 绑定微信登录
 */
public class BoundWeixinActivity extends Activity implements View.OnClickListener {
    private Context mContext;

    private EditText username_edit;

    private EditText password_edit;

    private Button mybutton;

    private TextView mytextview;

    private TextInputLayout username_input;

    private TextInputLayout password_input;

    private String username;    //用户名
    private String password;    //密码
    private String unionid;     //unionid

    SubscriberListener<HttpResult> subscriberListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bound_weixin);
        mContext = BoundWeixinActivity.this;
        Intent intent=getIntent();
        unionid =intent.getStringExtra("unionid");
        LogUtil.e("bound_unionid", unionid);
        init();
    }

    private void init() {
        username_input = (TextInputLayout) findViewById(R.id.usernameWrapper);
        password_input = (TextInputLayout) findViewById(R.id.passwordWrapper);
        username_edit = (EditText) findViewById(R.id.username);
        password_edit = (EditText) findViewById(R.id.password_edit);

        mybutton = (Button) findViewById(R.id.bound_btn);
        mybutton.setOnClickListener(this);

        mytextview = (TextView) findViewById(R.id.newuser_textview);
        mytextview.setOnClickListener(this);
    }

    private boolean checkData(){
        username =username_input.getEditText().getText().toString();   //获取Edittext中的username
        LogUtil.e("username",username);

        password = password_input.getEditText().getText().toString();    //获取Edittext中的password
        LogUtil.e("password",password);


        if (TextUtils.isEmpty(username)){
            Toast.makeText(mContext,"用户名不能为空",Toast.LENGTH_SHORT).show();
            return  false;
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(mContext,"密码不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
     return true;
    }

    /**
     * 绑定的点击事件，输入用户名和密码之后绑定
     * 我是新用户则直接登录
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bound_btn:
                bound();
                break;
            case R.id.newuser_textview:
                newuser();
                break;
        }
    }

    /**
     * 绑定用户信息的监听
     */
    private void bound() {
        if (checkData()) {
            subscriberListener = new SubscriberListener<HttpResult>() {
                @Override
                public void onStart() {

                }
                @Override
                public void onNext(HttpResult httpResult) {
                    switch (httpResult.getError_code()) {
                        case HttpErrorCode.ERROR_0:
                            Intent intent = new Intent(mContext, MainActivity.class);
                            startActivity(intent);
                            break;
                        case HttpErrorCode.ERROR_10901020:
                            Toast.makeText(mContext,"缺少输入参数",Toast.LENGTH_SHORT).show();
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
            LogUtil.e("unionid",unionid);
            LogUtil.e("username",username);
            LogUtil.e("password",password);

            String ThirdType= WebConfig.Third_Option.ThirdLogin_Option_2;
            try {
                HttpSend.getInstance().ThirdLogin("2",unionid,username,password,"",ThirdType,subscriberListener);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 我是新用户的监听
     */
    private void newuser() {
        subscriberListener = new SubscriberListener<HttpResult>() {
            @Override
            public void onStart() {

            }
            @Override
            public void onNext(HttpResult httpResult) {
                switch (httpResult.getError_code()) {
                    case HttpErrorCode.ERROR_0:
                        Intent intent = new Intent(mContext, MainActivity.class);
                        startActivity(intent);
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
        try {
            String ThirdType = WebConfig.Third_Option.ThirdLogin_Option_3;
            HttpMethods.getInstance().ThirdLogin("3", unionid, "", password, "", ThirdType, subscriberListener);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

}
