package com.siyann.taidaapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 详情页，根据点击的RecyclerView项将字段名传递过来显示在title上
 *
 * 静态时暂时写死
 */
public class DetailsActivity extends Activity implements View.OnClickListener {
    private WebView mWebView;       //webview
    private Context mContext;
    private ImageView mImageView;   //返回按钮
    private TextView mTextView;     //标题
    private String url="";
    private String title="";        //传递过来的标题的值
    private FloatingActionButton floatingActionButton;  //悬浮按钮
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mContext=DetailsActivity.this;
        init();
    }
    /**
     * 初始化
     */
    private void init() {
        floatingActionButton= (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(this);
        mImageView= (ImageView) findViewById(R.id.back);
        mImageView.setOnClickListener(this);
        mWebView= (WebView) findViewById(R.id.webview_detail);

        mTextView= (TextView) findViewById(R.id.title_view);

        Intent intent=getIntent();
        url= intent.getStringExtra("url");
        title=intent.getStringExtra("title");
        mTextView.setText(title);   //设置标题
        mWebView.loadUrl(url);
        Log.e("title", title);
        Log.e("url",url);
       //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
            mWebView.setInitialScale(50);
            //支持javascript
            mWebView.getSettings().

            setJavaScriptEnabled(true);
            //设置可以支持缩放
            mWebView.getSettings().

            setSupportZoom(true);
            //设置出现缩放工具
            mWebView.getSettings().

            setBuiltInZoomControls(true);
            //设置扩大比例的缩放
            mWebView.getSettings().

            setUseWideViewPort(true);
//        //自适应屏幕
            mWebView.getSettings().

            setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

            mWebView.getSettings().

            setLoadWithOverviewMode(true);
            //优先使用缓存
            mWebView.getSettings().

            setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);


        }


        /**
        * 返回按钮的点击事件
        * @param v
        */
        @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.fab:
                finish();
                break;

        }
    }

    /**
     * 重写返回键
      */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            finish();
        }
        return true;
    }

}
