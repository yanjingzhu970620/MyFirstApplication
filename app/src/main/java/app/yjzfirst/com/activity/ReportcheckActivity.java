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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.yjzfirst.adapter.ReportdetailAdapter;
import com.yjzfirst.bean.ReportFormBean;
import com.yjzfirst.bean.ReportProductBean;
import com.yjzfirst.util.IndexConstants;
import com.yjzfirst.util.PreferencesUtils;
import com.yjzfirst.util.Util;
import com.yjzfirst.util.beanParseUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import static com.yjzfirst.util.IndexConstants.token_key;
import static com.yjzfirst.util.Util.REQUEST_CODE_SCAN;
import static com.yjzfirst.util.Util.readStream;
import static com.yjzfirst.util.Util.setListViewHeightBasedOnChildren;
import static com.yjzfirst.util.Util.textsetError;
import static com.yzq.zxinglibrary.common.Constant.CODED_CONTENT;

public class ReportcheckActivity extends AppCompatActivity {
	private CheckCardidTask mCheckTask = null;
	EditText eCardid;
	EditText ePackagename;
	EditText ework_team_no;
	EditText eEquipmentcode;
	EditText eCurrentprocess;
	EditText eReportstate;
	EditText eContainerid;
	EditText eContainerweight;
	EditText eThousandweight;
	EditText eNetweight;
	EditText eWaste;
	EditText eGrossweight;
	EditText eReportnum;
	EditText eSplit_merge_cardid;
	EditText eSplit_merge_container_no;
	EditText eSplit_merge_container_weight;
	TextView tContainerid;
	TextView tContainerweight;
	TextView tThousandweight;
	TextView tNetweight;
//	TextView tWaste;
	TextView tGrossweight;
	TextView tReportnum;

	TextView Errortext;
//    EditText mcheckbatchnumber;
   enum qrcodemode {
	CARDID, MERGE_CARDID
   }
	Button report_submitbutton;
	Button reportcancel_submitbutton;
	Button report_stopruncard_button;
	Button reportinspect_submitbutton;
	Button reportinspect_ng_submitbutton;
	Button reportinspectcancel_submitbutton;
	Button reportmaterial_submitbutton;
	Button reportmaterialcancel_submitbutton;
	ImageView report_mergecard_id_button;
	LinearLayout split_merge_layout;
	Spinner spinner_split_merge_type;
	ListView mSimpleDetailList;
	ReportdetailAdapter mAdapter;

	String  split_merge_item;
	private  qrcodemode qrcodetextmode =  qrcodemode.CARDID;
	boolean weightupdate=false;
	boolean qtyupdate=false;
	boolean containerupdate=false;
	boolean grossupdate=false;
	//
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_check);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

			Window window = ReportcheckActivity.this.getWindow();

			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

			window.setStatusBarColor(Color.BLACK);


			//底部导航栏

			//window.setNavigationBarColor(activity.getResources().getColor(colorResId));

		}
		process_statusmap.put("to_material", "待领料");
		process_statusmap.put("to_report", "待报工");
		process_statusmap.put("to_inspect", "待检验");
		process_statusmap.put("to_ipqc", "待质量判定");
		process_statusmap.put("done", "完工");
		process_statusmap.put("cancel", "作废");
		TextView title=(TextView) findViewById(R.id.report_activity_title);
		title.setText(R.string.check_main_inspect);
		eCardid = (EditText) findViewById(R.id.edittext_report_card_id);
		LinearLayout package_name_layout= (LinearLayout) findViewById(R.id.package_name_layout);
		package_name_layout.setVisibility(View.VISIBLE);
		LinearLayout equipment_code_layout= (LinearLayout) findViewById(R.id.equipment_code_layout);
		equipment_code_layout.setVisibility(View.VISIBLE);
		LinearLayout work_team_layout= (LinearLayout) findViewById(R.id.work_team_layout);
		work_team_layout.setVisibility(View.VISIBLE);
		ework_team_no= (EditText) findViewById(R.id.edit_work_team_no);
		changeEditTextstatus(ework_team_no,false);
		ePackagename = (EditText) findViewById(R.id.edittext_package_name);
		changeEditTextstatus(ePackagename,false);
		eEquipmentcode = (EditText) findViewById(R.id.edittext_equipment_code);
		changeEditTextstatus(eEquipmentcode,false);

		ImageView work_teambtn = (ImageView) findViewById(R.id.work_team_no_button);
		work_teambtn.setVisibility(View.GONE);

		ImageView ePackagenamebtn = (ImageView) findViewById(R.id.report_package_name_button);
		ePackagenamebtn.setVisibility(View.GONE);

		ImageView eEquipmentcodebtn = (ImageView) findViewById(R.id.report_equipment_code_button);
		eEquipmentcodebtn.setVisibility(View.GONE);
//        mcheckbatchnumber.addTextChangedListener(shipsWatcher);
		eCurrentprocess = (EditText) findViewById(R.id.edittext_current_process);
		eCurrentprocess.setFocusableInTouchMode(false);//不可编辑
		eCurrentprocess.setFocusable(false);//不可编辑

//        mcheckbarcode.addTextChangedListener(shipsWatcher);
		eReportstate = (EditText) findViewById(R.id.edittext_report_state);
		eReportstate.setFocusableInTouchMode(false);//不可编辑
		eReportstate.setFocusable(false);//不可编辑
