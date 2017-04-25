package com.siyann.taidaapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 添加地址的activity
 */
public class AddAddressActivity extends Activity implements View.OnClickListener {
    private ImageView back;
    private TextView mtextView;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addusername);
        mContext=this;
        init();
    }
    /**
     * 初始化UI
     */
    private void init() {
        Intent intent=getIntent();
        mtextView= (TextView) findViewById(R.id.tv_ameter_garden);
        mtextView.setText(intent.getStringExtra("title"));

        back= (ImageView) findViewById(R.id.leftback);
        back.setOnClickListener(this);
    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.leftback:
                finish();
                break;

        }
    }
}
