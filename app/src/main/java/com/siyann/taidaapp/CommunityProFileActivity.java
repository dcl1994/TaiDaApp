package com.siyann.taidaapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapter.CommunityProfileAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import utils.ActivityCollector;
import utils.LogUtil;
import utils.OkHttpUtil;
import utils.Url;
import widget.Community;

/**
 * 社区简介
 */
public class CommunityProFileActivity extends Activity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.profile_recycle)
    RecyclerView profileRecycle;

    private Context mContext;

    private String result="";
    private JSONObject jsonObject=null;
    private JSONArray jsonArray=null;
    List<Community> mcommunityList=new ArrayList<>();
    private SweetAlertDialog dialog;

    private CommunityProfileAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_pro_file);
        ButterKnife.bind(this);
        mContext=this;

        ActivityCollector.addActivity(this);
        /**
         * 获取传递过来的标题
         */
        Intent intent=getIntent();
        titleView.setText(intent.getStringExtra("title"));

        LinearLayoutManager manager=new LinearLayoutManager(mContext);
        profileRecycle.setLayoutManager(manager);
        /**
         * 一进来就显示dialog
         */
        dialog=new SweetAlertDialog(mContext,SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("Loading...");
        dialog.getProgressHelper().setBarColor(Color.parseColor("#4b9be0"));
        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (OkHttpUtil.isNetworkAvailable(mContext)){
            doprofile();
        }else {
            /**
             * 提示没有网络连接
             */
            dialog = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE);
            dialog.setTitleText("提示")
                    .setContentText("网络连接错误，请检查网络连接")
                    .setConfirmText("确定")
                    .setCancelable(false);
                    dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            ActivityCollector.finishAll();
                            dialog.dismissWithAnimation();
                        }
                    }).show();
        }
    }




    /**
     *获取社区简介
     */
    private void doprofile() {
        OkHttpUtil.sendOkHttpRequest(Url.CommunityProfile, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.e("e",e+"");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                result=response.body().string();
                result=result.replace("<string xmlns=\"http://tempuri.org/\">", "")
                        .replace("</string>", "")
                        .replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "");
                LogUtil.e("result",result);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            jsonObject=new JSONObject(result);
                            final Boolean flag=jsonObject.getBoolean("ok");
                            jsonArray=jsonObject.getJSONArray("ds");
                            LogUtil.e("jsonArray",jsonArray+"");
                            if (flag==true){
                                Gson gson=new Gson();
                                mcommunityList=gson.fromJson
                                        (jsonArray+"", new TypeToken<List<Community>>(){}.getType());

                                /**
                                 * foreach遍历
                                 */
                                for (Community community:mcommunityList) {
                                    LogUtil.e("title",community.getTitle());
                                    LogUtil.e("content",community.getContent());
                                }
                                /**
                                 * 数据适配
                                 */
                                adapter=new CommunityProfileAdapter(mContext,mcommunityList);
                                profileRecycle.setAdapter(adapter);
                                /**
                                 * 过600毫秒关闭dialog
                                 */
                                new CountDownTimer(600, 100) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                    }
                                    @Override
                                    public void onFinish() {
                                        dialog.dismissWithAnimation();
                                    }
                                }.start();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });




    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }


    /**
     * 重写返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