//        mchecklibrarynumber.addTextChangedListener(shipsWatcher);
//		eContainerid = (EditText) findViewById(R.id.edittext_container_id);
//		eContainerid.setVisibility(View.INVISIBLE);
//		eContainerweight = (EditText) findViewById(R.id.edittext_container_weight);
//		eContainerweight.setVisibility(View.INVISIBLE);
//        mcheckNumberperbox.addTextChangedListener(shipsWatcher);
//		eThousandweight = (EditText) findViewById(R.id.edit_thousand_weight);
//		eThousandweight.setVisibility(View.INVISIBLE);
//        mchecknumboxes.addTextChangedListener(shipsWatcher);
//		eNetweight = (EditText) findViewById(R.id.edit_net_weight);
//		eNetweight.setVisibility(View.INVISIBLE);
//		eWaste = (EditText) findViewById(R.id.edit_waste);
//		LinearLayout wastelayout=(LinearLayout) findViewById(R.id.report_waste_layout);
//		wastelayout.setVisibility(View.GONE);
//		eGrossweight = (EditText) findViewById(R.id.edit_gross_weight);
//		eGrossweight.setVisibility(View.INVISIBLE);
//		eReportnum = (EditText) findViewById(R.id.edit_report_num);
//		eReportnum.setVisibility(View.INVISIBLE);
		eSplit_merge_cardid= (EditText) findViewById(R.id.edit_split_merge_cardid);
		eSplit_merge_container_no= (EditText) findViewById(R.id.edit_split_merge_container_no);
		eSplit_merge_container_weight= (EditText) findViewById(R.id.edit_split_merge_container_weight);

		tContainerid = (TextView) findViewById(R.id.text_container_id);
		tContainerweight = (TextView) findViewById(R.id.text_container_weight);
		tGrossweight = (TextView) findViewById(R.id.text_gross_weight);
		tNetweight = (TextView) findViewById(R.id.text_net_weight);
		tThousandweight = (TextView) findViewById(R.id.text_thousand_weight);
//		tWaste = (TextView) findViewById(R.id.text_waste);
		tReportnum = (TextView) findViewById(R.id.text_report_num);

		Errortext = (TextView) findViewById(R.id.report_errmsg);
		report_submitbutton = (Button) findViewById(R.id.report_submit_button);
		reportcancel_submitbutton = (Button) findViewById(R.id.report_cancel_submitbutton);
		report_stopruncard_button = (Button) findViewById(R.id.report_stopruncard_button);

		reportinspect_submitbutton = (Button) findViewById(R.id.report_inspect_submit_button);
		reportinspect_ng_submitbutton = (Button) findViewById(R.id.report_inspect_ng_submitbutton);
		reportinspectcancel_submitbutton = (Button) findViewById(R.id.report_inspect_cancel_submitbutton);

		reportmaterial_submitbutton = (Button) findViewById(R.id.report_material_submit_button);
		reportmaterialcancel_submitbutton = (Button) findViewById(R.id.report_material_cancel_submitbutton);
		report_mergecard_id_button  = (ImageView) findViewById(R.id.report_mergecard_id_button);

		split_merge_layout= (LinearLayout) findViewById(R.id.split_merge_layout);
		spinner_split_merge_type= (Spinner) findViewById(R.id.spinner_split_merge_type);

		String[] mItems = new String[3];
		mItems[0]="空";
		mItems[1]="并桶";
		mItems[2]="分桶";
// 建立Adapter并且绑定数据源
		ArrayAdapter<String> _Adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItems);
		spinner_split_merge_type.setAdapter(_Adapter);
		spinner_split_merge_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
//				String str=parent.getItemAtPosition(position).toString();
				split_merge_item=getData().get(position);
				if(position==0){
					eSplit_merge_cardid.setFocusable(false);
					eSplit_merge_cardid.setFocusableInTouchMode(false);
					report_mergecard_id_button.setVisibility(View.GONE);
				}else {
					eSplit_merge_cardid.setFocusable(true);
					eSplit_merge_cardid.setFocusableInTouchMode(true);
					report_mergecard_id_button.setVisibility(View.VISIBLE);
					if (position == 1) {//bing
						eSplit_merge_container_no.setFocusable(false);
						eSplit_merge_container_no.setFocusableInTouchMode(false);
						eSplit_merge_container_weight.setFocusable(false);
						eSplit_merge_container_weight.setFocusableInTouchMode(false);
					} else {
						eSplit_merge_container_no.setFocusable(true);
						eSplit_merge_container_no.setFocusableInTouchMode(true);
						eSplit_merge_container_weight.setFocusable(true);
						eSplit_merge_container_weight.setFocusableInTouchMode(true);
					}
				}
//				Toast.makeText(ReportActivity.this, "你点击的是:"+split_merge_item, Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			}
		});
		addTextWatcher();

		mSimpleDetailList = (ListView) findViewById(R.id.report_infolist);
		refreshdatalist();
