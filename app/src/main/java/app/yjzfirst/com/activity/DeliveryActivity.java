package app.yjzfirst.com.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yjzfirst.adapter.ChooseShipAdapter;
import com.yjzfirst.bean.DeliveryBean;
import com.yjzfirst.bean.ReportProductBean;
import com.yjzfirst.util.IndexConstants;
import com.yjzfirst.util.PreferencesUtils;
import com.yjzfirst.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.SyncFailedException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.yjzfirst.util.IndexConstants.ip_key;
import static com.yjzfirst.util.IndexConstants.port_key;
import static com.yjzfirst.util.IndexConstants.token_key;
import static com.yjzfirst.util.Util.REQUEST_CODE_SCAN;
import static com.yjzfirst.util.Util.readStream;
import static com.yjzfirst.util.Util.setListViewHeightBasedOnChildren;
import static com.yzq.zxinglibrary.common.Constant.CODED_CONTENT;

public class DeliveryActivity extends AppCompatActivity {
	EditText mdeliverybatchnumber;
	EditText mdeliverybarcode;
	EditText mdeliverylibrarynumber;
	EditText mdeliveryNumberperbox;
	EditText mdeliverynumboxes;
//	EditText mdeliverystocknumboxes;
	EditText mdeliveryInventoryquantity;
	EditText mdeliveryInventoryquantityboxes;
	EditText mdeliveryOrdernumber;
	ListView mSimpleDetailList;
	ChooseShipAdapter mAdapter;
	String lastproduct_content = "";
	List<DeliveryBean> deliveryBean = new ArrayList<DeliveryBean>();
	private OutStockTask mdeliveryTask = null;

	enum qrcodemode {
		BATCH_NUMBER, LIBRARY_NUMBER, BAR_CODE
	}

	;
	private qrcodemode qrcodetextmode = qrcodemode.BAR_CODE;
	private int boxnum = 0;
	private HashMap<String, HashMap<String, String>> productinfomap = new HashMap<String, HashMap<String, String>>();
	private HashMap<String, HashMap<String, String>> productidmap = new HashMap<String, HashMap<String, String>>();

	private HashMap<String, HashMap<String, String>> locationmap = new HashMap<String, HashMap<String, String>>();
	private HashMap<String, String> librarymap = new HashMap<String, String>();

	private ArrayList<Map<String, String>> boxesnum = new ArrayList<Map<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_delivery);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

			Window window = DeliveryActivity.this.getWindow();

			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

			window.setStatusBarColor(Color.BLACK);


			//底部导航栏

			//window.setNavigationBarColor(activity.getResources().getColor(colorResId));

		}
		mdeliverybatchnumber = (EditText) findViewById(R.id.delivery_batch_number);
//        mdeliverybatchnumber.addTextChangedListener(shipsWatcher);
		mdeliverybarcode = (EditText) findViewById(R.id.delivery_bar_code);//产品编号会有多个
//        mdeliverybarcode.addTextChangedListener(shipsWatcher);
		mdeliverylibrarynumber = (EditText) findViewById(R.id.delivery_library_number);
//        mdeliverylibrarynumber.addTextChangedListener(shipsWatcher);
		mdeliveryNumberperbox = (EditText) findViewById(R.id.delivery_Number_per_box);
//        mdeliveryNumberperbox.addTextChangedListener(shipsWatcher);
		mdeliverynumboxes = (EditText) findViewById(R.id.delivery_num_boxes);
//		mdeliverystocknumboxes = (EditText) findViewById(R.id.delivery_stocknum_boxes);
		mdeliveryInventoryquantity = (EditText) findViewById(R.id.delivery_inventory_quantity);
		mdeliveryInventoryquantityboxes = (EditText) findViewById(R.id.delivery_inventory_boxes);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		mdeliveryOrdernumber = (EditText) findViewById(R.id.delivery_Order_number);

		addTextWatcher();

		mSimpleDetailList = (ListView) findViewById(R.id.delivery_infolist);
		refreshdatalist();

//        Button mdeliverybarcodebtn = (Button) findViewById(R.id.delivery_bar_code_button);
//        Button mdeliverylibrarynumberbtn = (Button) findViewById(R.id.delivery_library_number_button);
//        Button mdeliverybatchnumberbtn = (Button) findViewById(R.id.delivery_batch_number_button);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
	}
	public void refreshdatalist(){
		Collections.sort(deliveryBean,idComparator);
		mAdapter = new ChooseShipAdapter(DeliveryActivity.this, deliveryBean);
		mSimpleDetailList.setAdapter(mAdapter);
		setListViewHeightBasedOnChildren(mSimpleDetailList);
	}

	public void addTextWatcher(){
		mdeliverybatchnumber.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {

				if(!mdeliverybatchnumber.getText().toString().equals("")) {
					GetforminfoTask getforminfotask = new GetforminfoTask();
					getforminfotask.execute((Void) null);
				}
			}
		});
		mdeliverylibrarynumber.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
//				mdeliverylibrarynumber.removeTextChangedListener(this);
				if(!mdeliverybatchnumber.getText().toString().equals("")) {
					CheckLibrarynumTask checklibrarynumTask = new CheckLibrarynumTask();
					checklibrarynumTask.execute();
				}
