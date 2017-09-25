package com.siyann.taidaapp.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.libhttp.entity.HttpResult;
import com.libhttp.subscribers.SubscriberListener;
import com.siyann.taidaapp.MainActivity;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import utils.LogUtil;
import utils.MyApplication;
import utils.OkHttpUtil;
import utils.Url;

/**
 * 微信登录页面
 * Created by szjdj on 2017-04-01.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
//    private static final String APP_SECRET = "aaa2d5131933a049c032e96f2f76e2a6";
    /**
     * 公众号的
     */
    private static final String APP_SECRET = "35b0f28236bdcc1b78cd8e0249b8db01";

    private IWXAPI mWeixinAPI;
//    public static final String WEIXIN_APP_ID = "wxeb75e53c13238e4e";

    /**
     * 公众号的
     */
    public static final String WEIXIN_APP_ID = "wx4a7556c9cfb27cc9";

    private String username="";    //用户名
    private String password="";    //密码
    private String unionid="";  //unionid
    private String nickname=""; //昵称
    private String open_id="";  //微信唯一ID
    private String headimgurl="";     //用户头像地址

    private String msg="";      //绑定微信的信息

    private SubscriberListener<HttpResult>  subscriberListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeixinAPI = WXAPIFactory.createWXAPI(this, WEIXIN_APP_ID, true);
//        mWeixinAPI = WXAPIFactory.createWXAPI(MyApplication.getContext(),null);
        mWeixinAPI.handleIntent(this.getIntent(), this);


        SharedPreferences pref=getSharedPreferences("data",MODE_PRIVATE);
        username=pref.getString("username","");
        password=pref.getString("password","");


        SharedPreferences preferences=getSharedPreferences("msg",MODE_PRIVATE);
        msg=preferences.getString("msg","");
        LogUtil.e("msg",msg);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mWeixinAPI.handleIntent(intent, this);//必须调用此句话
}

    //微信发送的请求将回调到onReq方法
    @Override
    public void onReq(BaseReq baseReq) {
        LogUtil.e("", "onReq");
    }

    @Override
    public void onResp(BaseResp resp) {
        LogUtil.e("","onResp");
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                //发送成功
                SendAuth.Resp sendResp = (SendAuth.Resp) resp;
                if (sendResp != null) {
                    String code = sendResp.code;
                    getAccess_token(code);
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //发送取消
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //发送被拒绝
                break;
            default:
                //发送返回
                break;
        }
    }

    /**
     * 获取openid accessToken值用于后期操作
     * @param code 请求码
     */
    private void getAccess_token(final String code) {
        String path = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                + WEIXIN_APP_ID
                + "&secret="
                + APP_SECRET
                + "&code="
                + code
                + "&grant_type=authorization_code";

        LogUtil.e("getAccess_token",path);


        Request.Builder builder=new Request.Builder();
        /**
         * 构造request
         */
        Request request=builder.get().url(path).build();
        //3.将Request封装为Call
        executeRequest(request);
        LogUtil.v("sms", "发送get请求");
    }

    private void executeRequest(Request request) {
        Call call=new OkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            /**
             * 失败的时候调用
             * @param call
             * @param e
             */

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.e("Tag", "onFailure:" + e.getMessage());
                e.printStackTrace();
            }

            /**
             * 成功的时候调用
             * @param call
             * @param response
             * @throws IOException
             */
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtil.e("onResponse", "onResponse:");
                final String res = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(res);
                            String openid = jsonObject.getString("openid").toString().trim();
                            LogUtil.e("openid", openid);
                            String access_token = jsonObject.getString("access_token").toString().trim();
                            LogUtil.e("access_token", access_token);
                            getUserMesg(access_token, openid);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        }

    /**
     * 获取微信的个人信息
      * @param access_token
     * @param openid
     */
    private void getUserMesg(final String access_token, final String openid) {
        String path = "https://api.weixin.qq.com/sns/userinfo?access_token="
                + access_token
                + "&openid="
                + openid;
        LogUtil.e("getUserMesg", path);

        Request.Builder builder=new Request.Builder();
        /**
         * 构造request
         */
        Request request=builder.get().url(path).build();
        //3.将Request封装为Call
        executeRequestUser(request);
        LogUtil.v("sms", "发送get请求");
    }
    private void executeRequestUser(Request request) {
        Call call=new OkHttpClient().newCall(request);

        call.enqueue(new Callback() {
            /**
             * 失败的时候调用
             * @param call
             * @param e
             */
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.e("Tag", "onFailure:" + e.getMessage());
                e.printStackTrace();
            }

            /**
             *成功的时候调用
             * @param call
             * @param response
             * @throws IOException
             */
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtil.e("onResponse", "onResponse:");
                final String res = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(res);
                            nickname = jsonObject.getString("nickname");

                            int sex = Integer.parseInt(jsonObject.get("sex").toString());

                            headimgurl = jsonObject.getString("headimgurl");

                            unionid = jsonObject.getString("unionid");

                            LogUtil.e("nickname", nickname);
                            LogUtil.e("sex", sex + "");
                            LogUtil.e("headimgurl", headimgurl);
                            LogUtil.e("unionid", unionid);

                            /**
                             * 获取微信用户信息存缓存
                             */
                            SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                            editor.putString("nickname", nickname);
                            editor.putInt("sex", sex);
                            editor.putString("headimgurl", headimgurl);
                            editor.apply();

//                            /**
//                             * 跳转到绑定微信界面
//                             * 用户可以选择绑定或者不绑定
//                             */
//                                if (TextUtils.isEmpty(msg)){
//                                    Intent intent=new Intent(MyApplication.getContext(),BindWxActivity.class);
//                                    intent.putExtra("unionid",unionid);
//                                    startActivity(intent);
//                                }else {
//                                    Intent intent=new Intent(MyApplication.getContext(),MainActivity.class);
//                                    startActivity(intent);
//                                    finish();
//
//                                }
                            dologin();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        finish();
                    }
                });
            }
        });

    }

    /**
     * 登录
     */
    private void dologin() {
        RequestBody body=new FormBody.Builder()
                .add("nickname", nickname)
                .add("open_id",unionid)
                .add("head",headimgurl)
                .build();
        LogUtil.e("nickname",nickname);
        LogUtil.e("open_id",unionid);
        LogUtil.e("head",headimgurl);
        OkHttpUtil.sendPostRequest(Url.login, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.e("e",e+"");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result=response.body().string();
                LogUtil.e("result",result);
                JSONObject jsonObject=null;
                try {
                    jsonObject=new JSONObject(result);
                    final String status=jsonObject.getString("status");
                    if (status.equals("1")){
                        Intent intent=new Intent(MyApplication.getContext(), MainActivity.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

