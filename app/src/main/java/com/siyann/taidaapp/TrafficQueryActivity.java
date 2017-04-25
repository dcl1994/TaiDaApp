package com.siyann.taidaapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import adapter.TrafficQueryAdapter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import utils.LogUtil;
import utils.OkHttpUtil;

/**
 * 路况查询的界面
 */
public class TrafficQueryActivity extends Activity implements View.OnClickListener{
    private ImageView mimageView;
    private TextView mtextView;
    private Context mContext;
    private ListView mlistView;
    private  TrafficQueryAdapter adapter;
    private String result="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic_query);
        mContext=this;
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
            OkHttpUtil.sendOkHttpRequest("http://120.76.22.150:8080/CellBank/getTopics1",
                    new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                result=response.body().string();
                LogUtil.e("result",result);
                try {
                    JSONObject jsonObject=null;
                    jsonObject=new JSONObject(result);

                    JSONArray jsonArray=jsonObject.getJSONArray("");
                    for (int i=0;i<jsonArray.length();i++){
                        adapter=new TrafficQueryAdapter(mContext,jsonArray);
                        mlistView.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 初始化UI
     */
    private void init() {
        mlistView= (ListView) findViewById(R.id.traffic_listview);

        mimageView= (ImageView) findViewById(R.id.back);
        mimageView.setOnClickListener(this);

        mtextView= (TextView) findViewById(R.id.title_view);
        mtextView.setText(getIntent().getStringExtra("title"));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;

        }
    }
}
