package com.uhf.setting;


import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class KillTagFragment extends BaseFragment implements View.OnClickListener,BackResult {

    private Spinner spFilterMbKill;
    private EditText EtFilterAdsKill, EtFilterLenKill, EtFilterDataKill;
    private EditText EtAcPwdKill;

    private View v;

    private Button BtRead;
    private LockDataAdapter mListAdapter;
    private ListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return v = inflater.inflate(R.layout.fragment_kill_tag_2, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        dataMap.clear();
        realDataMap.clear();
        realKeyList.clear();
        spFilterMbKill = v.findViewById(R.id.spinner_MB);

        EtFilterAdsKill = v.findViewById(R.id.Et_set_ads);
        EtFilterLenKill = v.findViewById(R.id.Et_Set_len);
        EtFilterDataKill = v.findViewById(R.id.Et_Set_data);

        EtAcPwdKill = v.findViewById(R.id.Et_AcPwdKill);
        v.findViewById(R.id.Bt_Kill).setOnClickListener(this);

        EtFilterDataKill.setText("1234567890ABCDEF12345678");
        EtAcPwdKill.setText("00000000");
        EtFilterAdsKill.setText("32");
        EtFilterLenKill.setText("96");
        if(!MyApp.ifLockTagRead) {
            v.findViewById(R.id.read_epc).setVisibility(View.GONE);
        }else {
            v.findViewById(R.id.read_epc).setVisibility(View.VISIBLE);
        }

        BtRead = v.findViewById(R.id.read_RFID);
        v.findViewById(R.id.read_RFID).setOnClickListener(this);
        v.findViewById(R.id.clear_Data).setOnClickListener(this);
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
                EtFilterDataKill.setText(Data);
            }
        });
        if (MyApp.protocolType == 2) {
            ArrayAdapter<String> gb_rw = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.gb_select_area));
            gb_rw.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spFilterMbKill.setAdapter(gb_rw);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Bt_Kill:
                if (GetRFIDThread.getInstance().getLockPostTag()) {
                    MUtil.show(R.string.notice_clean_data);
                }else {
                    if (ifNotNull()) {
                        int ads = Integer.valueOf(EtFilterAdsKill.getText().toString());
                        int len = Integer.valueOf(EtFilterLenKill.getText().toString());
                        int val = spFilterMbKill.getSelectedItemPosition();
                        //标签销毁操作
                        // Destroy labels
                        boolean status = false;
                        if (MyApp.protocolType == 1) {
                            status = MyApp.getMyApp().getUhfMangerImpl().killTag(EtAcPwdKill.getText().toString(), MyApp.UHF[val],
                                    ads, len, EtFilterDataKill.getText().toString());
                        }else {
                            status = MyApp.getMyApp().getUhfMangerImpl().killGBTag(EtAcPwdKill.getText().toString(),0,0,0,EtFilterDataKill.getText().toString());
                        }
                        int result = status ? R.string.kill_tag_success : R.string.kill_tag_failed;
                        MUtil.show(result);
                    } else {
                        MUtil.show(R.string.data_notnull);
                    }
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
            default:
                break;
        }
    }

    //如果EditText的数据都不为空
    // If all the EditText data is not null
    private boolean ifNotNull() {
        return !isEmpty(EtFilterDataKill.getText()) && !isEmpty(EtFilterAdsKill.getText())
                && !isEmpty(EtFilterLenKill.getText()) && !isEmpty(EtAcPwdKill.getText());
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
}
