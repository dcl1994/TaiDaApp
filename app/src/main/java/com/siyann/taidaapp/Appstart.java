package com.siyann.taidaapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import utils.MyApplication;

/**
 * App启动页，判断用户是否是第一次登录，是的话就进入主界面，否则进入loginactivity
 */

public class Appstart extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = View.inflate(this, R.layout.activity_appstart, null);
        setContentView(view);

        /**
         * 透明度从0.2-1.0，时间为2秒
         */
        AlphaAnimation animation = new AlphaAnimation(0.2f, 1.0f);
        animation.setDuration(2000);
        view.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            /**
             * 渐变动画结束时调用redirectTo方法，跳转到登录界面
             */
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                redirectTo();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    /**
     * 重写onstart方法，判断用户是否登录过
     */
    @Override
    protected void onStart() {
        SharedPreferences pref=getSharedPreferences("data",Activity.MODE_PRIVATE);
        /**获取缓存文件中的电话号码，如果为空就跳转到登录界面，否则就跳转到主界面 */
        if (!TextUtils.isEmpty(pref.getString("username",""))){
            Intent intent=new Intent(MyApplication.getContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }
        super.onStart();
    }

    /**
     * 渐变动画结束时跳转到登录界面
     */
    private void redirectTo() {
        Intent intent = new Intent(MyApplication.getContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
