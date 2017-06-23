package com.siyann.taidaapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.widget.Button;
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
 * 修改密码界面
 */
public class ChangePassword extends Activity {
    @Bind(R.id.newpwdWrapper)
    TextInputLayout newpwdWrapper;
    @Bind(R.id.againWrapper)
    TextInputLayout againWrapper;
    @Bind(R.id.changepwd_btn)
    Button changepwdBtn;
    private Context mContext;
    private String newPwd = ""; //新密码
    private String againPwd = ""; //再次确认
    private String username="";//电话号码
    private String uid="";      //userID
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        mContext = ChangePassword.this;

        /**
         * 获取缓存中的电话和ID
         */
        SharedPreferences pref=getSharedPreferences("data",MODE_PRIVATE);
        username=pref.getString("username","");
        uid=pref.getString("id","");

        LogUtil.e("username",username);
        LogUtil.e("uid",uid);
    }

    /**
     * 修改密码
     */
    @OnClick(R.id.changepwd_btn)
    public void onViewClicked() {
     newPwd=newpwdWrapper.getEditText().getText().toString();
     againPwd=againWrapper.getEditText().getText().toString();
     if (TextUtils.isEmpty(newPwd)){
         newpwdWrapper.setError("新密码不能为空");
     }else if (TextUtils.isEmpty(againPwd)){
         againWrapper.setError("再次确认密码不能为空");

     }else if (!newPwd.equals(againPwd)){
         againWrapper.setError("两次输入密码不一致");
     } else {
         newpwdWrapper.setErrorEnabled(false);
         againWrapper.setErrorEnabled(false);
         /**
          * 调用找回密码的接口
          */
         doUpdatePwd();
     }

    }

    /**
     * 找回密码
     */
    private void doUpdatePwd() {
        RequestBody body=new FormBody.Builder()
                .add("username",username)
                .add("uid",uid)
                .add("password",againPwd)
                .build();

        OkHttpUtil.sendPostRequest(Url.updatePassword, body, new Callback() {
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
                            if (status.equals("1")){
                                Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(mContext,LoginActivity.class);
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
}
