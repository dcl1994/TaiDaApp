package com.siyann.taidaapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import utils.LogUtil;

/**
 * 修改昵称界面
 */
public class NicknameActivity extends Activity implements View.OnClickListener{
    private TextView mtextView;
    private ImageView mimageView;
    private Context mContext;
    private EditText editText;
    private Button save;

    String equipmentname="";    //昵称

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nickname);
        mContext=this;
        init();
    }

    /**
     * 初始化UI界面
     */
    private void init() {
        Intent intent=getIntent();
        mtextView= (TextView) findViewById(R.id.title_view);
        mtextView.setText(intent.getStringExtra("title"));

        mimageView= (ImageView) findViewById(R.id.back);
        mimageView.setOnClickListener(this);

        editText= (EditText) findViewById(R.id.nickname_editext);
        editText.setFilters(new InputFilter[]{inputFilter});

        save= (Button) findViewById(R.id.save);
        save.setOnClickListener(this);
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
           case R.id.save:  //保存
               equipmentname=editText.getText().toString();
               if (TextUtils.isEmpty(equipmentname)){
                   Toast.makeText(mContext,"昵称不能为空",Toast.LENGTH_SHORT).show();
               }else{
                   SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();
                   editor.putString("equipmentname",equipmentname);
                   editor.apply();
                   LogUtil.e("equipmentname",equipmentname);

                   Intent intent=new Intent(mContext,EquipmentActivity.class);
                   startActivity(intent);
                   finish();
               }
               break;
       }
    }


    /**
     * 不能输入回车和换行
     */
    private InputFilter inputFilter=new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source.equals(" ")||source.toString().contentEquals("\n")) return "";
             else return null;
        }
    };
}
