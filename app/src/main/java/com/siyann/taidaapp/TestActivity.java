package com.siyann.taidaapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.libhttp.entity.LoginResult;
import com.libhttp.subscribers.SubscriberListener;
import com.p2p.core.P2PHandler;
import com.p2p.core.P2PSpecial.HttpErrorCode;
import com.p2p.core.P2PSpecial.HttpSend;

import utils.MyApplication;
import utils.P2PListener;
import utils.SettingListener;

public class TestActivity extends Activity {
    private Button request;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        request= (Button) findViewById(R.id.btn_request);

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dologin();
            }
        });
    }

    private void dologin() {
        SubscriberListener<LoginResult> subscriberListener = new SubscriberListener<LoginResult>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onNext(LoginResult loginResult) {
                switch (loginResult.getError_code()) {
                    case HttpErrorCode.ERROR_0:
                        //登录成功
                        //成功的逻辑不需要改成下面这样,以下仅演示过程
                        //原有的这部分代码可以不修改
                        //code1与code2是p2p连接的鉴权码,只有在帐号异地登录或者服务器强制刷新(一般不会干这件事)时才会改变
                        //所以可以将code1与code2保存起来,只需在下次登录时刷新即可
                        int code1 = Integer.parseInt(loginResult.getP2PVerifyCode1());
                        int code2 = Integer.parseInt(loginResult.getP2PVerifyCode2());
                        //p2pconnect方法在APP一次生命周期中只需调用一次,退出后台结束时配对调用disconnect一次
                        boolean connect = P2PHandler.getInstance().p2pConnect(loginResult.getUserID(), code1, code2);
                        Log.e("code1", code1 + "");
                        Log.e("code2", code2 + "");
                        Log.e("connect", connect + "");
                        if (connect) {
//                            P2PHandler.getInstance().p2pInit(mContext, new P2PListener(), new SettingListener());

                            P2PHandler.getInstance().p2pInit(MyApplication.app,new P2PListener(),new SettingListener());

                            // Intent callIntent = new Intent(MyApplication.app, EquipmentListActivity.class);

                            Toast.makeText(MyApplication.app, "登录成功", Toast.LENGTH_LONG).show();
                            /**
                             * 获取用户名存缓存，实现一次登录就不需要重复登录的效果
                             */
                            String LoginID = loginResult.getUserID();

                            Log.e("LoginID", LoginID);
                            SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                            editor.putString("LoginID", LoginID);    //拿到用户ID存缓存
                            editor.apply();

//                            startActivity(callIntent);

//                            //跳转之后结束disconnect的调用
//                            P2PHandler.getInstance().p2pDisconnect();
//                            finish();
                        } else {
                            //这里只是demo的写法,可加入自己的逻辑
                            //为false时p2p的功能不可用
                            Toast.makeText(MyApplication.app, "" + connect, Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }

            @Override
            public void onError(String error_code, Throwable throwable) {

            }
        };
        HttpSend.getInstance().login("86-13148700419", "123456", subscriberListener);

    }
}
