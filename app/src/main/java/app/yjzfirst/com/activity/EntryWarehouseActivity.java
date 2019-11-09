package app.yjzfirst.com.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.annotations.Until;
import com.yjzfirst.adapter.ChooseShipAdapter;
import com.yjzfirst.adapter.EntrydetailAdapter;
import com.yjzfirst.bean.DeliveryBean;
import com.yjzfirst.bean.EntryProductBean;
import com.yjzfirst.bean.EntryProductinfoBean;
import com.yjzfirst.bean.ReportFormBean;
import com.yjzfirst.bean.ReportProductBean;
import com.yjzfirst.bean.WorkOrderBean;
import com.yjzfirst.util.IndexConstants;
import com.yjzfirst.util.PreferencesUtils;
import com.yjzfirst.util.Util;
import com.yjzfirst.util.beanParseUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.yjzfirst.util.IndexConstants.ip_key;
import static com.yjzfirst.util.IndexConstants.port_key;
import static com.yjzfirst.util.IndexConstants.token_key;
import static com.yjzfirst.util.Util.REQUEST_CODE_SCAN;
import static com.yjzfirst.util.Util.readStream;
import static com.yjzfirst.util.Util.setListViewHeightBasedOnChildren;
import static com.yjzfirst.util.Util.textsetError;
import static com.yzq.zxinglibrary.common.Constant.CODED_CONTENT;

public class EntryWarehouseActivity extends AppCompatActivity {
	EditText mentryorderid;
	EditText mentrybarcode;
	EditText mentryOrdernumber;
	EditText mentryNumberperbox;
	EditText mentrynumboxes;
	EditText mentrywarenumber;
	TextView stateview;
	CheckBox finalbox;
	CheckBox defaultloccheckbox;
	String use_default_location="";
	String state="";
	EntryProductBean productBean;
	enum qrcodemode  {
		WORKORDER_ID, PRODUCT_CODE,WAREHOUSE_CODE
	};
	private qrcodemode qrcodetextmode=qrcodemode.WORKORDER_ID;
	WorkOrderBean ReportProductBean;
	String Productcontent="";
	String Ordername="";
	ListView mSimpleDetailList;
	EntrydetailAdapter mAdapter;
	private HashMap<String, String> locationmap = new HashMap<String, String>();
	private HashMap<String,HashMap<String,EntryProductBean>> productinfomap
			=new HashMap<String, HashMap<String,EntryProductBean>>();
	ArrayList<EntryProductinfoBean> EntryProductinfoBeans = new ArrayList<EntryProductinfoBean>();
	//    private CheckCodeTask mentryTask = null;
	private int boxnum=0;
	private ArrayList<Map<String,String>> boxesnum=new ArrayList<Map<String,String>>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_entryware);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

			Window window = EntryWarehouseActivity.this.getWindow();

			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

			window.setStatusBarColor(Color.BLACK);



			//底部导航栏

			//window.setNavigationBarColor(activity.getResources().getColor(colorResId));

		}
		mentryorderid = (EditText) findViewById(R.id.entryform_orderid);
//      mentrybatchnumber.addTextChangedListener(shipsWatcher);
		mentrywarenumber = (EditText) findViewById(R.id.entryform_warehouse_code);
		mentrybarcode = (EditText) findViewById(R.id.entryform_bar_code);
//      mentrybarcode.addTextChangedListener(shipsWatcher);
		mentryOrdernumber = (EditText) findViewById(R.id.entryform_order_number);
//      mentrylibrarynumber.addTextChangedListener(shipsWatcher);
		mentryNumberperbox = (EditText) findViewById(R.id.entryform_Number_per_box);
		mentryNumberperbox.setFocusable(false);
		mentryNumberperbox.setFocusableInTouchMode(false);
