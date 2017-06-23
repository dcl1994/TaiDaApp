package com.siyann.taidaapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mediatek.elian.ElianNative;

import java.util.List;

import utils.LogUtil;

/**
 * 智能联机中的WiFi设置页面
 */
public class SmartLinkActivity extends AppCompatActivity implements View.OnClickListener{
    private Context context;
    private ImageView mimageView;
    private TextView mtextView;
    private Button mbutton;
    private TextView wifiname;  //WiFi名称
    private EditText wifipwd;   //WiFi密码

    private LinearLayout mlayout;
    private LinearLayout mlayout_link;

    private TextView tx_receive; //监听的消息
    ElianNative elain;
    String ssid;
    String pwd="";
    boolean isRegFilter=false;
    boolean is5GWifi=false;
    boolean isWifiEncrypt=false;

    public UDPHelper mHelper;
    WifiManager wifiManager;
    boolean isSend=false;   //是否发送

    static {
        System.loadLibrary("elianjni");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart);
        context=SmartLinkActivity.this;
        init();
        currenWifi();
        regFilter();

        //监听UDP广播
        mHelper = new UDPHelper(context,9988);

        listen();
        mHelper.StartListen();
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

        mbutton= (Button) findViewById(R.id.btn_send);
        mbutton.setOnClickListener(this);

        wifiname= (TextView) findViewById(R.id.wifiname);

        wifipwd= (EditText) findViewById(R.id.wifipwd);

        tx_receive=(TextView)findViewById(R.id.tx_receive);

        pwd=wifipwd.getText().toString();

