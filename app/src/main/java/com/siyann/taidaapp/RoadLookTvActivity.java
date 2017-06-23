package com.siyann.taidaapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.VideoView;
import widget.CustomMediaController;

/**
 * 路况直播界面
 */
public class RoadLookTvActivity extends Activity {
    @Bind(R.id.vitamio)
    VideoView vitamio;
    @Bind(R.id.probar)
    ProgressBar probar;

    @Bind(R.id.download_rate)
    TextView downloadRate;

    @Bind(R.id.load_rate)
    TextView loadRate;
    private String tv_url = "";   //直播地址
    private CustomMediaController mCustomMediaController;

    public static boolean isfullscreen = false; //全屏显示，默认为true

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //定义全屏参数
        final int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //获得当前窗体对象
        Window window = RoadLookTvActivity.this.getWindow();
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);
        //检查vitamio框架是否可用
        if (!LibsChecker.checkVitamioLibs(this)) {
            return;
        }
        setContentView(R.layout.activity_road_look_tv);
        ButterKnife.bind(this);
        /**
         * 设置名字
         * 点击传递过来
         */
        mCustomMediaController = new CustomMediaController(this, vitamio, this);
        mCustomMediaController.setVideoName("视频直播");
        mCustomMediaController.show(5000); //5s隐藏

        /**
         * 全屏的监听的回调方法
         */
        mCustomMediaController.setMediaListener(new CustomMediaController.MediaControllListener() {
            @Override
            public void fullscreen() {
                isfullscreen = !isfullscreen;
                if (isfullscreen) {
                    //设置横屏
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    // 这样mVideoView会自己充满全屏
                    vitamio.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, 0);
                    //隐藏导航栏
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    isfullscreen = true;
                } else {
                    //设置竖屏
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    //video设置竖屏显示
                    vitamio.setVideoLayout(VideoView.VIDEO_LAYOUT_ORIGIN, 0);
                }
            }
        });

        /**
         * 返回的监听的回调方法
         */
        mCustomMediaController.setFinishListener(new CustomMediaController.FinishListener() {
            @Override
            public void finishlistener() {
                isfullscreen=!isfullscreen;
                if (isfullscreen){
                    finish();
                    isfullscreen=false;
                }
                else{
                    //设置竖屏
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    //video设置竖屏显示
                    vitamio.setVideoLayout(VideoView.VIDEO_LAYOUT_ORIGIN,0);
                }

            }
        });

        init();
        if (Vitamio.isInitialized(this)) {

            vitamio.setVideoURI(Uri.parse(tv_url));

            vitamio.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);

            vitamio.setMediaController(mCustomMediaController);


            vitamio.setBufferSize(12040); //设置视频缓冲大小

            vitamio.requestFocus();

            vitamio.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setPlaybackSpeed(1.0f);
                }
            });
            vitamio.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    loadRate.setText(percent + "%");
                }
            });

            vitamio.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    switch (what) {
                        //开始缓冲
                        case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                            /**
                             * 如果是播放状态的话就变为暂停按钮
                             */
                            if (vitamio.isPlaying()) {
                                vitamio.pause();
                                probar.setVisibility(View.VISIBLE);
                                downloadRate.setText("");
                                loadRate.setText("");
                                downloadRate.setVisibility(View.VISIBLE);
                                loadRate.setVisibility(View.VISIBLE);
                            }
                            break;
                        //缓冲结束
                        case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                            vitamio.start();
                            probar.setVisibility(View.GONE);
                            downloadRate.setVisibility(View.GONE);
                            loadRate.setVisibility(View.INVISIBLE);
                            break;

                        //正在缓冲
                        case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                            downloadRate.setText("" + extra + "kb/s" + "");
                            break;
                        default:
                            break;
                    }
                    return true;
                }
            });
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            //屏幕切换时，设置全屏
            if (vitamio != null) {
                vitamio.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, 0);
            }
        }
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 初始化
     */
    private void init() {
        Intent intent = getIntent();
        tv_url = intent.getStringExtra("tv_url");
    }
    /**
     * 重写返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
            /**
             * 返回的监听的回调方法
             */
            isfullscreen=!isfullscreen;
            if (isfullscreen){
                finish();
                isfullscreen=false;
            } else{
                //设置竖屏
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                //video设置竖屏显示
                vitamio.setVideoLayout(VideoView.VIDEO_LAYOUT_ORIGIN,0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
