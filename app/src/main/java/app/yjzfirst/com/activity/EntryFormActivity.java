package app.yjzfirst.com.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.google.gson.Gson;
import com.yjzfirst.bean.EntryProductBean;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.yjzfirst.util.IndexConstants.token_key;
import static com.yjzfirst.util.Util.REQUEST_CODE_SCAN;
import static com.yjzfirst.util.Util.readStream;
import static com.yzq.zxinglibrary.common.Constant.CODED_CONTENT;

public class EntryFormActivity extends AppCompatActivity {
    EditText mentryorderid;
    EditText mentrybarcode;
    EditText mentryOrdernumber;
    EditText mentryNumberperbox;
    EditText mentrynumboxes;
    EditText mentrybillnumber;
	EntryProductBean productBean;
	enum qrcodemode  {
		WORKORDER_ID, PRODUCT_CODE
	};
	private qrcodemode qrcodetextmode=qrcodemode.WORKORDER_ID;
	WorkOrderBean ReportProductlineBean;
//    private CheckCodeTask mentryTask = null;
    private int boxnum=0;
	private ArrayList<Map<String,String>> boxesnum=new ArrayList<Map<String,String>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entryform);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Window window = EntryFormActivity.this.getWindow();

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(Color.BLACK);



            //底部导航栏

            //window.setNavigationBarColor(activity.getResources().getColor(colorResId));

        }
        mentryorderid = (EditText) findViewById(R.id.entryform_orderid);
//        mentrybatchnumber.addTextChangedListener(shipsWatcher);
        mentrybarcode = (EditText) findViewById(R.id.entryform_bar_code);
//        mentrybarcode.addTextChangedListener(shipsWatcher);
        mentryOrdernumber = (EditText) findViewById(R.id.entryform_order_number);
//        mentrylibrarynumber.addTextChangedListener(shipsWatcher);
        mentryNumberperbox = (EditText) findViewById(R.id.entryform_Number_per_box);
//        mentryNumberperbox.addTextChangedListener(shipsWatcher);
        mentrynumboxes = (EditText) findViewById(R.id.entryform_num_boxes);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mentrybillnumber = (EditText) findViewById(R.id.entryform_billnumber);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.entryform_back) {
            finish();
        }else if (view.getId() == R.id.entryform_orderid_button) {
			qrcodetextmode=qrcodemode.WORKORDER_ID;
            Util.startQrCode(EntryFormActivity.this);
//            attemptCheck();
        }else if(view.getId() == R.id.entryform_bar_code_button){
			qrcodetextmode=qrcodemode.PRODUCT_CODE;
			Util.startQrCode(EntryFormActivity.this);
		}
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);



        // 扫描二维码/条码回传

        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {

            if (data != null) {

                String content = data.getStringExtra(CODED_CONTENT);

                Util.showShortToastMessage(EntryFormActivity.this,"扫描结果为："+ content);
				if(qrcodetextmode==qrcodemode.WORKORDER_ID){

//                    CheckproductinfoTask checkproductinfoTask=new CheckproductinfoTask(content);
					CheckOrderidTask checkorderidTask=new CheckOrderidTask(content);
					checkorderidTask.execute();
				}else if(qrcodetextmode==qrcodemode.PRODUCT_CODE){
					CheckProductidTask checkprodyctidTask=new CheckProductidTask(content);
					checkprodyctidTask.execute();
				}
            }

        }

    }


	private void saveBoxNum(String code,String num){
		Map<String,String> map=new HashMap<String,String>();
		map.put(code,num);
		boxesnum.add(map);//存起每个产品的数量 提交使用

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
//            Util.showShortToastMessage(EntryFormActivity.this,"请先扫描所有条目");
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
//                String url="http://"+PreferencesUtils.getString(EntryFormActivity.this,ip_key,"120.27.2.177")
//                        +":"+PreferencesUtils.getString(EntryFormActivity.this,port_key,"8062")+ IndexConstants.TAKINGCHECKBARCODE;
////                "login:","登录帐号","Password":"密码"
//                Print("url:::"+url);
//                Map<String,String> mparams=new HashMap<String,String>();
//                mparams.put("login",PreferencesUtils.getString(EntryFormActivity.this,email_key,"8062"));
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
//                Util.showShortToastMessage(EntryFormActivity.this,msg);
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
		String success = "";
		String msg = "";
		int responsecode = 0;

		CheckOrderidTask(String content) {
			String[] workorderinfo=content.split(",");
			if(workorderinfo.length>1)
			orderid = workorderinfo[0];
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				String url = "http://" +
						PreferencesUtils.getString(EntryFormActivity.this, ip_key, "120.27.2.177")
						+ ":" + PreferencesUtils.getString(EntryFormActivity.this, port_key, "8062") +
						IndexConstants.CHECKORDERID + "?token=" +
						PreferencesUtils.getString(EntryFormActivity.this, token_key, "")
						+ "&workorder_no=" + orderid;
//                "login:","登录帐号","Password":"密码"
				Print("workorder_no url:::" + url);
//                Map<String,String> mparams=new HashMap<String,String>();
//                mparams.put("login",PreferencesUtils.getString(ReportActivity.this,email_key,"8062"));
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
			if(success) {
				mentryorderid.setText(orderid);
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

	public class CheckProductidTask extends AsyncTask<Void, Void, Boolean> {
		//        String lot_no="";
		String product_code = "";
		String packaging_code = "";
		String success = "";
		String msg = "";
		int responsecode = 0;

		CheckProductidTask(String content) {
			String[] workorderinfo=content.split(",");
			if(workorderinfo.length>1)
				product_code = workorderinfo[0];
			    packaging_code= workorderinfo[3];
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				String url = "http://" +
						PreferencesUtils.getString(EntryFormActivity.this, ip_key, "120.27.2.177")
						+ ":" + PreferencesUtils.getString(EntryFormActivity.this, port_key, "8062") +
						IndexConstants.CHECKENTRYPRODUCTID + "?token=" +
						PreferencesUtils.getString(EntryFormActivity.this, token_key, "")
						+ "&product_code=" + product_code+ "&packaging_code=" + packaging_code;
//                "login:","登录帐号","Password":"密码"
				Print("workorder_no url:::" + url);
//                Map<String,String> mparams=new HashMap<String,String>();
//                mparams.put("login",PreferencesUtils.getString(ReportActivity.this,email_key,"8062"));
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
			if(success) {
				mentryorderid.setText(product_code);
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
//                String url="http://"+PreferencesUtils.getString(EntryFormActivity.this,ip_key,"120.27.2.177")
//                        +":"+PreferencesUtils.getString(EntryFormActivity.this,port_key,"8062")+ IndexConstants.INSTOCK;
////                "login:","登录帐号","Password":"密码"
//                Print("url:::"+url);
//                Map<String,String> mparams=new HashMap<String,String>();
//                mparams.put("login",PreferencesUtils.getString(EntryFormActivity.this,email_key,"8062"));
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
//            Util.showShortToastMessage(EntryFormActivity.this,msg);
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
				JSONObject entryformdataObject = dataarr.getJSONObject(i);
				ReportProductlineBean = beanParseUtility.parse(entryformdataObject, WorkOrderBean.class);


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
    String TAG="Entryactivity::";
    public void Print(String s){
        System.out.println(TAG+s);
    }
}