        mlayout= (LinearLayout) findViewById(R.id.smartline_linear);
        mlayout_link= (LinearLayout) findViewById(R.id.smartlink_linear);
    }

    public void regFilter(){
        IntentFilter filter=new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(br, filter);
        isRegFilter=true;
    }

    BroadcastReceiver br=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")){
                currenWifi();
            }
        }
    };


    //开始发包
    private void sendWifi(){
        if (elain == null) {
            elain = new ElianNative();
        }
        if (null != ssid && !"".equals(ssid)) {
            elain.InitSmartConnection(null, 1, 1);
            //wifi名  wifi密码  加密方式
            elain.StartSmartConnection(ssid, pwd, "", mAuthMode);
        }
    }

    //停止发包
    private void stopSendWifi() {
        if (elain != null) {
            elain.StopSmartConnection();
        }
    }

    void listen() {
        mHelper.setCallBack(new Handler() {

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                switch (msg.what) {
                    case UDPHelper.HANDLER_MESSAGE_BIND_ERROR:
                        Log.e("my", "HANDLER_MESSAGE_BIND_ERROR");
                        Toast.makeText(context, "处理消息绑定失误", Toast.LENGTH_SHORT).show();
                        break;
                    case UDPHelper.HANDLER_MESSAGE_RECEIVE_MSG:
                        Bundle bundle = msg.getData();
                        String deviceId = bundle.getString("contactId");//设备ID
                        String frag = bundle.getString("frag");//有无密码标记
                        String ip = bundle.getString("ip");//id地址
                        int type = bundle.getInt("type", 0);//设备主类型
                        int subType = bundle.getInt("subType", 0);//设备子类型
                        String deviceInfo="deviceId="+deviceId+" deviceType="+type+" subType="+subType+" ip="+ip;
                        if (Integer.parseInt(frag) == 0) {
                            deviceInfo=deviceInfo+"无密码";
                        } else {
                            deviceInfo=deviceInfo+"有密码";
                        }
                        tx_receive.append(deviceInfo + "\n\n");

                        LogUtil.e("deviceInfo", deviceInfo);
                        Intent intent=new Intent(context,AddEquipmentActivity.class);
                        intent.putExtra("deviceId",deviceId);
                        startActivity(intent);
                        finish();
                        break;
                }
            }

        });
    }



    private byte mAuthMode;
    private byte AuthModeAutoSwitch = 2;
    private byte AuthModeOpen = 0;
    private byte AuthModeShared = 1;
    private byte AuthModeWPA = 3;
    private byte AuthModeWPA1PSKWPA2PSK = 9;
    private byte AuthModeWPA1WPA2 = 8;
    private byte AuthModeWPA2 = 6;
    private byte AuthModeWPA2PSK = 7;
    private byte AuthModeWPANone = 5;
    private byte AuthModeWPAPSK = 4;

    //获取当前连接wifi
    public void currenWifi(){
        WifiInfo wifiInfo = getConnectWifiInfo();
        if (wifiInfo == null) {
            ssid="";
            wifiname.setText("请先连接wifi");
            return;
        }
        ssid = wifiInfo.getSSID();
        if (ssid == null) {
            return;
        }
        if (ssid.equals("")) {
            return;
        }
        if (ssid.length() <= 0) {
            return;
        }
        int a = ssid.charAt(0);
        if (a == 34) {
            ssid = ssid.substring(1, ssid.length() - 1);
        }
        if (!ssid.equals("<unknown ssid>") && !ssid.equals("0x")) {
            wifiname.setText(ssid);
        }
        List<ScanResult> wifiList = getLists(context);
        if (wifiList == null) {
            return;
        }
        for (int i = 0; i < wifiList.size(); i++) {
            ScanResult result = wifiList.get(i);
            if (!result.SSID.equals(ssid)) {
                continue;
            }
            is5GWifi=is5GWifi(result.frequency);
            isWifiEncrypt=isWifiEncrypt(result);
            boolean bool1, bool2, bool3, bool4;
            bool1 = result.capabilities.contains("WPA-PSK");
            bool2 = result.capabilities.contains("WPA2-PSK");
            bool3 = result.capabilities.contains("WPA-EAP");
            bool4 = result.capabilities.contains("WPA2-EAP");
            if (result.capabilities.contains("WEP")) {
                this.mAuthMode = this.AuthModeOpen;
            }
            if ((bool1) && (bool2)) {
                mAuthMode = AuthModeWPA1PSKWPA2PSK;
            } else if (bool2) {
                this.mAuthMode = this.AuthModeWPA2PSK;
            } else if (bool1) {
                this.mAuthMode = this.AuthModeWPAPSK;
            } else if ((bool3) && (bool4)) {
                this.mAuthMode = this.AuthModeWPA1WPA2;
            } else if (bool4) {
                this.mAuthMode = this.AuthModeWPA2;
            } else {
                if (!bool3)
                    break;
                this.mAuthMode = this.AuthModeWPA;
            }
        }
    }


    //判断是否连接上wifi
    public  boolean isWifiConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(wifiNetworkInfo.isConnected()){
            return true ;
        }
        return false ;
    }

    //获取当前连接wifi的WifiInfo
    public WifiInfo getConnectWifiInfo(){
        if(!isWifiConnected()){
            return null;
        }
        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        if(wifiManager==null){
            return null;
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo;
    }

    //获取wifi列表
    public List<ScanResult> getLists(Context context) {
        wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        wifiManager.startScan();
        List<ScanResult> lists = wifiManager.getScanResults();
        return lists;
    }

    //判断是不是5Gwifi
    public static boolean is5GWifi(int frequency){
        String str=String.valueOf(frequency);
        if(str.length()>0){
            char a=str.charAt(0);
            if(a=='5'){
                return true;
            }
        }
        return false;
    }

    //WiFi是否加密
    public static boolean isWifiEncrypt(ScanResult result) {
        return !(result.capabilities.toLowerCase().indexOf("wep") != -1
                || result.capabilities.toLowerCase().indexOf("wpa") != -1);
    }

    /**
     * 智能联机
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
            case R.id.btn_send:
                pwd=wifipwd.getText().toString().trim();
                LogUtil.e("pwd", pwd);
                if (!isWifiConnected()||ssid == null || ssid.equals("")||ssid.equals("<unknown ssid>")) {
                    Toast.makeText(context,"请先将手机连接到WiFi",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(is5GWifi){
                    Toast.makeText(context,"设备不支持5G网络",Toast.LENGTH_SHORT).show();
                    return;
                }
                /**
                 * 检查WiFi是否加密
                 */
                if (!isWifiEncrypt) {
                }
                if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(context,"请输入WiFi密码",Toast.LENGTH_SHORT).show();
                    return;
                }
                mlayout.setVisibility(View.GONE);   //隐藏输入框
                mlayout_link.setVisibility(View.VISIBLE);//显示连接设备的页面

                sendWifi();

                isSend=true;
                tx_receive.append("开始发包......\n");
                break;
            default:
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isSend){
            stopSendWifi();
            isSend=false;
        }
        if(isRegFilter){
            unregisterReceiver(br);
            isRegFilter=false;
        }
        if(mHelper!=null){
            mHelper.StopListen();
        }
    }


    //重写返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            finish();
        }
        return true;
    }
}
