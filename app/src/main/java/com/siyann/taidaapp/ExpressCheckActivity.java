package com.siyann.taidaapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import adapter.ExpressAdapter;
import utils.LogUtil;
import utils.MyApplication;
import widget.KdApiOrderDistinguish;
import widget.KdniaoTrackQueryAPI;

/**
 * 快递查询
 */
public class ExpressCheckActivity extends Activity implements View.OnClickListener {
    private Context mcontext;

    private ImageView searchimg;
    private TextView mtextview;
    private ImageView mimageView;
    private EditText meditText;
    private TextView express_view;  //快递公司名称
    private String address; //快递公司名称
    private String result;  //返回的json
    private String shipperCode; //快递公司编码
    private String shipperName; //快递公司名称
    private String address_edit;  //用户输入的订单号

    private ExpressAdapter adapter;

    private ListView mListView;
//    private RecyclerView mrecyclerView;

    private ExpressAddressAsyncTask asyncTask;  //获取快递公司编码
    private SearchAsyncTask searchAsyncTask;    //查询快递物流
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express_check);
        mcontext=ExpressCheckActivity.this;
        init();
    }

    /**
     * 初始化UI数据
     */
    private void init() {
        mtextview = (TextView) findViewById(R.id.title_view);
        Intent intent = getIntent();
        mtextview.setText(intent.getStringExtra("title"));

        mimageView = (ImageView) findViewById(R.id.back);
        mimageView.setOnClickListener(this);

        searchimg = (ImageView) findViewById(R.id.search);
        searchimg.setOnClickListener(this);

        meditText = (EditText) findViewById(R.id.express_edit);
        express_view = (TextView) findViewById(R.id.express_view);

        mListView= (ListView) findViewById(R.id.mylist);
//        mrecyclerView= (RecyclerView) findViewById(R.id.myrecycle);
    }

    @Override
    public void onClick(View v) {
        address_edit = meditText.getText().toString();    //拿到用户输入的订单号
        switch (v.getId()) {
            case R.id.search:
                if (TextUtils.isEmpty(address_edit)) {
                    Toast.makeText(MyApplication.getContext(), "订单号不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    asyncTask = new ExpressAddressAsyncTask();
                    asyncTask.execute();
                }
                break;
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }
    /**
     * 获取快递公司名称
     */
    class ExpressAddressAsyncTask extends AsyncTask<Map, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Map... params) {
            KdApiOrderDistinguish express = new KdApiOrderDistinguish();
            try {
                address = express.getOrderTracesByJson(address_edit);
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(address);
                LogUtil.e("jsonObject", jsonObject + "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject;
        }

        /**
         * 更新UI
         *
         * @param jsonObject
         */
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (isCancelled()) {
                return;
            }
            JSONArray jsonArray=null;
            try {
                String message=jsonObject.optString("Success");
                if (message.equals("true")){
                jsonArray = jsonObject.getJSONArray("Shippers");
                LogUtil.e("jsonArray",jsonArray+"");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    shipperCode = jsonObject1.getString("ShipperCode");
                    shipperName = jsonObject1.getString("ShipperName");
                }
                LogUtil.e("shipperCode", shipperCode);
                LogUtil.e("shipperName", shipperName);
                express_view.setText(shipperName);  //设置textview
                /**
                 * 开启查物流的异步线程
                 */
                searchAsyncTask=new SearchAsyncTask();
                searchAsyncTask.execute();
            }else {
                    Toast.makeText(mcontext, "订单号错误", Toast.LENGTH_SHORT).show();
                }
        } catch (JSONException e) {
                e.printStackTrace();
        }

        super.onPostExecute(jsonObject);
        }
    }


    /**
     * 将异步线程的生命周期与activity的生命周期绑定
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (asyncTask != null && asyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            asyncTask.cancel(true);
        }
        if (searchAsyncTask!=null && searchAsyncTask.getStatus()==AsyncTask.Status.RUNNING){
            searchAsyncTask.cancel(true);
        }
    }

    /**
     * 通过订单号查询物流详情
     */
    class SearchAsyncTask extends AsyncTask<Map,Void,JSONObject>{
        @Override
        protected JSONObject doInBackground(Map... params) {
            KdniaoTrackQueryAPI api = new KdniaoTrackQueryAPI();
            try {
                result = api.getOrderTracesByJson(shipperCode, address_edit);
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result);
                LogUtil.e("jsonObject", jsonObject + "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject;
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (isCancelled()) {
                return;
            }
            try {

                /**
                 * 拿到json中的Reason字段，该字段只有在出问题的时候才会返回
                 * 根据Reason字段的值来进行业务逻辑判断
                 */
                String message=jsonObject.optString("Reason");
                LogUtil.e("message",message);
                if (TextUtils.isEmpty(message)){
                JSONArray  jsonArray = jsonObject.getJSONArray("Traces");
                LogUtil.e("jsonArray", jsonArray + "");
                for (int i = 0; i < jsonArray.length(); i++) {
//                    LinearLayoutManager manager=new LinearLayoutManager(mcontext);
//                    mrecyclerView.setLayoutManager(manager);
                    adapter=new ExpressAdapter(mcontext,jsonArray);
                    mListView.setAdapter(adapter);
//                    mrecyclerView.setAdapter(adapter);
                }
            }else {
                    Toast.makeText(ExpressCheckActivity.this, message, Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(jsonObject);
        }
    }

}
