package com.uhf.event;

import android.view.KeyEvent;

/**
 * Author CYD
 * Date 2019/2/20
 *
 */
public interface OnKeyListener extends OnKeyDownListener {

    void onKeyUp(int keyCode, KeyEvent event);
}