//				mdeliverylibrarynumber.addTextChangedListener(this);
			}
		});
		mdeliverybarcode.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				String content=mdeliverybarcode.getText().toString();

				if(!content.equals("")) {
					mdeliverybarcode.removeTextChangedListener(this);
//					String product_code="";
//					try {
//						String productinfo[] = content.split(",");
//						for (int i = 0; i < productinfo.length; i++) {
//							String info = productinfo[i];
//							if (i == 0) {
//								product_code = info;
//							}
//						}
//					}catch (Exception e){
//						e.printStackTrace();
//					}
//					mdeliverybarcode.setText(product_code);
//					mdeliverybarcode.addTextChangedListener(this);
					CheckproductinfoTask checkproductinfoTask = new CheckproductinfoTask(content, this);
					checkproductinfoTask.execute();
				}
			}
		});
	}
	public void onClick(View view) {
		if (view.getId() == R.id.delivery_back) {
			finish();
		} else if (view.getId() == R.id.delivery_submit_button) {
			Print("delivery_submit_button:::");
			attemptCheck();
		} else if (view.getId() == R.id.delivery_submitcancle_button) {
			Print("delivery_submit_button:::");
			cancleALLdata();
		} else if (view.getId() == R.id.delivery_warehouse_button) {
			Print("delivery_submit_button:::");
			CheckwareinfoTask mCheckwareinfoTask = new CheckwareinfoTask();
			mCheckwareinfoTask.execute((Void) null);

		} else if (view.getId() == R.id.delivery_bar_code_button) {
			Print("delivery_bar_code_button:::");
			qrcodetextmode = qrcodemode.BAR_CODE;
			Util.startQrCode(DeliveryActivity.this);
		} else if (view.getId() == R.id.delivery_library_number_button) {
			Print("delivery_library_number_button:::");
			qrcodetextmode = qrcodemode.LIBRARY_NUMBER;
			Util.startQrCode(DeliveryActivity.this);
		} else if (view.getId() == R.id.delivery_batch_number_button) {
			Print("delivery_batch_number_button:::");
			qrcodetextmode = qrcodemode.BATCH_NUMBER;
			Util.startQrCode(DeliveryActivity.this);
		}
	}

	@Override

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);


		// 扫描二维码/条码回传

		if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {

			if (data != null) {

				String content = data.getStringExtra(CODED_CONTENT);

//                Util.showShortToastMessage(DeliveryActivity.this,"扫描结果为："+ content);
				if (qrcodetextmode == qrcodemode.BAR_CODE) {
					mdeliverybarcode.setText(content);
//                    CheckproductinfoTask checkproductinfoTask=new CheckproductinfoTask(content);
//					CheckproductinfoTask checkproductinfoTask = new CheckproductinfoTask(content);
//					checkproductinfoTask.execute();
				} else if (qrcodetextmode == qrcodemode.LIBRARY_NUMBER) {
					mdeliverylibrarynumber.setText(content);
//                    mdeliverylibrarynumber.setText("9995-0001");
//					CheckLibrarynumTask checklibrarynumTask = new CheckLibrarynumTask();
//					checklibrarynumTask.execute();//监听了
				} else if (qrcodetextmode == qrcodemode.BATCH_NUMBER) {
					mdeliverybatchnumber.setText(content);//
//                    mdeliverybatchnumber.setText("SD20190625-01");//测试数据
//					GetforminfoTask getforminfotask = new GetforminfoTask();
//					getforminfotask.execute((Void) null);//监听了
				}
			}

		}

	}

	private void attemptCheck() {
		if (mdeliveryTask != null) {
			return;
		}

//
		String lot_no = mdeliverybatchnumber.getText().toString();
		String barcode = mdeliverybarcode.getText().toString();
		String location = mdeliverylibrarynumber.getText().toString();
		String cn_box = mdeliverynumboxes.getText().toString();
		String min_box = mdeliveryNumberperbox.getText().toString();

		String so = mdeliveryOrdernumber.getText().toString();
		boolean cancel = false;
//
		if (lot_no.equals("") ||
				barcode.equals("") ||
				location.equals("") ||
				cn_box.equals("") ||
				min_box.equals("") ||
				so.equals("")) {
			cancel = true;
		}
		if (cancel) {
//            // There was an error; don't attempt login and focus the first
//            // form field with an error.
//            focusView.requestFocus();
			Util.showShortToastMessage(DeliveryActivity.this, "请先扫描所有条目");
		} else {
//            // Show a progress spinner, and kick off a background task to
//            // perform the user login attempt.
//        showProgress(true);
			mdeliveryTask = new OutStockTask();
			mdeliveryTask.execute((Void) null);
		}


	}

	private void cancleALLdata() {
//         mdeliverybatchnumber.setText("");
//         mdeliverybarcode.setText("");
//         mdeliverylibrarynumber.setText("");
//         mdeliveryNumberperbox.setText("");
//         mdeliverynumboxes.setText("");

		mdeliverybarcode.setText("");
		mdeliveryNumberperbox.setText("");
		mdeliverynumboxes.setText("");
//		mdeliverystocknumboxes.setText("");
		mdeliveryInventoryquantity.setText("");
		mdeliveryInventoryquantityboxes.setText("");
		mdeliveryOrdernumber.setText("");
		mdeliveryTask = null;
		boxnum = 0;
//        productinfomap=new HashMap<String, HashMap<String,String>>();
//        locationmap=new HashMap<String, String>();
//        boxesnum=new ArrayList<Map<String,String>>();
//        mAdapter = new ChooseShipAdapter(DeliveryActivity.this, deliveryBean);
//        mSimpleDetailList.setAdapter(mAdapter);
//        setListViewHeightBasedOnChildren(mSimpleDetailList);
	}

	private String email_key = "email";

	public class GetforminfoTask extends AsyncTask<Void, Void, Boolean> {
		String token = "";
		String batch_num = "";
		String success = "";
		String msg = "";
		int responsecode = 0;

		GetforminfoTask() {
			batch_num = mdeliverybatchnumber.getText().toString();
			token = PreferencesUtils.getString(DeliveryActivity.this, token_key, "");
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				String url = "http://" + PreferencesUtils.getString(DeliveryActivity.this, ip_key, "120.27.2.177")
						+ ":" + PreferencesUtils.getString(DeliveryActivity.this, port_key, "8062") +
						IndexConstants.CHECKDELIVERYFORM + "?name=" + batch_num + "&token=" + token;
				Print("url:::" + url);
				URL posturl = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) posturl.openConnection();
				conn.setConnectTimeout(10000);
				//使用Post方式不能使用缓存

				responsecode = conn.getResponseCode();
				if (responsecode == 200) {
					InputStream ins = conn.getInputStream();
					JSONObject rootjsonObject = parseJson(ins);
					JSONObject jsonObject = null;
					if (rootjsonObject != null) {
						jsonObject = rootjsonObject.getJSONArray("results").getJSONObject(0);
					}
					if (jsonObject != null) {
						msg = jsonObject.getString("message");
						success = jsonObject.getString("success");
						if (success.equals("true")) {
							deliveryBean= new ArrayList<DeliveryBean>();
							JSONArray data = jsonObject.getJSONArray("data");
							Print(" return:::" + data);
							for (int d = 0; d < data.length(); d++) {
//                        String token =data.getString("line_data");
								JSONArray line_data = data.getJSONObject(d).getJSONArray("line_data");
								for (int i = 0; i < line_data.length(); i++) {
									DeliveryBean deliver = new DeliveryBean();
									deliver.sequence = line_data.getJSONObject(i).getString("sequence");
									deliver.bar_code = line_data.getJSONObject(i).getString("product_code");
									deliver.product_id = line_data.getJSONObject(i).getString("product_id");
									deliver.product_specification = line_data.getJSONObject(i).getString("product_name");
									deliver.number_applications = line_data.getJSONObject(i).getString("qty");
									deliver.number_boxes = line_data.getJSONObject(i).getString("box_qty");
									deliver.numbers = line_data.getJSONObject(i).getString("product_qty");

									HashMap<String, String> map = new HashMap<String, String>();
									map.put(deliver.bar_code, deliver.product_id);
									productidmap.put(deliver.bar_code + "id", map);

									deliveryBean.add(deliver);

								}
							}
						}
					}
				}
//                Print(" return:::"+responsecode);

			} catch (Exception e) {
				// TODO: handle exception
				System.err.println("未能获取网络数据");
				e.printStackTrace();
			}


			// TODO: register the new account here.
			return success.equals("true");
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			if (success) {
				Util.showShortToastMessage(DeliveryActivity.this, msg);
				mdeliverylibrarynumber.requestFocus();

				refreshdatalist();
				mdeliverybatchnumber.setError(null, null);//焦点聚焦时去除错误图标
			} else {
				Util.showShortToastMessage(DeliveryActivity.this, msg);
				mdeliverybatchnumber.requestFocus();
				mdeliverybatchnumber.setError("出货单号有错");
			}
		}

		@Override
		protected void onCancelled() {
//            mAuthTask = null;
//            showProgress(false);
		}

		private JSONObject parseJson(InputStream ins) {
			byte[] data = new byte[0];   // 把输入流转换成字符数组
			try {
				data = readStream(ins);

				String json = new String(data);        // 把字符数组转换成字符串
				Print("check forminfo msgmsg:::" + json);
//            JSONArray array = new JSONArray(json);
//            for(int i = 0 ; i < array.length() ; i++){
				JSONObject jsonObject = new JSONObject(json);//array.getJSONObject(i);
//                String msg=jsonObject.getString("message");
//                String success=jsonObject.getString("success");
				return jsonObject;
//            }
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}


	}

	public static Comparator idComparator = new Comparator() {
		@Override
		public int compare(Object o1, Object o2) {
			return (Integer.compare(Integer.parseInt(((DeliveryBean) o1).sequence), Integer.parseInt(((DeliveryBean) o2).sequence)));
		}
	};

	public class CheckLibrarynumTask extends AsyncTask<Void, Void, Boolean> {
		String token = "";
		String library_num = "";
		String success = "";
		String msg = "";
		int responsecode = 0;

		CheckLibrarynumTask() {
			library_num = mdeliverylibrarynumber.getText().toString();
			token = PreferencesUtils.getString(DeliveryActivity.this, token_key, "");
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				String url = "http://" + PreferencesUtils.getString(DeliveryActivity.this, ip_key, "120.27.2.177")
						+ ":" + PreferencesUtils.getString(DeliveryActivity.this, port_key, "8062") +
						IndexConstants.CHECKLIBRARY + "?location_barcode=" + library_num + "&token=" + token;
//                "login:","登录帐号","Password":"密码"
				Print("url:::" + url);
				URL posturl = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) posturl.openConnection();
				conn.setConnectTimeout(10000);
				//使用Post方式不能使用缓存

				responsecode = conn.getResponseCode();
				if (responsecode == 200) {
					InputStream ins = conn.getInputStream();
					JSONObject rootjsonObject = parseJson(ins);
					JSONObject jsonObject = null;
					if (rootjsonObject != null) {
						jsonObject = rootjsonObject.getJSONArray("results").getJSONObject(0);
					}
					if (jsonObject != null) {
						msg = jsonObject.getString("message");
						success = jsonObject.getString("success");

						JSONArray data = jsonObject.getJSONArray("data");
						Print(" return:::" + data);
						for (int d = 0; d < data.length(); d++) {
//                        String token =data.getString("line_data");
							JSONObject locationdata = data.getJSONObject(d);
							String warehouse_id = locationdata.getString("warehouse_id");
							String location_id = locationdata.getString("location_id");
							HashMap<String,String> map=new HashMap<>();
							map.put("warehouse_id", warehouse_id);
							map.put("location_id", location_id);
							locationmap.put(library_num ,map);
						}
					}
//                    ins.close();
				}
				Print(" return:::" + responsecode);

			} catch (Exception e) {
				// TODO: handle exception
				System.err.println("未能获取网络数据");
				e.printStackTrace();
			}

			// TODO: register the new account here.
			return success.equals("true");
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			if (success) {
				Util.showShortToastMessage(DeliveryActivity.this, msg);
				mdeliverybarcode.requestFocus();
				mdeliverylibrarynumber.setError(null, null);//焦点聚焦时去除错误图标
			} else {
				Util.showShortToastMessage(DeliveryActivity.this, msg);
				mdeliverylibrarynumber.requestFocus();
				mdeliverylibrarynumber.setError("库位编号有错");
			}
		}

		@Override
		protected void onCancelled() {
//            mAuthTask = null;
//            showProgress(false);
		}

		private JSONObject parseJson(InputStream ins) {
			byte[] data = new byte[0];   // 把输入流转换成字符数组
			try {
				data = readStream(ins);

				String json = new String(data);        // 把字符数组转换成字符串
//            JSONArray array = new JSONArray(json);
//            for(int i = 0 ; i < array.length() ; i++){
				JSONObject jsonObject = new JSONObject(json);//array.getJSONObject(i);
//                String msg=jsonObject.getString("message");
//                String success=jsonObject.getString("success");
				return jsonObject;
//            }
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}


	}

	public class CheckproductinfoTask extends AsyncTask<Void, Void, Boolean> {
		String content = "";
		String token = "";
		String batch_num = "";
		String librarynum="";
		String product_code = "";
		String success = "";
		String msg = "";
		String lot_id = "";
		String qty = "";
		TextWatcher textwatcher;
		int responsecode = 0;

		CheckproductinfoTask(String content,TextWatcher textwatcher) {
			this.content = content;
			this.textwatcher=textwatcher;
			String productinfo[] = content.split(",");
			for (int i = 0; i < productinfo.length; i++) {
				String info = productinfo[i];
				if (i == 0) {
					product_code = info;
				} else if (i == 1) {

				} else if (i == 2) {
					qty = info;
				} else if (i == 3) {

				} else if (i == 4) {
					lot_id = info;
				} else if (i == 5) {

				} else if (i == 6) {

				} else if (i == 7) {

				} else if (i == 8) {

				}
			}
//            product_code=content;
			librarynum=mdeliverylibrarynumber.getText().toString();
			batch_num = mdeliverybatchnumber.getText().toString();
			token = PreferencesUtils.getString(DeliveryActivity.this, token_key, "");

		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				String url = "http://" + PreferencesUtils.getString(DeliveryActivity.this, ip_key, "120.27.2.177")
						+ ":" + PreferencesUtils.getString(DeliveryActivity.this, port_key, "8062") +
						IndexConstants.CHECKDELIVERYPRODUCT + "?product_code=" + product_code + "&name=" + batch_num + "&token=" + token;
//                "login:","登录帐号","Password":"密码"
				Print("lot_id:::" + lot_id + "qty:::" + qty);
				Print("url:::" + url);
				URL posturl = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) posturl.openConnection();
				conn.setConnectTimeout(10000);
				//使用Post方式不能使用缓存

				responsecode = conn.getResponseCode();
				if (responsecode == 200) {
					InputStream ins = conn.getInputStream();
					JSONObject rootjsonObject = parseJson(ins);
					JSONObject jsonObject = null;
					if (rootjsonObject != null) {
						jsonObject = rootjsonObject.getJSONArray("results").getJSONObject(0);
					}
					if (jsonObject != null) {
						msg = jsonObject.getString("message");
						success = jsonObject.getString("success");
//                        JSONObject data =jsonObject.getJSONObject("data");
//                        String token =data.getString("token");
//                        JSONArray rights=data.getJSONArray("rights");//"group_app_mrp_finish_in","group_app_mrp_finish_in_confirm","group_app_mrp_move","group_app_sales_delivery"

					}
//                    ins.close();
				}
				Print(" return:::" + responsecode);

			} catch (Exception e) {
				// TODO: handle exception
				System.err.println("未能获取网络数据");
				e.printStackTrace();
			}

