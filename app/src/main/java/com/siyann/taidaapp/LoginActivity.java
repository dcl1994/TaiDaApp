package com.siyann.taidaapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.libhttp.entity.LoginResult;
import com.libhttp.subscribers.SubscriberListener;
import com.p2p.core.P2PHandler;
import com.p2p.core.P2PSpecial.HttpErrorCode;
import com.p2p.core.P2PSpecial.HttpSend;
import com.siyann.taidaapp.wxapi.WXEntryActivity;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import utils.LogUtil;
import utils.MyApplication;
import utils.P2PListener;
import utils.SettingListener;

/**
 * 泰达的登录界面
 * 实现一次登录就不用再登录了
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private TextInputLayout username_input;
    private TextInputLayout password_input;
    private Button login_btn;   //登录
    private Button wxlogin_btn; //微信登录
    private TextView myTextView;//忘记密码
    private String username;
    private String password;
    private TextView myregisterView;

//    private String UnionID;     //微信登录的唯一ID
//    private String nickname;    //用户名
//    private String headimgurl;  //头像地址
//    private int sex;         //性别



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = LoginActivity.this;
        init();
    }
    /**
     * 初始化控件
     */
    private void init() {
        myregisterView= (TextView) findViewById(R.id.register);
        myregisterView.setOnClickListener(this);
        username_input = (TextInputLayout) findViewById(R.id.usernameWrapper);
        password_input = (TextInputLayout) findViewById(R.id.passwordWrapper);
        login_btn = (Button) findViewById(R.id.login);
        login_btn.setOnClickListener(this);
        wxlogin_btn = (Button) findViewById(R.id.wx_login);
        wxlogin_btn.setOnClickListener(this);
        myTextView = (TextView) findViewById(R.id.forgetpwd);
        myTextView.setOnClickListener(this);
    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                hideKeyboard(); //隐藏键盘
                username =username_input.getEditText().getText().toString();   //获取Edittext中的username

                password = password_input.getEditText().getText().toString();    //获取Edittext中的password
                //  TextInputLayout的错误处理简单快速。需要的方法是setErrorEnabled和setError。
                if (TextUtils.isEmpty(username)) {
                    username_input.setError("手机号不能为空");
                }
                else if (!validatePassword(password)) {
                    password_input.setError("密码长度不能少于6位");
                } else {
                    username_input.setErrorEnabled(false);
                    password_input.setErrorEnabled(false);
                    dologin();  //登录的方法
                }
//                dologin();
                break;

            /**
             * 微信登录
             */
            case R.id.wx_login:
                IWXAPI mApi = WXAPIFactory.createWXAPI(this, WXEntryActivity.WEIXIN_APP_ID, true);
                mApi.registerApp(WXEntryActivity.WEIXIN_APP_ID);
                if (mApi != null && mApi.isWXAppInstalled()) {
                    SendAuth.Req req = new SendAuth.Req();
                    req.scope = "snsapi_userinfo";
                    req.state = "wechat_sdk_demo_test_neng";
                    mApi.sendReq(req);
                } else {
                    Toast.makeText(this, "用户未安装微信", Toast.LENGTH_SHORT).show();
                }

//                /**
//                 * 获取微信用户信息
//                 */
//                SharedPreferences pref=getSharedPreferences("data",MODE_PRIVATE);
//                UnionID=pref.getString("unionid","");
//                nickname=pref.getString("nickname","");
//                headimgurl=pref.getString("headimgurl","");
//                sex=pref.getInt("sex", sex);
//                /**
//                 * 调用yoosee微信登录的方法
//                 */
//                dowxlogin();
                break;

            /**
             * 忘记密码
             */
            case R.id.forgetpwd:
                Intent intent=new Intent(mContext,FindPassword.class);
                intent.putExtra("title","找回密码");
                startActivity(intent);
                break;
            /**
             *注册账号
             */
            case R.id.register:
                Intent intentReg=new Intent(mContext,RegisterActivity.class);
                startActivityForResult(intentReg,1);
                break;
        }
    }
    /**
     * 密码的判断
     *
     * @param password
     * @return
     */
    private boolean validatePassword(String password) {
        return password.length() > 5;
    }
    /**
     * 登录的方法
     */
    private void dologin() {
        SubscriberListener<LoginResult> subscriberListener=new SubscriberListener<LoginResult>() {
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
                        LogUtil.e("code1", code1 + "");
                        LogUtil.e("code2",code2+"");
                        LogUtil.e("connect",connect+"");
                        if (connect) {
                            P2PHandler.getInstance().p2pInit(mContext, new P2PListener(), new SettingListener());
                            Intent callIntent = new Intent(MyApplication.app, MainActivity.class);

                            Toast.makeText(mContext, "登录成功", Toast.LENGTH_LONG).show();
                            /**
                             * 获取用户名存缓存，实现一次登录就不需要重复登录的效果
                             */
                            String LoginID=loginResult.getUserID();

                            LogUtil.e("LoginID", LoginID);
                            SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();
                            editor.putString("username", username); //用户名
                            editor.putString("password",password);  //密码
                            editor.putString("LoginID", LoginID);    //拿到用户ID存缓存
                            editor.apply();

                            startActivity(callIntent);

                            //跳转之后结束disconnect的调用
                            P2PHandler.getInstance().p2pDisconnect();
                            finish();
                        } else {
                            //这里只是demo的写法,可加入自己的逻辑
                            //为false时p2p的功能不可用
                            Toast.makeText(MyApplication.app,""+connect,Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case HttpErrorCode.ERROR_10901050:
                        Toast.makeText(mContext, "APP信息不正确", Toast.LENGTH_LONG).show();
                        break;
                    case HttpErrorCode.ERROR_10902011:
                        Toast.makeText(mContext, "用户不存在", Toast.LENGTH_LONG).show();
                        break;
                    case HttpErrorCode.ERROR_10902003:
                        Toast.makeText(mContext, "密码错误", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        //其它错误码需要用户自己实现
                        String msg = String.format("注册测试版(%s)", loginResult.getError_code());
                        Toast.makeText(MyApplication.app, msg, Toast.LENGTH_LONG).show();
                        break;
                }
            }
            @Override
            public void onError(String error_code, Throwable throwable) {
                Toast.makeText(MyApplication.app, "onError:" + error_code, Toast.LENGTH_LONG).show();
            }
        };
        HttpSend.getInstance().login("86-" + username, password, subscriberListener);




//        /**
//         * 使用第三方登录的方式登录，这是另外一种写法
//         */
//        try {
//            HttpSend.getInstance().ThirdLogin("1","123456789","","","","3", new SubscriberListener<ThirdPartyLoginResult>() {
//                @Override
//                public void onStart() {
//
//                }
//                @Override
//                public void onNext(ThirdPartyLoginResult thirdPartyLoginResult) {
//                    switch (thirdPartyLoginResult.getError_code()) {
//                        case HttpErrorCode.ERROR_0:
//                            //登录成功
//                        //成功的逻辑不需要改成下面这样,以下仅演示过程
//                        //原有的这部分代码可以不修改
//                        //code1与code2是p2p连接的鉴权码,只有在帐号异地登录或者服务器强制刷新(一般不会干这件事)时才会改变
//                        //所以可以将code1与code2保存起来,只需在下次登录时刷新即可
//                        int code1 = Integer.parseInt(thirdPartyLoginResult.getP2PVerifyCode1());
//                        int code2 = Integer.parseInt(thirdPartyLoginResult.getP2PVerifyCode2());
//                        //p2pconnect方法在APP一次生命周期中只需调用一次,退出后台结束时配对调用disconnect一次
//                        boolean connect = P2PHandler.getInstance().p2pConnect(thirdPartyLoginResult.getUserID(), code1, code2);
//                        LogUtil.e("code1", code1 + "");
//                        LogUtil.e("code2",code2+"");
//                        LogUtil.e("connect",connect+"");
//                            if (connect) {
//                            P2PHandler.getInstance().p2pInit(mContext, new P2PListener(), new SettingListener());
//                            Intent callIntent = new Intent(MyApplication.app, MainActivity.class);
//                            Toast.makeText(mContext, "登录成功", Toast.LENGTH_LONG).show();
//                            /**
//                             * 获取用户名存缓存，实现一次登录就不需要重复登录的效果
//                             */
//                            String LoginID=thirdPartyLoginResult.getUserID();
//                            LogUtil.e("LoginID", LoginID);
//                            SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();
//                            editor.putString("username", username); //用户名
//                            editor.putString("password",password);  //密码
//                            editor.putString("LoginID", LoginID);    //拿到用户ID存缓存
//                            editor.apply();
//                            callIntent.putExtra("LoginID", thirdPartyLoginResult.getUserID());
//                            startActivity(callIntent);
//                            finish();
//                        } else {
//                            //这里只是demo的写法,可加入自己的逻辑
//                            //为false时p2p的功能不可用
//                            Toast.makeText(MyApplication.app,""+connect,Toast.LENGTH_SHORT).show();
//                        }
//                            break;
//                        case HttpErrorCode.ERROR_10901020:
//                            Toast.makeText(mContext,"缺少输入参数",Toast.LENGTH_SHORT).show();
//                            break;
//
//                        default:
//                            //其它错误码需要用户自己实现
//                            String msg = String.format("注册测试版(%s)", thirdPartyLoginResult.getError_code());
//                            Toast.makeText(MyApplication.app, msg, Toast.LENGTH_LONG).show();
//                            break;
//                    }
//                    }
//                @Override
//                public void onError(String error_code, Throwable throwable) {
//
//                }
//            });
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }

    }
    /**
     * 隐藏键盘的方法
     */
    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromInputMethod(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==1&& resultCode==2){
            String phonenumber =data.getStringExtra("phonenumber");
            String password=data.getStringExtra("password");
            if (!TextUtils.isEmpty(phonenumber)&&!TextUtils.isEmpty(password)){
                username_input.getEditText().setText(phonenumber);
                password_input.getEditText().setText(password);
                username_input.getEditText().setSelection(phonenumber.length());
                password_input.getEditText().setSelection(password.length());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //重写返回键时要记得过滤点击事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        return super.onKeyDown(keyCode, event);
    }

}
