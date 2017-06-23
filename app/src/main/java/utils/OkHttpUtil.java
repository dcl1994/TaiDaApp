package utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 使用okHttp来发网络请求
 */
public class OkHttpUtil {
   public static  OkHttpClient client=new OkHttpClient();

    /**
     * get传参
     * @param address
     * @param callback
     * @return
     */

    public static void  sendOkHttpRequest(String address, Callback callback){
        Request request=new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }


    /**
     * post传参
     * @param address
     * @param body
     * @param callback
     */
    public static void sendPostRequest(String address, RequestBody body,Callback callback){
        Request request=new Request.Builder()
                .url(address)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }


    /**
     * 判断网络是否连接
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobNetInfo = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (activeNetInfo.isConnected()) {
            return true;
        }
        if (mobNetInfo.isConnected()) {
            return true;
        }
        if (!activeNetInfo.isConnected() && !mobNetInfo.isConnected()) {
            Toast.makeText(context, "网络连接不可用，请检查网络设置", Toast.LENGTH_SHORT).show();
            return false;
        }
        return false;
    }

}
