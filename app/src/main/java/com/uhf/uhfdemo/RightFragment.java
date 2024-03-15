package com.uhf.uhfdemo;

import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.uhf.event.BaseFragment;
import com.uhf.event.GetRFIDThread;
import com.uhf.event.OnKeyDownListener;
import com.uhf.event.OnKeyListener;
import com.uhf.setting.AboutHelpFragment;
import com.uhf.setting.KillTagFragment;
import com.uhf.setting.LockTagFragment;
import com.uhf.setting.PoweFrequencyFragment;
import com.uhf.setting.ReadOrWriteTagFragment;
import com.uhf.setting.TestFragment;
import com.uhf.util.MUtil;


public class RightFragment extends BaseFragment implements AdapterView.OnItemClickListener, OnKeyListener {

    private PoweFrequencyFragment pSetFragment;
    private ReadOrWriteTagFragment wTFragment;
    private LockTagFragment lTFragment;
    private KillTagFragment kTFragment;
    private AboutHelpFragment helpFragment;
    private TestFragment testFragment;

    private FragmentManager manager;
    private Object fragment = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_right, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ListView lv = view.findViewById(R.id.showTitle);
        if (MyApp.getMyApp().getUhfMangerImpl().ifJ06()) {
            LinearLayout.LayoutParams leftWidth = (LinearLayout.LayoutParams) lv.getLayoutParams();
            leftWidth.width = 80;
            lv.setLayoutParams(leftWidth);
        }
        lv.setOnItemClickListener(this);
        lv.requestFocusFromTouch();
        //默认选中第一项
        // Default selects the first
        lv.setSelection(0);

        manager = getChildFragmentManager();
        setCurrentPage(0);
    }

    //跳转指定页面
    // Jump to the selected page
    private void setCurrentPage(int postion) {
        FragmentTransaction transaction = manager.beginTransaction();
        switch (postion) {
            case 0:
                if (pSetFragment == null) {
                    pSetFragment = new PoweFrequencyFragment();
                }
                fragment = pSetFragment;
                break;
            case 1:
                if (wTFragment == null) {
                    wTFragment = new ReadOrWriteTagFragment();
                }
                fragment = wTFragment;
                break;
            case 2:
                if (lTFragment == null) {
                    lTFragment = new LockTagFragment();
                }
                fragment = lTFragment;
                break;
            case 3:
                if (kTFragment == null) {
                    kTFragment = new KillTagFragment();
                }
                fragment = kTFragment;
                break;
            case 4:
                if (helpFragment == null) {
                    helpFragment = new AboutHelpFragment();
                }
                fragment = helpFragment;
                break;
            case 5:
                if (testFragment == null ) {
                    testFragment = new TestFragment();
                }
                fragment = testFragment;
                break;
        }
        transaction.replace(R.id.setting_data, (Fragment) fragment);
        transaction.commit();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (GetRFIDThread.getInstance().getLockPostTag()) {
            MUtil.show(R.string.notice_clean_data);
        }else {
            setCurrentPage(position);
        }
    }


    @Override
    public void onKeyUp(int keyCode, KeyEvent event) {
        if (fragment instanceof OnKeyDownListener)
            ((OnKeyDownListener) fragment).onKeyDown(keyCode, event);

    }

    @Override
    public void onKeyDown(int keyCode, KeyEvent event) {
        if (fragment instanceof OnKeyListener)
            ((OnKeyListener) fragment).onKeyUp(keyCode, event);
    }
}
