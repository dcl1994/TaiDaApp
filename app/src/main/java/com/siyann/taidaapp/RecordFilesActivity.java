package com.siyann.taidaapp;

import android.app.Activity;
import android.os.Bundle;

/**
 * 获取设备中存储卡的录像列表
 */
public class RecordFilesActivity extends Activity {
    public static final String RECORDFILES = "com.yoosee.RECORDFILES";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_files);
    }
}
