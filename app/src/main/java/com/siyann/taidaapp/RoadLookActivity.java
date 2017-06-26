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

import adapter.RoadLookAdapter;
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
import widget.Road;

/**
 * 路况查看
 */
public class RoadLookActivity extends Activity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.roadlook_recycle)
    RecyclerView roadlookRecycle;

    private Context mContext;

    private String result="";

    private List<Road> roadList=new ArrayList<>();

    private RoadLookAdapter adapter;

    private SweetAlertDialog dialog;

    JSONObject jsonObject=null;
    JSONArray jsonArray=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road_look);
        ButterKnife.bind(this);
        mContext=this;

        /**
         * 获取传递过来的标题
         */
        Intent intent = getIntent();
        titleView.setText(intent.getStringExtra("title"));


        LinearLayoutManager manager=new LinearLayoutManager(mContext);
        roadlookRecycle.setLayoutManager(manager);


        /**
         * 一进来就显示dialog
         */
        dialog=new SweetAlertDialog(mContext,SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("Loading...");
        dialog.getProgressHelper().setBarColor(Color.parseColor("#4b9be0"));
        dialog.show();


        initroadList();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (OkHttpUtil.isNetworkAvailable(mContext)){
//            doroad();

        }else {
            /**
             * 提示没有网络连接
             */
            dialog=new SweetAlertDialog(mContext,SweetAlertDialog.WARNING_TYPE);
              dialog.setTitleText("提示")
                    .setContentText("网络连接错误，请检查网络连接")
                    .setConfirmText("确定")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            dialog.dismissWithAnimation();
                        }
                    }).show();
        }
    }


    /**
     * 填充数据
     */
    private void initroadList() {
       Road road1=new Road("","时尚旺角小区北门口","http://111.30.78.162:89/live1/live1.m3u8","");
       roadList.add(road1);

        Road road2=new Road("","南海路二大街","http://111.30.78.162:89/live2/live2.m3u8","");
        roadList.add(road2);

        Road road3=new Road("","黄海路三大街","http://111.30.78.162:89/live3/live3.m3u8","");
        roadList.add(road3);

        Road road4=new Road("","北海路五大街","http://111.30.78.162:89/live4/live4.m3u8","");
        roadList.add(road4);

        Road road5=new Road("","南海路三大街","http://111.30.78.162:89/live5/live5.m3u8","");
        roadList.add(road5);

        Road road6=new Road("","黄海路二大街","http://111.30.78.162:89/live6/live6.m3u8","");
        roadList.add(road6);


        new CountDownTimer(600, 100) {
            @Override
            public void onTick(long millisUntilFinished) {

            }
            @Override
            public void onFinish() {
                adapter=new RoadLookAdapter(mContext,roadList);
                roadlookRecycle.setAdapter(adapter);
                dialog.dismissWithAnimation();
            }
        }.start();
    }

    /**
     * 获取路况地址
     */
    private void doroad() {
        OkHttpUtil.sendOkHttpRequest(Url.RoadLook, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.e("e",e+"");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                result=response.body().string();
                result=result.replace("<string xmlns=\"http://tempuri.org/\">", "")
                        .replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "")
                        .replace("&amp;#183;", "")
                        .replace("</string>", "");
                LogUtil.e("result", result);

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
                                roadList=gson.fromJson(jsonArray+"",new TypeToken<List<Road>>(){}.getType());

                                new CountDownTimer(600, 100) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {

                                    }
                                    @Override
                                    public void onFinish() {
                                        adapter=new RoadLookAdapter(mContext,roadList);
                                        roadlookRecycle.setAdapter(adapter);
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
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
