package com.siyann.taidaapp;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.p2p.core.BaseMonitorActivity;
import com.p2p.core.P2PHandler;
import com.p2p.core.P2PValue;
import com.p2p.core.P2PView;

import java.io.File;

import utils.LogUtil;
import widget.Equipment;

/**
 * 监控的界面,登录成功后才可以进入
 */
public class MonitoerActivity extends BaseMonitorActivity implements View.OnClickListener{

    public static String P2P_ACCEPT = "com.XXX.P2P_ACCEPT";

    public static String P2P_READY = "com.XXX.P2P_READY";

    public static String P2P_REJECT = "com.XXX.P2P_REJECT";

    private Context mContext;

    private ImageView mImageView;   //返回按钮

    private RelativeLayout rlP2pview;

    private String equipmentNickname=""; //设备昵称

    private String equipmentid ="";      //设备ID

    private String equipmentpwd ="";     //设备密码

    private ImageView volume;            //静音按键

    private ImageView btnTalk;         //视频对讲

    private String LoginID;             //登录ID

    private int screenWidth, screenHeigh;

    private TextView tvContent;

    private LinearLayout l_control;

    private RelativeLayout mrelative;   //标题栏

    private RelativeLayout control_bottom;  //被隐藏的底部栏

    private ProgressBar progressBar;    //加载控件

    private TextView mtextView;         //login。。。

    private ImageView send_voice;

    private ImageView iv_half_screen;   //底部菜单栏的按钮

    private Button mvideo;              //视频清晰度切换按钮

    private LinearLayout control_top;   //视频清晰度列表

    private TextView video_mode_hd;     //高清

    private TextView video_mode_sd;     //标清

    private TextView video_mode_ld;     //流畅

    private ImageView locking;             //锁定

    private ImageView fullscreenimg;    //横竖屏切换按钮

    private ImageView screenshot;       //截图键


    private boolean mIsLand = false;    //横竖屏切换

    private boolean isMute = false;     //声音

    private boolean ischeck = false;    //清晰度切换的标志

    private boolean islocking = false;    //锁定设备的按钮

    private ImageView close_voice;      //静音键


