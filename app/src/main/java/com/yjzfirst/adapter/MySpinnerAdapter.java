package com.yjzfirst.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;

import app.yjzfirst.com.activity.R;

/**
 * Created by 94012 on 2019/7/21.
 */

public class MySpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
    private Context context;
    private List<String> list;

    public MySpinnerAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.spinner_simple_detail, null);
        TextView tvgetView = (TextView) convertView.findViewById(R.id.text_item_spinner);
        tvgetView.setText(getItem(position).toString());
        return convertView;
    }

//    @Override
//    public View getDropDownView(int position, View convertView, ViewGroup parent) {
////        convertView = LayoutInflater.from(context).inflate(R.layout.getdropdowview, null);
////        TextView tvdropdowview = (TextView) convertView.findViewById(R.id.tvgetdropdownview);
////        tvdropdowview.setText(getItem(position).toString());
//        return convertView;
//    }
}

