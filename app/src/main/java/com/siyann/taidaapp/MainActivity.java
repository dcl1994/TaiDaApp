package com.siyann.taidaapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapter.CommunityAdapter;
import adapter.ServiceAdapter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import utils.ActivityCollector;
import utils.BaseActivity;
import utils.GlideImageLoader;
import utils.ItemUtil;
import utils.LogUtil;
import utils.urlUtil;

/**
 * 泰达APP主界面
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    private ImageView myuser_img;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Context mContext;
    private Banner banner;      //图片滑动控件
    private ImageView smart_img;
    private ImageView tv_img;
    private TextView service_text;  //便民服务
    private TextView community_text;//社区政务
    private ImageView service_img;
    private ImageView community_img;
    private long exittime = 0;

    private RecyclerView recyclerView;  //滚动控件

    private List<ItemUtil> itemUtilList = new ArrayList<>();
    private List<ItemUtil> itemUtilListcom = new ArrayList<>();


    private TextView mweather_info;    //天气

    private TextView mtemperature;      //温度

    private ImageView mweather_img;     //天气图标

    OkHttpClient okHttpClient = new OkHttpClient();

    JSONObject jsonObject = null;

    String data=""; //日期
    String temperature=""; //温度
    String  info="";    //天气

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;
        init();
        initUtilList(); //初始化数据

        /**
         * 设置Adapter
         */
        recyclerView = (RecyclerView) findViewById(R.id.recyclermain_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ServiceAdapter adapter = new ServiceAdapter(itemUtilList);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 初始化RecyclerView中的数据，填充相应内容
     */
    private void initUtilList() {
        ItemUtil item0 = new ItemUtil("路况查看", "查看实时交通路况", R.drawable.lukuang);
        itemUtilList.add(item0);

        ItemUtil item1 = new ItemUtil("家居报修", "您的水管漏水吗", R.drawable.jiajubaoxiu);
        itemUtilList.add(item1);

        ItemUtil item2 = new ItemUtil("快递查询", "看看你的快递到哪里了", R.drawable.kuaidi);
        itemUtilList.add(item2);

        ItemUtil item3 = new ItemUtil("便民号码", "快速查找便民号码", R.drawable.phonenumber);
        itemUtilList.add(item3);

        ItemUtil item4 = new ItemUtil("社区地图", "当前社区的地图", R.drawable.map);
        itemUtilList.add(item4);


        //itemUtilListcom的控件
        ItemUtil item6 = new ItemUtil("社区动态", "了解社区最新动态", R.drawable.shequdongtai);
        itemUtilListcom.add(item6);

        ItemUtil item7 = new ItemUtil("社区推客", "社区政务办理指南", R.drawable.shequtuike);
        itemUtilListcom.add(item7);

        ItemUtil item8 = new ItemUtil("问卷调查", "说说您对社区的满意度", R.drawable.wenjuandiaocha);
        itemUtilListcom.add(item8);

        ItemUtil item9 = new ItemUtil("居民反馈", "你有什么建议呢", R.drawable.jumfankui);
        itemUtilListcom.add(item9);

        ItemUtil item10 = new ItemUtil("缤纷活动", "各式各样的活动送给你", R.drawable.binfenactivity);
        itemUtilListcom.add(item10);

        ItemUtil item11 = new ItemUtil("个人中心", "查看修改自己的个人信息", R.drawable.myself);
        itemUtilListcom.add(item11);
    }


    /**
     * 初始化
     */
    private void init() {
        mweather_info = (TextView) findViewById(R.id.weather_info);
        mtemperature = (TextView) findViewById(R.id.temperature);
        mweather_img = (ImageView) findViewById(R.id.weather_img);

        service_text = (TextView) findViewById(R.id.service_customers);
        service_text.setOnClickListener(this);
        community_text = (TextView) findViewById(R.id.community_affairs);
        community_text.setOnClickListener(this);
        service_img = (ImageView) findViewById(R.id.service_customersimg);
        community_img = (ImageView) findViewById(R.id.community_affairsimg);


        smart_img = (ImageView) findViewById(R.id.SmartCam);
        smart_img.setOnClickListener(this);
        tv_img = (ImageView) findViewById(R.id.TV);
        tv_img.setOnClickListener(this);
        banner = (Banner) findViewById(R.id.banner);
        /**
         * 加载网络图片
         */
        //本地图片数据（资源文件）
        List<String> list = new ArrayList<>();
        list.add("http://www.bhdtv.net/_data/2016/10/21/d9701245_e53a_4b64_a858_aac54b64ae8b/Thumbnail/201610210917376621121111949.jpg");
        list.add("http://www.bhdtv.net/_data/2016/10/21/8226ad09_cd4f_474c_8c5b_e103c0eb366d/Thumbnail/201610210910438041653664119.jpg");
        list.add("http://www.bhdtv.net/_data/2016/06/03/ff45a383_a4bf_4757_bd3e_43648c557cf0/Thumbnail/20160603152901265335782509.png");

        /**
         * 标题
         */
        List<String> titlelist = new ArrayList<>();
        titlelist.add("");
        titlelist.add("");
        titlelist.add("");
        banner.setImages(list)
                .setImageLoader(new GlideImageLoader())
                .setDelayTime(2500)
                .setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE)
                .setBannerTitles(titlelist)
                        //设置标题集合（当banner样式有显示title时）
                .setBannerAnimation(Transformer.DepthPage)
                .start();

        myuser_img = (ImageView) findViewById(R.id.user_img);
        myuser_img.setOnClickListener(this);

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.usertitle_bg);
        }
        /**
         * 滑动页面的点击事件
         */
        mNavigationView.setCheckedItem(R.id.nav_findpwd);
        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.nav_findpwd:
                                Intent intent1 = new Intent(mContext, FindPassword.class);
                                intent1.putExtra("title", "找回密码");
                                startActivity(intent1);
                                mDrawerLayout.closeDrawers();
                                break;
                            case R.id.nav_switch:       //跳转到登录界面
                                Intent inten2 = new Intent(mContext, LoginActivity.class);
                                startActivity(inten2);
                                mDrawerLayout.closeDrawers();
                                break;
                            case R.id.nav_yimi:
                                Toast.makeText(mContext, "一米菜园", Toast.LENGTH_SHORT).show();
                                mDrawerLayout.closeDrawers();
                                break;
                            case R.id.nav_exit:         //退出程序
                                /**
                                 * 销毁所有活动，并杀掉所有进程
                                 */
                                ActivityCollector.finishAll();
                                android.os.Process.killProcess(android.os.Process.myPid());
                                cleanSharedPreference(mContext);
                                mDrawerLayout.closeDrawers();
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });

    }

    /**
     * 主页面的点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /**
             * 点击按钮显示滑动菜单
             */
            case R.id.user_img:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.SmartCam:     //跳转到设备列表界面
                Intent intent = new Intent(mContext, EquipmentActivity.class);
                intent.putExtra("title", "设备列表");
                startActivity(intent);
                break;

            case R.id.TV:
                Intent intent1 = new Intent(mContext, TvLiveActivity.class);
                intent1.putExtra("title", "直播界面");
                startActivity(intent1);
                break;

            case R.id.service_customers:    //便民服务
                service_text.setTextColor(ContextCompat.getColor(this, R.color.blue_main));
                service_img.setVisibility(View.VISIBLE);
                community_text.setTextColor(ContextCompat.getColor(this, R.color.blackmin));
                community_img.setVisibility(View.GONE);
                /**
                 * 点击配置adapter
                 */
                ServiceAdapter adapter = new ServiceAdapter(itemUtilList);
                recyclerView.setAdapter(adapter);
                break;
            case R.id.community_affairs:   //社区政务
                community_text.setTextColor(ContextCompat.getColor(this, R.color.blue_main));
                community_img.setVisibility(View.VISIBLE);
                service_text.setTextColor(ContextCompat.getColor(this, R.color.blackmin));
                service_img.setVisibility(View.GONE);
                /**
                 * 点击配置adapter
                 */
                CommunityAdapter adapter1 = new CommunityAdapter(itemUtilListcom);
                recyclerView.setAdapter(adapter1);
                break;

        }
    }

    /**
     * 公司网址的点击方法
     *
     * @param v
     */
    public void open(View v) {
        Intent intent = new Intent(mContext, DetailsActivity.class);
        intent.putExtra("title", "泰达官方网址");
        intent.putExtra("url", "http://www.bhdtv.net");
        startActivity(intent);
    }


    /**
     * 返回按钮的点击事件，点击两次退出登录
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if ((System.currentTimeMillis() - exittime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exittime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //开始轮播
        banner.startAutoPlay();

        //获取天气查询显示在headView中
        getWeather();
    }


    /**
     * 发送一个get请求获取天气情况
     */
    private void getWeather() {
        Request.Builder builder = new Request.Builder();
        String cityname = "天津";
        //2.构造Request
        Request request = builder.get().url(urlUtil.ProcessUrl + "&cityname=" + cityname).build();
        //3.将Request封装为Call
        executeRequest(request);
        LogUtil.v("sms", "发送get请求");

    }

    private void executeRequest(Request request) {
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            //失败的时候调用
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.e("Tag", "onFailure:" + e.getMessage());
                e.printStackTrace();
            }

            //成功的时候调用
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtil.e("Tag", "onResponse:");
                final String res = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            jsonObject = new JSONObject(res);
                            LogUtil.e("jsonObject", jsonObject + "");

                            int code = jsonObject.optInt("error_code");   //code
                            String message = jsonObject.optString("reason");  //reason

                            if (code == 0) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                                JSONObject jsonObject2 = jsonObject1.getJSONObject("realtime");
                                /**
                                 * 拿到天气，赋值
                                 */
                                LogUtil.e("jsonObject2", jsonObject2 + "");
                                data = jsonObject2.get("date").toString();
                                LogUtil.e("data", data);

                                JSONObject jsonObject3 = jsonObject2.getJSONObject("weather");
                                LogUtil.e("weather", jsonObject3 + "");


                                temperature = jsonObject3.getString("temperature");
                                LogUtil.e("temperature", temperature + "");

                                mtemperature.setText(temperature + "℃");

                                info = jsonObject3.getString("info");
                                LogUtil.e("info", info + "");

                                mweather_info.setText(info);


                                if (info.equals("晴")) {
                                    mweather_img.setImageResource(R.drawable.sunny);
                                }
                                if (info.equals("阴")){
                                    mweather_img.setImageResource(R.drawable.overcast);
                                }
                                if (info.equals("多云")) {
                                    mweather_img.setImageResource(R.drawable.cloudy);
                                }
                                if (info.equals("雨")) {
                                    mweather_img.setImageResource(R.drawable.rain);
                                }
                                if (info.equals("小雨")) {
                                    mweather_img.setImageResource(R.drawable.rain);
                                }
                                if (info.equals("中雨")) {
                                    mweather_img.setImageResource(R.drawable.rain);
                                }
                                if (info.equals("大雨")) {
                                    mweather_img.setImageResource(R.drawable.rain);
                                }
                                if (info.equals("阵雨")) {
                                    mweather_img.setImageResource(R.drawable.rain);
                                }
                                if (info.equals("雪")) {
                                    mweather_img.setImageResource(R.drawable.snow);
                                }
                                if (info.equals("小雪")) {
                                    mweather_img.setImageResource(R.drawable.snow);
                                }
                                if (info.equals("中雪")) {
                                    mweather_img.setImageResource(R.drawable.snow);
                                }
                                if (info.equals("大雪")) {
                                    mweather_img.setImageResource(R.drawable.snow);
                                }
                            }
                            if (message.equals("Success")) {
                                LogUtil.e("success", "成功");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }


    /**
     * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理
     *
     * @param context
     */
    public void cleanSharedPreference(Context context) {
        deleteFilesByDirectory(new File("/data/data/"
                + context.getPackageName() + "/shared_prefs"));
    }

    private void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }


}
