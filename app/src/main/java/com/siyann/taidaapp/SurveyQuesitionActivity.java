package com.siyann.taidaapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import adapter.SurveyQuesitionAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import utils.ActivityCollector;
import utils.CheckBoxOnclick;
import utils.LogUtil;
import utils.OkHttpUtil;
import widget.SurveyQuesition;

/**
 * 调查问卷的题目
 * 的详细信息
 */
public class SurveyQuesitionActivity extends Activity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.survey_quesition)
    RecyclerView surveyQuesition;
    @Bind(R.id.submit_text)
    TextView submitText;

    private Context mContext;

    private SurveyQuesitionAdapter adapter;

    private SweetAlertDialog dialog;

    private String IDN="";

    private String Content="";

    private String result="";

    JSONObject jsonObject = null;
    JSONArray jsonArray = null;

    private List<SurveyQuesition> surveyQuesitionList = new ArrayList<>();


    private String username=""; //用户名
    private String date="";     //时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_quesition);
        ButterKnife.bind(this);
        mContext = this;

        ActivityCollector.addActivity(this);

        /**
         * 获取传递过来的标题
         * ID
         *
         */
        Intent intent = getIntent();
        titleView.setText(intent.getStringExtra("title"));
        IDN = intent.getStringExtra("IDN");
        LogUtil.e("titleView", titleView + "");
        LogUtil.e("IDN", IDN);

        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        surveyQuesition.setLayoutManager(manager);

        /**
         * 一进来就显示dialog
         */
        dialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("Loading....");
        dialog.getProgressHelper().setBarColor(Color.parseColor("#4b9be0"));
        dialog.show();



        /**
         * 获取当前时间
         */
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        date=simpleDateFormat.format(new java.util.Date());
        LogUtil.e("date",date);

        /**
         * 从缓存中读取username
         */
        SharedPreferences pref=getSharedPreferences("data",MODE_PRIVATE);
        username=pref.getString("username","");
        LogUtil.e("username", username);

    }


    /**
     * CheckBox点击之后才显示发送按钮
     */
    private void setonclick(){
        adapter.setCheckBox(new CheckBoxOnclick() {
            @Override
            public void onclick() {
                submitText.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (OkHttpUtil.isNetworkAvailable(mContext)) {
            dosurveyquesition();
        } else {
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
     * 获取问答题目
     */
    private void dosurveyquesition() {
        OkHttpUtil.sendOkHttpRequest("http://121.42.32.107:8001/SurveyDeputy.asmx/SurveyDeputyTo?UserID=FHMS&Password=FHMS2016&ID=" + IDN, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.e("e", e + "");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                result = response.body().string();
                result = result.replace("<string xmlns=\"http://tempuri.org/\">", "")
                        .replace("</string>", "")
                        .replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "");
                LogUtil.e("result", result);

                try {
                    jsonObject = new JSONObject(result);
                    jsonArray = jsonObject.getJSONArray("ds");
                    final Boolean flag = jsonObject.getBoolean("ok");
                    if (flag == true) {
                        Gson gson = new Gson();
                        surveyQuesitionList = gson.fromJson(jsonArray + "",
                                new TypeToken<List<SurveyQuesition>>() {
                                }.getType());

                        for (SurveyQuesition surveyQuesition : surveyQuesitionList) {
                            LogUtil.e("topic", surveyQuesition.getSurveyD_Topic());
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                adapter = new SurveyQuesitionAdapter(surveyQuesitionList);
                                surveyQuesition.setAdapter(adapter);
                                /**
                                 * CheckBox的点击事件
                                 */
                                setonclick();

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
                        });
                    }
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 点击事件
     */
    @OnClick({R.id.back, R.id.submit_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:         //finish
                finish();
                break;
            case R.id.submit_text: //提交
                if (OkHttpUtil.isNetworkAvailable(mContext)){
                    dosubmit();
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
                break;
        }
    }
    /**
     * 提交
     * Content=1、2，2、4   题目、答案（1,2,3,4）
     * 我要知道用户点击的是第几个CheckBox，然后就把那个CheckBox+答案的下标用一个字符串存起来
     */
    private void dosubmit() {
        OkHttpUtil.sendOkHttpRequest("http://121.42.32.107:8001/SurveyReturn.asmx/SurveyReturnTo?UserID=FHMS&Password=FHMS2016&PID=1&Content="+Content+"+&Name="
                +username+"&Date="+date+"&Type=App", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
              LogUtil.e("e",e+"");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                result=response.body().string();
                result=result
                .replace("<string xmlns=\"http://tempuri.org/\">", "")
                .replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "")
                .replace("</string>", "");
                LogUtil.e("result",result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
                        if (!TextUtils.isEmpty(result)) {
                            finish();
                        }
                    }
                });
            }
        });

    }
}
