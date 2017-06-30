package com.siyann.taidaapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import utils.LogUtil;
import utils.OkHttpUtil;
import utils.Url;


/**
 * 用户详情界面
 * 根据点击传送过来的值将值返回回去
 */
public class UserDetailActivity extends Activity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.detail_edittext)
    EditText detailEdittext;
    @Bind(R.id.detail_save)
    Button detailSave;
    private Context mContext;

    private String userid = "";


    private String mobile = "";   //联系电话
    private String place_qu = ""; //小区名称
    private String mobile_m = ""; //电话
    private String address_m = ""; //默认收货地址
    private String name_m = "";    //联系人

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        ButterKnife.bind(this);
        mContext = this;

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        LogUtil.e("id", id + "");

        titleView.setText(intent.getStringExtra("title"));
        /**
         * 根据不同的ID传不同的值给edittext
         */
        switch (id) {
            case 1:
                mobile = "请输入联系电话";
                detailEdittext.setHint(mobile);
                break;
            case 2:
                place_qu = "请输入小区名称";
                detailEdittext.setHint(place_qu);
                break;
            case 3:
                address_m = "默认收货地址";
                detailEdittext.setHint(address_m);
                break;
            case 4:
                name_m = "联系人";
                detailEdittext.setHint(name_m);
                break;
            case 5:
                mobile_m = "电话";
                detailEdittext.setHint(mobile_m);
                break;
            default:
                break;
        }


        /**
         * 从缓存中拿到userid
         */
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        userid = pref.getString("id", "");
        LogUtil.e("userid", userid);

    }

    @OnClick({R.id.back, R.id.detail_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.detail_save:
                /**
                 * 保存
                 */
                if (!TextUtils.isEmpty(detailEdittext.getText().toString())) {
                    if (mobile.equals("请输入联系电话")) {
                        mobile = detailEdittext.getText().toString();
                        domobile();
                    }
                    if (place_qu.equals("请输入小区名称")) {
                        place_qu = detailEdittext.getText().toString();
                        doplace_qu();
                    }
                    if (mobile_m.equals("电话")) {
                        mobile_m = detailEdittext.getText().toString();
                        domobile_m();
                    }
                    if (address_m.equals("默认收货地址")) {
                        address_m=detailEdittext.getText().toString();
                        doaddress_m();
                    }

                    if (name_m.equals("联系人")) {
                        name_m = detailEdittext.getText().toString();
                        doname_m();
                    }

                } else {
                    Toast.makeText(mContext, "内容不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    /**
     * 联系人
     */
    private void doname_m() {
        OkHttpUtil.sendOkHttpRequest(Url.editPersonal + "&user_id=" + userid + "&name_m="
                + name_m, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.e("e", e + "");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                LogUtil.e("result", result);
                JSONObject jsonObject = null;
                String status = "";
                try {
                    jsonObject = new JSONObject(result);
                    status = jsonObject.getString("status");
                    final String msg = jsonObject.getString("msg");
                    if (status.equals("1")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 默认收货地址
     */
    private void doaddress_m() {
        OkHttpUtil.sendOkHttpRequest(Url.editPersonal + "&user_id=" + userid + "&address_m="
                + address_m, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.e("e", e + "");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                LogUtil.e("result", result);
                JSONObject jsonObject = null;
                String status = "";
                try {
                    jsonObject = new JSONObject(result);
                    status = jsonObject.getString("status");
                    final String msg = jsonObject.getString("msg");
                    if (status.equals("1")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 电话
     */
      private void domobile_m() {
          OkHttpUtil.sendOkHttpRequest(Url.editPersonal + "&user_id=" + userid + "&mobile_m="
                  + mobile_m, new Callback() {
              @Override
              public void onFailure(Call call, IOException e) {
                  LogUtil.e("e", e + "");
              }

              @Override
              public void onResponse(Call call, Response response) throws IOException {
                  String result = response.body().string();
                  LogUtil.e("result", result);
                  JSONObject jsonObject = null;
                  String status = "";
                  try {
                      jsonObject = new JSONObject(result);
                      status = jsonObject.getString("status");
                      final String msg = jsonObject.getString("msg");
                      if (status.equals("1")) {
                          runOnUiThread(new Runnable() {
                              @Override
                              public void run() {
                                  Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                                  finish();
                              }
                          });
                      }
                  } catch (JSONException e) {
                      e.printStackTrace();
                  }
              }
          });
      }


    /**
     * 上传小区地址
     */
    private void doplace_qu() {
        OkHttpUtil.sendOkHttpRequest(Url.editPersonal + "&user_id=" + userid + "&place_qu="
                + place_qu, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.e("e", e + "");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                LogUtil.e("result", result);
                JSONObject jsonObject = null;
                String status = "";
                try {
                    jsonObject = new JSONObject(result);
                    status = jsonObject.getString("status");
                    final String msg = jsonObject.getString("msg");
                    if (status.equals("1")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 上传电话号码
     */
    private void domobile() {
        OkHttpUtil.sendOkHttpRequest(Url.editPersonal + "&user_id=" + userid + "&mobile=" + mobile, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.e("e", e + "");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                LogUtil.e("result", result);
                JSONObject jsonObject = null;
                String status = "";
                try {
                    jsonObject = new JSONObject(result);
                    status = jsonObject.getString("status");
                    final String msg = jsonObject.getString("msg");
                    if (status.equals("1")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
