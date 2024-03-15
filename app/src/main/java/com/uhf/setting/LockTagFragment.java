package com.uhf.setting;

import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.uhf.base.UHFManager;
import com.uhf.base.UHFModuleType;
import com.uhf.event.BackResult;
import com.uhf.event.BaseFragment;
import com.uhf.event.GetRFIDThread;
import com.uhf.uhfdemo.MyApp;
import com.uhf.uhfdemo.R;
import com.uhf.util.LockDataAdapter;
import com.uhf.util.MLog;
import com.uhf.util.MUtil;

import static android.text.TextUtils.isEmpty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LockTagFragment extends BaseFragment implements View.OnClickListener, BackResult, AdapterView.OnItemSelectedListener {

    private Spinner spFilterMbLock;
    private EditText EtFilterAdsLock, EtFilterLenLock, EtFilterDataLock;

    private Spinner spLock;
    private EditText EtAcPwdLock;

    private Button BtRead;

    private LockDataAdapter mListAdapter;
    private ListView mListView;
    //新增控制国标锁标签的UI界面
    private View iso_lock,gb_lock;
    private Spinner spLockGB,spFilterGB,spLockActionGB,spLockObjectGB;
    private EditText EtAcPwdLockGB,EtFilterDataGB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lock_tag_2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        initView(v);
    }

    private void initView(View v){
        View top = v.findViewById(R.id.lockTop);
        iso_lock = v.findViewById(R.id.iso_lock);
        gb_lock = v.findViewById(R.id.gb_lock);
        dataMap.clear();
        realDataMap.clear();
        realKeyList.clear();
        spLock = v.findViewById(R.id.sp_Lock);
        spFilterMbLock = top.findViewById(R.id.spinner_MB);

        EtFilterAdsLock = top.findViewById(R.id.Et_set_ads);
        EtFilterLenLock = top.findViewById(R.id.Et_Set_len);
        EtFilterDataLock = top.findViewById(R.id.Et_Set_data);

        EtAcPwdLock = v.findViewById(R.id.Et_AcPwdLock);
        v.findViewById(R.id.Bt_Lock).setOnClickListener(this);
        v.findViewById(R.id.Bt_unLock).setOnClickListener(this);

        EtAcPwdLock.setText("00000000");
        EtFilterAdsLock.setText("32");
        EtFilterLenLock.setText("96");
        EtFilterDataLock.setText("1234567890ABCDEF12345678");

        //国标部分
        spLockGB = gb_lock.findViewById(R.id.sp_area);
        spFilterGB = gb_lock.findViewById(R.id.sp_select_area);
        spLockObjectGB = gb_lock.findViewById(R.id.sp_lock_object);
        spLockActionGB = gb_lock.findViewById(R.id.sp_lock_action);
        EtAcPwdLockGB = gb_lock.findViewById(R.id.et_pwd);
        EtFilterDataGB = gb_lock.findViewById(R.id.et_select_data);
        gb_lock.findViewById(R.id.bt_lock_gb).setOnClickListener(this);
        spLockObjectGB.setOnItemSelectedListener(this);
        EtAcPwdLockGB.setText("00000000");
        EtFilterDataGB.setText("1234567890ABCDEF12345678");
        spLockGB.setSelection(1);

        if (MyApp.protocolType == 1) {
            iso_lock.setVisibility(View.VISIBLE);
            gb_lock.setVisibility(View.GONE);
        }else {
            iso_lock.setVisibility(View.GONE);
            gb_lock.setVisibility(View.VISIBLE);
        }

        //定制部分，用于T2X UM2，在此页面做盘点标签（盘点EPC）的功能，且点击itme会将数据显示在锁的数据输入框上
        BtRead = v.findViewById(R.id.read_RFID);
        v.findViewById(R.id.read_RFID).setOnClickListener(this);
        v.findViewById(R.id.clear_Data).setOnClickListener(this);
        if(!MyApp.ifLockTagRead) {
            v.findViewById(R.id.read_epc).setVisibility(View.GONE);
        }else {
            v.findViewById(R.id.read_epc).setVisibility(View.VISIBLE);
        }
        mListView = v.findViewById(R.id.specific_Msg);
        BtRead.setText(GetRFIDThread.getInstance().getLockPostTag() ? R.string.stop_rfid : R.string.read);
        mListAdapter = new LockDataAdapter(getActivity(),realKeyList);
        mListView.setAdapter(mListAdapter);
        GetRFIDThread.getInstance().setBackResult(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String Data = mListView.getItemAtPosition(i).toString();
                MLog.e("Data = " + Data);
                EtFilterDataLock.setText(Data);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Bt_Lock:
                //锁标签
                // lock
                if (GetRFIDThread.getInstance().getLockPostTag()) {
                    MUtil.show(R.string.notice_clean_data);
                }else {
                    lock(true);
                }
                break;
            case R.id.Bt_unLock:
                //解锁标签
                // unlock
                if (GetRFIDThread.getInstance().getLockPostTag()) {
                    MUtil.show(R.string.notice_clean_data);
                }else {
                    lock(false);
                }
                break;
            case R.id.read_RFID:
                //读取
                startOrStopRFID();
                break;
            case R.id.clear_Data:
                if (GetRFIDThread.getInstance().getLockPostTag()) {
                    MUtil.show(R.string.notice_clean_data);
                } else {
                    clearData();
                }
                break;
            case R.id.bt_lock_gb:
                lockOrUnlockGB();
                break;
            default:
                break;
        }
    }


    private void lock(boolean flag) {
        if (ifNotNull()) {
            int ads = Integer.valueOf(EtFilterAdsLock.getText().toString());
            int len = Integer.valueOf(EtFilterLenLock.getText().toString());
            int lockitem = spLock.getSelectedItemPosition() + 2;
            int val = spFilterMbLock.getSelectedItemPosition();
            boolean status = false;
            if (flag) {
                //具体的锁标签操作
                // lock
                status = MyApp.getMyApp().getUhfMangerImpl().lockMen(EtAcPwdLock.getText().toString(), MyApp.UHF[val],
                        ads, len, EtFilterDataLock.getText().toString(), lockitem,0);
            } else {
                //具体的锁标签操作
                // unlock
                status = MyApp.getMyApp().getUhfMangerImpl().unlockMen(EtAcPwdLock.getText().toString(), MyApp.UHF[val],
                        ads, len, EtFilterDataLock.getText().toString(), lockitem,0);
            }
            int str = status ? (flag ? R.string.lock_success : R.string.unlock_success) : (flag ? R.string.lock_failed : R.string.unlock_failed);
            MUtil.show(str);
        } else {
            MUtil.show(R.string.data_notnull);
        }

    }

    private void lockOrUnlockGB() {
        if (ifNotNullGB()) {
            String data = EtFilterDataGB.getText().toString();
            String pwd = EtAcPwdLockGB.getText().toString();
            int area = spLockGB.getSelectedItemPosition();
            int object = spLockObjectGB.getSelectedItemPosition();
            int action = spLockActionGB.getSelectedItemPosition();
            if (object == 1) {
                action = action + 1;
            }
            boolean status = MyApp.getMyApp().getUhfMangerImpl().lockGBTag(pwd, 0,0,0, data, area, object,action);
            MUtil.show(status ? R.string.lock_success : R.string.lock_failed);
        }else {
            MUtil.show(R.string.data_notnull);
        }
    }

    private boolean ifNotNull() {
        return !isEmpty(EtFilterAdsLock.getText()) && !isEmpty(EtFilterLenLock.getText())
                && !isEmpty(EtFilterDataLock.getText()) && !isEmpty(EtAcPwdLock.getText());
    }

    private boolean ifNotNullGB() {
        return !isEmpty(EtAcPwdLockGB.getText()) && !isEmpty(EtFilterDataGB.getText());
    }
    private void startOrStopRFID() {

        MyApp.currentInvtDataType = 0;
        boolean flag = !GetRFIDThread.getInstance().getLockPostTag();
        if (flag) {
            if (UHFModuleType.SLR_MODULE == UHFManager.getType() && MyApp.if5100Module ){
                //MyApp.getMyApp().getUhfMangerImpl().slrInventoryModeSet(0);
            }
            Boolean i = MyApp.getMyApp().getUhfMangerImpl().startInventoryTag();
        } else {
            MyApp.getMyApp().getUhfMangerImpl().stopInventory();
            //GetRFIDThread.getInstance().destoryThread();
        }
        GetRFIDThread.getInstance().setLockPostTag(flag);
        BtRead.setText(flag ? R.string.stop_rfid : R.string.read);
    }

    private void clearData() {
        dataMap.clear();
        realDataMap.clear();
        realKeyList.clear();
        mListAdapter.notifyDataSetChanged();
    }

    private Map<String, Integer> dataMap = new HashMap<>(); //数据

    @Override
    public void postResult(String[] tagData) {
        String tid = tagData[0];//获取TID
        String epc = tagData[1]; //拿到EPC
        MLog.e("tid = " + tid + " epc = " + epc);
        //showDialog(epc);
        String filterData = epc ;
        Integer number = dataMap.get(/*epc*/filterData);//如果已存在，就拿到数量
        if (number == null) {
            dataMap.put(/*epc*/filterData, 1);
            updateUI(epc, 1);
        }
    }

    @Override
    public void postInventoryRate(long rate) {

    }


    private Map<String, Integer> realDataMap = new HashMap<>();
    // realDataMap的key
    // realDataMap's key
    private List<String> realKeyList = new ArrayList<>();
    private void updateUI(final String epc, final int num) {
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(num == 1){
                    String filterData = epc;
                    realKeyList.add(/*epc*/filterData);
                    mListAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    @Override
    public void onKeyDown(int keyCode, KeyEvent event) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i == 0) {
            ArrayAdapter<String> gb_rw = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.gb_lock_action));
            gb_rw.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spLockActionGB.setAdapter(gb_rw);
        }else if (i == 1) {
            ArrayAdapter<String> gb_rw = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.gb_safe_mode));
            gb_rw.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spLockActionGB.setAdapter(gb_rw);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
