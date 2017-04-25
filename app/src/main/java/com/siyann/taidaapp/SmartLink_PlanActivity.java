package com.siyann.taidaapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 智能联机提示
 */
public class SmartLink_PlanActivity extends Activity implements View.OnClickListener{
    private Context mContext;
    private ImageView mimageView;   //返回键
    private TextView mtextView;     //标题
    private Button  mbutton;        //下一步按钮
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_link__plan);
        mContext=SmartLink_PlanActivity.this;
        init();
    }

    /**
     * 初始化UI界面
     */
    private void init() {
        mimageView= (ImageView) findViewById(R.id.back);
        mimageView.setOnClickListener(this);

        Intent intent=getIntent();
        mtextView= (TextView) findViewById(R.id.title_view);
        mtextView.setText(intent.getStringExtra("title"));


        mbutton= (Button) findViewById(R.id.btn_next);
        mbutton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.btn_next:
                Intent intent=new Intent(mContext,SmartLinkActivity.class);
                intent.putExtra("title","智能联机网络连接");
                startActivity(intent);
                break;
        }
    }
}
