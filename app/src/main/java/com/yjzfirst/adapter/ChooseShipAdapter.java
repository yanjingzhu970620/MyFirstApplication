package com.yjzfirst.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.yjzfirst.bean.DeliveryBean;

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
//		RelativeLayout shipcell = (RelativeLayout) convertView
//				.findViewById(R.id.effectRelativeLayout_details);
		final DeliveryBean deliveryproduct = mdeliveryBean.get(position);
		barcode.setText(deliveryproduct.bar_code);
		product_pecification.setText(deliveryproduct.product_specification);
		number_applications.setText( deliveryproduct.number_applications);
		number_of_boxes.setText(deliveryproduct.number_boxes);
		numbers.setText(deliveryproduct.numbers);
//		shipcell.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				ShipsListAdapter.isShow = true;
//				ShipsListAdapter.isMove = true;
//				if (ShipsInfoLayer.tap_ships.size() >= 1) {
//					int have = 0;
//					for (int at = 0; at < ShipsInfoLayer.alltap_ships.size(); at++) {
//						if (ShipsInfoLayer.alltap_ships.get(at).m
//								.equals(ShipsInfoLayer.tap_ships.get(0).m)) {
//							ShipsInfoLayer.alltap_ships.remove(at);
//							ShipsInfoLayer.alltap_ships
//									.add(ShipsInfoLayer.tap_ships.get(0));
//							ShipsInfoLayer.tap_ships.clear();
//							have++;
//							break;
//						}
//					}
//					if (have == 0) {
//						ShipsInfoLayer.alltap_ships
//								.add(ShipsInfoLayer.tap_ships.get(0));
//						ShipsInfoLayer.tap_ships.clear();
//					}
//				}
//				if (ShipsInfoLayer.teamship.size() > 0) {
//					for (int i1 = 0; i1 < ShipsInfoLayer.teamship.size(); i1++) {
//						boolean have = false;
//						for (int i = 0; i < ShipsInfoLayer.allteamship.size(); i++) {
//
//							if (ShipsInfoLayer.teamship.get(i1).m
//									.equals(ShipsInfoLayer.allteamship.get(i).m)) {
//								ShipsInfoLayer.allteamship.remove(i);
//								ShipsInfoLayer.allteamship
//										.add(ShipsInfoLayer.teamship.get(i1));
//								have = true;
//								break;
//							}
//						}
//						if (!have) {
//							ShipsInfoLayer.allteamship
//									.add(ShipsInfoLayer.teamship.get(i1));
//						}
//					}
//					ShipsInfoLayer.teamship.clear();
//				}
//				if (ShipsInfoLayer.searchshipsBeans.size() > 0) {
//					for (int i1 = 0; i1 < ShipsInfoLayer.searchshipsBeans
//							.size(); i1++) {
//						boolean have = false;
//						for (int i = 0; i < ShipsInfoLayer.allsearchshipsBeans
//								.size(); i++) {
//							if (ShipsInfoLayer.searchshipsBeans.get(i1).m
//									.equals(ShipsInfoLayer.allsearchshipsBeans
//											.get(i).m)) {
//								ShipsInfoLayer.allsearchshipsBeans.remove(i);
//								ShipsInfoLayer.allsearchshipsBeans
//										.add(ShipsInfoLayer.searchshipsBeans
//												.get(i1));
//								have = true;
//								break;
//							}
//						}
//						if (!have) {
//							ShipsInfoLayer.allsearchshipsBeans
//									.add(ShipsInfoLayer.searchshipsBeans
//											.get(i1));
//						}
//					}
//				}
//				ShipsInfoLayer.searchshipsBeans.clear();
//				ShipsInfoLayer.searchshipsBeans.add(m);
//
//				if(ShipsInfoLayer.tap_shipsPoint.size()>0){
//					if(!ShipsInfoLayer.tap_shipsPoint.get(0).getM().equals(m.getM())){
//						ShipsInfoLayer.tap_shipsPoint.clear();
//						ShipsInfoLayer.tap_shipsPoint.add(m);//记录轨迹点
//					}
//				}else if(ShipsInfoLayer.tap_shipsPoint.size()==0){
////				ShipsInfoLayer.tap_shipsPoint.clear();
//				ShipsInfoLayer.tap_shipsPoint.add(m);//记录轨迹点
//				}
//				System.out.println("search tap_shipsPoint"+ShipsInfoLayer.tap_shipsPoint.size());
//
////				if(app.SearchshipRecord.size()<=20){
////					app.SearchshipRecord.put(m.getM(),m);
////				}else{
//////					app.SearchshipRecord.remove(0);
////					Set<String> keSet=app.SearchshipRecord.keySet();
////			        for (Iterator<String> iterator = keSet.iterator(); iterator.hasNext();) {
////			            String string = iterator.next();
////			            app.SearchshipRecord.remove(string);
////			            break;
////			            //System.out.println(string+" value: "+hm.get(string));
////
////			        }
////					app.SearchshipRecord.put(m.getM(),m);
////				}
//
//
//
////				if(m.cname!=null
////						&&!m.cname.equals("")
////						&&!m.cname.equals("null")){
////					if(!app.Searchshipkey.contains(m.cname)){
////					app.Searchshipkey.add(m.cname);
////					}else{
////						int i=app.Searchshipkey.indexOf(m.cname);
////						app.Searchshipkey.remove(i);
////						app.Searchshipkey.add(m.cname);
////					}
////				}else if(m.n!=null
////						&&!m.n.equals("")
////						&&!m.n.equals("null")){
////					if(!app.Searchshipkey.contains(m.n)
////							&&!app.Searchshipkey.contains(m.cname)){
////						app.Searchshipkey.add(m.n);
////						}else{
////							int i=0;
////							if(app.Searchshipkey.contains(m.n)){
////								i=app.Searchshipkey.indexOf(m.n);
////								}else if(app.Searchshipkey.contains(m.cname)){
////									i=app.Searchshipkey.indexOf(m.cname);
////								}
////							app.Searchshipkey.remove(i);
////							app.Searchshipkey.add(m.n);
////						}
////				}else if(m.getM()!=null
////						&&!m.getM().equals("")
////						&&!m.getM().equals("null")){
////					if(!app.Searchshipkey.contains(m.getM())
////							&&!app.Searchshipkey.contains(m.n)
////							&&!app.Searchshipkey.contains(m.cname)){
////						app.Searchshipkey.add(m.getM());
////						}else{
////							int i=0;
////							if(app.Searchshipkey.contains(m.getM())){
////									i=app.Searchshipkey.indexOf(m.getM());
////								}else if(app.Searchshipkey.contains(m.cname)){
////									i=app.Searchshipkey.indexOf(m.cname);
////								}else if(app.Searchshipkey.contains(m.n)){
////									i=app.Searchshipkey.indexOf(m.n);
////									}
////							app.Searchshipkey.remove(i);
////							app.Searchshipkey.add(m.getM());
////						}
////				}//原先存储搜索过的船名
//
////				if(app.Searchshipkey.size()>20){
////					app.Searchshipkey.remove(0);
////				}
////				app.saveSearchArray();//原先存储搜索过的船名
//				SearchActivity.searchActivity.finish();
//
//				((ChooseShipActivity) context).finish();
//				((ChooseShipActivity) context).overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
//			}
//		});
		return convertView;
	}
}