//        mentryNumberperbox.addTextChangedListener(shipsWatcher);
		mentrynumboxes = (EditText) findViewById(R.id.entryform_num_boxes);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		stateview= (TextView) findViewById(R.id.entryware_state);
		addTextWatcher();

		finalbox= (CheckBox) findViewById(R.id.entryform_finalbox);
		finalbox.setClickable(false);

		defaultloccheckbox= (CheckBox) findViewById(R.id.check_entry_defaultloc);
		defaultloccheckbox.setChecked(false);

		mSimpleDetailList = (ListView) findViewById(R.id.entry_infolist);
		refreshdatalist();
	}

	public void addTextWatcher(){
		mentryorderid.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				String content=mentryorderid.getText().toString();
				if(!content.equals("")) {
					CheckOrderidTask checkorderidTask = new CheckOrderidTask(content, true);
					checkorderidTask.execute();
				}

			}
		});
		mentrywarenumber.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				String content=mentrywarenumber.getText().toString();
				if(!content.equals("")) {
					CheckLibrarynumTask checkprodyctidTask = new CheckLibrarynumTask(content);
					checkprodyctidTask.execute();
				}

			}
		});
		mentrybarcode.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				String content=mentrybarcode.getText().toString();
				if(!content.equals("")) {
					mentrybarcode.removeTextChangedListener(this);
					CheckProductidTask checkprodyctidTask = new CheckProductidTask(content, this);
					checkprodyctidTask.execute();
				}
			}
		});
	}
	public void refreshdatalist(){
		Collections.sort(EntryProductinfoBeans,idComparator);
		mAdapter = new EntrydetailAdapter(this, EntryProductinfoBeans,"EntryWarehouseActivity");
		mSimpleDetailList.setAdapter(mAdapter);
		setListViewHeightBasedOnChildren(mSimpleDetailList);
	}
	public void onClick(View view) {
		if (view.getId() == R.id.entryform_back) {
			finish();
		}else if (view.getId() == R.id.entryform_orderid_button) {
			qrcodetextmode=qrcodemode.WORKORDER_ID;
			Util.startQrCode(EntryWarehouseActivity.this);
//            attemptCheck();
		}else if (view.getId() == R.id.entryform_warehouse_code_button) {
			qrcodetextmode=qrcodemode.WAREHOUSE_CODE;
			Util.startQrCode(EntryWarehouseActivity.this);
//            attemptCheck();
		}else if(view.getId() == R.id.entryform_bar_code_button){
			qrcodetextmode=qrcodemode.PRODUCT_CODE;
			Util.startQrCode(EntryWarehouseActivity.this);
		}else if(view.getId() == R.id.entryform_scan){
			if(!Productcontent.equals("")) {
				AddOrderidTask addorderidTask = new AddOrderidTask(Productcontent);
				addorderidTask.execute();
			}else{
				Util.showShortToastMessage(EntryWarehouseActivity.this,"请先扫描产品二维码");
			}
		}else if(view.getId() == R.id.entryform_canclescan){
			cancleALLdata();
			EntryProductinfoBeans = new ArrayList<EntryProductinfoBean>();
		}else if(view.getId() == R.id.entryform_submit_button){
			if(!mentryorderid.getText().toString().equals("")) {
				SubmitOrderTask submitorderTask = new SubmitOrderTask();
				submitorderTask.execute();
			}else{
				Util.showShortToastMessage(EntryWarehouseActivity.this,"请先扫描单号");
			}
		}
//		else if(view.getId() == R.id.entryform_finalbox){
//			if(boxesnum.size()>0) {
//				Map<String,String> map=boxesnum.get(boxesnum.size()-1);
//				String content="";
//				for(String key :map.keySet()) {
//					String[] workorderinfo=key.split(",");
//					if(workorderinfo.length>7) {
//						String checkfinalbox="";
//						if(finalbox.isChecked()){
//							checkfinalbox="true";
//						}else{
//							checkfinalbox="false";
//						}
//						content=workorderinfo[0]+","+workorderinfo[1]+","+workorderinfo[2]
//								+","+workorderinfo[3]+","+workorderinfo[4]
//								+","+workorderinfo[5]+","+workorderinfo[6]+","+checkfinalbox;
//					}
//				}
//				boxesnum.remove(boxesnum.size() - 1);
//				saveBoxNum(content,mentrynumboxes.getText().toString());
//			}
//		}
	}

	@Override

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);



		// 扫描二维码/条码回传

		if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {

			if (data != null) {

				String content = data.getStringExtra(CODED_CONTENT);

				Util.showShortToastMessage(EntryWarehouseActivity.this,"扫描结果为："+ content);
				if(qrcodetextmode==qrcodemode.WORKORDER_ID){
					mentryorderid.setText(content);
//                    CheckproductinfoTask checkproductinfoTask=new CheckproductinfoTask(content);
//					CheckOrderidTask checkorderidTask=new CheckOrderidTask(content,true);
//					checkorderidTask.execute();
				}else if(qrcodetextmode==qrcodemode.PRODUCT_CODE){
					mentrybarcode.setText(content);
//					CheckProductidTask checkprodyctidTask=new CheckProductidTask(content);
//					checkprodyctidTask.execute();
				}else if(qrcodetextmode==qrcodemode.WAREHOUSE_CODE){
					mentrywarenumber.setText(content);
//					CheckLibrarynumTask checkprodyctidTask=new CheckLibrarynumTask(content);
//					checkprodyctidTask.execute();
				}
			}

		}

	}

	private void cancleALLdata(){

//		mentryorderid.setText("");
		mentrybarcode.setText("");
		mentryOrdernumber.setText("");
		mentryNumberperbox.setText("");
		mentrynumboxes.setText("");
		mentrywarenumber.setText("");
		finalbox.setChecked(false);
//        defaultloccheckbox.setChecked(false);
		mentrybarcode.setError(null,null);
		mentrywarenumber.setError(null, null);//焦点聚焦时去除错误图标
		mentryorderid.setError(null,null);

		mentrywarenumber.requestFocus();
		boxnum=0;
		productBean=null;
		ReportProductBean=null;
		Productcontent="";
		productinfomap
				=new HashMap<String, HashMap<String,EntryProductBean>>();
		boxesnum=new ArrayList<Map<String,String>>();
	}

	private void resetdata(){
		locationmap = new HashMap<String, String>();
		productinfomap
				=new HashMap<String, HashMap<String,EntryProductBean>>();
		EntryProductinfoBeans = new ArrayList<EntryProductinfoBean>();
		//    private CheckCodeTask mentryTask = null;
		boxnum=0;
		boxesnum=new ArrayList<Map<String,String>>();
	}

	private void saveBoxNum(String code,String num){
		for (int i = 0; i < boxesnum.size(); i++) {

			for (String contentkey : boxesnum.get(i).keySet()) {
				if (code.equals(contentkey)) {
					String oldnumstr = boxesnum.get(i).get(contentkey);
					try {
						int oldnum=Integer.valueOf(oldnumstr);
						int newnum=Integer.valueOf(num);
						int nownum=oldnum+newnum;
						System.err.println("oldnum "+oldnum+"newnum"+newnum+" now "+nownum);
						Map<String, String> map = new HashMap<String, String>();
						map.put(code, nownum+"");
						boxesnum.remove(i);
						boxesnum.add(map);//存起每个产品的数量 提交使用
						return;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		}

		Map<String, String> map = new HashMap<String, String>();
		map.put(code, num);
		boxesnum.add(map);//存起每个产品的数量 提交使用
		return;
	}
	//    private void attemptCheck() {
//        if (mentryTask != null) {
//            return;
//        }
////
////        // Reset errors.
////        mEmailView.setError(null);
////        mPasswordView.setError(null);
////
////        // Store values at the time of the login attempt.
////        String email = mEmailView.getText().toString();
////        String password = mPasswordView.getText().toString();
////
//        String lot_no=mentrybatchnumber.getText().toString();
//        String barcode=mentrybarcode.getText().toString();
//        String location=mentrylibrarynumber.getText().toString();
//        String cn_box=mentrynumboxes.getText().toString();
//        String min_box=mentryNumberperbox.getText().toString();
//
//        boolean cancel = false;
////
//        if (lot_no.equals("")||
//                barcode.equals("")||
//                location.equals("")||
//                cn_box.equals("")||
//                min_box.equals("")) {
//            cancel=true;
//        }
//        if (cancel) {
////            // There was an error; don't attempt login and focus the first
////            // form field with an error.
////            focusView.requestFocus();
//            Util.showShortToastMessage(EntryWarehouseActivity.this,"请先扫描所有条目");
//        } else {
////            // Show a progress spinner, and kick off a background task to
////            // perform the user login attempt.
////        showProgress(true);
//            mentryTask =new CheckCodeTask();
//            mentryTask.execute((Void) null);
//        }
//
//
//    }
	public String ip_key="ip";
	public String port_key="port";
	private String email_key = "email";
	//    public class CheckCodeTask extends AsyncTask<Void, Void, Boolean> {
//        //        String lot_no="";
//        String location="";
//        String barcode="";
//        String lot_no="";
//        String success="";
//        String msg="";
//        int responsecode=0;
//        CheckCodeTask() {
//            lot_no=mentrybatchnumber.getText().toString();
//            barcode=mentrybarcode.getText().toString();
//            location=mentrylibrarynumber.getText().toString();
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            // TODO: attempt authentication against a network service.
//
//            try {
//                String url="http://"+PreferencesUtils.getString(EntryWarehouseActivity.this,ip_key,"120.27.2.177")
//                        +":"+PreferencesUtils.getString(EntryWarehouseActivity.this,port_key,"8069")+ IndexConstants.TAKINGCHECKBARCODE;
////                "login:","登录帐号","Password":"密码"
//                Print("url:::"+url);
//                Map<String,String> mparams=new HashMap<String,String>();
//                mparams.put("login",PreferencesUtils.getString(EntryWarehouseActivity.this,email_key,"8069"));
//                mparams.put("lot_no",lot_no);
//                mparams.put("barcode",barcode);
//                mparams.put("location",location);
//
//
//                String postparams = new Gson().toJson(mparams);
////                postparams=URLEncoder.encode(postparams,"utf-8");
//
////                String postparams ="{"+"login:",mEmail,"Password:",mPassword}//"login:"+mEmail+"&password:"+mPassword;
//                byte[] data = postparams.getBytes();
////                System.err.println("postparams postparams:::"+postparams+data.length);
//                URL posturl = new URL(url);
//                HttpURLConnection conn = (HttpURLConnection) posturl.openConnection();
//                conn.setConnectTimeout(10000);
//                conn.setDoInput(true);                  //打开输入流，以便从服务器获取数据
//                conn.setDoOutput(true);                 //打开输出流，以便向服务器提交数据
//                conn.setRequestMethod("POST");     //设置以Post方式提交数据
//                conn.setUseCaches(false);               //使用Post方式不能使用缓存
//                //设置请求体的类型是文本类型
//                conn.setRequestProperty("Content-Type", "application/json");
//                conn.setRequestProperty("Content-Length", String.valueOf(data.length)); // 注意是字节长度, 不是字符长度
//
////                conn.setDoOutput(true); // 准备写出
//                conn.getOutputStream().write(data);
//
//                responsecode=conn.getResponseCode();
//                if(responsecode==200) {
//                    InputStream ins = conn.getInputStream();
//                    JSONObject jsonObject= parseJson(ins);
//                    if(jsonObject!=null) {
//                        msg = jsonObject.getString("message");
//                        success = jsonObject.getString("success");
//                    }
////                    String s = ins.toString();
////                    System.err.println("sssssssss:::"+s);
//                }
//                Print(" return:::"+responsecode);
////                ins.close();
//            } catch (Exception e) {
//                // TODO: handle exception
//                System.err.println("未能获取网络数据");
//                e.printStackTrace();
//            }
//
////            for (String credential : DUMMY_CREDENTIALS) {
////                String[] pieces = credential.split(":");
////                if (pieces[0].equals(mEmail)) {
////                    // Account exists, return true if the password matches.
////                    return pieces[1].equals(mPassword);
////                }
////            }
//
//            // TODO: register the new account here.
//            return success.equals("1");
//        }
//
//        @Override
//        protected void onPostExecute(final Boolean success) {
//            mentryTask = null;
////            showProgress(false);
////            Util.showShortToastMessage(EntryActivity.this,msg);
//            if (success) {
////                PreferencesUtils.putString(EntryActivity.this,email_key,mEmail);
////                PreferencesUtils.putString(EntryActivity.this,password_key,mPassword);
////                Intent intent=new Intent(EntryActivity.this,MainActivity.class);
////                EntryActivity.this.startActivity(intent);
////                finish();
//                InStockTask tst =new InStockTask();
//                tst.execute();
//            } else {
//                Util.showShortToastMessage(EntryWarehouseActivity.this,msg);
////                mPasswordView.setError(getString(R.string.error_incorrect_password));
////                mPasswordView.requestFocus();
//            }
//        }
//
//        @Override
//        protected void onCancelled() {
//            mentryTask = null;
////            showProgress(false);
//        }
//        private JSONObject parseJson(InputStream ins){
//            byte[] data = new byte[0];   // 把输入流转换成字符数组
//            try {
//                data = readStream(ins);
//
//                String  json = new String(data);        // 把字符数组转换成字符串
////            JSONArray array = new JSONArray(json);
////            for(int i = 0 ; i < array.length() ; i++){
//                JSONObject jsonObject = new JSONObject(json);//array.getJSONObject(i);
////                String msg=jsonObject.getString("message");
////                String success=jsonObject.getString("success");
//                return jsonObject;
////                Print("login msgmsg:::"+msg);
////            }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//        /**
//         182.     * 把输入流转换成字符数组
//         183.     * @param inputStream   输入流
//         184.     * @return  字符数组
//         185.     * @throws Exception
//         186.     */
//        public  byte[] readStream(InputStream inputStream) throws Exception {
//            ByteArrayOutputStream bout = new ByteArrayOutputStream();
//            byte[] buffer = new byte[1024];
//            int len = 0;
//            while ((len = inputStream.read(buffer)) != -1) {
//                bout.write(buffer, 0, len);
//            }
//            bout.close();
//            inputStream.close();
//
//            return bout.toByteArray();
//        }
//
//    }
	public class CheckOrderidTask extends AsyncTask<Void, Void, Boolean> {
		//        String lot_no="";
		String orderid = "";
		boolean setidtext=true;
		String success = "";
		String msg = "";
		int responsecode = 0;

		CheckOrderidTask(String content,boolean setidtext) {
				orderid =  content;
				this.setidtext=setidtext;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				String url = "http://" +
						PreferencesUtils.getString(EntryWarehouseActivity.this, ip_key, "120.27.2.177")
						+ ":" + PreferencesUtils.getString(EntryWarehouseActivity.this, port_key, "8069") +
						IndexConstants.CHECKWAREORDERID + "?token=" +
						PreferencesUtils.getString(EntryWarehouseActivity.this, token_key, "")
						+ "&name=" + orderid;
//                "login:","登录帐号","Password":"密码"
				Print("workorder_no url:::" + url);
//                Map<String,String> mparams=new HashMap<String,String>();
//                mparams.put("login",PreferencesUtils.getString(ReportActivity.this,email_key,"8069"));
//                mparams.put("lot_no",lot_no);
//                mparams.put("barcode",barcode);
//                mparams.put("location",location);


//                String postparams = new Gson().toJson(mparams);
//                postparams=URLEncoder.encode(postparams,"utf-8");

//                String postparams ="{"+"login:",mEmail,"Password:",mPassword}//"login:"+mEmail+"&password:"+mPassword;
//                byte[] data = postparams.getBytes();
//                System.err.println("postparams postparams:::"+postparams+data.length);
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
//
////                conn.setDoOutput(true); // 准备写出
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
//						Print(" return:::" + jsonObject);
						msg = jsonObject.getString("message");
						success = jsonObject.getString("success");
						Print(" return: ReportProductBeans success::" + success);
						if (success.equals("true")) {
							parseEntryForm(jsonObject);
							EntryProductinfoBeans = new ArrayList<EntryProductinfoBean>();
							parseReportproduct(jsonObject);
						}
					}
//                    String s = ins.toString();
//                    System.err.println("sssssssss:::"+s);
				}

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
//			mCheckTask = null;
//			reloadviewText(success,msg);
			Util.showShortToastMessage(EntryWarehouseActivity.this,msg);
			if(success) {
				cancleALLdata();
				mentrybarcode.requestFocus();
				mentryorderid.setError(null,null);
				if(use_default_location.equals("true")){
					defaultloccheckbox.setChecked(true);
				}else{
					defaultloccheckbox.setChecked(false);
				}
				stateview.setText(state);
//				if(setidtext) {
//					mentryorderid.setText(orderid);
////					mentryorderid.setFocusable(false);
////					mentryorderid.setFocusableInTouchMode(false);
//				}

				refreshdatalist();
			}else{
//				mentryorderid.requestFocus();
//				mentryorderid.setError(msg);
				textsetError(EntryWarehouseActivity.this,mentrywarenumber,"库位编号有错"+msg);
			}
		}

		@Override
		protected void onCancelled() {
//			mCheckTask = null;
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

	public class CheckLibrarynumTask extends AsyncTask<Void, Void, Boolean> {
		String token = "";
		String library_num = "";
		String success = "";
		String msg = "";
		int responsecode = 0;

		CheckLibrarynumTask(String content) {
			library_num = content;
			token = PreferencesUtils.getString(EntryWarehouseActivity.this, token_key, "");
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				String url = "http://" + PreferencesUtils.getString(EntryWarehouseActivity.this, ip_key, "120.27.2.177")
						+ ":" + PreferencesUtils.getString(EntryWarehouseActivity.this, port_key, "8069") +
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
							locationmap.put("warehouse_id", warehouse_id);
							locationmap.put("location_id", location_id);
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
			Util.showShortToastMessage(EntryWarehouseActivity.this, msg);
			if (success) {
				mentrybarcode.requestFocus();
				mentrywarenumber.setError(null, null);//焦点聚焦时去除错误图标
			} else {
//				mentrywarenumber.requestFocus();
//				mentrywarenumber.setError("库位编号有错");
				textsetError(EntryWarehouseActivity.this,mentrywarenumber,"库位编号有错"+msg);
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
	public class CheckProductidTask extends AsyncTask<Void, Void, Boolean> {
		//        String lot_no="";
		String product_code = "";
		String packaging_code = "";
		String num_perbox = "";
		String batch_number = "";
		String success = "";
		String msg = "";
		String content="";
		boolean checkbox;
		boolean isfinalbox=false;
		int responsecode = 0;
		TextWatcher textWatcher;
		CheckProductidTask(String content,TextWatcher textWatcher) {
			this.content=content;
			this.textWatcher=textWatcher;
			checkbox=defaultloccheckbox.isChecked();
			String[] workorderinfo=content.split(",");
			if(workorderinfo.length>7) {
				product_code = workorderinfo[0];
				num_perbox = workorderinfo[2];
				packaging_code = workorderinfo[3];
				batch_number = workorderinfo[4];
				String checkfinalbox = workorderinfo[7];
				if (checkfinalbox.equals("true")) {
					isfinalbox = true;
				}
			}
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				String url = "http://" +
						PreferencesUtils.getString(EntryWarehouseActivity.this, ip_key, "120.27.2.177")
						+ ":" + PreferencesUtils.getString(EntryWarehouseActivity.this, port_key, "8069") +
						IndexConstants.CHECKENTRYPRODUCTID + "?token=" +
						PreferencesUtils.getString(EntryWarehouseActivity.this, token_key, "")
						+ "&product_code=" + product_code+ "&packaging_code=" + packaging_code;
//                "login:","登录帐号","Password":"密码"
				Print("workorder_no url:::" + url);
//                Map<String,String> mparams=new HashMap<String,String>();
//                mparams.put("login",PreferencesUtils.getString(ReportActivity.this,email_key,"8069"));
//                mparams.put("lot_no",lot_no);
//                mparams.put("barcode",barcode);
//                mparams.put("location",location);


//                String postparams = new Gson().toJson(mparams);
//                postparams=URLEncoder.encode(postparams,"utf-8");

//                String postparams ="{"+"login:",mEmail,"Password:",mPassword}//"login:"+mEmail+"&password:"+mPassword;
//                byte[] data = postparams.getBytes();
//                System.err.println("postparams postparams:::"+postparams+data.length);
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
//
////                conn.setDoOutput(true); // 准备写出
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
//						Print(" return:::" + jsonObject);
						msg = jsonObject.getString("message");
						success = jsonObject.getString("success");
						Print(" return: ReportProductBeans success::" + success);
						if (success.equals("true")) {
							productBean=parseEntryproductbean(jsonObject);
							HashMap<String,EntryProductBean> map=new HashMap<String, EntryProductBean>();
							map.put(content,productBean);
							productinfomap.put(content+"id",map);
						}
					}
//                    String s = ins.toString();
//                    System.err.println("sssssssss:::"+s);
				}

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
			Util.showShortToastMessage(EntryWarehouseActivity.this,msg);
			if(success) {
				mentrybarcode.setError(null,null);
				if(boxnum==0||Productcontent.equals(content)){
					boxnum++;
					if(boxesnum.size()>0) {
						boxesnum.remove(boxesnum.size() - 1);
					}

						mentrybarcode.setText(product_code);

					saveBoxNum(content,boxnum+"");
//					mentrynumboxes.setError(boxnum+"累加"+"Productcontent："+Productcontent+"content："+content);
//					Util.showShortToastMessage(EntryWarehouseActivity.this,
//							"boxnum==0||Productcontent.equals(content)"+boxnum);
				}else {
					mentrybarcode.setText(product_code);
					boxnum = 1;
					saveBoxNum(content, boxnum + "");

//					mentrynumboxes.setError(boxnum+"重置"+"Productcontent:"+Productcontent+"content:"+content);
////					mentrynumboxes.setError(boxnum+"重置");
//					Util.showShortToastMessage(EntryWarehouseActivity.this,
//							"boxnum==0||Productcontent.equals(content)"+boxnum);
				}
				mentrynumboxes.setText(boxnum+"");
				if(checkbox) {
					mentrywarenumber.setText(productBean.location_barcode);
				}
				mentryOrdernumber.setText(batch_number);
				mentryOrdernumber.setFocusable(false);
				mentryOrdernumber.setFocusableInTouchMode(false);
				mentryNumberperbox.setText(num_perbox);
				finalbox.setChecked(isfinalbox);
				Productcontent=content;
			}else{
//				mentrybarcode.requestFocus();
//				mentrybarcode.setError(msg);
				textsetError(EntryWarehouseActivity.this,mentrybarcode,msg);
			}
			mentrybarcode.addTextChangedListener(textWatcher);
		}

		@Override
		protected void onCancelled() {
//			mCheckTask = null;
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

	public class AddOrderidTask extends AsyncTask<Void, Void, Boolean> {
		String orderid="";
		String date="";
		String product_code = "";
		//		String packaging_code = "";
		String packaging_code_id = "";
		String num_perbox = "";
		String batch_number = "";
		String product_number ="";
		String packaging_id = "";
		String location_barcode="";
		String success = "";
		String isfinal = "false";
		String msg = "";
		String content="";
		boolean checkbox=false;
		int responsecode = 0;
		SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyy-MM-dd");
		AddOrderidTask(String content) {
			orderid=mentryorderid.getText().toString();
			date=sdf.format(System.currentTimeMillis());
			checkbox=defaultloccheckbox.isChecked();
			if(checkbox){
				use_default_location="true";
			}else{
				use_default_location="false";
			}
			location_barcode=mentrywarenumber.getText().toString();
			////data={"(产品id, 包装数量, 包装方案id, 批号合并名称, 序列号名称, 包装条码id)":{"qty":7000, "box_qty":10},}
			String[] workorderinfo=content.split(",");
			//二维码内容：产品编码+产品批次+包装数量+包装方案编码+批号合并名称+序列号名称+包装条码id+是尾箱  中间分隔符分开
			if(workorderinfo.length>7) {
				product_code = workorderinfo[0];
				num_perbox = workorderinfo[2];
//				packaging_code = workorderinfo[3];
				batch_number = workorderinfo[4];
				product_number = workorderinfo[5];
				packaging_code_id = workorderinfo[6];
				if(finalbox.isChecked()){
					isfinal="true";
				}
				this.content=workorderinfo[0]+","+workorderinfo[1]+","+workorderinfo[2]
						+","+workorderinfo[3]+","+workorderinfo[4]
						+","+workorderinfo[5]+","+workorderinfo[6]+","+isfinal;
			}
			if(!(boxnum+"").equals(mentrynumboxes.getText().toString())){

				if(boxesnum.size()>0) {
					boxesnum.remove(boxesnum.size() - 1);
				}
				saveBoxNum(this.content,mentrynumboxes.getText().toString());
			}
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				String url = "http://" +
						PreferencesUtils.getString(EntryWarehouseActivity.this, ip_key, "120.27.2.177")
						+ ":" + PreferencesUtils.getString(EntryWarehouseActivity.this, port_key, "8069") +
						IndexConstants.ENTRYWAREADDORDERID + "?token=" +
						PreferencesUtils.getString(EntryWarehouseActivity.this, token_key, "")
						+ "&name=" + orderid+ "&location_barcode=" + location_barcode+"&use_default_location="+use_default_location;
//                "login:","登录帐号","Password":"密码"

				Map<String,String> mparams=new HashMap<String,String>();
//				url=url+"workorder_no="+batch_num;
//                mparams.put("login",PreferencesUtils.getString(DeliveryActivity.this,email_key,"8069"));
//                Print("boxesnum.size():::"+boxesnum.size());
//                String mparams="";
				//data={"(产品id, 包装数量, 包装方案id, 批号合并名称, 序列号名称, 包装条码id)":{"qty":7000, "box_qty":10},}
				String jsondata = "{";
				String parajsondata = "";
				for(int i=0;i<boxesnum.size();i++){
					Map<String, HashMap<String, Integer>> data
							= new HashMap<String, HashMap<String, Integer>>();
					for (String contentkey : boxesnum.get(i).keySet()) {
						String productkey = "";
						String checkfinalbox = "false";
						String num_perbox="";
						String batch_number="";
						String packaging_id="";
						String product_number="";
						String packaging_code_id="";
						String[] workorderinfo = contentkey.split(",");
						//二维码内容：产品编码+产品批次+包装数量+包装方案编码+批号合并名称+序列号名称+包装条码id+是尾箱  中间分隔符分开
						if (workorderinfo.length > 7) {
							productkey = workorderinfo[0];
							num_perbox = workorderinfo[2];
							batch_number = workorderinfo[4];
							product_number= workorderinfo[5];
							packaging_code_id = workorderinfo[6];
							checkfinalbox = workorderinfo[7];
//							if (checkfinalbox.equals("true")) {
//								isfinalbox = true;
//							}
						}
						Print("productinfomap. key:::" + productinfomap.keySet().toString());
						HashMap<String, EntryProductBean> productinfo = productinfomap.get(contentkey + "id");
						String product_id = productinfo.get(contentkey).product_id;
						packaging_id = productinfo.get(contentkey).packaging_id;
						String key = "(" + product_id + "," + num_perbox + ","
								+ packaging_id + ",\"" + batch_number + "\",\"" + product_number + "\"," + packaging_code_id + ",\"" + checkfinalbox + "\"" + ")";
						String boxnum = boxesnum.get(i).get(contentkey);
//                        String value="{\"qty\"="+boxnum+",\"box_qty\"="+boxnum+"}}";
						System.out.println("num_perbox::" + num_perbox + " boxnum:::" + boxnum);
						HashMap<String, Integer> nummap = new HashMap<String, Integer>();
						nummap.put("qty", Integer.valueOf(num_perbox));
						nummap.put("box_qty", Integer.valueOf(boxnum));
						data.put(key, nummap);
					}
					parajsondata = new Gson().toJson(data);
					parajsondata = parajsondata.substring(1, parajsondata.length() - 1);
					jsondata = jsondata + parajsondata;
					if (i == (boxesnum.size() - 1)) {
						jsondata = jsondata + "}";
					} else {
						jsondata = jsondata + ",";
					}


				}
				url = url + "&data=" + jsondata;
				Print("workorder_no url:::" + url);
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
//
////                conn.setDoOutput(true); // 准备写出
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
//						Print(" return:::" + jsonObject);
						msg = jsonObject.getString("message");
						success = jsonObject.getString("success");
						Print(" return: AddOrderidTask jsonObject::" + jsonObject);
						if (success.equals("true")) {
//							Ordername = parseEntryordername(jsonObject);
							EntryProductinfoBeans = new ArrayList<EntryProductinfoBean>();
							parseSuccessproduct(jsonObject);
						}
					}
//                    String s = ins.toString();
//                    System.err.println("sssssssss:::"+s);
				}

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
			Util.showShortToastMessage(EntryWarehouseActivity.this,msg);
			if(success) {
				boxnum=0;
				Ordername=orderid;
				boxesnum=new ArrayList<Map<String,String>>();
				mentrybarcode.setError(null,null);
//				mentrybarcode.setText(orderid);

				refreshdatalist();
				cancleALLdata();
//				mentryorderid.setText(product_code);
//				mentryOrdernumber.setText(batch_number);
//				mentryNumberperbox.setText(num_perbox);
//				Productcontent=content;
			}else{
//				mentrybarcode.requestFocus();
//				mentrybarcode.setError(msg);
				textsetError(EntryWarehouseActivity.this,mentrybarcode,msg);
			}
		}

		@Override
		protected void onCancelled() {
//			mCheckTask = null;
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

	public class SubmitOrderTask extends AsyncTask<Void, Void, Boolean> {
				String orderid="";
//		String date="";
//		String product_code = "";
//		//		String packaging_code = "";
//		String packaging_code_id = "";
//		String num_perbox = "";
//		String batch_number = "";
//		String product_number ="";
//		String packaging_id = "";
		String success = "";
		String msg = "";
		String content="";
		int responsecode = 0;
		SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyy-mm-dd");
		SubmitOrderTask() {
			orderid=mentryorderid.getText().toString();
//			date=sdf.format(new Date());
//			////data={"(产品id, 包装数量, 包装方案id, 批号合并名称, 序列号名称, 包装条码id)":{"qty":7000, "box_qty":10},}
//			String[] workorderinfo=content.split(",");
//			//二维码内容：产品编码+产品批次+包装数量+包装方案编码+批号合并名称+序列号名称+包装条码id+是尾箱  中间分隔符分开
//			if(workorderinfo.length>1) {
//				product_code = workorderinfo[0];
//				num_perbox = workorderinfo[2];
////				packaging_code = workorderinfo[3];
//				batch_number = workorderinfo[4];
//				product_number = workorderinfo[5];
//				packaging_code_id = workorderinfo[6];
//			}
//			if(!(boxnum+"").equals(mentrynumboxes.getText().toString())){
//
//				if(boxesnum.size()>0) {
//					boxesnum.remove(boxesnum.size() - 1);
//				}
//				saveBoxNum(product_code,mentrynumboxes.getText().toString());
//			}
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				String url = "http://" +
						PreferencesUtils.getString(EntryWarehouseActivity.this, ip_key, "120.27.2.177")
						+ ":" + PreferencesUtils.getString(EntryWarehouseActivity.this, port_key, "8069") +
						IndexConstants.ENTRYWARESUBMITORDER + "?token=" +
						PreferencesUtils.getString(EntryWarehouseActivity.this, token_key, "")
						+ "&name=" + orderid;
//                "login:","登录帐号","Password":"密码"
				Print("entry submit url:::" + url);

//                System.err.println("postparams postparams:::"+postparams+data.length);
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
//
////                conn.setDoOutput(true); // 准备写出
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
//						Print(" return:::" + jsonObject);
						msg = jsonObject.getString("message");
						success = jsonObject.getString("success");
						Print(" return: SubmitOrderTask jsonObject::" + jsonObject);
						if (success.equals("true")) {
							EntryProductinfoBeans = new ArrayList<EntryProductinfoBean>();
							parseSuccessproduct(jsonObject);
//							productBean=parseEntryproductbean(jsonObject);
						}
					}
//                    String s = ins.toString();
//                    System.err.println("sssssssss:::"+s);
				}

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
			Util.showShortToastMessage(EntryWarehouseActivity.this,msg);
			if(success) {
//				CheckOrderidTask getinfoTask = new CheckOrderidTask(mentryorderid.getText().toString(),false);
//				getinfoTask.execute();
				stateview.setText(state);
				cancleALLdata();
//				mentryorderid.setText(product_code);
//				mentryOrdernumber.setText(batch_number);
//				mentryNumberperbox.setText(num_perbox);
//				Productcontent=content;

			}
		}

		@Override
		protected void onCancelled() {
//			mCheckTask = null;
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

	protected String parseEntryordername(JSONObject jsonObject) {
//		JSONArray dataarr = null;
		try {
//			dataarr = jsonObject.getJSONArray("data");
//			for (int i = 0; i < dataarr.length(); i++) {
				JSONObject reprotformdataObject = jsonObject.getJSONObject("data");
//				ReportFormBean ReportFormBean = beanParseUtility.parse(reprotformdataObject, ReportFormBean.class);
//				ReportFormBeans.add(ReportFormBean);
//				ReportProductBean ReportProductdataBean =
//						beanParseUtility.parse(reprotformdataObject, ReportProductBean.class);
//				ReportProductBeans.add(ReportProductdataBean);
				if(reprotformdataObject.has("line_data")) {
					JSONArray linedataarr = reprotformdataObject.getJSONArray("line_data");
					for (int j = 0; j < linedataarr.length(); j++) {
						JSONObject reprotformlinedataObject = linedataarr.getJSONObject(j);
						String name = reprotformlinedataObject.getString("name");
					return name;
					}
				}
//				if(reprotformdataObject.has("line_data")) {
////					JSONObject reprotformdataObject = dataarr.getJSONObject(i);
//					String name = reprotformdataObject.getString("name");
//					return name;
//				}
//				ReportFormBean ReportFormBean = beanParseUtility.parse(reprotformdataObject, ReportFormBean.class);
//				ReportFormBeans.add(ReportFormBean);
//				if(reprotformdataObject.has("line_data")) {
//					JSONArray linedataarr = reprotformdataObject.getJSONArray("line_data");
//					for (int j = 0; j < linedataarr.length(); j++) {
//						JSONObject reprotformlinedataObject = linedataarr.getJSONObject(j);
//						ReportProductBean ReportProductlineBean =
//								beanParseUtility.parse(reprotformlinedataObject, ReportProductBean.class);
//						ReportProductBeans.add(ReportProductlineBean);
//					}
//				}

//			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return"";
	}
//    public class InStockTask extends AsyncTask<Void, Void, Boolean> {
//        //        String lot_no="";
//        String location="";
//        String barcode="";
//        String min_box="";
//        String lot_no="";
//        String cn_box="";
//        String stock_weight="";
//        String success="";
//        String msg="";
//        int responsecode=0;
//        InStockTask() {
//            lot_no=mentrybatchnumber.getText().toString();
//            barcode=mentrybarcode.getText().toString();
//            location=mentrylibrarynumber.getText().toString();
//            cn_box=mentrynumboxes.getText().toString();
//            min_box=mentryNumberperbox.getText().toString();
//            stock_weight=entryweightthousands.getText().toString();
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            // TODO: attempt authentication against a network service.
//
//            try {
//                String url="http://"+PreferencesUtils.getString(EntryWarehouseActivity.this,ip_key,"120.27.2.177")
//                        +":"+PreferencesUtils.getString(EntryWarehouseActivity.this,port_key,"8069")+ IndexConstants.INSTOCK;
////                "login:","登录帐号","Password":"密码"
//                Print("url:::"+url);
//                Map<String,String> mparams=new HashMap<String,String>();
//                mparams.put("login",PreferencesUtils.getString(EntryWarehouseActivity.this,email_key,"8069"));
//                mparams.put("lot_no",lot_no);
//                mparams.put("barcode",barcode);
//                mparams.put("location",location);
//                mparams.put("min_box",min_box);
//                mparams.put("cn_box",cn_box);
//                mparams.put("stock_weight",stock_weight);
//
//                String postparams = new Gson().toJson(mparams);
////                postparams=URLEncoder.encode(postparams,"utf-8");
//
////                String postparams ="{"+"login:",mEmail,"Password:",mPassword}//"login:"+mEmail+"&password:"+mPassword;
//                byte[] data = postparams.getBytes();
////                System.err.println("postparams postparams:::"+postparams+data.length);
//                URL posturl = new URL(url);
//                HttpURLConnection conn = (HttpURLConnection) posturl.openConnection();
//                conn.setConnectTimeout(10000);
//                conn.setDoInput(true);                  //打开输入流，以便从服务器获取数据
//                conn.setDoOutput(true);                 //打开输出流，以便向服务器提交数据
//                conn.setRequestMethod("POST");     //设置以Post方式提交数据
//                conn.setUseCaches(false);               //使用Post方式不能使用缓存
//                //设置请求体的类型是文本类型
//                conn.setRequestProperty("Content-Type", "application/json");
//                conn.setRequestProperty("Content-Length", String.valueOf(data.length)); // 注意是字节长度, 不是字符长度
//
////                conn.setDoOutput(true); // 准备写出
//                conn.getOutputStream().write(data);
//
//                responsecode=conn.getResponseCode();
//                if(responsecode==200) {
//                    InputStream ins = conn.getInputStream();
//                    JSONObject jsonObject= parseJson(ins);
//                    if(jsonObject!=null) {
//                        msg = jsonObject.getString("message");
//                        success = jsonObject.getString("success");
//                    }
////                    String s = ins.toString();
////                    System.err.println("sssssssss:::"+s);
//                }
//                Print(" return:::"+responsecode);
////                ins.close();
//            } catch (Exception e) {
//                // TODO: handle exception
//                System.err.println("未能获取网络数据");
//                e.printStackTrace();
//            }
//
////            for (String credential : DUMMY_CREDENTIALS) {
////                String[] pieces = credential.split(":");
////                if (pieces[0].equals(mEmail)) {
////                    // Account exists, return true if the password matches.
////                    return pieces[1].equals(mPassword);
////                }
////            }
//
//            // TODO: register the new account here.
//            return success.equals("1");
//        }
//
//        @Override
//        protected void onPostExecute(final Boolean success) {
////            mAuthTask = null;
////            showProgress(false);
//            Util.showShortToastMessage(EntryWarehouseActivity.this,msg);
//            if (success) {
//                 mentrybatchnumber.setText("");
//                 mentrybarcode.setText("");
//                 mentrylibrarynumber.setText("");
//                 mentryNumberperbox.setText("");
//                 mentrynumboxes.setText("");
//                 entryweightthousands.setText("");
//                 mentrybatchnumber.requestFocus();
////                PreferencesUtils.putString(EntryActivity.this,email_key,mEmail);
////                PreferencesUtils.putString(EntryActivity.this,password_key,mPassword);
////                Intent intent=new Intent(EntryActivity.this,MainActivity.class);
////                EntryActivity.this.startActivity(intent);
////                finish();
//            } else {
////                mPasswordView.setError(getString(R.string.error_incorrect_password));
////                mPasswordView.requestFocus();
//            }
//        }
//
//        @Override
//        protected void onCancelled() {
////            mAuthTask = null;
////            showProgress(false);
//        }
//        private JSONObject parseJson(InputStream ins){
//            byte[] data = new byte[0];   // 把输入流转换成字符数组
//            try {
//                data = readStream(ins);
//
//                String  json = new String(data);        // 把字符数组转换成字符串
////            JSONArray array = new JSONArray(json);
////            for(int i = 0 ; i < array.length() ; i++){
//                JSONObject jsonObject = new JSONObject(json);//array.getJSONObject(i);
////                String msg=jsonObject.getString("message");
////                String success=jsonObject.getString("success");
//                return jsonObject;
////                Print("login msgmsg:::"+msg);
////            }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//        /**
//         182.     * 把输入流转换成字符数组
//         183.     * @param inputStream   输入流
//         184.     * @return  字符数组
//         185.     * @throws Exception
//         186.     */
//        public  byte[] readStream(InputStream inputStream) throws Exception {
//            ByteArrayOutputStream bout = new ByteArrayOutputStream();
//            byte[] buffer = new byte[1024];
//            int len = 0;
//            while ((len = inputStream.read(buffer)) != -1) {
//                bout.write(buffer, 0, len);
//            }
//            bout.close();
//            inputStream.close();
//
//            return bout.toByteArray();
//        }
//
//    }


	protected void parseEntryForm(JSONObject jsonObject) {
		JSONArray dataarr = null;
		try {
			dataarr = jsonObject.getJSONArray("data");
			for (int i = 0; i < dataarr.length(); i++) {
				use_default_location=dataarr.getJSONObject(i).getString("use_default_location");
				JSONObject entryformdataObject = dataarr.getJSONObject(i);
				ReportProductBean = beanParseUtility.parse(entryformdataObject, WorkOrderBean.class);


			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	protected EntryProductBean parseEntryproductbean(JSONObject jsonObject) {
		JSONObject dataobj = null;
		try {
			dataobj = jsonObject.getJSONObject("data");
			EntryProductBean ReportProductBean = beanParseUtility.parse(dataobj, EntryProductBean.class);
			return ReportProductBean;

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected void parseReportproduct(JSONObject jsonObject) {
		JSONArray dataarr = null;
		try {
			dataarr = jsonObject.getJSONArray("data");
			for (int i = 0; i < dataarr.length(); i++) {
				JSONObject reprotformdataObject = dataarr.getJSONObject(i);
				state=reprotformdataObject.getString("state");
//				ReportFormBean ReportFormBean = beanParseUtility.parse(reprotformdataObject, ReportFormBean.class);
//				ReportFormBeans.add(ReportFormBean);
//				ReportProductBean ReportProductdataBean =
//						beanParseUtility.parse(reprotformdataObject, ReportProductBean.class);
//				ReportProductBeans.add(ReportProductdataBean);
				if(reprotformdataObject.has("line_data")) {
					JSONArray linedataarr = reprotformdataObject.getJSONArray("line_data");
					for (int j = 0; j < linedataarr.length(); j++) {
						JSONObject reprotformlinedataObject = linedataarr.getJSONObject(j);
						EntryProductinfoBean ReportProductlineBean =
								beanParseUtility.parse(reprotformlinedataObject, EntryProductinfoBean.class);
						EntryProductinfoBeans.add(ReportProductlineBean);
					}
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	protected void parseSuccessproduct(JSONObject jsonObject) {
//		JSONArray dataarr = null;
		try {
//			dataarr = jsonObject.getJSONArray("data");
//			for (int i = 0; i < dataarr.length(); i++) {
				JSONObject reprotformdataObject = jsonObject.getJSONObject("data");
				if(reprotformdataObject.has("state")) {
					state = reprotformdataObject.getString("state");
				}
//				ReportFormBean ReportFormBean = beanParseUtility.parse(reprotformdataObject, ReportFormBean.class);
//				ReportFormBeans.add(ReportFormBean);
//				ReportProductBean ReportProductdataBean =
//						beanParseUtility.parse(reprotformdataObject, ReportProductBean.class);
//				ReportProductBeans.add(ReportProductdataBean);
//				if(reprotformdataObject.has("line_data")) {
				JSONArray linedataarr = reprotformdataObject.getJSONArray("line_data");
				for (int j = 0; j < linedataarr.length(); j++) {
					JSONObject reprotformlinedataObject = linedataarr.getJSONObject(j);
					EntryProductinfoBean ReportProductlineBean =
							beanParseUtility.parse(reprotformlinedataObject, EntryProductinfoBean.class);
					EntryProductinfoBeans.add(ReportProductlineBean);
				}


//			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

//	protected void parseSuccessproduct(JSONObject jsonObject) {
////		JSONArray dataarr = null;
//		try {
////			dataarr = jsonObject.getJSONArray("data");
////			for (int i = 0; i < dataarr.length(); i++) {
//			JSONObject reprotformdataObject = jsonObject.getJSONObject("data");
////				ReportFormBean ReportFormBean = beanParseUtility.parse(reprotformdataObject, ReportFormBean.class);
////				ReportFormBeans.add(ReportFormBean);
////				ReportProductBean ReportProductdataBean =
////						beanParseUtility.parse(reprotformdataObject, ReportProductBean.class);
////				ReportProductBeans.add(ReportProductdataBean);
////				if(reprotformdataObject.has("line_data")) {
//			JSONArray linedataarr = reprotformdataObject.getJSONArray("line_data");
//			for (int j = 0; j < linedataarr.length(); j++) {
//				JSONObject reprotformlinedataObject = linedataarr.getJSONObject(j);
//				EntryProductinfoBean ReportProductlineBean =
//						beanParseUtility.parse(reprotformlinedataObject, EntryProductinfoBean.class);
//				EntryProductinfoBeans.add(ReportProductlineBean);
//			}
//
//
////			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//	}
	String TAG="Entryactivity::";
	public void Print(String s){
		System.out.println(TAG+s);
	}
	public static Comparator idComparator = new Comparator() {
		@Override
		public int compare(Object o1, Object o2) {
			return (Integer.compare(Integer.parseInt(((EntryProductinfoBean) o1).sequence), Integer.parseInt(((EntryProductinfoBean) o2).sequence)));
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		Util.showShortToastMessage(EntryWarehouseActivity.this,"keycode:"+keyCode);
//		mentrynumboxes.setError(boxnum+"keyCode:"+keyCode);
		if(keyCode==301) {
			if (mentryorderid.isFocused()) {
				mentryorderid.setText("");
			} else if (mentrywarenumber.isFocused()) {
				mentrywarenumber.setText("");
			} else if (mentrybarcode.isFocused()) {
				mentrybarcode.setText("");
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
