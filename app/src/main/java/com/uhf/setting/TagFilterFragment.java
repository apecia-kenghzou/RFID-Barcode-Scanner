package com.uhf.setting;

import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.uhf.event.BaseFragment;
import com.uhf.uhfdemo.MyApp;
import com.uhf.uhfdemo.R;
import com.uhf.util.MUtil;

import static android.text.TextUtils.isEmpty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TagFilterFragment extends BaseFragment implements View.OnClickListener {
    private EditText etSetAds, etSetLen, etSetData;
    private CheckBox cBSetFilterSave;
    private Spinner spSetFilterMb;

    @Override

    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tag_filter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        spSetFilterMb = v.findViewById(R.id.spinner_MB);

        etSetAds = v.findViewById(R.id.Et_set_ads);
        etSetLen = v.findViewById(R.id.Et_Set_len);
        etSetData = v.findViewById(R.id.Et_Set_data);

        cBSetFilterSave = v.findViewById(R.id.CB_Save);

        v.findViewById(R.id.Bt_SetFilter).setOnClickListener(this);
        v.findViewById(R.id.Bt_Clear).setOnClickListener(this);

        //单位bit
        // Unit is bit
        etSetAds.setText("32");
        //单位bit
        // Unit is bit
        etSetLen.setText("96");
        //单位hex
        // Unit is hex
        etSetData.setText("1234567890ABCDEF12345678");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Bt_SetFilter:
                filter();
                break;
            case R.id.Bt_Clear:
                clearFilter();
                break;
            default:
                break;
        }
    }


    private void filter() {
        if (ifNotNull()) {
            int ads = Integer.valueOf(etSetAds.getText().toString());
            int len = Integer.valueOf(etSetLen.getText().toString());
            int val = spSetFilterMb.getSelectedItemPosition();
            int flag = 0;
            if(cBSetFilterSave.isChecked()) {
                flag = 1;
            }else flag = 0;
            boolean status = MyApp.getMyApp().getUhfMangerImpl().filterSet
                    (MyApp.UHF[val], ads, len, etSetData.getText().toString(), flag);
            if (status) {
                //设置美标,定功率25
                //set american standard ,rated power 25
                MyApp.getMyApp().getUhfMangerImpl().powerSet(25);
                MyApp.getMyApp().getUhfMangerImpl().frequencyModeSet(3);
                MUtil.show(R.string.fiter_success);
            } else {
                MUtil.show(R.string.fiter_failed);
            }
        } else {
            MUtil.show(R.string.data_notnull);
        }

    }

    private void clearFilter() {
        int val = spSetFilterMb.getSelectedItemPosition();
        int flag = 0;
        if(cBSetFilterSave.isChecked()) {
            flag = 1;
        }else flag = 0;
        boolean status = MyApp.getMyApp().getUhfMangerImpl().filterSet
                (MyApp.UHF[val], 0, 0, etSetData.getText().toString(), flag);
        if (status) {
            MUtil.show(R.string.clean_success);
            //重置功率为30，区域频率为美国
            // Reset power is 30, area frequency is US
            MyApp.getMyApp().getUhfMangerImpl().powerSet(30);
            MyApp.getMyApp().getUhfMangerImpl().frequencyModeSet(4);
        } else {
            MUtil.show(R.string.clean_failed);
        }
    }

    private boolean ifNotNull() {
        return !isEmpty(etSetAds.getText()) && !isEmpty(etSetLen.getText()) && !isEmpty(etSetData.getText());
    }
}
