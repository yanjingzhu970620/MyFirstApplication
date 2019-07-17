package com.yjzfirst.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yjzfirst.bean.EntryProductinfoBean;
import com.yjzfirst.bean.EntryProductinfoBean;
import com.yjzfirst.util.Util;

import java.util.List;

import app.yjzfirst.com.activity.EntryWarehouseActivity;
import app.yjzfirst.com.activity.R;
import app.yjzfirst.com.activity.EntryFormActivity;

/**
 * @{# WeatherStationsAdapter.java Create on 2015年5月26日 下午6:50:23
 * 
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @description
 *
 */
public class EntrydetailAdapter extends BaseAdapter {
	private Activity context;
	private List<EntryProductinfoBean> EntryProductinfoBeans;
	private String from;
	/*
	 * (non-Javadoc)
	 *
	 * @see android.widget.Adapter#getCount()
	 */

	public EntrydetailAdapter(Activity context, List<EntryProductinfoBean> deliveryBean,String from) {
		this.context = context;
		this.EntryProductinfoBeans = deliveryBean;
		this.from=from;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return EntryProductinfoBeans.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return EntryProductinfoBeans.get(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = LayoutInflater.from(context).inflate(
				R.layout.item_simple_detail, null);
		TextView barcode = (TextView) convertView
				.findViewById(R.id.text_barcode);
		TextView product_pecification = (TextView) convertView
				.findViewById(R.id.text_product_pecification);
		TextView number_applications = (TextView) convertView
				.findViewById(R.id.text_number_applications);
		TextView number_of_boxes = (TextView) convertView
				.findViewById(R.id.text_number_of_boxes);
		TextView numbers = (TextView) convertView
				.findViewById(R.id.text_numbers);
		TextView listtext_weight = (TextView) convertView
				.findViewById(R.id.listtext_weight);
		TextView text_box_ware = (TextView) convertView
				.findViewById(R.id.text_box_ware);

//		RelativeLayout shipcell = (RelativeLayout) convertView
//				.findViewById(R.id.effectRelativeLayout_details);
		final EntryProductinfoBean deliveryproduct = EntryProductinfoBeans.get(position);
		barcode.setText(Util.CheckNullString(deliveryproduct.sequence));
		product_pecification.setText(Util.CheckNullString(deliveryproduct.product_code));
		if(from.equals("EntryFormActivity")) {
			number_applications.setText(Util.CheckNullString(deliveryproduct.qty));
			number_of_boxes.setText(Util.CheckNullString(deliveryproduct.apply_box_qty));
			listtext_weight.setVisibility(View.GONE);
			text_box_ware.setVisibility(View.GONE);
		}else if(from.equals("EntryWarehouseActivity")) {
			number_applications.setText(Util.CheckNullString(deliveryproduct.qty));
			number_of_boxes.setText(Util.CheckNullString(deliveryproduct.apply_box_qty));
			listtext_weight.setText(Util.CheckNullString(deliveryproduct.product_qty));
			text_box_ware.setText(Util.CheckNullString(deliveryproduct.box_qty));
		}else {
			number_applications.setText(Util.CheckNullString(deliveryproduct.qty));
			number_of_boxes.setText(Util.CheckNullString(deliveryproduct.apply_box_qty));
			listtext_weight.setVisibility(View.GONE);
			text_box_ware.setVisibility(View.GONE);
		}
		if(deliveryproduct.is_tail_box!=null) {
			if (deliveryproduct.is_tail_box.equals("true")) {
				numbers.setText("是");
			} else if (deliveryproduct.is_tail_box.equals("false")) {
				numbers.setText("否");
			} else {
				numbers.setText(Util.CheckNullString(deliveryproduct.is_tail_box));
			}
		}
		return convertView;
	}
}
