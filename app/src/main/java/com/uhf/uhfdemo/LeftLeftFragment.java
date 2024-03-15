package com.uhf.uhfdemo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.uhf.event.BackResult;
import com.uhf.event.BaseFragment;
import com.uhf.event.GetRFIDThread;
import com.uhf.event.OnKeyListener;
import com.uhf.util.DataAdapter;
import com.uhf.util.DataConversionUtils;
import com.uhf.util.ExcelUtil;
import com.uhf.util.MLog;
import com.uhf.util.MUtil;
import com.uhf.util.ThreadUtil;
import com.uhf.base.UHFManager;
import com.uhf.base.UHFModuleType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static com.uhf.uhfdemo.MyApp.ifASCII;
import static com.uhf.uhfdemo.MyApp.ifRMModule;
import static com.uhf.uhfdemo.MyApp.ifSupportR2000Fun;

import com.example.iscandemo.iScanInterface;
import android.os.IScanListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LeftLeftFragment extends BaseFragment implements View.OnClickListener, BackResult, AdapterView.OnItemSelectedListener,OnKeyListener,IScanListener{
    private String tagNumber, readNumber, takeTime;
    private TextView tag;
    private iScanInterface miScanInterface;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tagNumber = "Tags" + ":";
        readNumber = "Tags/S" + ":";
        takeTime = "Time(ms)" + ":";
        LeftLeftFragment context = this;
        //miScanInterface = new iScanInterface(context.getContext());

        return inflater.inflate(R.layout.fragment_leftleft, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tag = view.findViewById(R.id.textTest);
        //registerForContextMenu(view);
        Context context = requireContext();
        miScanInterface = new iScanInterface(context);
        miScanInterface.registerScan(this);
        miScanInterface.setOCREnable(true,0);
        miScanInterface.setAimLightMode(0);

        miScanInterface.setOutputMode(1);
//        miScanInterface.setBarcodeEnable(0,true);
//
//        miScanInterface.setBarcodeEnable(1,false);
//        miScanInterface.setBarcodeEnable(2,false);
//        miScanInterface.setBarcodeEnable(3,false);
//        miScanInterface.setBarcodeEnable(4,false);
//        miScanInterface.setBarcodeEnable(6,false);
//        miScanInterface.setBarcodeEnable(48,false);
//        miScanInterface.setBarcodeEnable(9,false);
//        miScanInterface.setBarcodeEnable(10,false);
//        miScanInterface.setBarcodeEnable(11,false);
//        miScanInterface.setBarcodeEnable(12,false);
//        miScanInterface.setBarcodeEnable(13,false);
//        miScanInterface.setBarcodeEnable(19,false);
//        miScanInterface.setBarcodeEnable(20,false);
//        miScanInterface.setBarcodeEnable(17,true);
//        miScanInterface.setBarcodeEnable(15,true);



    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        miScanInterface.unregisterScan(this);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void postResult(String[] tagData) {

    }

    @Override
    public void postInventoryRate(long rate) {

    }

    @Override
    public void onKeyDown(int keyCode, KeyEvent event) {

        miScanInterface.scan_start();
    }

    @Override
    public void onKeyUp(int keyCode, KeyEvent event) {
        miScanInterface.scan_stop();
    }
    @Override
    public void onScanResults(String s, int i, long l, long l1, String s1) {
        Log.d("idata", "data = " + s);
        Log.d("idata", "type = " + i);
        Log.d("idata", "scantime = " + l);
        Log.d("idata", "keytime = " + l1);

        tag.setText(s);
    }

}
