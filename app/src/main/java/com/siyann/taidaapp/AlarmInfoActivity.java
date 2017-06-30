package com.siyann.taidaapp;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utils.LogUtil;


/**
 * 报警详情界面
 */
public class AlarmInfoActivity extends AppCompatActivity {

    @Bind(R.id.sound_img)   //声音
            ImageView soundImg;
    @Bind(R.id.alarm_gif)   //报警动画
            ImageView alarmGif;
    @Bind(R.id.alarm_finish) //打开视频播放
            ImageView alarmFinish;
    @Bind(R.id.alarm_equname)   //设置名称
    TextView alarmEquname;

    private Context mContext;

    private String id="";   //设备ID
    private String pwd="";  //设备密码
    private String nickname=""; //设备名称

    private boolean isMute = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_info);
        ButterKnife.bind(this);
        mContext = this;

        /**
         * 报警图片闪动
         */
        Glide.with(mContext).load(R.drawable.alarm).into(alarmGif);

        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        pwd=intent.getStringExtra("pwd");
        nickname=intent.getStringExtra("name");

        LogUtil.e("alarm_id",id);
        LogUtil.e("alarm_pwd",pwd);
        LogUtil.e("alarm_nickname",nickname);
        /**
         * 设置机房名称
         */
        alarmEquname.setText(nickname);
    }

    @OnClick({R.id.sound_img,R.id.alarm_finish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sound_img:    //关闭蜂鸣器
                /**
                 * 点击关闭蜂鸣器
                 */
                changeMuteState();
                break;
            case R.id.alarm_finish: //跳转到视频播放界面
                Intent intent = new Intent(mContext, MonitoerActivity.class);

                intent.putExtra("id",id);
                intent.putExtra("pwd",pwd);
                intent.putExtra("nickname",nickname);

                LogUtil.e("id", id);
                LogUtil.e("pwd", pwd);
                LogUtil.e("nickname",nickname);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
    /**
     * 关闭蜂鸣器
     */
    private void changeMuteState() {
        isMute = !isMute;
        AudioManager manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (manager != null) {
            if (isMute) {
                /**
                 * 静音
                 */
                manager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                soundImg.setImageResource(R.drawable.btn_call_sound_out_s);
            } else {
                manager.setStreamVolume(AudioManager.STREAM_MUSIC, manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
                soundImg.setImageResource(R.drawable.btn_call_sound_out);
            }
        }
    }
    /**
     * 重写返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
