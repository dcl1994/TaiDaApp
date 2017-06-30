package com.siyann.taidaapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import utils.LogUtil;
import utils.OkHttpUtil;
import utils.Url;

/**
 * 个人详细信息
 */
public class UserInfoActivity extends Activity {

    /**
     * 联系电话
     */
    @Bind(R.id.contact_phone)
    TextView contactPhone;

    @Bind(R.id.contactphone_linearlayout)
    RelativeLayout contactphoneLinearlayout;


    /**
     * 小区名称
     */
    @Bind(R.id.community_name)
    TextView communityName;

    @Bind(R.id.community_linearlayout)
    RelativeLayout communityLinearlayout;

    /**
     * 默认收货地址
     */
    @Bind(R.id.shippingaddress_textview)
    TextView shippingaddressTextview;

    @Bind(R.id.expressaddress_linearlayout)
    RelativeLayout expressaddressLinearlayout;

    /**
     * 联系人
     */
    @Bind(R.id.contact_textview)
    TextView contactTextview;

    @Bind(R.id.contact_linearlayout)
    RelativeLayout contactLinearlayout;

    /**
     * 电话
     */
    @Bind(R.id.phone_textview)
    TextView phoneTextview;

    @Bind(R.id.phone_linearlayout)
    RelativeLayout phoneLinearlayout;


    /**
     * 微信头像
     */
    @Bind(R.id.wxuser_img)
    ImageView wxuserImg;

    /**
     * 昵称
     */
    @Bind(R.id.nickname)
    TextView nickname;

    private Context mContext;

    private String user_id="";

    private SweetAlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        mContext = this;
        /**
         * 获取微信登录后的昵称和头像地址
         */
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        nickname.setText(pref.getString("nickname", ""));
        user_id=pref.getString("id","");

        LogUtil.e("nickname",nickname+"");
        LogUtil.e("user_id",user_id);
        Glide.with(mContext).load(pref.getString("headimgurl", "")).into(wxuserImg);


        /**
         * 一进来就显示dialog
         */
        dialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("Loading....");
        dialog.getProgressHelper().setBarColor(Color.parseColor("#4b9be0"));
        dialog.show();


    }

    /**
     * 点击事件
     * @param view
     */
    @OnClick({R.id.contactphone_linearlayout, R.id.community_linearlayout, R.id.expressaddress_linearlayout, R.id.contact_linearlayout, R.id.phone_linearlayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.contactphone_linearlayout:        //联系电话
                Intent intent=new Intent(mContext,UserDetailActivity.class);
                intent.putExtra("id",1);
                intent.putExtra("title", "联系电话");
                startActivityForResult(intent, 1);
                break;
            case R.id.community_linearlayout:           //小区名称
                Intent intent1=new Intent(mContext,UserDetailActivity.class);
                intent1.putExtra("id",2);
                intent1.putExtra("title","小区名称");
                startActivityForResult(intent1, 2);
                break;
            case R.id.expressaddress_linearlayout:      //送货地址
                Intent intent2=new Intent(mContext,UserDetailActivity.class);
                intent2.putExtra("id", 3);
                intent2.putExtra("title","送货地址");
                startActivityForResult(intent2, 3);
                break;
            case R.id.contact_linearlayout:             //联系电话
                Intent intent3=new Intent(mContext,UserDetailActivity.class);
                intent3.putExtra("id",4);
                intent3.putExtra("title","联系电话");
                startActivityForResult(intent3, 4);
                break;
            case R.id.phone_linearlayout:               //电话
                Intent intent4=new Intent(mContext,UserDetailActivity.class);
                intent4.putExtra("id",5);
                intent4.putExtra("title","电话");
                startActivityForResult(intent4,5);
                break;
            default:
                break;
        }
    }

    /**
     * 获取用户信息
     */
    @Override
    protected void onStart() {
        super.onStart();

        getPersonInfo();
    }


    /**
     * 获取用户信息
     */
    private void getPersonInfo() {
        OkHttpUtil.sendOkHttpRequest(Url.getPersonInfo+"&user_id="+user_id, new Callback() {
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
                    final JSONObject jsonObject1=jsonObject.getJSONObject("data");
                    LogUtil.e("jsonObject1",jsonObject1+"");
                    String status=jsonObject.getString("status");
                    if (status.equals("1")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new CountDownTimer(100,600) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                    }
                                    @Override
                                    public void onFinish() {
                                        try {
                                            contactPhone.setText(jsonObject1.getString("mobile"));
                                            communityName.setText(jsonObject1.getString("place_qu"));
                                            shippingaddressTextview.setText(jsonObject1.getString("address_m"));
                                            contactTextview.setText(jsonObject1.getString("name_m"));
                                            phoneTextview.setText(jsonObject1.getString("mobile_m"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        dialog.dismissWithAnimation();
                                    }
                                }.start();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String status="";
            }
        });
    }
    /**
     * 重写返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
