package com.siyann.taidaapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import utils.LogUtil;
import utils.OkHttpUtil;
import utils.Url;

/**
 * 绑定微信界面
 */
public class BindWxActivity extends Activity {

    @Bind(R.id.againpassword)
    EditText againpassword;
    @Bind(R.id.phoneWrapper)
    TextInputLayout phoneWrapper;
    @Bind(R.id.bind_btn)
    Button bindBtn;
    @Bind(R.id.cancel_btn)
    Button cancelBtn;

    private Context mContext;

    private String username;    //电话号码
    private String open_id;     //微信界面传递过来的唯一标识
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_wx);
        ButterKnife.bind(this);
        mContext = this;


        Intent intent=getIntent();
        open_id=intent.getStringExtra("unionid");
        LogUtil.e("open_id",open_id);
    }


    /**
     * 点击事件
     * @param view
     */
    @OnClick({R.id.bind_btn, R.id.cancel_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bind_btn: //绑定微信
                username=phoneWrapper.getEditText().getText().toString();
                //1:通过规则判断手机号
                if (!judgePhoneNums(username)) {
                    return;
                } else {
                    phoneWrapper.setErrorEnabled(false);
                    /**
                     * 绑定微信
                     */
                    bindwx();
                }
                break;
            case R.id.cancel_btn: //取消
                Intent intent=new Intent(mContext,MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }


    /**
     * 绑定微信的方法
     */
    private void bindwx() {
        RequestBody body=new FormBody.Builder()
                .add("username",username)
                .add("open_id",open_id)
                .build();

        OkHttpUtil.sendPostRequest(Url.Bind_WeChat, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.e("e",e+"");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
               String result=response.body().string();
               LogUtil.e("result",result);
                JSONObject jsonObject=null;
                try {
                    jsonObject=new JSONObject(result);
                    final String status=jsonObject.getString("status");
                    final String msg=jsonObject.getString("msg");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            /**
                             * 绑定成功之后
                             * 将绑定的信息存缓存
                             * 下次进来的时候就不需要再次绑定了
                             */
                            if (status.equals("1")){
                                SharedPreferences.Editor editor=getSharedPreferences("bindwx",MODE_PRIVATE).edit();
                                editor.putString("msg",msg);
                                editor.commit();

                                Intent intent=new Intent(mContext,MainActivity.class);
                                startActivity(intent);
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
     * 判断手机号码是否合理
     */
    private boolean judgePhoneNums(String phoneNums) {
        if (isMatchLength(phoneNums, 11) && isMobileNO(phoneNums)) {
            return true;
        }
        phoneWrapper.setError("请输入正确的手机号");
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


}