//            for (String credential : DUMMY_CREDENTIALS) {
//                String[] pieces = credential.split(":");
//                if (pieces[0].equals(mEmail)) {
//                    // Account exists, return true if the password matches.
//                    return pieces[1].equals(mPassword);
//                }
//            }

			// TODO: register the new account here.
			return success.equals("true");
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			if (success) {
				Util.showShortToastMessage(DeliveryActivity.this, msg);
				librarymap.put(this.content,librarynum);

				if (this.content.equals(lastproduct_content) ||
						boxnum==0) {
					boxnum++;
					if (boxesnum.size() > 0) {
						boxesnum.remove(boxesnum.size() - 1);
					}
					mdeliverybarcode.setText(product_code);

					saveBoxNum(this.content, boxnum + "");
				} else {
					mdeliverybarcode.setText(product_code);
					boxnum = 1;
					saveBoxNum(this.content, boxnum + "");
				}
				lastproduct_content=this.content;
				mdeliverynumboxes.setText(boxnum + "");
				mdeliveryOrdernumber.setText(lot_id);
				mdeliveryNumberperbox.setText(qty);

				mdeliveryOrdernumber.setFocusable(false);
				mdeliveryOrdernumber.setFocusableInTouchMode(false);
				mdeliveryNumberperbox.setFocusable(false);
				mdeliveryNumberperbox.setFocusableInTouchMode(false);
				mdeliveryInventoryquantity.setFocusable(false);
				mdeliveryInventoryquantity.setFocusableInTouchMode(false);
				mdeliveryInventoryquantityboxes.setFocusable(false);
				mdeliveryInventoryquantityboxes.setFocusableInTouchMode(false);

				mdeliverybarcode.setError(null, null);//焦点聚焦时去除错误图标

				CheckproductlabelTask checkproductlabelTask = new CheckproductlabelTask(content);
				checkproductlabelTask.execute();

				GetinventoryinfoTask getinventoryinfoTask = new GetinventoryinfoTask();
				getinventoryinfoTask.execute();
			} else {
				Util.showShortToastMessage(DeliveryActivity.this, msg);
				mdeliverybarcode.requestFocus();
				mdeliverybarcode.setError(msg);
			}
			mdeliverybarcode.addTextChangedListener(textwatcher);
		}

		@Override
		protected void onCancelled() {
//            mAuthTask = null;
//            showProgress(false);
		}

		private JSONObject parseJson(InputStream ins) {
			byte[] data = new byte[0];   // 把输入流转换成字符数组
			try {
				data = readStream(ins);

				String json = new String(data);        // 把字符数组转换成字符串
				Print("check forminfo msgmsg:::" + json);
//            JSONArray array = new JSONArray(json);
//            for(int i = 0 ; i < array.length() ; i++){
				JSONObject jsonObject = new JSONObject(json);//array.getJSONObject(i);
//                String msg=jsonObject.getString("message");
//                String success=jsonObject.getString("success");
				return jsonObject;
//            }
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}


	}

	public class CheckproductlabelTask extends AsyncTask<Void, Void, Boolean> {
		String token = "";
		String batch_num = "";
		String product_code = "";
		String success = "";
		String msg = "";
		String lot_id = "";
		String lot_name = "";
		String package_name = "";
		String qty = "";
		String content="";
		int responsecode = 0;

		CheckproductlabelTask(String content) {
			String productinfo[] = content.split(",");
			for (int i = 0; i < productinfo.length; i++) {
				String info = productinfo[i];
				if (i == 0) {
					product_code = info;
				} else if (i == 1) {
					lot_id = info;
				} else if (i == 2) {
					qty = info;
				} else if (i == 3) {

				} else if (i == 4) {
					lot_name = info;
				} else if (i == 5) {
					package_name = info;
				} else if (i == 6) {

				} else if (i == 7) {

				}
			}

			this.content=content;
//            product_code=content;
			batch_num = mdeliverybatchnumber.getText().toString();
			token = PreferencesUtils.getString(DeliveryActivity.this, token_key, "");
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				String url = "http://" + PreferencesUtils.getString(DeliveryActivity.this, ip_key, "120.27.2.177")
						+ ":" + PreferencesUtils.getString(DeliveryActivity.this, port_key, "8062") +
						IndexConstants.CHECKDELIVERYPRODUCTLABEL + "?product_code=" + product_code +
						"&lot_no=" + lot_name + "&package_name=" + package_name + "&token=" + token;
				Print("url:::" + url);
				URL posturl = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) posturl.openConnection();
				conn.setConnectTimeout(10000);
				//使用Post方式不能使用缓存

				responsecode = conn.getResponseCode();
				if (responsecode == 200) {
					InputStream ins = conn.getInputStream();
					JSONObject rootjsonObject = parseJson(ins);
					JSONObject jsonObject = null;
					if (rootjsonObject != null) {
						jsonObject = rootjsonObject.getJSONArray("results").getJSONObject(0);
					}
					if (jsonObject != null) {
						msg = jsonObject.getString("message");
						success = jsonObject.getString("success");
						if (success.equals("true")) {
							HashMap<String, String> map = new HashMap<String, String>();
							JSONObject data = jsonObject.getJSONObject("data");
							String ownerid = data.getString("owner_id");
							String package_name = data.getString("package_name");
							String lot_id = data.getString("lot_id");
							String package_id = data.getString("package_id");
							map.put("owner_id", ownerid);
							map.put("package_name", package_name);
							map.put("lot_id", lot_id);
							map.put("package_id", package_id);
							productinfomap.put(content, map);

						}
					}
//                    ins.close();
				}
				Print(" return:::" + responsecode);

			} catch (Exception e) {
				// TODO: handle exception
				System.err.println("未能获取网络数据");
				e.printStackTrace();
			}


			// TODO: register the new account here.
			return success.equals("true");
		}

		@Override
		protected void onPostExecute(final Boolean success) {
            if (success) {
//                Util.showShortToastMessage(DeliveryActivity.this,msg);
                mdeliverybatchnumber.setError(null,null);
            } else {
	            mdeliverybatchnumber.requestFocus();
	            mdeliverybatchnumber.setError(msg);
                Util.showShortToastMessage(DeliveryActivity.this, msg);
            }
		}

		@Override
		protected void onCancelled() {
//            mAuthTask = null;
//            showProgress(false);
		}

		private JSONObject parseJson(InputStream ins) {
			byte[] data = new byte[0];   // 把输入流转换成字符数组
			try {
				data = readStream(ins);

				String json = new String(data);        // 把字符数组转换成字符串
				Print("check forminfo msgmsg:::" + json);
//            JSONArray array = new JSONArray(json);
//            for(int i = 0 ; i < array.length() ; i++){
				JSONObject jsonObject = new JSONObject(json);//array.getJSONObject(i);
//                String msg=jsonObject.getString("message");
//                String success=jsonObject.getString("success");
				return jsonObject;
//            }
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}


	}

	public class GetinventoryinfoTask extends AsyncTask<Void, Void, Boolean> {
		String token = "";
		String product_code = "";
		String success = "";
		String msg = "";
		String qty = "";//,#数量
		String box_qty = "";// 8#箱数
		int responsecode = 0;

		GetinventoryinfoTask() {
			product_code = mdeliverybarcode.getText().toString();
			token = PreferencesUtils.getString(DeliveryActivity.this, token_key, "");

		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				String url = "http://" + PreferencesUtils.getString(DeliveryActivity.this, ip_key, "120.27.2.177")
						+ ":" + PreferencesUtils.getString(DeliveryActivity.this, port_key, "8062") +
						IndexConstants.CHECKDELIVERYINVENTORY + "?product_code=" + product_code + "&token=" + token;

				Print("url:::" + url);
				URL posturl = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) posturl.openConnection();
				conn.setConnectTimeout(10000);
				//使用Post方式不能使用缓存

				responsecode = conn.getResponseCode();
				if (responsecode == 200) {
					InputStream ins = conn.getInputStream();

					JSONObject rootjsonObject = parseJson(ins);
					JSONObject jsonObject = null;
					if (rootjsonObject != null) {
						jsonObject = rootjsonObject.getJSONArray("results").getJSONObject(0);
					}
					if (jsonObject != null) {
						msg = jsonObject.getString("message");
						success = jsonObject.getString("success");
						Print("parse submit json:::" + success);
//                        JSONObject data =jsonObject.getJSONObject("data");
//                        String token =data.getString("token");
//                        JSONArray rights=data.getJSONArray("rights");//"group_app_mrp_finish_in","group_app_mrp_finish_in_confirm","group_app_mrp_move","group_app_sales_delivery"
						JSONArray data = jsonObject.getJSONArray("data");
						Print(" return:::" + data);
						for (int d = 0; d < data.length(); d++) {
//                        String token =data.getString("line_data");
							JSONObject numdata = data.getJSONObject(d);
							box_qty = numdata.getString("box_qty");
							qty = numdata.getString("qty");
						}
					}
				}
				Print(" return:::" + responsecode);

			} catch (Exception e) {
				// TODO: handle exception
				System.err.println("未能获取网络数据");
				e.printStackTrace();
			}


			// TODO: register the new account here.
			return success.equals("true");
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			if (success) {

				mdeliveryInventoryquantity.setText(qty);
				mdeliveryInventoryquantityboxes.setText(box_qty);
//                mdeliveryNumberperbox.setText(qty);

			} else {
				Util.showShortToastMessage(DeliveryActivity.this, msg);
//                mdeliverybarcode.setError("产品编号有错");
			}
		}

		@Override
		protected void onCancelled() {
//            mAuthTask = null;
//            showProgress(false);
		}

		private JSONObject parseJson(InputStream ins) {
			byte[] data = new byte[0];   // 把输入流转换成字符数组
			try {
				data = readStream(ins);

				String json = new String(data);        // 把字符数组转换成字符串
				Print("check forminfo msgmsg:::" + json);
//            JSONArray array = new JSONArray(json);
//            for(int i = 0 ; i < array.length() ; i++){
				JSONObject jsonObject = new JSONObject(json);//array.getJSONObject(i);
//                String msg=jsonObject.getString("message");
//                String success=jsonObject.getString("success");
				return jsonObject;
//            }
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}


	}

	public class CheckwareinfoTask extends AsyncTask<Void, Void, Boolean> {
		String token = "";
		String batch_num = "";
		String success = "";
		String msg = "";
		int responsecode = 0;

		CheckwareinfoTask() {
			batch_num = mdeliverybatchnumber.getText().toString();
			token = PreferencesUtils.getString(DeliveryActivity.this, token_key, "");
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				String url = "http://" + PreferencesUtils.getString(DeliveryActivity.this, ip_key, "120.27.2.177")
						+ ":" + PreferencesUtils.getString(DeliveryActivity.this, port_key, "8062") +
						IndexConstants.CHECKDELIVERYWAREHOUSE + "?name=" + batch_num + "&token=" + token;
				Print("url:::" + url);
				URL posturl = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) posturl.openConnection();
				conn.setConnectTimeout(10000);
				//使用Post方式不能使用缓存

				responsecode = conn.getResponseCode();
				if (responsecode == 200) {
					InputStream ins = conn.getInputStream();
					JSONObject rootjsonObject = parseJson(ins);
					JSONObject jsonObject = null;
					if (rootjsonObject != null) {
						jsonObject = rootjsonObject.getJSONArray("results").getJSONObject(0);
					}
					if (jsonObject != null) {
						msg = jsonObject.getString("message");
						success = jsonObject.getString("success");
						if (success.equals("true")) {
							JSONArray data = jsonObject.getJSONArray("data");
							Print(" return:::" + data);
							for (int d = 0; d < data.length(); d++) {
//                          String token =data.getString("line_data");

							}
						}
					}
				}
//                Print(" return:::"+responsecode);

			} catch (Exception e) {
				// TODO: handle exception
				System.err.println("未能获取网络数据");
				e.printStackTrace();
			}


			// TODO: register the new account here.
			return success.equals("true");
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			if (success) {
				Util.showShortToastMessage(DeliveryActivity.this, msg);

			} else {
				Util.showShortToastMessage(DeliveryActivity.this, msg);
			}
		}

		@Override
		protected void onCancelled() {
//            mAuthTask = null;
//            showProgress(false);
		}

		private JSONObject parseJson(InputStream ins) {
			byte[] data = new byte[0];   // 把输入流转换成字符数组
			try {
				data = readStream(ins);

				String json = new String(data);        // 把字符数组转换成字符串
				Print("check forminfo msgmsg:::" + json);
//            JSONArray array = new JSONArray(json);
//            for(int i = 0 ; i < array.length() ; i++){
				JSONObject jsonObject = new JSONObject(json);//array.getJSONObject(i);
//                String msg=jsonObject.getString("message");
//                String success=jsonObject.getString("success");
				return jsonObject;
//            }
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}


	}

	private void saveBoxNum(String code, String num) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(code, num);
		boxesnum.add(map);//存起每个产品的数量 提交使用
	}

	public class CheckCodeTask extends AsyncTask<Void, Void, Boolean> {
		//        String lot_no="";
		String location = "";
		String barcode = "";
		String lot_no = "";
		String success = "";
		String msg = "";
		int responsecode = 0;

		CheckCodeTask() {
			lot_no = mdeliverybatchnumber.getText().toString();
			barcode = mdeliverybarcode.getText().toString();
			location = mdeliverylibrarynumber.getText().toString();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				String url = "http://" + PreferencesUtils.getString(DeliveryActivity.this, ip_key, "120.27.2.177")
						+ ":" + PreferencesUtils.getString(DeliveryActivity.this, port_key, "8062") + IndexConstants.TAKINGCHECKBARCODE;
//                "login:","登录帐号","Password":"密码"
				Print("url:::" + url);
				Map<String, String> mparams = new HashMap<String, String>();
				mparams.put("login", PreferencesUtils.getString(DeliveryActivity.this, email_key, "8062"));
				mparams.put("lot_no", lot_no);
				mparams.put("barcode", barcode);
				mparams.put("location", location);


				String postparams = new Gson().toJson(mparams);
//                postparams=URLEncoder.encode(postparams,"utf-8");

//                String postparams ="{"+"login:",mEmail,"Password:",mPassword}//"login:"+mEmail+"&password:"+mPassword;
				byte[] data = postparams.getBytes();
//                System.err.println("postparams postparams:::"+postparams+data.length);
				URL posturl = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) posturl.openConnection();
				conn.setConnectTimeout(10000);
				conn.setDoInput(true);                  //打开输入流，以便从服务器获取数据
				conn.setDoOutput(true);                 //打开输出流，以便向服务器提交数据
				conn.setRequestMethod("POST");     //设置以Post方式提交数据
				conn.setUseCaches(false);               //使用Post方式不能使用缓存
				//设置请求体的类型是文本类型
				conn.setRequestProperty("Content-Type", "application/json");
				conn.setRequestProperty("Content-Length", String.valueOf(data.length)); // 注意是字节长度, 不是字符长度

//                conn.setDoOutput(true); // 准备写出
				conn.getOutputStream().write(data);

				responsecode = conn.getResponseCode();
				if (responsecode == 200) {
					InputStream ins = conn.getInputStream();
					JSONObject jsonObject = parseJson(ins);
					if (jsonObject != null) {
						msg = jsonObject.getString("message");
						success = jsonObject.getString("success");
					}
//                    String s = ins.toString();
//                    System.err.println("sssssssss:::"+s);
				}
				Print(" return:::" + responsecode);
//                ins.close();
			} catch (Exception e) {
				// TODO: handle exception
				System.err.println("未能获取网络数据");
				e.printStackTrace();
			}

