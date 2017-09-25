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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapter.SurveyAdapter;
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
import widget.Survey;

/**
 * 问卷调查
 */
public class SurveyActivity extends Activity {
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.relative_title)
    RelativeLayout relativeTitle;
    @Bind(R.id.SurverRecycler)
    RecyclerView SurverRecycler;

    private Context mContext;

    private SweetAlertDialog dialog;

    private List<Survey> msurveyList=new ArrayList<>();

    private String result="";

    private SurveyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        ButterKnife.bind(this);
        mContext=this;

        ActivityCollector.addActivity(this);
        /**
         * 设置标题
         */
        Intent intent=getIntent();
        titleView.setText(intent.getStringExtra("title"));

        /**
         * 一进来就显示dialog
         */
        dialog=new SweetAlertDialog(mContext,SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("Loading....");
        dialog .getProgressHelper().setBarColor(Color.parseColor("#4b9be0"));
        dialog.show();


        LinearLayoutManager manager=new LinearLayoutManager(mContext);
        SurverRecycler.setLayoutManager(manager);

    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (OkHttpUtil.isNetworkAvailable(mContext)){
            doSurvey();
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
     *获取问卷调查
     */
    private void doSurvey() {
        OkHttpUtil.sendOkHttpRequest(Url.Survey, new Callback() {
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
                LogUtil.e("result", result);
                JSONObject jsonObject=null;
                JSONArray jsonArray=null;
                try {
                    jsonObject=new JSONObject(result);
                    jsonArray=jsonObject.getJSONArray("ds");
                    final Boolean flag = jsonObject.getBoolean("ok");
                    if (flag==true){
                        Gson gson=new Gson();
                        msurveyList=gson.fromJson(jsonArray+"", new TypeToken<List<Survey>>(){}.getType());

                        for (Survey survey:
                                msurveyList) {
                            LogUtil.e("title",survey.getTitle());
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter=new SurveyAdapter(msurveyList);
                                SurverRecycler.setAdapter(adapter);

                                /**
                                 * dialog过600毫秒消失
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
