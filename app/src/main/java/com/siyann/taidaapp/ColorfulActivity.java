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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapter.ColorFulAdapter;
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
 * 后台缤纷活动
 */
public class ColorfulActivity extends Activity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.colorful_recycler)
    RecyclerView colorfulRecycler;

    private Context mContext;
    private String result="";

    private List<Community> mcommunityList=new ArrayList<>();

    private ColorFulAdapter adapter;

    private SweetAlertDialog dialog;

    JSONObject jsonObject=null;
    JSONArray jsonArray=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colorful);
        ButterKnife.bind(this);
        mContext=this;

        /**
         * 添加进去
         */
        ActivityCollector.addActivity(this);

        /**
         * 获取title
         */
        Intent intent=getIntent();
        titleView.setText(intent.getStringExtra("title"));

        LinearLayoutManager manager=new LinearLayoutManager(mContext);
        colorfulRecycler.setLayoutManager(manager);


        /**
         * 一进来就显示dialog
         */
        dialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("Loading....");
        dialog.getProgressHelper().setBarColor(Color.parseColor("#4b9be0"));
        dialog.show();

    }


    @Override
    protected void onStart() {
        super.onStart();

        if (OkHttpUtil.isNetworkAvailable(mContext)){
            docolotful();
        }else {
            /**
             * 提示没有网络连接
             */
            final SweetAlertDialog dialog = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE);
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
     * 获取缤纷活动
     */
    private void docolotful() {
        OkHttpUtil.sendOkHttpRequest(Url.ColorfulActivity, new Callback() {
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
                            jsonObject = new JSONObject(result);
                            final Boolean flag = jsonObject.getBoolean("ok");
                            jsonArray = jsonObject.getJSONArray("ds");
                            LogUtil.e("jsonArray", jsonArray + "");
                            if (flag == true) {
                                /**
                                 * 使用Gson解析来遍历jsonArray数组
                                 */
                                LogUtil.e("flag",flag+"");
                                Gson gson = new Gson();
                                mcommunityList = gson.fromJson(jsonArray + "", new TypeToken<List<Community>>() {
                                }.getType());

                                adapter = new ColorFulAdapter(mcommunityList);
                                colorfulRecycler.setAdapter(adapter);
                                /**
                                 * dialog过一秒隐藏
                                 */
                                new CountDownTimer(600, 100) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                    }
                                    @Override
                                    public void onFinish() {
                                        /**
                                         * 隐藏dialog
                                         */
                                        dialog.dismissWithAnimation();
                                    }
                                }.start();
                            } else {
                                Toast.makeText(mContext, "获取数据失败", Toast.LENGTH_SHORT).show();
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
