package com.siyann.taidaapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Process;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.siyann.taidaapp.wxapi.WXEntryActivity;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import utils.LogUtil;
import utils.OkHttpUtil;
import utils.Url;

/**
 * 泰达的登录界面
 * 实现一次登录就不用再登录了
 */
public class LoginActivity extends AppCompatActivity {
    @Bind(R.id.usernameWrapper)
    TextInputLayout usernameWrapper;
    @Bind(R.id.passwordWrapper)
    TextInputLayout passwordWrapper;
    @Bind(R.id.login)       //登录
            Button login;
    @Bind(R.id.register)    //注册
            TextView register;
    @Bind(R.id.wx_login)    //微信登录
            Button wxLogin;
    @Bind(R.id.line_login)
    LinearLayout lineLogin;

    private Context mContext;

    private String username;

    private String password;

    ProgressBar mProBar;

    private SweetAlertDialog pdialog;  //清新dialog

    JSONObject jsonObject = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mContext = LoginActivity.this;

        addLayoutListener(lineLogin,login);

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



    /**
     * 自定义一个帧布局显示progressbar在注册的中间
     */
    private void createProgressBar() {
        FrameLayout layout = (FrameLayout) findViewById(android.R.id.content);
        FrameLayout.LayoutParams layoutParams =
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        mProBar = new ProgressBar(this);
        mProBar.setLayoutParams(layoutParams);
        mProBar.setVisibility(View.VISIBLE);
        layout.addView(mProBar);
    }

    /**
     * 密码的判断
     */
    private boolean validatePassword(String password) {
        return password.length() > 5;
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

    //重写返回键时要记得过滤点击事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Process.killProcess(Process.myPid());
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.login, R.id.register, R.id.wx_login, R.id.forgetpwd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login:        //登录
                hideKeyboard(); //隐藏键盘
                username = usernameWrapper.getEditText().getText().toString();   //获取Edittext中的username
                password = passwordWrapper.getEditText().getText().toString();    //获取Edittext中的password

                /**
                 * 判断用户输入的手机号是否正确
                 */
                if (!judgePhoneNums(username)) {
                    return;
                } else if (!validatePassword(password)) {
                    passwordWrapper.setError("密码长度不能少于6位");
                } else {
                    usernameWrapper.setErrorEnabled(false);
                    passwordWrapper.setErrorEnabled(false);
                    createProgressBar();
                    /**
                     * 检查网络是否已连接
                     */
                    if (OkHttpUtil.isNetworkAvailable(mContext)) {
                        dologin();  //调用登录方法
                    }
                }
                break;
            case R.id.wx_login:   //微信登录
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
                break;
            case R.id.register:     //注册
                Intent intentReg = new Intent(mContext, RegisterActivity.class);
                startActivityForResult(intentReg, 1);
                break;

            case R.id.forgetpwd:    //忘记密码
                Intent intent = new Intent(mContext, FindPassword.class);
                intent.putExtra("title", "找回密码");
                startActivity(intent);
                break;
            default:
                break;
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
     * 得到返回值
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String phone = data.getStringExtra("phone");
                    String password = data.getStringExtra("password");
                    /**
                     * 将返回的数据填充到edittext中
                     */
                    usernameWrapper.getEditText().setText(phone);
                    passwordWrapper.getEditText().setText(password);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 登录的方法
     */
    private void dologin() {
        RequestBody body = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();
        OkHttpUtil.sendPostRequest(Url.login, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.e("e", e + "");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                LogUtil.e("result", result);
                try {
                    jsonObject = new JSONObject(result);
                    final String msg = jsonObject.getString("msg");
                    final String status = jsonObject.getString("status");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            /**
                             * 登录成功
                             */
                            if (status.equals("1")) {
                                try {
                                    JSONObject jsonObject1 = null;
                                    jsonObject1 = jsonObject.getJSONObject("data");
                                    final String id = jsonObject1.getString("id");

                                    /**
                                     * 将电话号码和用户ID存缓存
                                     */
                                    SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                                    editor.putString("id", id);
                                    editor.putString("username", username);
                                    editor.commit();

                                    LogUtil.e("msg", msg);
                                    LogUtil.e("status", status);
                                    LogUtil.e("id", id);


                                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(mContext, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                /**
                                 * 登录失败弹出提示框
                                 */
                                pdialog = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE);
                                pdialog.setTitleText("登录失败")
                                        .setContentText(msg)
                                        .setConfirmText("确定")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                mProBar.setVisibility(View.GONE);
                                                pdialog.dismissWithAnimation();
                                            }
                                        })
                                        .show();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
