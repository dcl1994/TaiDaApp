package com.siyann.taidaapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adapter.ConvenienceAdapter;
import widget.ConveniencePhone;


/**
 * 便民号码页面
 */
public class ConveniencePhoneActivity extends Activity {
    private TextView mtextView;
    private ImageView mimageView;
    private RecyclerView mrecycler;
    private List<ConveniencePhone> conveniencePhoneList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convenience_phone);
        initconvenience();
        /**
         * 初始化recycler滑动组件
         * 适配adapter
         */
        mrecycler= (RecyclerView) findViewById(R.id.recycler_phone);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        mrecycler.setLayoutManager(layoutManager);

        ConvenienceAdapter adapter=new ConvenienceAdapter(conveniencePhoneList);
        mrecycler.setAdapter(adapter);

        init();
    }


    /**
     * 初始化数据
     */
    private void initconvenience() {
        ConveniencePhone company=new ConveniencePhone("泰达有线电视网络有线公司","25204666","","","");
        conveniencePhoneList.add(company);

        ConveniencePhone company1=new ConveniencePhone("泰达自来水公司","66200001","","","");
        conveniencePhoneList.add(company1);

        ConveniencePhone company2=new ConveniencePhone("泰达电力公司","25202526","25328937","(白天)","(晚上)");
        conveniencePhoneList.add(company2);


        ConveniencePhone company3=new ConveniencePhone("泰达新水源公司","66252500","66253902","(白天)","(晚上)");
        conveniencePhoneList.add(company3);

        ConveniencePhone company4=new ConveniencePhone("泰达燃气公司","59816000","25326936","(公司一)","(公司二)");
        conveniencePhoneList.add(company4);

        ConveniencePhone company5=new ConveniencePhone("泰达热电公司","25295500","66299371","(居民)","(企业)");
        conveniencePhoneList.add(company5);

    }

    /**
     * 初始化UI，填充数据
     */
    private void init() {
        Intent intent=getIntent();
        mtextView= (TextView) findViewById(R.id.title_view);
        mtextView.setText(intent.getStringExtra("title"));

        /**
         * 返回按钮的点击事件
         */
        mimageView= (ImageView) findViewById(R.id.back);
        mimageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
