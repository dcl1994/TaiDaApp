package utils;

import android.app.Application;
import android.content.Context;

import com.p2p.core.P2PSpecial.P2PSpecial;

/**
 * 获取全局变量context的方法
 *
 */
public class MyApplication extends Application {
    private static Context context;

    public static MyApplication app;
    //这三个参数需要服务器分配(ID token与版本(在同一版本)是固定的,可硬编码在代码中。版本迭代时需要修改版本)
    //连接时需要传入，下面的数据仅供测试使用

    public final static String APPID="7918db6b2489d857c16e5a21407e0440";
    public final static String APPToken="3cd52ab611da0b899b0ccc448cc494837bddec29e1c6c9a748bdfdda0b9ec4d5";

    //前两位是客户APP唯一编号(00.00 由技威分配),后两位是APP版本号(客户自定义),此参数不可省略
    public final static String APPVersion="04.39.00.01";


    @Override
    public void onCreate() {
        context=getApplicationContext();
        app = this;
        initP2P(app);

    }
    private void initP2P(MyApplication app) {
        //ID、TOKEN与APPVersion需要在服务器申请登记
        P2PSpecial.getInstance().init(app,APPID,APPToken,APPVersion);
    }

    public  static Context getContext(){
        return context;
    }

}
