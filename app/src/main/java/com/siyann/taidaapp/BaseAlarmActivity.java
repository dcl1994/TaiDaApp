package com.siyann.taidaapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hwangjr.rxbus.RxBus;

/**
 * Created by szjdj on 2017-06-27.
 */
public class BaseAlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBus.get().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
    }
}
