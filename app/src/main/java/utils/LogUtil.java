package utils;

import android.util.Log;

/**
 * 打印日志的工具类
 */
public class LogUtil {
    public static final int VERBOSE=1;

    public static final int DEBUG=2;

    public static final int INFO=3;

    public static final int WARN=4;

    public static final int ERROR=5;

    public static final int NOTHING=6;


    /**
     * 修改level变量的值就可以自由的控制日志的打印行为了
     * VERBOSE：表示打印所有的
     * NOTHING：屏蔽所有日志
     *
     * 使用方法：LogUtil.d(Tag,String);
     */

    public static int level=NOTHING;


    public static void v(String tag,String msg){
        if (level<=VERBOSE){
            Log.v(tag,msg);
        }
    }


    public static void d(String tag,String msg){
        if (level<=DEBUG){
            Log.d(tag, msg);
        }
    }

    public static void i(String tag,String msg){
        if (level<=INFO){
            Log.i(tag, msg);
        }
    }

    public static void w(String tag,String msg){
        if (level<=WARN){
            Log.w(tag, msg);
        }
    }

    public static void e(String tag,String msg){
        if (level<=ERROR){
            Log.e(tag,msg);
        }
    }

}
