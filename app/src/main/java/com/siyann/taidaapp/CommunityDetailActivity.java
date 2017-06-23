package com.siyann.taidaapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utils.LogUtil;

/**
 * 社区新闻详情页
 */
public class CommunityDetailActivity extends Activity {

    String content = "";
    @Bind(R.id.textview_text)
    TextView textviewText;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.relative_title)
    RelativeLayout relativeTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        titleView.setText(intent.getStringExtra("title"));
        content = intent.getStringExtra("content");
        content = content.replace("&lt;p&gt;", "")
                .replace("&lt;/p&gt", "")
                .replace("&amp;hellip;", "")
                .replace("&lt;br /&gt;","")
                .replace("&amp;nbsp;;","")
                .replace("&amp;ldquo","")
                .replace("&amp;rdquo","")
                .replace("&lt;br/&gt;","")
                .replace(";","");
        LogUtil.e("content", content.length() + "");

        /**
         * 正则表达式来判断图片地址
         */
        isimage(content);
        textviewText.setText(content);
    }

    public static boolean isimage(String content) {
        LogUtil.e("content", content);
        Pattern p = Pattern.compile("img src=\".*?\"");
        Matcher m = p.matcher(content);
        boolean b = m.matches();
        LogUtil.e("aaaa", b + "");
        return m.matches();
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }
    /**
     * 重写返回键
     */

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
