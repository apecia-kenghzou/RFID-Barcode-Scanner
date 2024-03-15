package com.uhf.util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.uhf.uhfdemo.R;

import java.util.List;
import java.util.Map;

public class LockDataAdapter extends BaseAdapter {
    private Context con;
    private List<String> realKeyList;
    public LockDataAdapter(Context con, List<String> realKeyList)
    {
        this.con = con;
        this.realKeyList = realKeyList;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh;
        if (view == null) {
            view = LayoutInflater.from(con).inflate(R.layout.item1, null);
            vh = new ViewHolder();
            vh.epc = view.findViewById(R.id.epc);
            view.setTag(vh);
        }else {
            vh = (ViewHolder) view.getTag();
        }
        String epc;
        epc = realKeyList.get(i);
        Log.e("LockDataAdapter", "getView: " + epc);
        vh.epc.setText(epc);
        return view;
    }
    private class ViewHolder {
        private TextView epc;
    }
}
