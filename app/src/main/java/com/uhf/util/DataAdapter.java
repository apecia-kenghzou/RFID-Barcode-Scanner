package com.uhf.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.uhf.uhfdemo.R;

import java.util.List;
import java.util.Map;

/**
 * author CYD
 * date 2018/11/21
 *
 */
public class DataAdapter extends BaseAdapter {

    private Context con;
    private Map<String, Integer> realDataMap;
    private List<String> realKeyList, tidList, usrList, rfuList,rssiList,gtinList;
    private int mode = 0;

    public DataAdapter(Context con, Map<String, Integer> realDataMap, List<String> realKeyList, List<String> tidList, List<String> usrList, List<String> rfuList, List<String> rssiList, List<String> gtinList) {
        this.con = con;
        this.realDataMap = realDataMap;
        this.realKeyList = realKeyList;
        this.tidList = tidList;
        this.usrList = usrList;
        this.rfuList = rfuList;
        this.rssiList = rssiList;
        this.gtinList = gtinList;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    @Override
    public int getCount() {
        return realKeyList.size();
    }

    @Override
    public Object getItem(int position) {
        return realKeyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        ViewHolder vh;
        if (v == null) {
            v = LayoutInflater.from(con).inflate(R.layout.item, null);
            vh = new ViewHolder();
            vh.epc = v.findViewById(R.id.epc);
            vh.tid = v.findViewById(R.id.tid);
            vh.usr = v.findViewById(R.id.usr);
            vh.rfu = v.findViewById(R.id.rfu);
            vh.rssi = v.findViewById(R.id.rssi);
            vh.gtin = v.findViewById(R.id.gtin);
            vh.epc_usr_divd_Line =v.findViewById(R.id.epc_usr_divd_Line);
            vh.epc_rfu_divd_Line = v.findViewById(R.id.epc_rfu_divd_Line);
            vh.epc_tid_divd_Line = v.findViewById(R.id.epc_tid_divd_Line);
            vh.count = v.findViewById(R.id.count);
            vh.sn = v.findViewById(R.id.sn);
            v.setTag(vh);
        } else {
            vh = (ViewHolder) v.getTag();
        }
        vh.epc.setVisibility(mode == 1 ? View.GONE : View.VISIBLE);
        vh.tid.setVisibility(mode == 0 || mode ==3 || mode ==6 ? View.GONE : View.VISIBLE);
        vh.usr.setVisibility(mode == 3 || mode == 4 ? View.VISIBLE : View.GONE);
        vh.rfu.setVisibility(mode == 5 || mode == 6 ? View.VISIBLE : View.GONE);
        vh.epc_tid_divd_Line.setVisibility(mode == 0 || mode ==3 || mode ==6 ? View.GONE : View.VISIBLE);
        vh.epc_usr_divd_Line.setVisibility(mode == 3 || mode == 4 ? View.VISIBLE : View.GONE);
        vh.epc_rfu_divd_Line.setVisibility(mode == 5 || mode == 6 ? View.VISIBLE : View.GONE);
        vh.rssi.setVisibility(View.GONE);
        vh.gtin.setVisibility(View.GONE);
        boolean ifOnlyEPC = mode == 0 || mode == 3 || mode == 6;
        String epc, tid, usr, rfu,rssi,gtin;
        String data = realKeyList.get(position);
        String data1 = tidList.get(position);
        String data2 = usrList.get(position);
        String data3 = rfuList.get(position);
        String data4 = rssiList.get(position);
        String data5 = gtinList.get(position);
        //MLog.e("usr = " + data2 + " rfu = " + data3);

        epc = ifOnlyEPC ? data : data1;
        tid = ifOnlyEPC ? data1 : data;
        usr = data2;
        rfu = data3;
        rssi = data4;
        gtin = data5;
        String readNumber = String.valueOf(realDataMap.get(ifOnlyEPC ? epc : tid));
        vh.sn.setText((position + 1) + "");
        vh.epc.setText(epc);
        vh.tid.setText(tid);
        vh.usr.setText(usr);
        vh.rfu.setText(rfu);
        vh.count.setText(readNumber);
        vh.rssi.setText(rssi);
        vh.gtin.setText(gtin);
        return v;
    }


    private class ViewHolder {
        private TextView sn, epc, tid, count, usr, rfu, rssi, gtin;
        private View epc_tid_divd_Line, epc_usr_divd_Line, epc_rfu_divd_Line;
    }
}
