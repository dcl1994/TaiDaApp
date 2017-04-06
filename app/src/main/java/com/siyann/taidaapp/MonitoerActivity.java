package com.siyann.taidaapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.p2p.core.BaseMonitorActivity;
import com.p2p.core.P2PHandler;
import com.p2p.core.P2PValue;
import com.p2p.core.P2PView;

import utils.LogUtil;

/**
 * 监控的界面,登录成功后才可以进入
 */
public class MonitoerActivity extends BaseMonitorActivity implements View.OnClickListener {
    public static String P2P_ACCEPT = "com.XXX.P2P_ACCEPT";
    public static String P2P_READY = "com.XXX.P2P_READY";
    public static String P2P_REJECT = "com.XXX.P2P_REJECT";

    private Context mContext;
    private ImageView mImageView;   //返回按钮
    private TextView mTextView;     //标题

    private RelativeLayout rlP2pview;

    private String equipmentname="";    //设备昵称
    private String equipmentid="";      //设备ID
    private String equipmentpwd="";     //设备密码


    private ImageView volume;   //声音


    private ImageView btnTalk;         //视频对讲
    private String LoginID;             //登录ID
    private int screenWidth, screenHeigh;
    private TextView tvContent;
    private LinearLayout layoutElse;

    SharedPreferences sharedPreferences;

    OrientationEventListener mOrientationEventListener;

    private boolean mIsLand = false;

    private Button call_btn;    //连接

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoer);
        mContext=MonitoerActivity.this;
        init();
        initUI();
        getScreenWithHeigh();
        regFilter();
        initData();

        CallOnClick();  //连接设备

    }
    private void init() {

        /**
         * 连接设备
         */
        call_btn= (Button) findViewById(R.id.Call);
        call_btn.setVisibility(View.VISIBLE);
        call_btn.setOnClickListener(this);


        rlP2pview= (RelativeLayout) findViewById(R.id.rl_p2pview);
        /**
         * 从缓存中取出设备的昵称，ID，密码
         */
        sharedPreferences=getSharedPreferences("data", MODE_PRIVATE);


        equipmentname=sharedPreferences.getString("equipmentname", "");
        LogUtil.e("equipmentname",equipmentname);

        equipmentpwd=sharedPreferences.getString("equipmentpwd","");


        mImageView= (ImageView) findViewById(R.id.back);
        mImageView.setOnClickListener(this);

        btnTalk= (ImageView) findViewById(R.id.iv_speak);

        mTextView= (TextView) findViewById(R.id.title_view);
        Intent intent=getIntent();
        String title= intent.getStringExtra("title");
        mTextView.setText(title);

        tvContent= (TextView) findViewById(R.id.tv_content);


        layoutElse= (LinearLayout) findViewById(R.id.layout_else);


        /**
         * 静音
         */
        volume= (ImageView) findViewById(R.id.volume_img);
        volume.setOnClickListener(this);
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

    private void initData() {
        //此处是一种并不常见的横竖屏监听,客户可自行修改实现
        mOrientationEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int rotation) {
                // 设置横屏
                if (((rotation >= 0) && (rotation <= 30)) || (rotation >= 330)) {
                    if (mIsLand) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        mIsLand = false;
                        setHalfScreen(true);
                    }
                } else if (((rotation >= 230) && (rotation <= 310))) {
                    if (!mIsLand) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        mIsLand = true;
                        setHalfScreen(false);
                    }
                }
            }
        };
        mOrientationEventListener.enable();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e("dxsTest", "config:" + newConfig.orientation);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setHalfScreen(false);
            layoutElse.setVisibility(View.GONE);
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
            layoutElse.setVisibility(View.VISIBLE);
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
                tvContent.append("\n 监控数据接收");
                LogUtil.e("dxsTest", "监控数据接收:" + equipmentid);
                P2PHandler.getInstance().openAudioAndStartPlaying(1);//打开音频并准备播放，calllType与call时type一致
            } else if (intent.getAction().equals(P2P_READY)) {
                tvContent.append("\n 监控准备,开始监控");
                LogUtil.e("dxsTest", "监控准备,开始监控" + equipmentid);
                pView.sendStartBrod();
            } else if (intent.getAction().equals(P2P_REJECT)) {
                tvContent.append("\n 监控挂断");
            }
        }
    };


    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        P2PHandler.getInstance().reject();
        super.onDestroy();

    }

    @Override
    protected void onCaptureScreenResult(boolean isSuccess, int prePoint) {
        if (isSuccess) {
            Toast.makeText(mContext,"连接设备成功",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext,"连接设备失败",Toast.LENGTH_SHORT).show();
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

    //连设备的方法
    private void CallOnClick() {
        LoginID=sharedPreferences.getString("LoginID","");

        LogUtil.e("LoginID",LoginID);
        equipmentid=sharedPreferences.getString("equipmentid", "");

        LogUtil.e("equipmentpwd",equipmentpwd);
        LogUtil.e("equipmentid",equipmentid);

        String pwd = P2PHandler.getInstance().EntryPassword(equipmentpwd);//经过转换后的设备密码
        P2PHandler.getInstance().call(LoginID, pwd, true, 1, equipmentid, "", "", 2, equipmentid);
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

            /**
             * 连接设备
             */
            case R.id.Call:
                CallOnClick();
                break;

            /**
             * 静音
             */
            case R.id.volume_img:
            Toast.makeText(mContext,"静音",Toast.LENGTH_SHORT).show();
                break;

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

}
