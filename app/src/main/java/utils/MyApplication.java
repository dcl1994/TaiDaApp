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

    public final static String APPID="7918db6b2489d857c16e5a21407e0440";
    public final static String APPToken="3cd52ab611da0b899b0ccc448cc494837bddec29e1c6c9a748bdfdda0b9ec4d5";

    //前两位是客户APP唯一编号(00.00 由技威分配),后两位是APP版本号(客户自定义),此参数不可省略
    public final static String APPVersion="04.39.00.01";

    /**
     * mob的appkey和appscrent
     */
    public final static String MobAPPID="1e9d218c52438";
    public final static String MobSecret="bc3e43e6b7bc96d1c7d16c77da8e2105";


    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        app = this;
        initP2P(app);




//        /**
//         * 搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
//         *
//         */
//        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
//
//            @Override
//            public void onViewInitFinished(boolean arg0) {
//                // TODO Auto-generated method stub
//                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
//                LogUtil.d("app", " onViewInitFinished is " + arg0);
//            }
//
//            @Override
//            public void onCoreInitFinished() {
//                // TODO Auto-generated method stub
//            }
//        };
//        //x5内核初始化接口
//        QbSdk.initX5Environment(getApplicationContext(), cb);


    }
    private void initP2P(MyApplication app) {
        //ID、TOKEN与APPVersion需要在服务器申请登记
        P2PSpecial.getInstance().init(app,APPID,APPToken,APPVersion);
    }

    public  static Context getContext(){
        return context;
    }

}
