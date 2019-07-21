package com.yjzfirst.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.annotations.Until;
import com.yjzfirst.bean.ReportProductBean;
import com.yjzfirst.util.Util;

import java.util.List;

import app.yjzfirst.com.activity.ReportActivity;
import app.yjzfirst.com.activity.R;

/**
 * @{# WeatherStationsAdapter.java Create on 2015年5月26日 下午6:50:23
 * 
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @description
 *
 */
public class ReportdetailAdapter extends BaseAdapter {
	private Activity context;
	private List<ReportProductBean> ReportProductBeans;
	/*
	 * (non-Javadoc)
	 *
	 * @see android.widget.Adapter#getCount()
	 */

	public ReportdetailAdapter(Activity context, List<ReportProductBean> deliveryBean) {
		this.context = context;
		this.ReportProductBeans = deliveryBean;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ReportProductBeans.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return ReportProductBeans.get(position);
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
		listtext_weight.setVisibility(View.VISIBLE);
		TextView text_box_ware = (TextView) convertView
				.findViewById(R.id.text_box_ware);
		text_box_ware.setVisibility(View.GONE);
//		RelativeLayout shipcell = (RelativeLayout) convertView
//				.findViewById(R.id.effectRelativeLayout_details);
		final ReportProductBean deliveryproduct = ReportProductBeans.get(position);
		barcode.setText(Util.CheckNullString(deliveryproduct.sequence));
		product_pecification.setText(Util.CheckNullString(deliveryproduct.process_name));
		number_applications.setText(Util.CheckNullString( deliveryproduct.qty_in));
		number_of_boxes.setText(Util.CheckNullString(deliveryproduct.weight_in));
		numbers.setText(Util.CheckNullString(deliveryproduct.qty));
		listtext_weight.setText(Util.CheckNullString(deliveryproduct.weight));
		return convertView;
	}
}