//      report_submitbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//	            Print("report_submit_button");
//
//	            CheckCReportTask checkreporttask=new CheckCReportTask();
//	            checkreporttask.execute();
//            }
//        });

	}
	private List<String> getData() {
		List<String> list=new ArrayList<String>();
		list.add("none");
		list.add("merge");
		list.add("split");
		return list;
	}


	public void onClick(View view) {
		Errortext.setVisibility(View.GONE);
		if (view.getId() == R.id.report_back) {
			finish();
		} else if (view.getId() == R.id.report_card_id_button) {
			Util.startQrCode(ReportcheckActivity.this);
			qrcodetextmode= qrcodemode.CARDID;
		}else if (view.getId() == R.id.report_mergecard_id_button) {
			Util.startQrCode(ReportcheckActivity.this);
			qrcodetextmode= qrcodemode.MERGE_CARDID;
		} else if (view.getId() == R.id.report_submit_button) {
			Print("report_submit_button");
			CheckCReportTask checkreporttask = new CheckCReportTask();
			checkreporttask.execute();
		} else if (view.getId() == R.id.report_cancel_submitbutton) {
			Print("report_cancel_submitbutton");
			CancleReportTask canclereporttask = new CancleReportTask();
			canclereporttask.execute();
		} else if (view.getId() == R.id.report_stopruncard_button) {
			Print("report_stopruncard_button");

		}else if (view.getId() == R.id.report_inspect_submit_button) {
			Print("report_inspect_submit_button");
			ReportInspectpassTask reportInspectpassTask = new ReportInspectpassTask();
			reportInspectpassTask.execute();
		} else if (view.getId() == R.id.report_inspect_cancel_submitbutton) {
			Print("report_inspect_cancel_submitbutton");
			ReportInspectcancleTask reportInspectcancleTask = new ReportInspectcancleTask();
			reportInspectcancleTask.execute();
		} else if (view.getId() == R.id.report_inspect_ng_submitbutton) {
			Print("report_inspect_ng_submitbutton");
			ReportInspectNgTask reportInspectNgTask = new ReportInspectNgTask();
			reportInspectNgTask.execute();
		} else if (view.getId() == R.id.report_material_submit_button) {
			Print("report_material_submit_button");
			ReportMaterialTask materialreporttask = new ReportMaterialTask();
			materialreporttask.execute();
		} else if (view.getId() == R.id.report_material_cancel_submitbutton) {
			Print("report_material_cancel_submitbutton");
			ReportMaterialCancleTask reportMaterialCancletask = new ReportMaterialCancleTask();
			reportMaterialCancletask.execute();
		}
	}

	@Override

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);


		// 扫描二维码/条码回传

		if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {

			if (data != null) {

				String content = data.getStringExtra(CODED_CONTENT);
				if(qrcodetextmode== qrcodemode.CARDID) {
					eCardid.setText(content);
				}else if(qrcodetextmode== qrcodemode.MERGE_CARDID){
					eSplit_merge_cardid.setText(content);
				}
//				Util.showToastMessage(ReportActivity.this, "扫描结果为：" + content);
//				attemptCheck();
			}

		}

	}

	public void addTextWatcher(){

		eCardid.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
//				String content = eCardid.getText().toString();
				attemptCheck();
			}
		});
		eSplit_merge_cardid.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
