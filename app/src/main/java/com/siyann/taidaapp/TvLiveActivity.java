package com.siyann.taidaapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 电视直播界面
 */
public class TvLiveActivity extends AppCompatActivity {
    private GridView mGridView; //网格布局
    private TextView titleview;
    private ImageView mimageView;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_live);
        mContext = TvLiveActivity.this;
        init();

    }

    /**
     * 初始化UI布局
     */
    private void init() {
        /**
         * 设置标题
         */
        final Intent intent = getIntent();
        titleview = (TextView) findViewById(R.id.title_view);
        titleview.setText(intent.getStringExtra("title"));

        /**
         * 返回上一个界面
         */
        mimageView = (ImageView) findViewById(R.id.back);
        mimageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /**
         * 配置GridView的adapter
         */
        mGridView = (GridView) findViewById(R.id.gridview);
        //图片ID
        int[] imageid = new int[]{R.drawable.tv, R.drawable.video, R.drawable.add};
        //文本
        String[] title = new String[]{"微直播", "微点播", "更多"};
        List<Map<String, Object>> listitems = new ArrayList<>();
        for (int i = 0; i < imageid.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("image", imageid[i]);
            map.put("title", title[i]);
            listitems.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listitems, R.layout.grid_item, new String[]{"title", "image"},
                new int[]{R.id.Itemtxt, R.id.Itemimg});
        mGridView.setAdapter(simpleAdapter);


        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent intent1 = new Intent(mContext, ProgramListActivity.class);
                        intent1.putExtra("title","直播列表");
                        startActivity(intent1);
                        break;
                    case 1:
                        Snackbar.make(view, "暂未开通尽请期待", Snackbar.LENGTH_SHORT).setAction("关闭", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        }).show();
                        break;
                    case 2:
                        Snackbar.make(view, "更多功能尽请期待", Snackbar.LENGTH_SHORT).setAction("关闭", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        }).show();

                        break;

                }
            }
        });
    }


}