//            for (String credential : DUMMY_CREDENTIALS) {
//                String[] pieces = credential.split(":");
//                if (pieces[0].equals(mEmail)) {
//                    // Account exists, return true if the password matches.
//                    return pieces[1].equals(mPassword);
//                }
//            }

			// TODO: register the new account here.
			return success.equals("1");
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mdeliveryTask = null;
//            showProgress(false);
//            Util.showShortToastMessage(EntryActivity.this,msg);
			if (success) {
//                PreferencesUtils.putString(EntryActivity.this,email_key,mEmail);
//                PreferencesUtils.putString(EntryActivity.this,password_key,mPassword);
//                Intent intent=new Intent(EntryActivity.this,MainActivity.class);
//                EntryActivity.this.startActivity(intent);
//                finish();
				OutStockTask tst = new OutStockTask();
				tst.execute();
			} else {
				Util.showShortToastMessage(DeliveryActivity.this, msg);
//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
//            mAuthTask = null;
//            showProgress(false);
		}

		private JSONObject parseJson(InputStream ins) {
			byte[] data = new byte[0];   // 把输入流转换成字符数组
			try {
				data = readStream(ins);

				String json = new String(data);        // 把字符数组转换成字符串
//            JSONArray array = new JSONArray(json);
//            for(int i = 0 ; i < array.length() ; i++){
				JSONObject jsonObject = new JSONObject(json);//array.getJSONObject(i);
//                String msg=jsonObject.getString("message");
//                String success=jsonObject.getString("success");
				return jsonObject;
//                Print("login msgmsg:::"+msg);
//            }
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}


	}

	public class OutStockTask extends AsyncTask<Void, Void, Boolean> {
		//        String lot_no="";
//        String location="";
		String barcode = "";
		String success = "";
		String msg = "";
		String owner_id = "";
//		String package_id = "";
		String batch_num = "";
//		String lot_id = "";//批号
//		String qty = "";//数量
//		String box_qty = "";//箱数
		String token = "";//token
		int responsecode = 0;

		OutStockTask() {


			batch_num = mdeliverybatchnumber.getText().toString();
			barcode = mdeliverybarcode.getText().toString();
//            String Warehouse=mdeliverylibrarynumber.getText().toString();

//			box_qty = mdeliverynumboxes.getText().toString();
//			qty = mdeliveryNumberperbox.getText().toString();

			token = PreferencesUtils.getString(DeliveryActivity.this, token_key, "");


			if (!(boxnum + "").equals(mdeliverynumboxes.getText().toString())) {

				if (boxesnum.size() > 0) {
					boxesnum.remove(boxesnum.size() - 1);
				}
				saveBoxNum(lastproduct_content, mdeliverynumboxes.getText().toString());
			}

		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				String url = "http://" + PreferencesUtils.getString(DeliveryActivity.this, ip_key, "120.27.2.177")
						+ ":" + PreferencesUtils.getString(DeliveryActivity.this, port_key, "8062") +
						IndexConstants.OUTSTOCK + "?";//+ "token="+PreferencesUtils.getString(DeliveryActivity.this,token_key,"");
//                "login:","登录帐号","Password":"密码"
//				Map<String, String> mparams = new HashMap<String, String>();
				url = url + "delivery_no=" + batch_num;
//                mparams.put("login",PreferencesUtils.getString(DeliveryActivity.this,email_key,"8062"));
//                Print("boxesnum.size():::"+boxesnum.size());
//                String mparams="";
				String jsondata = "{";
				String parajsondata = "";
				String productkey="";
				for (int i = 0; i < boxesnum.size(); i++) {
					Print("productinfomap. key:::" + locationmap.size()+"   "+productinfomap.keySet());
					Map<String, HashMap<String, Integer>> data
							= new HashMap<String, HashMap<String, Integer>>();
					for (String contentkey : boxesnum.get(i).keySet()) {
						String qty="";
						String lot_id = "";
						String location_id = "";
						String warehouse_id = "";
						String package_id = "";
						String productinfo[] = contentkey.split(",");
						for (int j = 0; j< productinfo.length; j++) {
							String info = productinfo[j];
							if (j == 0) {
								productkey = info;
							} else if (j == 1) {

							} else if (j == 2) {
								qty = info;
							} else if (j == 3) {

							} else if (j == 4) {
								lot_id = info;
							} else if (j == 5) {

							} else if (j == 6) {

							} else if (j == 7) {

							} else if (j == 8) {

							}
						}
						warehouse_id = locationmap.get(librarymap.get(contentkey)).get("warehouse_id");
						location_id = locationmap.get(librarymap.get(contentkey)).get("location_id");
						lot_id = productinfomap.get(contentkey).get("lot_id");
						owner_id = productinfomap.get(contentkey).get("owner_id").replace("false", "False");
						package_id = productinfomap.get(contentkey).get("package_id");
						HashMap<String,String> produntinfo=productidmap.get(productkey + "id");
						String product_id = produntinfo.get(productkey);
						String key = "(" + product_id + "," + warehouse_id + ","
								+ location_id + "," + lot_id + "," + package_id + "," + owner_id + ")";
						String boxnum = boxesnum.get(i).get(contentkey);
//                        String value="{\"qty\"="+boxnum+",\"box_qty\"="+boxnum+"}}";
						HashMap<String, Integer> nummap = new HashMap<String, Integer>();
						nummap.put("qty", Integer.valueOf(qty));
						nummap.put("box_qty", Integer.valueOf(boxnum));
						data.put(key, nummap);
					}
					parajsondata = new Gson().toJson(data);
					parajsondata=parajsondata.substring(1,parajsondata.length()-1);
					jsondata = jsondata+parajsondata;
					if(i==(boxesnum.size()-1)){
						jsondata=jsondata+"}";
					}else{
						jsondata=jsondata+",";
					}
					System.out.println("jsondata:::" + jsondata);
//					mparams.put("data", jsondata);
//					url = url + "&data=" + jsondata;
				}
				url = url + "&data=" + jsondata;
//              mparams.put("delivery_no",batch_num);
//              JSONObject testjsonObject = new JSONObject(mparams);
//              System.out.println("输出的结果是：" + testjsonObject);

//                String postparams = "{\"params\":{\"data\":"+jsondata+","+"\"delivery_no\":\""+batch_num+"\"}}";
//                Print("urlpostparams:::"+postparams);
//                postparams= URLEncoder.encode(postparams,"utf-8");
//
////                String postparams ="{"+"login:",mEmail,"Password:",mPassword}//"login:"+mEmail+"&password:"+mPassword;
//                byte[] data = postparams.getBytes();
//                System.err.println("postparams postparams:::"+postparams+data.length);
//                url=url+"&"+mparams;
				Print("delivery url:::" + url);
				URL posturl = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) posturl.openConnection();
				conn.setConnectTimeout(10000);
//                conn.setDoInput(true);                  //打开输入流，以便从服务器获取数据
//                conn.setDoOutput(true);                 //打开输出流，以便向服务器提交数据
//                conn.setRequestMethod("POST");     //设置以Post方式提交数据
//                conn.setUseCaches(false);               //使用Post方式不能使用缓存
//                //设置请求体的类型是文本类型
//                conn.setRequestProperty("Content-Type", "application/json");
//                conn.setRequestProperty("Content-Length", String.valueOf(data.length)); // 注意是字节长度, 不是字符长度

//                conn.setDoOutput(true); // 准备写出
//                conn.getOutputStream().write(data);

				responsecode = conn.getResponseCode();
				if (responsecode == 200) {
					InputStream ins = conn.getInputStream();
					JSONObject rootjsonObject = parseJson(ins);
					JSONObject jsonObject = null;
					if (rootjsonObject != null) {
						jsonObject = rootjsonObject.getJSONArray("results").getJSONObject(0);
					}
					if (jsonObject != null) {
						msg = jsonObject.getString("message");
						success = jsonObject.getString("success");
						Print("parse submit json:::" + success);
						if (success.equals("true")) {
							deliveryBean= new ArrayList<DeliveryBean>();
							JSONObject data = jsonObject.getJSONObject("data");
//							Print(" return:::" + data);
//							for (int d = 0; d < data.length(); d++) {
////                        String token =data.getString("line_data");
								JSONArray line_data = data.getJSONArray("line_data");//data.getJSONObject(d).getJSONArray("line_data");
								for (int i = 0; i < line_data.length(); i++) {
									DeliveryBean deliver = new DeliveryBean();
									deliver.sequence = line_data.getJSONObject(i).getString("sequence");
									deliver.bar_code = line_data.getJSONObject(i).getString("product_code");
									deliver.product_id = line_data.getJSONObject(i).getString("product_id");
									deliver.product_specification = line_data.getJSONObject(i).getString("product_name");
									deliver.number_applications = line_data.getJSONObject(i).getString("qty");
									deliver.number_boxes = line_data.getJSONObject(i).getString("box_qty");
									deliver.numbers = line_data.getJSONObject(i).getString("product_qty");

//									HashMap<String, String> map = new HashMap<String, String>();
//									map.put(deliver.bar_code, deliver.product_id);
//									productidmap.put(deliver.bar_code + "id", map);

									deliveryBean.add(deliver);

								}
//							}
						}
					}
//                    ins.close();
				}
				Print(" return:::" + responsecode);
//                ins.close();
			} catch (Exception e) {
				// TODO: handle exception
				System.err.println("未能获取网络数据");
				e.printStackTrace();
			}

