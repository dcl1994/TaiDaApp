package com.siyann.taidaapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import utils.LogUtil;
import utils.MyApplication;
import utils.OkHttpUtil;
import utils.Url;

/**
 * 注册的activity
 */
public class RegisterActivity extends AppCompatActivity {
    @Bind(R.id.phone_edit)
    EditText phoneEdit;         //电话号码
    @Bind(R.id.code)
    EditText code;              //验证码
    @Bind(R.id.VerfcodeWrapper)
    TextInputLayout VerfcodeWrapper;
    @Bind(R.id.getcode)
    Button getcode;             //获取验证码
    @Bind(R.id.password_edit)
    EditText passwordEdit;      //密码
    @Bind(R.id.register_btn)
    Button registerBtn;         //注册
    @Bind(R.id.PhoneWrapper)
    TextInputLayout PhoneWrapper;
    @Bind(R.id.lineart)
    LinearLayout lineart;
    @Bind(R.id.PasswordreWrapper)
    TextInputLayout PasswordreWrapper;
    private Context mContext;

    private String phone;   //电话号码
    private String password;  //密码
    private String str_code;      //验证码
    int i = 30;   //30秒后重新发送


    JSONObject jsonobject = null;
    JSONArray jsonarray = null;
    String code_json = null;
    String token = null;
    String msg = null;        //返回的提示信息


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mContext = this;
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        //启动短信验证SDK
        SMSSDK.initSDK(this, MyApplication.MobAPPID, MyApplication.MobSecret);
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        //注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);
    }

    /**
     * 接收初始化传递过来的值
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            phone = PhoneWrapper.getEditText().getText().toString();  //拿到电话号码
            password = PasswordreWrapper.getEditText().getText().toString();  //拿到密码
            if (msg.what == -9) {
                getcode.setText("重新发送(" + i + ")");
            } else if (msg.what == -8) {
                getcode.setText("获取验证码");
                getcode.setClickable(true);
                i = 30;
            } else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                LogUtil.e("event", "event=" + event);
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //短信注册成功后，返回
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        /**
                         * 提交验证码成功
                         * 调用注册的接口注册
                         */
                        doRegister();
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Toast.makeText(getApplicationContext(), "验证码已发送", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "验证码成功", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };

    /**
     * 密码长度必须大于5位
     *
     * @param password
     * @return
     */
    private boolean validatePassword(String password) {
        return password.length() > 5;
    }

    /**
     * 隐藏键盘
     */
    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromInputMethod(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 判断手机号码是否合理
     */
    private boolean judgePhoneNums(String phoneNums) {
        if (isMatchLength(phoneNums, 11) && isMobileNO(phoneNums)) {
            return true;
        }
        PhoneWrapper.setError("请输入正确的手机号");
        return false;
    }

    /**
     * 判断一个字符串的位数
     */
    private boolean isMatchLength(String str, int length) {
        if (str.isEmpty()) {
            return false;
        } else {
            return str.length() == length ? true : false;
        }
    }

    /**
     * 验证手机格式
     *
     * @param mobileNums
     * @return
     */
    private boolean isMobileNO(String mobileNums) {
        /*
       * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
       * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
       * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
       */
        String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }


    /**
     * 销毁线程时调用
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }


    /**
     * 点击事件
     * @param view
     */
    @OnClick({R.id.getcode, R.id.register_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.getcode:  //获取验证码
                hideKeyboard();
                phone = PhoneWrapper.getEditText().getText().toString();  //拿到电话号码
                password = PasswordreWrapper.getEditText().getText().toString();  //拿到密码
                //1:通过规则判断手机号
                if (!judgePhoneNums(phone)) {
                    return;
                }else if (!validatePassword(password)){
                    PasswordreWrapper.setError("密码长度不能少于6位");
                }else {
                    PhoneWrapper.setErrorEnabled(false);
                    PasswordreWrapper.setErrorEnabled(false);

                    /**
                     * 获取验证码
                     */
                    sendcode();
                }
                break;

            case R.id.register_btn: //注册的点击事件
                str_code=VerfcodeWrapper.getEditText().getText().toString();
                LogUtil.e("strcode",str_code);

                if (TextUtils.isEmpty(str_code)){
                    VerfcodeWrapper.setError("验证码不能为空");
                }else {
                    VerfcodeWrapper.setErrorEnabled(false);
                    /**
                     * 验证的监听需要传递三个参数
                     */
                    SMSSDK.submitVerificationCode("86", phone,str_code);
                }
                break;
            default:
                break;

        }
    }

    /**
     * 发送验证码的方法
     */
    private void sendcode(){
        //2:通过sdk发送验证短信
        SMSSDK.getVerificationCode("86", phone);
        //3:把按钮变成不可点击的，并显示倒计时(正在获取)
        getcode.setClickable(false);
        getcode.setText("重新发送(" + i + ")");
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (; i> 0; i--) {
                    handler.sendEmptyMessage(-9);
                    if (i< 0)
                        break;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                handler.sendEmptyMessage(-8);
            }
        }).start();
    }


    /**
     * 注册的方法
     */
    private void doRegister() {
        RequestBody body=new FormBody.Builder()
                .add("username",phone)
                .add("password",password)
                .build();

        OkHttpUtil.sendPostRequest(Url.register, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.e("e",e+"");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result=response.body().string();
                try {
                    jsonobject=new JSONObject(result);
                    final String status = jsonobject.getString("status");
                    final String msg =jsonobject.getString("msg");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            /**
                             * 请求成功
                             */
                            if (status.equals("1")){
                                Toast.makeText(mContext, msg,Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent();
                                intent.putExtra("phone",phone);
                                intent.putExtra("password", password);
                                setResult(RESULT_OK, intent);
                                finish();
                            }else {
                                Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    /**
     * 重写返回键
     */
    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.putExtra("phone",phone);
        intent.putExtra("password", password);
        setResult(RESULT_OK, intent);
        finish();
    }
}
