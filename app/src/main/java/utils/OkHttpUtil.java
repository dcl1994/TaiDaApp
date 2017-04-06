package utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 使用okHttp来发网络请求
 */
public class OkHttpUtil {
    public static void  sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