//            for (String credential : DUMMY_CREDENTIALS) {
//                String[] pieces = credential.split(":");
//                if (pieces[0].equals(mEmail)) {
//                    // Account exists, return true if the password matches.
//                    return pieces[1].equals(mPassword);
//                }
//            }

			// TODO: register the new account here.
			return success.equals("true");
		}

		@Override
		protected void onPostExecute(final Boolean success) {
//            mAuthTask = null;
//            showProgress(false);
//            if() {
			Util.showShortToastMessage(DeliveryActivity.this, msg);
//            }
			if (success) {
//                PreferencesUtils.putString(DeliveryActivity.this,email_key,mEmail);
//                PreferencesUtils.putString(DeliveryActivity.this,password_key,mPassword);
//                Intent intent=new Intent(DeliveryActivity.this,MainActivity.class);
//                DeliveryActivity.this.startActivity(intent);
//                finish();
//				GetforminfoTask getforminfotask = new GetforminfoTask();
//				getforminfotask.execute((Void) null);
				cancleALLdata();
				refreshdatalist();
//				boxnum = 0;
//				mdeliverybatchnumber.setText("");
//				mdeliverybarcode.setText("");
//				mdeliverylibrarynumber.setText("");
//				mdeliveryNumberperbox.setText("");
//				mdeliverynumboxes.setText("");
//				mdeliveryOrdernumber.setText("");
//				mdeliveryInventoryquantity.setText("");
//				mdeliveryInventoryquantityboxes.setText("");
//				mdeliverybatchnumber.requestFocus();
			} else {
//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
			}
			mdeliveryTask = null;
		}

		@Override
		protected void onCancelled() {
//            mAuthTask = null;
//            showProgress(false);
		}

		private JSONObject parseJson(InputStream ins) {
			byte[] data = new byte[0];   // 把输入流转换成字符数组
			try {
				data = readStream(ins);

				String json = new String(data);        // 把字符数组转换成字符串
//            JSONArray array = new JSONArray(json);
//            for(int i = 0 ; i < array.length() ; i++){
				JSONObject jsonObject = new JSONObject(json);//array.getJSONObject(i);
//                String msg=jsonObject.getString("message");
//                String success=jsonObject.getString("success");
				return jsonObject;
//                Print("login msgmsg:::"+msg);
//            }
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}


	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		Util.showShortToastMessage(EntryWarehouseActivity.this,"keycode:"+keyCode);
//		mentrynumboxes.setError(boxnum+"keyCode:"+keyCode);
		if(keyCode==301) {
			if (mdeliverybatchnumber.isFocused()) {
				mdeliverybatchnumber.setText("");
			}else if(mdeliverybarcode.isFocused()){
				mdeliverybarcode.setText("");
			}else if(mdeliverylibrarynumber.isFocused()){
				mdeliverylibrarynumber.setText("");
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	String TAG = "Deliveryactivity::";

	public void Print(String s) {
		System.out.println(TAG + s);
	}
}
