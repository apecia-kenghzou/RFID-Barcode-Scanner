package com.uhf.uhfdemo;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.uhf.base.UHFManager;
import com.uhf.base.UHFModuleType;
import com.uhf.util.MUtil;

import java.io.File;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        final String filePath = intent.getStringExtra("path");
        Log.e("TAG", "onReceive: Receive Broadcast"  + " path = " + filePath);
        if (TextUtils.isEmpty(filePath) || !filePath.contains("rfidbin/"))
            return;
        String temp = filePath.substring(filePath.indexOf("rfidbin/"));
        final String fileName = temp.substring(temp.indexOf("/") + 1);
        //MyApp.getMyApp().setUhfMangerImpl(UHFManager.getUHFImplSigleInstance(UHFModuleType.UM_MODULE));
        final AlertDialog.Builder dialogBuilder =  new AlertDialog.Builder(context);
        dialogBuilder.setMessage("正在升级固件中,请稍后..." + "\n" +"注：升级完成后请重新启动APP");
        dialogBuilder.setCancelable(false);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        alertDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyApp.getMyApp().getUhfMangerImpl().powerOff();
                SystemClock.sleep(1000);
                MyApp.getMyApp().getUhfMangerImpl().powerOn();
                SystemClock.sleep(2500);
                //boolean ifSuccess =MyApp.getMyApp().getUhfMangerImpl().updateFirmware(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "UM510_V3.1.0.bin","UM510_V3.1.0.bin");
                boolean ifSuccess = MyApp.getMyApp().getUhfMangerImpl().updateFirmware(filePath,fileName);
                Log.e("TAG", " ifSuccess" + ifSuccess);
                SystemClock.sleep(1000);
                MyApp.getMyApp().getUhfMangerImpl().powerOff();
                alertDialog.cancel();
            }
        }).start();
    }
}