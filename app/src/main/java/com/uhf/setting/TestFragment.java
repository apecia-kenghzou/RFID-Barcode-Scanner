package com.uhf.setting;

import static android.text.TextUtils.isEmpty;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.uhf.event.BaseFragment;
import com.uhf.uhfdemo.MyApp;
import com.uhf.uhfdemo.R;
import com.uhf.util.MLog;
import com.uhf.util.MUtil;

public class TestFragment extends BaseFragment implements View.OnClickListener{

    private EditText lossPower , lossPowerTime , Et_set_start,Et_set_finish,Et_set_one,Et_set_two,Et_set_start_two,Et_set_finish_two;
    private Button Bt_Less_Power,Bt_get_powerCompensation,Bt_set_powerCompensation;

    private int sec1_start,sec1_end,sec1_value,sec2_start,sec2_end,sec2_value;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MLog.e("onViewCreated");
        initView(view);
    }
    private void initView(View v) {
        lossPower = v.findViewById(R.id.loss_power);
        lossPowerTime = v.findViewById(R.id.loss_time);
        Bt_Less_Power = v.findViewById(R.id.Bt_Less_Power);
        Et_set_start = v.findViewById(R.id.Et_Set_start);
        Et_set_finish = v.findViewById(R.id.Et_Set_finish);
        Et_set_start_two = v.findViewById(R.id.Et_Set_start_two);
        Et_set_finish_two = v.findViewById(R.id.Et_Set_finish_two);
        Et_set_one = v.findViewById(R.id.Et_set_one);
        Et_set_two = v.findViewById(R.id.Et_Set_two);
        Bt_get_powerCompensation = v.findViewById(R.id.Bt_get_powerCompensation);
        Bt_set_powerCompensation = v.findViewById(R.id.Bt_set_powerCompensationr);

        lossPowerTime.setText("10");
        Et_set_start_two.setText("0");
        Et_set_finish_two.setText("0");
        Et_set_two.setText("0");
        Bt_Less_Power.setOnClickListener(this);
        Bt_set_powerCompensation.setOnClickListener(this);
        Bt_get_powerCompensation.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Bt_Less_Power:
                int time = Integer.valueOf(lossPowerTime.getText().toString());
                int power = MyApp.getMyApp().getUhfMangerImpl().getLossPower(time);
                if (power > 0 ) {
                    lossPower.setText(String.valueOf(power));
                    setResult_New(1);
                }else setResult_New(0);
                break;
            case R.id.Bt_set_powerCompensationr:
                if (!ifNotNull()) {
                    MUtil.show(R.string.data_notnull);
                    return;
                }
                sec1_start = Integer.valueOf(Et_set_start.getText().toString());
                sec1_end = Integer.valueOf(Et_set_finish.getText().toString());
                sec2_start = Integer.valueOf(Et_set_start_two.getText().toString());
                sec2_end = Integer.valueOf(Et_set_finish_two.getText().toString());
                sec1_value = Integer.valueOf(Et_set_one.getText().toString());
                sec2_value = Integer.valueOf(Et_set_two.getText().toString());
                boolean result = MyApp.getMyApp().getUhfMangerImpl().setPowerCompensationr(sec1_start,sec1_end,sec1_value,sec2_start,sec2_end,sec2_value);
                setResult(result);
                break;
            case R.id.Bt_get_powerCompensation:
                Log.e("TAG", "onClick: " );
                int[] buf = MyApp.getMyApp().getUhfMangerImpl().getPowerCompensationr();
                if (buf == null) {
                    setResult(false);
                    return;
                }
                Et_set_start.setText(String.valueOf(buf[0]));
                Et_set_finish.setText(String.valueOf(buf[1]));
                Et_set_one.setText(String.valueOf(buf[2]));
                Et_set_start_two.setText(String.valueOf(buf[3]));
                Et_set_finish_two.setText(String.valueOf(buf[4]));
                Et_set_two.setText(String.valueOf(buf[5]));
                setResult(true);
                break;
        }
    }

    private void setResult_New(int flag) {
        if (flag ==1) {
            MUtil.show(R.string.success);
        } else {
            MUtil.show(R.string.failed);
        }
    }

    private boolean ifNotNull() {
        return !isEmpty(Et_set_start.getText()) && !isEmpty(Et_set_finish.getText()) && !isEmpty(Et_set_start_two.getText()) &&
                !isEmpty(Et_set_finish_two.getText()) && !isEmpty(Et_set_one.getText()) && !isEmpty(Et_set_two.getText());
    }

    private void setResult(boolean flag) {
        if (flag) {
            MUtil.show(R.string.success);
        } else {
            MUtil.show(R.string.failed);
        }
    }
}