    public  static boolean linesuccess=false; // 连接设备成功

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoer);
        mContext = MonitoerActivity.this;
        init();
        initUI();
        getScreenWithHeigh();
        regFilter();
        CallOnClick();  //连接设备
    }

    /**
     * 过一段时间再进来的时候会自动挂断视频连接
     */
    @Override
    protected void onPause() {
        super.onPause();
        P2PHandler.getInstance().reject();
        Toast.makeText(mContext, "挂断", Toast.LENGTH_SHORT).show();
    }

    private void init() {
        /**
         * 底部栏的静音键
         */
        close_voice = (ImageView) findViewById(R.id.close_voice);
        close_voice.setOnClickListener(this);
        /**
         * 截图按键
         */
        screenshot = (ImageView) findViewById(R.id.iv_screenshot);
        screenshot.setOnClickListener(this);

        /**
         * 锁定设备的按键
         */
        locking = (ImageView) findViewById(R.id.iv_defence);
        locking.setOnClickListener(this);

        control_top = (LinearLayout) findViewById(R.id.control_top);
        /**
         * 视频切换清晰度按钮
         */
        mvideo = (Button) findViewById(R.id.choose_video_format);
        mvideo.setOnClickListener(this);


        /**
         * 调整视频清晰度
         */
        video_mode_hd = (TextView) findViewById(R.id.video_mode_hd);
        video_mode_hd.setOnClickListener(this);

        video_mode_sd = (TextView) findViewById(R.id.video_mode_sd);
        video_mode_sd.setOnClickListener(this);

        video_mode_ld = (TextView) findViewById(R.id.video_mode_ld);
        video_mode_ld.setOnClickListener(this);
        /**
         * 底部菜单栏的按钮
         */
        iv_half_screen = (ImageView) findViewById(R.id.iv_half_screen);
        iv_half_screen.setOnClickListener(this);

        progressBar = (ProgressBar) findViewById(R.id.prg_monitor);
        mtextView = (TextView) findViewById(R.id.tx_wait_for_connect);

        control_bottom = (RelativeLayout) findViewById(R.id.control_bottom);

        l_control = (LinearLayout) findViewById(R.id.l_control);

        mrelative = (RelativeLayout) findViewById(R.id.relative_title);
        /**
         * 横竖屏切换按钮
         */
        fullscreenimg = (ImageView) findViewById(R.id.iv_full_screen);
        fullscreenimg.setOnClickListener(this);

        rlP2pview = (RelativeLayout) findViewById(R.id.r_p2pview);  //p2pview的父控件

        mImageView = (ImageView) findViewById(R.id.back);

        mImageView.setOnClickListener(this);

        btnTalk = (ImageView) findViewById(R.id.iv_speak);


        /**
         * 静音
         */
        volume = (ImageView) findViewById(R.id.iv_voice);
        volume.setOnClickListener(this);

        close_voice = (ImageView) findViewById(R.id.close_voice);
        close_voice.setOnClickListener(this);
        send_voice = (ImageView) findViewById(R.id.send_voice);
        send_voice.setOnClickListener(this);

        /**
         * 获取adapter中传递过来的值
         */
        Intent intent = getIntent();
        if (intent !=null) {
            equipmentNickname=intent.getStringExtra("nickname");
            equipmentid = intent.getStringExtra("id");
            equipmentpwd = intent.getStringExtra("pwd");

            LogUtil.e("Oncreateid",equipmentid);
            LogUtil.e("Oncreatepwd",equipmentpwd);
            LogUtil.e("nickname",equipmentNickname);
        }
    }
    private void getScreenWithHeigh() {
        DisplayMetrics dm = new DisplayMetrics();
        // 获取屏幕信息
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeigh = dm.heightPixels;
    }


    private void initUI() {
        //pView已在父类声明，不要在子类重复
        pView = (P2PView) findViewById(R.id.p2pview);
        initP2PView(7, P2PView.LAYOUTTYPE_TOGGEDER);//7是设备类型(技威定义的)
        btnTalk.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        setMute(false);

                        return true;
                    case MotionEvent.ACTION_UP:

                        setMute(true);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e("dxsTest", "config:" + newConfig.orientation);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setHalfScreen(false);
            l_control.setVisibility(View.GONE);
            //以下代码是因为 方案商设备类型很多,视频比例也比较多
            //客户更具自己的视频比例调整画布大小
            //这里的实现比较绕,如果能弄清楚这部分原理,客户可自行修改此处代码
            if (P2PView.type == 1) {
                if (P2PView.scale == 0) {
                    isFullScreen = false;
                    pView.halfScreen();//刷新画布比例
                } else {
                    isFullScreen = true;
                    pView.fullScreen();
                }
            } else {
                //这里本应该用设备类型判断,如果只有一种类型可不用这么麻烦
                if (7 == P2PValue.DeviceType.NPC) {
                    isFullScreen = false;
                    pView.halfScreen();
                } else {
                    isFullScreen = true;
                    pView.fullScreen();
                }
            }
            LinearLayout.LayoutParams parames = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            rlP2pview.setLayoutParams(parames);//调整画布容器宽高(比例)
        } else {
            setHalfScreen(true);
            l_control.setVisibility(View.VISIBLE);
            if (isFullScreen) {
                isFullScreen = false;
                pView.halfScreen();
            }
            //这里简写,只考虑了16:9的画面类型  大部分设备画面比例是这种
            int Heigh = screenWidth * 9 / 16;
            LinearLayout.LayoutParams parames = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            parames.height = Heigh;
            rlP2pview.setLayoutParams(parames);
        }
    }

    private void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(P2P_REJECT);
        filter.addAction(P2P_ACCEPT);
        filter.addAction(P2P_READY);
        registerReceiver(mReceiver, filter);
    }

    public BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(P2P_ACCEPT)) {
                int[] type = intent.getIntArrayExtra("type");
                P2PView.type = type[0];
                P2PView.scale = type[1];
                //  tvContent.append("\n 监控数据接收");
                LogUtil.e("dxsTest", "监控数据接收:");
                P2PHandler.getInstance().openAudioAndStartPlaying(1);//打开音频并准备播放，calllType与call时type一致

            } else if (intent.getAction().equals(P2P_READY)) {
                //tvContent.append("\n 监控准备,开始监控");
                LogUtil.e("dxsTest", "监控准备,开始监控" + equipmentid);
                /**
                 * 隐藏progressbar和login
                 */
                progressBar.setVisibility(View.GONE);
                mtextView.setVisibility(View.GONE);

                pView.sendStartBrod();
                /**
                 * 监控成功设置为
                 */
                linesuccess=true;

            } else if (intent.getAction().equals(P2P_REJECT)) {
                // tvContent.append("\n 监控挂断");
                LogUtil.e("dxsTest", "监控挂断" + equipmentid);
            }
        }
    };

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);

        /**
         * 挂断
         */
        P2PHandler.getInstance().reject();
        super.onDestroy();
    }

    @Override
    protected void onCaptureScreenResult(boolean isSuccess, int prePoint) {
        if (isSuccess) {
            Toast.makeText(mContext, "截图成功", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onVideoPTS(long videoPTS) {
    }
    @Override
    protected void onP2PViewSingleTap() {

    }
    @Override
    protected void onP2PViewFilling() {

    }
    /**
     * 连接摄像头的方法
     */
    private void CallOnClick() {
        SharedPreferences pref=getSharedPreferences("data", MODE_PRIVATE);
        LoginID = pref.getString("LoginID", "");
        LogUtil.e("LoginID", LoginID);


        String pwd = P2PHandler.getInstance().EntryPassword(equipmentpwd);//经过转换后的设备密码
        LogUtil.e("pwd",pwd);
        LogUtil.e("equipmentid",""+equipmentid);
        P2PHandler.getInstance().call(LoginID, pwd, true, 1, equipmentid, "", "", 2, equipmentid);
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                if (linesuccess){
                    /**
                     * 调用截图的方法
                     */
                    ScreenShotClock();
                    P2PHandler.getInstance().reject();
                    Toast.makeText(mContext, "挂断", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    P2PHandler.getInstance().reject();
                    Toast.makeText(mContext, "挂断", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            /**
             * 静音
             */
            case R.id.iv_voice:
                changeMuteState();
                break;
            /**
             * 横竖屏切换
             */
            case R.id.iv_full_screen:
                initData();
                break;
            /**
             * 全屏底部菜单栏的全屏显示按钮的点击事件
             */
            case R.id.iv_half_screen:
                initData();
                break;

            /**
             * 清晰度切换的点击事件
             */
            case R.id.choose_video_format:
                ischeck = !ischeck;
                if (ischeck) {
                    control_top.setVisibility(View.VISIBLE);
                } else {
                    control_top.setVisibility(View.GONE);
                }
                break;
            /**
             * 布防的点击事件
             */
            case R.id.iv_defence:
                islocking = !islocking;
                if (islocking) {
                    locking.setImageResource(R.drawable.selector_portrait_arm);
                    Toast.makeText(v.getContext(), "布防", Toast.LENGTH_SHORT).show();
                    P2PHandler.getInstance().setRemoteDefence(equipmentid, equipmentpwd, 1);
                } else {
                    locking.setImageResource(R.drawable.selector_portrait_disarm);
                    Toast.makeText(v.getContext(), "撤防", Toast.LENGTH_SHORT).show();
                    P2PHandler.getInstance().setRemoteDefence(equipmentid, equipmentpwd, 0);
                }
                break;

            /**
             * 截图的点击事件
             */
            case R.id.iv_screenshot:
                if (ContextCompat.checkSelfPermission
                        (MonitoerActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MonitoerActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }else {
                    ScreenShotClock();
                }
                break;
            /**
             * 高清
             */
            case R.id.video_mode_hd:
                HDOnClick();
                video_mode_hd.setTextColor(ContextCompat.getColor(this, R.color.blue));
                video_mode_sd.setTextColor(ContextCompat.getColor(this, R.color.white));
                video_mode_ld.setTextColor(ContextCompat.getColor(this, R.color.white));
                mvideo.setText("高清");
                control_top.setVisibility(View.GONE);
                break;

            /**
             * 标清
             */
            case R.id.video_mode_sd:
                SDOnclick();
                video_mode_sd.setTextColor(ContextCompat.getColor(this, R.color.blue));
                video_mode_ld.setTextColor(ContextCompat.getColor(this, R.color.white));
                video_mode_hd.setTextColor(ContextCompat.getColor(this, R.color.white));
                mvideo.setText("标清");
                control_top.setVisibility(View.GONE);
                break;

            /**
             * 流畅
             */
            case R.id.video_mode_ld:
                LDOnClick();
                video_mode_ld.setTextColor(ContextCompat.getColor(this, R.color.blue));
                video_mode_sd.setTextColor(ContextCompat.getColor(this, R.color.white));
                video_mode_hd.setTextColor(ContextCompat.getColor(this, R.color.white));
                mvideo.setText("流畅");
                control_top.setVisibility(View.GONE);
                break;

            /**
             * 底部栏的静音键的点击事件
             */
            case R.id.close_voice:
                changeMuteState();
                break;
            default:
                break;
        }
    }

    /**
     * 流畅
     */
    void LDOnClick() {
        P2PHandler.getInstance().setVideoMode(P2PValue.VideoMode.VIDEO_MODE_LD);
    }

    /**
     * 标清
     */
    void SDOnclick() {
        P2PHandler.getInstance().setVideoMode(P2PValue.VideoMode.VIDEO_MODE_SD);
    }

    /**
     * 高清
     */
    void HDOnClick() {
        P2PHandler.getInstance().setVideoMode(P2PValue.VideoMode.VIDEO_MODE_HD);
    }


    /**
     * 自定义截图路径
     */
    void ScreenShotClock() {
        // 参数是一个标记,截图回调会原样返回这个标记
        //注意SD卡权限
        File file = Environment.getExternalStorageDirectory();
        long time = System.currentTimeMillis();
        int d = P2PHandler.getInstance().setScreenShotpath(file + "/sdcard/11/22/33", time+".jpg");
        LogUtil.e("ddddd",d+"");
        /**
         * 如果创建路径成功的话就调用截图的方法
         */
        if (d==0){
            String imagepath=file + "/sdcard/11/22/33/"+time+".jpg";
            Equipment equipment=new Equipment();
            equipment.setImagepath(imagepath);
            equipment.updateAll("equipid=?",equipmentid);

            captureScreen(-1); //普通截图
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    ScreenShotClock();
                }else {
                    Toast.makeText(mContext,"没有此权限",Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }




    /**
     * 设置静音的方法
     */
    private void changeMuteState() {
        isMute = !isMute;
        AudioManager manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (manager != null) {
            if (isMute) {
                manager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                volume.setImageResource(R.drawable.selector_half_screen_voice_close);
                close_voice.setImageResource(R.drawable.m_voice_off);
            } else {
                manager.setStreamVolume(AudioManager.STREAM_MUSIC, manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
                volume.setImageResource(R.drawable.selector_half_screen_voice_open);
                close_voice.setImageResource(R.drawable.m_voice_on);
            }
        }
    }

    /**
     * 横竖屏切换的方法
     *
     * @return
     */
    private void initData() {
        mIsLand = !mIsLand;
        if (mIsLand) {
            //设置竖屏
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            mrelative.setVisibility(View.GONE);
            control_bottom.setVisibility(View.VISIBLE);
        } else {
            this.getWindow().setFlags(0, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            // 设置横屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            mrelative.setVisibility(View.VISIBLE);
            control_bottom.setVisibility(View.GONE);
            control_top.setVisibility(View.GONE);
        }
    }


    @Override
    public int getActivityInfo() {
        return 0;
    }

    @Override
    protected void onGoBack() {

    }

    @Override
    protected void onGoFront() {

    }

    @Override
    protected void onExit() {

    }


    /**
     * 重写返回键
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
            ScreenShotClock();
            P2PHandler.getInstance().reject();
            Toast.makeText(mContext, "挂断", Toast.LENGTH_SHORT).show();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
