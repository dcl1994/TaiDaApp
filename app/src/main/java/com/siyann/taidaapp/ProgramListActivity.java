package com.siyann.taidaapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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

import adapter.ProgramAdapter;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import utils.LogUtil;
import utils.OkHttpUtil;
import utils.Url;
import widget.Program;

/**
 * 节目列表界面
 */
public class ProgramListActivity extends AppCompatActivity implements View.OnClickListener{
    private List<Program> programList=new ArrayList<>();
    private Context mContext;
    private ImageView back;
    private TextView mtitleview;
    private SweetAlertDialog dialog;
    private String result="";

    JSONObject jsonObject=null;
    JSONArray jsonArray=null;

    private ProgramAdapter adapter;

    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_list);
        mContext=ProgramListActivity.this;
        init();

        recyclerView= (RecyclerView) findViewById(R.id.tvrecycler_view);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        /**
         * 一进来就显示dialog
         */
        dialog=new SweetAlertDialog(mContext,SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("Loading....");
        dialog .getProgressHelper().setBarColor(Color.parseColor("#4b9be0"));
        dialog.show();
    }
    /**
     * 初始化ui
     */
    private void init() {
        /**
         * 设置标题
         */
        Intent intent=getIntent();
        mtitleview= (TextView) findViewById(R.id.title_view);
        mtitleview.setText(intent.getStringExtra("title"));

        back= (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
    }


    @Override
    protected void onStart() {
        super.onStart();


       if (OkHttpUtil.isNetworkAvailable(mContext)){
            getprograms();
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
                   dialog.dismissWithAnimation();
               }
           }).show();
       }

    }

    /**
     * 获取直播
     */
    private void getprograms() {

        OkHttpUtil.sendOkHttpRequest(Url.TVlive, new Callback() {
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
                                programList = gson.fromJson(jsonArray + "", new TypeToken<List<Program>>() {
                                }.getType());

                                for (Program program:programList){
                                    LogUtil.e("Title",program.getTitle());
                                    LogUtil.e("Link",program.getLink());
                                    LogUtil.e("litpic",program.getLitpic());
                                }
                                adapter=new ProgramAdapter(mContext,programList);
                                recyclerView.setAdapter(adapter);
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

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
        }
    }
}
