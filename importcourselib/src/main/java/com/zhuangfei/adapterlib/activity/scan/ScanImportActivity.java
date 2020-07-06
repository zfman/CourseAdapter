package com.zhuangfei.adapterlib.activity.scan;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zhuangfei.adapterlib.R;
import com.zhuangfei.adapterlib.activity.AdapterSchoolActivity;

public class ScanImportActivity extends AppCompatActivity {

    public static final int REQUEST_SCAN = 2;
    public static final int REQUEST_OPEN_LOCAL = 10;
    private CaptureFragment captureFragment;
    private LinearLayout backLayout;
    private LinearLayout localLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_import);
        initView();
        initEvent();
    }

    private void initView() {
        backLayout = (LinearLayout) findViewById(R.id.id_back_layout);
        localLayout = (LinearLayout) findViewById(R.id.id_scan_local);

        captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.view_scan_mycamera);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_my_container, captureFragment).commit();
    }

    private void initEvent() {
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

        localLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                openImage();
            }
        });
    }

    public void openImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_OPEN_LOCAL);
    }

    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            analyzeCode(result);
        }

        @Override
        public void onAnalyzeFailed() {
            Toast.makeText(ScanImportActivity.this, "识别失败", Toast.LENGTH_SHORT)
                    .show();
            finish();
        }
    };


    public void analyzeCode(String url) {
        if(!TextUtils.isEmpty(url)&&url.startsWith("gs://import/")){
            AdapterSchoolActivity.htmlCode= url.substring(12);
            finish();
            return;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