//				String content = eCardid.getText().toString();
				CheckMergecardidTask mCheckTask = new CheckMergecardidTask();
				mCheckTask.execute((Void) null);
			}
		});
	}
	private void attemptCheck() {
		if (mCheckTask != null) {
			return;
		}
//
//        // Reset errors.
//        mEmailView.setError(null);
//        mPasswordView.setError(null);
//
//        // Store values at the time of the login attempt.
//        String email = mEmailView.getText().toString();
//        String password = mPasswordView.getText().toString();
//
		String cardid = eCardid.getText().toString();
		String containerid = "132323";//eContainerid.getText().toString();
		boolean cancel = false;
//
		if (cardid.equals("") ) {
			cancel = true;
		}
		if (cancel) {
//            // There was an error; don't attempt login and focus the first
//            // form field with an error.
//            focusView.requestFocus();
//			Util.showToastMessage(ReportcheckActivity.this, "请先扫描所有条目");
		} else {
//            // Show a progress spinner, and kick off a background task to
//            // perform the user login attempt.
//        showProgress(true);
			mCheckTask = new CheckCardidTask();
			mCheckTask.execute((Void) null);
		}


	}

	public String ip_key = "ip";
	public String port_key = "port";
	private String email_key = "email";
	ArrayList<ReportFormBean> ReportFormBeans = new ArrayList<ReportFormBean>();
	ArrayList<ReportProductBean> ReportProductBeans = new ArrayList<ReportProductBean>();
	HashMap<String, String> process_statusmap = new HashMap<String, String>();

	public class CheckCardidTask extends AsyncTask<Void, Void, Boolean> {
		//        String lot_no="";
		String cardid = "";
		String success = "";
		String msg = "";
		int responsecode = 0;

		CheckCardidTask() {
			cardid = eCardid.getText().toString();

		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				String url = "http://" +
						PreferencesUtils.getString(ReportcheckActivity.this, ip_key, "120.27.2.177")
						+ ":" + PreferencesUtils.getString(ReportcheckActivity.this, port_key, "8069") +
						IndexConstants.CHECKCARDID + "?token=" +
						PreferencesUtils.getString(ReportcheckActivity.this, token_key, "") + "&runcard_no=" + cardid;
//                "login:","登录帐号","Password":"密码"
				Print("url:::" + url);
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
							ReportFormBeans = new ArrayList<ReportFormBean>();
							ReportProductBeans = new ArrayList<ReportProductBean>();
							parseReportid(jsonObject);
							Print(" return: ReportProductBeans ::" + ReportProductBeans.size());
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
			mCheckTask = null;
			reloadviewText(success,msg);
		}

		@Override
		protected void onCancelled() {
			mCheckTask = null;
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
	public class CheckMergecardidTask extends AsyncTask<Void, Void, Boolean> {
		//        String lot_no="";
		ArrayList<ReportFormBean> ReportFormBeans = new ArrayList<ReportFormBean>();
		String cardid = "";
		String success = "";
		String msg = "";
		int responsecode = 0;

		CheckMergecardidTask() {
			cardid = eSplit_merge_cardid.getText().toString();

		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				String url = "http://" +
						PreferencesUtils.getString(ReportcheckActivity.this, ip_key, "120.27.2.177")
						+ ":" + PreferencesUtils.getString(ReportcheckActivity.this, port_key, "8069") +
						IndexConstants.CHECKCARDID + "?token=" +
						PreferencesUtils.getString(ReportcheckActivity.this, token_key, "") + "&runcard_no=" + cardid;
//                "login:","登录帐号","Password":"密码"
				Print("url:::" + url);
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
							ReportFormBeans = new ArrayList<ReportFormBean>();
							parseReportid(jsonObject);
							Print(" return: ReportProductBeans ::" + ReportProductBeans.size());
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
			Util.showShortToastMessage(ReportcheckActivity.this,msg);
			if(success&&split_merge_item.equals("merge")) {
				eSplit_merge_container_no.setText(CheckNullString(ReportFormBeans.get(0).container_no));
				eSplit_merge_container_weight.setText(CheckNullString(ReportFormBeans.get(0).container_weight));
			}
		}

		@Override
		protected void onCancelled() {
//            showProgress(false);
		}
		protected void parseReportid(JSONObject jsonObject) {
			JSONArray dataarr = null;
			try {
				dataarr = jsonObject.getJSONArray("data");
				for (int i = 0; i < dataarr.length(); i++) {
					JSONObject reprotformdataObject = dataarr.getJSONObject(i);
					ReportFormBean ReportFormBean = beanParseUtility.parse(reprotformdataObject, ReportFormBean.class);
					ReportFormBeans.add(ReportFormBean);
//					if(reprotformdataObject.has("line_data")) {
//						JSONArray linedataarr = reprotformdataObject.getJSONArray("line_data");
//						for (int j = 0; j < linedataarr.length(); j++) {
//							JSONObject reprotformlinedataObject = linedataarr.getJSONObject(j);
//							ReportProductBean ReportProductlineBean =
//									beanParseUtility.parse(reprotformlinedataObject, ReportProductBean.class);
//							ReportProductBeans.add(ReportProductlineBean);
//						}
//					}

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
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
	public class CheckCReportTask extends AsyncTask<Void, Void, Boolean> {
		//        String lot_no="";
//		String cardid = "";
		String qty = "";
		String weight = "";
		String gross_weight = "";
		String token = "";
		String success = "";
		String msg = "";
		String runcard_no = "";
		String unit_weight = "";
		String loss_weight = "";
		String container_no = "";
		String contrain_weight = "";
        String runcard_no_2="";
		String container_no_2="";
		String container_weight_2="";

		int responsecode = 0;

		CheckCReportTask() {
			runcard_no = eCardid.getText().toString();
			weight = eNetweight.getText().toString();
			unit_weight = eThousandweight.getText().toString();
			qty = eReportnum.getText().toString();
			gross_weight = eGrossweight.getText().toString();
			loss_weight = eWaste.getText().toString();
			container_no = eContainerid.getText().toString();
			contrain_weight = eContainerweight.getText().toString();
			token = PreferencesUtils.getString(ReportcheckActivity.this, token_key, "");
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				String url = "http://" +
						PreferencesUtils.getString(ReportcheckActivity.this, ip_key, "120.27.2.177")
						+ ":" + PreferencesUtils.getString(ReportcheckActivity.this, port_key, "8069") +
						IndexConstants.REPORTCARD + "?"
						+ "token=" + token + "&runcard_no=" + runcard_no
						+ "&weight=" + weight + "&unit_weight=" + unit_weight
						+ "&loss_weight=" + loss_weight + "&qty=" + qty + "&gross_weight=" + gross_weight
						+ "&container_no=" + container_no + "&container_weight=" + contrain_weight+"&split_merge_type="+split_merge_item
						+"&runcard_no_2="+"&container_no_2="+"&container_weight_2=";
//                "login:","登录帐号","Password":"密码"
				Print("url:::" + url);
//                Map<String,String> mparams=new HashMap<String,String>();
////                mparams.put("login",PreferencesUtils.getString(ReportActivity.this,email_key,"8069"));
//                mparams.put("token",PreferencesUtils.getString(ReportActivity.this, token_key, ""));
//                mparams.put("runcard_no",cardid);
//                mparams.put("qty",qty);
//				mparams.put("weight",weight);
//				mparams.put("gross_weight",grossweight);
//
//
//                String postparams = new Gson().toJson(mparams);
//                postparams= URLEncoder.encode(postparams,"utf-8");
//
////                String postparams ="{"+"login:",mEmail,"Password:",mPassword}//"login:"+mEmail+"&password:"+mPassword;
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
						Print(" return report:::" + jsonObject);
//						ReportProductBeans = new ArrayList<ReportProductBean>();
						msg = jsonObject.getString("message");
						success = jsonObject.getString("success");
						if (success.equals("true")) {
							ReportFormBeans = new ArrayList<ReportFormBean>();
							ReportProductBeans = new ArrayList<ReportProductBean>();
//							parseReportproduct(jsonObject);
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
//            showProgress(false);
			reloadviewText(success,msg);
		}

		@Override
		protected void onCancelled() {
//            showProgress(false);
		}

		private JSONObject parseJson(InputStream ins) {
			byte[] data = new byte[0];   // 把输入流转换成字符数组
			try {
				data = readStream(ins);

				String json = new String(data);        // 把字符数组转换成字符串
				JSONObject jsonObject = new JSONObject(json);//array.getJSONObject(i);
				return jsonObject;
//                Print("login msgmsg:::"+msg);
//            }
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * 182.     * 把输入流转换成字符数组
		 * 183.     * @param inputStream   输入流
		 * 184.     * @return  字符数组
		 * 185.     * @throws Exception
		 * 186.
		 */
		public byte[] readStream(InputStream inputStream) throws Exception {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inputStream.read(buffer)) != -1) {
				bout.write(buffer, 0, len);
			}
			bout.close();
			inputStream.close();

			return bout.toByteArray();
		}

	}
	public class CheckStopReportTask extends AsyncTask<Void, Void, Boolean> {

		String token = "";
		String success = "";
		String msg = "";
		String runcard_no = "";

		int responsecode = 0;

		CheckStopReportTask() {
			runcard_no = eCardid.getText().toString();
			token = PreferencesUtils.getString(ReportcheckActivity.this, token_key, "");
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				String url = "http://" +
						PreferencesUtils.getString(ReportcheckActivity.this, ip_key, "120.27.2.177")
						+ ":" + PreferencesUtils.getString(ReportcheckActivity.this, port_key, "8069") +
						IndexConstants.REPORTCARD_STOP + "?"
						+ "token=" + token + "&runcard_no=" + runcard_no;
//                "login:","登录帐号","Password":"密码"
				Print("url:::" + url);
//                Map<String,String> mparams=new HashMap<String,String>();
////                mparams.put("login",PreferencesUtils.getString(ReportActivity.this,email_key,"8069"));
//                mparams.put("token",PreferencesUtils.getString(ReportActivity.this, token_key, ""));
//                mparams.put("runcard_no",cardid);
//                mparams.put("qty",qty);
//				mparams.put("weight",weight);
//				mparams.put("gross_weight",grossweight);
//
//
//                String postparams = new Gson().toJson(mparams);
//                postparams= URLEncoder.encode(postparams,"utf-8");
//
////                String postparams ="{"+"login:",mEmail,"Password:",mPassword}//"login:"+mEmail+"&password:"+mPassword;
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
						Print(" return report:::" + jsonObject);
//						ReportProductBeans = new ArrayList<ReportProductBean>();
						msg = jsonObject.getString("message");
						success = jsonObject.getString("success");
						if (success.equals("true")) {
//							ReportFormBeans = new ArrayList<ReportFormBean>();
//							ReportProductBeans = new ArrayList<ReportProductBean>();
//							parseReportproduct(jsonObject);
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
//            showProgress(false);
//			reloadviewText(success,msg);
			Util.showShortToastMessage(ReportcheckActivity.this,msg);
			if(success){
				eCardid.setError(null,null);
			}else{
				textsetError(ReportcheckActivity.this,eCardid,msg);
			}
		}

		@Override
		protected void onCancelled() {
//            showProgress(false);
		}

		private JSONObject parseJson(InputStream ins) {
			byte[] data = new byte[0];   // 把输入流转换成字符数组
			try {
				data = readStream(ins);

				String json = new String(data);        // 把字符数组转换成字符串
				JSONObject jsonObject = new JSONObject(json);//array.getJSONObject(i);
				return jsonObject;
//                Print("login msgmsg:::"+msg);
//            }
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * 182.     * 把输入流转换成字符数组
		 * 183.     * @param inputStream   输入流
		 * 184.     * @return  字符数组
		 * 185.     * @throws Exception
		 * 186.
		 */
		public byte[] readStream(InputStream inputStream) throws Exception {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inputStream.read(buffer)) != -1) {
				bout.write(buffer, 0, len);
			}
			bout.close();
			inputStream.close();

			return bout.toByteArray();
		}

	}

	public class ReportMaterialTask extends AsyncTask<Void, Void, Boolean> {
		//        String lot_no="";
		String runcard_no = "";
		String success = "";
		String msg = "";
		String token = "";
		int responsecode = 0;

		String qty = "";
		String weight = "";
		String gross_weight = "";

		ReportMaterialTask() {
			runcard_no = eCardid.getText().toString();
			weight = eNetweight.getText().toString();
			qty = eReportnum.getText().toString();
			gross_weight = eGrossweight.getText().toString();
			token = PreferencesUtils.getString(ReportcheckActivity.this, token_key, "");
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				String url = "http://" +
						PreferencesUtils.getString(ReportcheckActivity.this, ip_key, "120.27.2.177")
						+ ":" + PreferencesUtils.getString(ReportcheckActivity.this, port_key, "8069") +
						IndexConstants.REPORTMATERIAL + "?"
						+ "token=" + token + "&runcard_no=" + runcard_no + "&weight=" + weight
						+ "&qty=" + qty
						+ "&gross_weight=" + gross_weight;
//                "login:","登录帐号","Password":"密码"
				Print("REPORTMATERIAL url:::" + url);
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
//				conn.setRequestMethod("POST");     //设置以Post方式提交数据
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
						Print(" return:::" + jsonObject);
						ReportProductBeans = new ArrayList<ReportProductBean>();
						msg = jsonObject.getString("message");
						success = jsonObject.getString("success");
						if (success.equals("true")) {
							ReportFormBeans = new ArrayList<ReportFormBean>();
							ReportProductBeans = new ArrayList<ReportProductBean>();
//							parseReportproduct(jsonObject);
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
			reloadviewText(success,msg);
		}

		@Override
		protected void onCancelled() {
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
				return jsonObject;
//                Print("login msgmsg:::"+msg);
//            }
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}


	}

	public class CancleReportTask extends AsyncTask<Void, Void, Boolean> {
		//        String lot_no="";
		String runcard_no = "";
		String success = "";
		String msg = "";
		String token = "";
		int responsecode = 0;

		CancleReportTask() {
			runcard_no = eCardid.getText().toString();
			token = PreferencesUtils.getString(ReportcheckActivity.this, token_key, "");
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				String url = "http://" +
						PreferencesUtils.getString(ReportcheckActivity.this, ip_key, "120.27.2.177")
						+ ":" + PreferencesUtils.getString(ReportcheckActivity.this, port_key, "8069") +
						IndexConstants.REPORTCARD_CANCLE + "?"
						+ "token=" + token + "&runcard_no=" + runcard_no;
//                "login:","登录帐号","Password":"密码"
				Print("url:::" + url);
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
//				conn.setRequestMethod("POST");     //设置以Post方式提交数据
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
						Print(" return:::" + jsonObject);
						ReportProductBeans = new ArrayList<ReportProductBean>();
						msg = jsonObject.getString("message");
						success = jsonObject.getString("success");
						if (success.equals("true")) {
							ReportFormBeans = new ArrayList<ReportFormBean>();
							ReportProductBeans = new ArrayList<ReportProductBean>();
//							parseReportproduct(jsonObject);
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
			reloadviewText(success,msg);
		}

		@Override
		protected void onCancelled() {
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
				return jsonObject;
//                Print("login msgmsg:::"+msg);
//            }
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}


	}

	public class ReportInspectpassTask extends AsyncTask<Void, Void, Boolean> {
		//        String lot_no="";
		String runcard_no = "";
		String success = "";
		String msg = "";
		String token = "";
		int responsecode = 0;

		ReportInspectpassTask() {
			runcard_no = eCardid.getText().toString();
			token = PreferencesUtils.getString(ReportcheckActivity.this, token_key, "");
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				String url = "http://" +
						PreferencesUtils.getString(ReportcheckActivity.this, ip_key, "120.27.2.177")
						+ ":" + PreferencesUtils.getString(ReportcheckActivity.this, port_key, "8069") +
						IndexConstants.REPORT_INSPECTPASS + "?"
						+ "token=" + token + "&runcard_no=" + runcard_no;
//                "login:","登录帐号","Password":"密码"
				Print("url:::" + url);
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
//				conn.setRequestMethod("POST");     //设置以Post方式提交数据
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
						Print(" return:::" + jsonObject);
						ReportProductBeans = new ArrayList<ReportProductBean>();
						msg = jsonObject.getString("message");
						success = jsonObject.getString("success");
						if (success.equals("true")) {
							ReportFormBeans = new ArrayList<ReportFormBean>();
							ReportProductBeans = new ArrayList<ReportProductBean>();
//							parseReportproduct(jsonObject);
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
			reloadviewText(success,msg);
		}

		@Override
		protected void onCancelled() {
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
				return jsonObject;
//                Print("login msgmsg:::"+msg);
//            }
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}


	}

	public class ReportInspectcancleTask extends AsyncTask<Void, Void, Boolean> {
		//        String lot_no="";
		String runcard_no = "";
		String success = "";
		String msg = "";
		String token = "";
		int responsecode = 0;

		ReportInspectcancleTask() {
			runcard_no = eCardid.getText().toString();
			token = PreferencesUtils.getString(ReportcheckActivity.this, token_key, "");
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				String url = "http://" +
						PreferencesUtils.getString(ReportcheckActivity.this, ip_key, "120.27.2.177")
						+ ":" + PreferencesUtils.getString(ReportcheckActivity.this, port_key, "8069") +
						IndexConstants.REPORT_INSPECTCANCLE + "?"
						+ "token=" + token + "&runcard_no=" + runcard_no;
//                "login:","登录帐号","Password":"密码"
				Print("url:::" + url);
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
//				conn.setRequestMethod("POST");     //设置以Post方式提交数据
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
						Print(" return:::" + jsonObject);
						ReportProductBeans = new ArrayList<ReportProductBean>();
						msg = jsonObject.getString("message");
						success = jsonObject.getString("success");
						if (success.equals("true")) {
							ReportFormBeans = new ArrayList<ReportFormBean>();
							ReportProductBeans = new ArrayList<ReportProductBean>();
//							parseReportproduct(jsonObject);
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
			reloadviewText(success,msg);
		}

		@Override
		protected void onCancelled() {
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

	public class ReportInspectNgTask extends AsyncTask<Void, Void, Boolean> {
		//        String lot_no="";
		String runcard_no = "";
		String success = "";
		String msg = "";
		String token = "";
		int responsecode = 0;

		ReportInspectNgTask() {
			runcard_no = eCardid.getText().toString();
			token = PreferencesUtils.getString(ReportcheckActivity.this, token_key, "");
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				String url = "http://" +
						PreferencesUtils.getString(ReportcheckActivity.this, ip_key, "120.27.2.177")
						+ ":" + PreferencesUtils.getString(ReportcheckActivity.this, port_key, "8069") +
						IndexConstants.REPORT_INSPECTNG + "?"
						+ "token=" + token + "&runcard_no=" + runcard_no;
//                "login:","登录帐号","Password":"密码"
				Print("url:::" + url);
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
//				conn.setRequestMethod("POST");     //设置以Post方式提交数据
//                conn.setUseCaches(false);               //使用Post方式不能使用缓存
//                //设置请求体的类型是文本类型
//                conn.setRequestProperty("Content-Type", "application/json");
//                conn.setRequestProperty("Content-Length", String.valueOf(data.length)); // 注意是字节长度, 不是字符长度
//
////                conn.setDoOutput(true); // 准备写出
//                conn.getOutputStream().write(data);

				responsecode = conn.getResponseCode();
				Print(" ng return respondcode:::" + responsecode);
				if (responsecode == 200) {
					InputStream ins = conn.getInputStream();
					JSONObject rootjsonObject = parseJson(ins);
					Print(" ng return rootjsonObject:::" + rootjsonObject);
					JSONObject jsonObject = null;
					if (rootjsonObject != null) {
						jsonObject = rootjsonObject.getJSONArray("results").getJSONObject(0);
					}
					if (jsonObject != null) {
						Print(" return:::" + jsonObject);
						ReportProductBeans = new ArrayList<ReportProductBean>();
						msg = jsonObject.getString("message");
						success = jsonObject.getString("success");
						if (success.equals("true")) {
							ReportFormBeans = new ArrayList<ReportFormBean>();
							ReportProductBeans = new ArrayList<ReportProductBean>();
//							parseReportproduct(jsonObject);
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
			reloadviewText(success,msg);
		}

		@Override
		protected void onCancelled() {
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

	public class ReportMaterialCancleTask extends AsyncTask<Void, Void, Boolean> {
		//        String lot_no="";
		String runcard_no = "";
		String success = "";
		String msg = "";
		String token = "";
		int responsecode = 0;

		ReportMaterialCancleTask() {
			runcard_no = eCardid.getText().toString();
			token = PreferencesUtils.getString(ReportcheckActivity.this, token_key, "");
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				String url = "http://" +
						PreferencesUtils.getString(ReportcheckActivity.this, ip_key, "120.27.2.177")
						+ ":" + PreferencesUtils.getString(ReportcheckActivity.this, port_key, "8069") +
						IndexConstants.REPORT_MATERIALCANCLE + "?"
						+ "token=" + token + "&runcard_no=" + runcard_no;
//                "login:","登录帐号","Password":"密码"
				Print("url:::" + url);
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
//				conn.setRequestMethod("POST");     //设置以Post方式提交数据
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
						Print(" return:::" + jsonObject);
						ReportProductBeans = new ArrayList<ReportProductBean>();
						msg = jsonObject.getString("message");
						success = jsonObject.getString("success");
						if (success.equals("true")) {
							ReportFormBeans = new ArrayList<ReportFormBean>();
							ReportProductBeans = new ArrayList<ReportProductBean>();
//							parseReportproduct(jsonObject);
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
			reloadviewText(success,msg);
		}

		@Override
		protected void onCancelled() {
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


	String TAG = "Checkctivity::";

	public void Print(String s) {
		System.out.println(TAG + s);
	}

	public String CheckNullString(String s) {

		if (s == null || s.equals("null")) {
			return "";
		} else {
			return s;
		}
	}

	protected void parseReportid(JSONObject jsonObject) {
		JSONArray dataarr = null;
		try {
			dataarr = jsonObject.getJSONArray("data");
			for (int i = 0; i < dataarr.length(); i++) {
				JSONObject reprotformdataObject = dataarr.getJSONObject(i);
				ReportFormBean ReportFormBean = beanParseUtility.parse(reprotformdataObject, ReportFormBean.class);
				ReportFormBeans.add(ReportFormBean);
				if(reprotformdataObject.has("line_data")) {
					JSONArray linedataarr = reprotformdataObject.getJSONArray("line_data");
					for (int j = 0; j < linedataarr.length(); j++) {
						JSONObject reprotformlinedataObject = linedataarr.getJSONObject(j);
						ReportProductBean ReportProductlineBean =
								beanParseUtility.parse(reprotformlinedataObject, ReportProductBean.class);
						ReportProductBeans.add(ReportProductlineBean);
					}
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	protected void parseReportproduct(JSONObject jsonObject) {
		JSONArray dataarr = null;
		try {
			dataarr = jsonObject.getJSONArray("data");
			for (int i = 0; i < dataarr.length(); i++) {
				JSONObject reprotformdataObject = dataarr.getJSONObject(i);
//				ReportFormBean ReportFormBean = beanParseUtility.parse(reprotformdataObject, ReportFormBean.class);
//				ReportFormBeans.add(ReportFormBean);
				ReportProductBean ReportProductdataBean =
						beanParseUtility.parse(reprotformdataObject, ReportProductBean.class);
				ReportProductBeans.add(ReportProductdataBean);
//				if(reprotformdataObject.has("line_data")) {
//					JSONArray linedataarr = reprotformdataObject.getJSONArray("line_data");
//					for (int j = 0; j < linedataarr.length(); j++) {
//						JSONObject reprotformlinedataObject = linedataarr.getJSONObject(j);
//						ReportProductBean ReportProductlineBean =
//								beanParseUtility.parse(reprotformlinedataObject, ReportProductBean.class);
//						ReportProductBeans.add(ReportProductlineBean);
//					}
//				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	protected void reloadviewText(Boolean success,String msg){
		Util.showToastMessage(ReportcheckActivity.this, msg);
		if (success&&ReportFormBeans!=null) {
			Print("ReportProductBeans size::"+ReportProductBeans.size());
			eCardid.requestFocus();
			if(ReportFormBeans.size()>0) {
				eCurrentprocess.setText(CheckNullString(ReportFormBeans.get(0).process_name));
				ework_team_no.setText(CheckNullString(ReportFormBeans.get(0).work_team_no));
				ePackagename.setText(CheckNullString(ReportFormBeans.get(0).package_name));
				eEquipmentcode.setText(CheckNullString(ReportFormBeans.get(0).equipment_code));
				String process_status = ReportFormBeans.get(0).process_status;
				eReportstate.setText(CheckNullString(process_statusmap.get(process_status)));
//				eContainerid.setText(CheckNullString(ReportFormBeans.get(0).container_no));
//				eContainerweight.setText(CheckNullString(ReportFormBeans.get(0).container_weight));
//				eThousandweight.setText(CheckNullString(ReportFormBeans.get(0).unit_weight));
//				eNetweight.setText(CheckNullString(ReportFormBeans.get(0).weight));
//				eGrossweight.setText(CheckNullString(ReportFormBeans.get(0).gross_weight));
//				eReportnum.setText(CheckNullString( ReportFormBeans.get(0).qty));
//				eSplit_merge_cardid.setText(CheckNullString( ReportFormBeans.get(0).qty));
//				eSplit_merge_container_no.setText(CheckNullString( ReportFormBeans.get(0).qty));
//				eSplit_merge_container_weight.setText(CheckNullString( ReportFormBeans.get(0).qty));

//				eReportstate.setText(CheckNullString(process_statusmap.get(process_status)));
				tContainerid.setText(CheckNullString(ReportFormBeans.get(0).container_no));
				tContainerweight.setText(CheckNullString(ReportFormBeans.get(0).container_weight));
				tThousandweight.setText(CheckNullString(ReportFormBeans.get(0).unit_weight));
				tNetweight.setText(CheckNullString(ReportFormBeans.get(0).weight));
				tGrossweight.setText(CheckNullString(ReportFormBeans.get(0).gross_weight));
				tReportnum.setText(CheckNullString( ReportFormBeans.get(0).qty));

//				TextView tContainerweight;
//				TextView tThousandweight;
//				TextView tNetweight;
//				TextView tWaste;
//				TextView tGrossweight;
//				TextView tReportnum;
//				EditText eCardid;
//				EditText eCurrentprocess;
//				EditText eReportstate;
//				EditText eContainerid;
//				EditText eContainerweight;
//				EditText eThousandweight;
//				EditText eNetweight;
//				EditText eWaste;
//				EditText eGrossweight;
//				EditText eReportnum;


//                 eWaste.setText(ReportProductBeans.get(0).);
//                 eGrossweight.setText(ReportProductBeans.get(0).weight);
//                 eReportnum.setText(ReportProductBeans.get(0).);
//				String process_status = ReportProductBeans.get(0).process_status;
//				{
				if (process_status.equals("to_report")) {
//					report_submitbutton.setVisibility(View.VISIBLE);
//					reportmaterialcancel_submitbutton.setVisibility(View.VISIBLE);
//					report_stopruncard_button.setVisibility(View.VISIBLE);
				} else if (process_status.equals("to_inspect")) {
//					reportmaterialcancel_submitbutton.setVisibility(View.VISIBLE);
					reportinspect_submitbutton.setVisibility(View.VISIBLE);
					reportinspect_ng_submitbutton.setVisibility(View.VISIBLE);

					reportinspectcancel_submitbutton.setVisibility(View.GONE);

//					reportcancel_submitbutton.setVisibility(View.VISIBLE);
				} else if (process_status.equals("to_material")) {
//					reportmaterial_submitbutton.setVisibility(View.VISIBLE);
					reportinspectcancel_submitbutton.setVisibility(View.VISIBLE);

					reportinspect_submitbutton.setVisibility(View.GONE);
					reportinspect_ng_submitbutton.setVisibility(View.GONE);
				}
			}else{
				eCardid.setText("");
				ework_team_no.setText("");
				ePackagename.setText("");
				eEquipmentcode.setText("");
				eCurrentprocess.setText("");
				eReportstate.setText("");
//				eContainerid.setText("");
//				eContainerweight.setText("");
//				eThousandweight.setText("");
//				eNetweight.setText("");
//				eGrossweight.setText("");
//				eReportnum.setText("");
//				eWaste.setText("");

				tContainerid.setText("");
				tContainerweight.setText("");
				tThousandweight.setText("");
				tNetweight.setText("");
				tGrossweight.setText("");
				tReportnum.setText("");

			}
			refreshdatalist();
//				}
		} else {
			Errortext.setVisibility(View.VISIBLE);
			Errortext.setText("数据错误"+msg);
//			eReportnum.setError(msg);
//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
		}
	}
	public void refreshdatalist(){
		Collections.sort(ReportProductBeans,idComparator);
		mAdapter = new ReportdetailAdapter(ReportcheckActivity.this, ReportProductBeans);
		mSimpleDetailList.setAdapter(mAdapter);
		setListViewHeightBasedOnChildren(mSimpleDetailList);
	}
	public static Comparator idComparator = new Comparator() {
		@Override
		public int compare(Object o1, Object o2) {
			return (Integer.compare(Integer.parseInt(((ReportProductBean) o1).sequence), Integer.parseInt(((ReportProductBean) o2).sequence)));
		}
	};
//	private void changePackagestatus(boolean status){
//		ePackagename.setFocusableInTouchMode(status);
//		ePackagename.setFocusable(status);
//	}
//	private void changeEquipmentstatus(boolean status){
//		eEquipmentcode.setFocusableInTouchMode(status);
//		eEquipmentcode.setFocusable(status);
//	}
	private void changeEditTextstatus(EditText etext,boolean status){
		etext.setFocusableInTouchMode(status);
		etext.setFocusable(status);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		Util.showShortToastMessage(EntryWarehouseActivity.this,"keycode:"+keyCode);
//		mentrynumboxes.setError(boxnum+"keyCode:"+keyCode);
		if(keyCode==301) {
			if (eCardid.isFocused()) {
				eCardid.setText("");
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
