package com.yjzfirst.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.yjzfirst.bean.DeliveryBean;
import com.yjzfirst.util.Util;

import java.util.List;

import app.yjzfirst.com.activity.DeliveryActivity;
import app.yjzfirst.com.activity.R;

/**
 * @{# WeatherStationsAdapter.java Create on 2015年5月26日 下午6:50:23
 * 
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @description
 *
 */
public class ChooseShipAdapter extends BaseAdapter {
	private DeliveryActivity context;
	private List<DeliveryBean> mdeliveryBean;
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */

	public ChooseShipAdapter(DeliveryActivity context, List<DeliveryBean> deliveryBean) {
		this.context = context;
		this.mdeliveryBean = deliveryBean;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mdeliveryBean.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mdeliveryBean.get(position);
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
		TextView text_box_ware = (TextView) convertView
				.findViewById(R.id.text_box_ware);
		text_box_ware.setVisibility(View.GONE);
		TextView listtext_weight = (TextView) convertView
				.findViewById(R.id.listtext_weight);
		listtext_weight.setVisibility(View.GONE);
//		RelativeLayout shipcell = (RelativeLayout) convertView
//				.findViewById(R.id.effectRelativeLayout_details);
		final DeliveryBean deliveryproduct = mdeliveryBean.get(position);
		barcode.setText(Util.CheckNullString(deliveryproduct.sequence));
		product_pecification.setText(Util.CheckNullString(deliveryproduct.bar_code));
		number_applications.setText( Util.CheckNullString(deliveryproduct.number_applications));
		number_of_boxes.setText(Util.CheckNullString(deliveryproduct.number_boxes));
		numbers.setText(Util.CheckNullString(deliveryproduct.numbers));

		return convertView;
	}
}
