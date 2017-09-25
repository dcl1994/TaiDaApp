package com.siyann.taidaapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
 * 找回密码界面
 */
public class FindPassword extends Activity {
    String uid;
    @Bind(R.id.usernameWrapper)
    TextInputLayout usernameWrapper;
    @Bind(R.id.VerfcodeWrapper)
    TextInputLayout VerfcodeWrapper;
    @Bind(R.id.getcode)
    Button getcode;
    @Bind(R.id.findpwd_btn)
    Button findpwdBtn;
    @Bind(R.id.line_login)
    RelativeLayout lineLogin;

    private Context mContext;

    private int i = 30; //30秒后重新发送
    private String phone = "";
    private String code = ""; //验证码
    String userid="";     //用户id
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        ButterKnife.bind(this);
        mContext = FindPassword.this;
        init();

        addLayoutListener(lineLogin, findpwdBtn);

    }
    public void addLayoutListener(final View lineLogin, final View scroll) {
        lineLogin.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                /**
                 * 1获取main在窗体的可视区域
                 */
                lineLogin.getWindowVisibleDisplayFrame(rect);
                /**
                 2获取main在窗体的不可视区域高度，在键盘没有弹起时
                 main.getRootView().getHeight() 调节度应该和rect.bottom高度一样
                 */
                int mainInvisibleHight = lineLogin.getRootView().getHeight() - rect.bottom;

                /**
                 * 3.不可见区域大于100，说明键盘弹起了
                 */
                if (mainInvisibleHight > 100) {
                    int[] location = new int[2];
                    scroll.getLocationInWindow(location);

                    /**
                     * 4，获取scroll的窗体坐标，算出main需要滚动的高度
                     */
                    int scrollHeight = (location[1] + scroll.getHeight()) - rect.bottom;


                    /**
                     * 5.让界面整体上移键盘的高度
                     */
                    lineLogin.scrollTo(0, scrollHeight);
                } else {
                    /**
                     * 3.不可见区域小于100，说明键盘隐藏了，把界面下移，移回到原有高度
                     */
                    lineLogin.scrollTo(0, 0);
                }
            }
        });
    }


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
            phone = usernameWrapper.getEditText().getText().toString();  //拿到电话号码
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
                         * 获取userid传递给changepwd界面
                         */
                        Intent intent = new Intent(mContext, ChangePassword.class);
                        intent.putExtra("userid",userid);
                        intent.putExtra("username",phone);
                        LogUtil.e("userid", userid);
                        LogUtil.e("username",phone);
                        startActivity(intent);
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Toast.makeText(getApplicationContext(), "验证码已发送", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "验证成功", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };

    /**
     * 根据传入的username获取用户ID
     */
    private void getuserid(String phone) {
        RequestBody requestBody=new FormBody.Builder()
                .add("username",phone)
                .build();
        OkHttpUtil.sendPostRequest(Url.getuserid, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.e("e",e+"");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                LogUtil.e("result", result);
                JSONObject jsonObject=null;
                try {
                    jsonObject=new JSONObject(result);
                    final String code=jsonObject.getString("code");
                    LogUtil.e("code",code);
                    if (code.equals("0")){
                        userid=jsonObject.getString("data");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
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
        usernameWrapper.setError("请输入正确的手机号");
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
     * 发送验证码的方法
     */
    private void sendcode() {
        //2:通过sdk发送验证短信
        SMSSDK.getVerificationCode("86", phone);
        //3:把按钮变成不可点击的，并显示倒计时(正在获取)
        getcode.setClickable(false);
        getcode.setText("重新发送(" + i + ")");
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (; i > 0; i--) {
                    handler.sendEmptyMessage(-9);
                    if (i < 0)
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
     * 点击事件
     *
     * @param view
     */
    @OnClick({R.id.getcode, R.id.findpwd_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.getcode:  //获取验证码
                hideKeyboard(); //隐藏键盘
                phone = usernameWrapper.getEditText().getText().toString();  //拿到电话号码
                //1:通过规则判断手机号
                if (!judgePhoneNums(phone)) {
                    return;
                } else {
                    usernameWrapper.setErrorEnabled(false);
                    /**
                     * 调用验证手机号码唯一
                     * 的接口
                     */
                    sendcode();

                    getuserid(phone);
                }
                break;
            case R.id.findpwd_btn:  //找回密码按钮的点击事件
                code = VerfcodeWrapper.getEditText().getText().toString();
                phone = usernameWrapper.getEditText().getText().toString();  //拿到电话号码
                if (TextUtils.isEmpty(phone)) {
                    usernameWrapper.setError("电话号码不能为空");
                }
                if (TextUtils.isEmpty(code)) {
                    VerfcodeWrapper.setError("验证码不能为空");
                } else {
                    VerfcodeWrapper.setErrorEnabled(false);
                    SMSSDK.submitVerificationCode("86", phone, code);
                }
                break;
        }
    }
}
