package utils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 *知晓当前是哪一个活动
 */
public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.e("BaseActivity", getClass().getSimpleName());


        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
