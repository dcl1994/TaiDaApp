package com.siyann.taidaapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adapter.ProgramAdapter;
import widget.Program;

/**
 * 节目列表界面
 */
public class ProgramListActivity extends AppCompatActivity implements View.OnClickListener{
    private List<Program> programList=new ArrayList<>();
    private Context mContext;
    private ImageView back;
    private TextView mtitleview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_list);
        mContext=ProgramListActivity.this;
        init();
        initPrograms();

        RecyclerView recyclerView= (RecyclerView) findViewById(R.id.tvrecycler_view);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        ProgramAdapter adapter=new ProgramAdapter(programList);
        recyclerView.setAdapter(adapter);
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

    /**
     * 初始化电视节目数据
     */
    private void initPrograms() {
        Program fenghuang=new Program("凤凰卫视",R.drawable.fenghuang);
        programList.add(fenghuang);

        Program gansu=new Program("江苏电视台",R.drawable.gansu);
        programList.add(gansu);

        Program heilongjiang=new Program("黑龙江电视台",R.drawable.heilongjiang);
        programList.add(heilongjiang);

        Program hunan=new Program("湖南电视台",R.drawable.hunan);
        programList.add(hunan);


        Program jiangxi=new Program("江西电视台",R.drawable.jiangxi);
        programList.add(jiangxi);

        Program lvyou=new Program("旅游卫视",R.drawable.lvyou);
        programList.add(lvyou);

        Program nantong=new Program("南通电视台",R.drawable.nantong);
        programList.add(nantong);


        Program suzhou=new Program("苏州电视台",R.drawable.suzhou);
        programList.add(suzhou);

        Program zhejiang=new Program("浙江电视台",R.drawable.zhejiang);
        programList.add(zhejiang);
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
