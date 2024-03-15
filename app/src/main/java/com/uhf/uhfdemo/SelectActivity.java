package com.uhf.uhfdemo;

import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.uhf.util.MLog;
import com.tencent.mmkv.MMKV;
import com.uhf.base.UHFManager;
import com.uhf.base.UHFModuleType;

import realid.rfidlib.CommonUtil;

public class SelectActivity extends AppCompatActivity {


    private MMKV mkv;
    private Button umModule, slrModule,rmModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        mkv = MMKV.defaultMMKV();
        umModule = findViewById(R.id.umModule);
        slrModule = findViewById(R.id.slrModule);
        rmModule = findViewById(R.id.rmModule);
        String moduleType = mkv.decodeString(CommonUtil.CURRENT_UHF_MODULE, "");
        MLog.e("moduleType = " + moduleType);

        if (TextUtils.isEmpty(moduleType) ) {
            umModule.setVisibility(View.VISIBLE);
            slrModule.setVisibility(View.VISIBLE);
            rmModule.setVisibility(View.VISIBLE);

        } else {
            MyApp.getMyApp().setUhfMangerImpl(UHFManager.getUHFImplSigleInstance(UHFModuleType.valueOf(moduleType)));
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    public void onClick(View view) {
        UHFModuleType mType = UHFModuleType.UM_MODULE;
        switch (view.getId()) {
            case R.id.umModule:
                mType = UHFModuleType.UM_MODULE;
                break;
            case R.id.slrModule:
                mType = UHFModuleType.SLR_MODULE;
                break;
            case R.id.rmModule:
                mType = UHFModuleType.RM_MODULE;
            default:
                break;
        }
        mkv.encode(CommonUtil.CURRENT_UHF_MODULE, mType.name());
        MyApp.getMyApp().setUhfMangerImpl(UHFManager.getUHFImplSigleInstance(mType));
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
