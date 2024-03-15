package com.uhf.setting;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.uhf.event.BaseFragment;
import com.uhf.uhfdemo.BuildConfig;
import com.uhf.uhfdemo.MyApp;
import com.uhf.uhfdemo.R;
import com.uhf.uhfdemo.SelectActivity;
import com.uhf.util.MLog;
import com.uhf.util.MUtil;
import com.uhf.util.ThreadUtil;
import com.tencent.mmkv.MMKV;
import com.uhf.base.UHFManager;
import com.uhf.base.UHFModuleType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import realid.rfidlib.CommonUtil;


public class AboutHelpFragment extends BaseFragment implements View.OnClickListener {

    private TextView be_seleted_ver;
    private TextView sw_ver;
    private TextView hw_ver;
    private TextView fm_ver;
    private TextView slrModuleType;
    private Button fm_update;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about_help, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        sw_ver = v.findViewById(R.id.sw_ver);
        hw_ver = v.findViewById(R.id.hw_ver);
        fm_ver = v.findViewById(R.id.fm_ver);
        slrModuleType = v.findViewById(R.id.slrModuleType);
        be_seleted_ver = v.findViewById(R.id.be_seleted_ver);
        fm_update = v.findViewById(R.id.fm_update);
        fm_update.setOnClickListener(this);
        v.findViewById(R.id.switch_module).setOnClickListener(this);
        if (UHFModuleType.SLR_MODULE == UHFManager.getType()) {
            be_seleted_ver.setVisibility(View.VISIBLE);
            fm_update.setVisibility(View.VISIBLE);
            slrModuleType.setVisibility(View.VISIBLE);
        }
        v.findViewById(R.id.bin_file).setOnClickListener(this);
        updateUI();
    }

    private void updateUI() {
        sw_ver.setText(getString(R.string.software_ver) + BuildConfig.VERSION_NAME);
        hw_ver.setText(getString(R.string.hardware_ver) + MyApp.getMyApp().getUhfMangerImpl().hardwareVerGet());
        fm_ver.setText(getString(R.string.firemware_ver) + MyApp.getMyApp().getUhfMangerImpl().firmwareVerGet());
        slrModuleType.setText(getString(R.string.slr_module_type) + MyApp.getMyApp().getUhfMangerImpl().getUHFModuleType());
    }


    //机器内置sd卡的根目录(具体的目录地址可自行修改拼接处理)
    // The root directory of the device's internal sd card (you can enter the specific directory address yourself)
    private String sdcardpath = Environment.getExternalStorageDirectory().getAbsolutePath();
    boolean value;
    //更新固件
    // Update firmware
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fm_update:
                MUtil.showProgressDialog(getString(R.string.firmware_update_wait), getActivity());

                ThreadUtil.getInstance().getExService().execute(new Runnable() {
                    @Override
                    public void run() {
                        //value为固件更新状态，true成功，false失败，更新大约40s到60s左右，推荐使用子线程更新固件写法
                        // value is the firmware update status, true succeeds, false fails, the update is about 40s to 60s, it is recommended to use sub-threaded firmware update writing method
                        if (!MyApp.ifRMModule)
                            value = MyApp.getMyApp().getUhfMangerImpl().updateFirmware(sdcardpath + File.separator + currentBinName, currentBinName);
                        else
                            value = MyApp.getMyApp().getUhfMangerImpl().updateFirmwareRm(sdcardpath + File.separator + currentBinName, currentBinName);

                        //更新完毕切换到主线程操作ui通知(android里面ui更新只建议在主线程操作)
                        // Switch to the main thread to operate ui notifications when the update is complete (ui updates in android are only recommended to be operated in the main thread)
                        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (value) {
                                    SystemClock.sleep(1000);
                                    MyApp.getMyApp().getUhfMangerImpl().getRFIDProtocolStandard();
                                    updateUI();
                                    hw_ver.setText(getString(R.string.hardware_ver) + MyApp.getMyApp().getUhfMangerImpl().hardwareVerGet());
                                    fm_ver.setText(getString(R.string.firemware_ver) + MyApp.getMyApp().getUhfMangerImpl().firmwareVerGet());
                                }
                                MUtil.cancleDialog();
                                MUtil.show(value ? R.string.firmware_update_success : R.string.firmware_update_failed);
                                fm_update.setEnabled(false);
                            }
                        });
                    }
                });
                break;
            case R.id.bin_file:
                showDiaog();
                break;
            case R.id.switch_module:
                MMKV.defaultMMKV().encode(CommonUtil.CURRENT_UHF_MODULE, "");
                UHFManager.clearConfigInfo(); //废弃掉上次使用UHF模块实例
                startActivity(new Intent(getActivity(), SelectActivity.class));
                Objects.requireNonNull(getActivity()).finish();
                break;
            default:
                break;
        }
    }


    //sd卡根目录.bin文件的获取
    // Get the root .bin file of the sd card
    private List<String> getBinFileName() {
        List<String> ar = new ArrayList<>();
        File file = new File(sdcardpath);
        MLog.e("sdcardpath = " + sdcardpath);
        File allFiles[] = file.listFiles();
        MLog.e(" size = " + (allFiles == null));
        if (allFiles != null) {
            for (File allFile : allFiles) {
                String mfileName = allFile.getName();
                MLog.e(" currentFile = " + mfileName);
                if (mfileName.endsWith(".bin")||mfileName.endsWith(".mdfw") ||mfileName.endsWith(".MDFW")) {
                    ar.add(mfileName);
                }
            }
        }
        return ar;
    }

    private AlertDialog adialog;
    private String currentBinName;

    private void showDiaog() {
        if (adialog == null) {
            adialog = new AlertDialog.Builder(getActivity()).create();
            adialog.setTitle(R.string.select_bin_file);
            List<String> allBinFile = getBinFileName();
            ListView showBin = new ListView(getActivity());
            showBin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String fvVer = ((TextView) view.findViewById(android.R.id.text1)).getText().toString();
                    be_seleted_ver.setText(getString(R.string.select_firemaware_ver) + fvVer);
                    currentBinName = fvVer;
                    cancleDialog();
                    fm_update.setEnabled(true);

                }
            });
            showBin.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, allBinFile));
            adialog.setView(showBin);
            //  adialog.create();
            adialog.show();
        } else {
            adialog.show();
        }
    }

    private void cancleDialog() {
        if (adialog != null) {
            adialog.cancel();
            adialog = null;
        }
    }


}
