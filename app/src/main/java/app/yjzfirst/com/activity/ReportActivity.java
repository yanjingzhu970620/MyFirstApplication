package app.yjzfirst.com.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.yjzfirst.bean.ReportFormBean;
import com.yjzfirst.util.IndexConstants;
import com.yjzfirst.util.PreferencesUtils;
import com.yjzfirst.util.Util;
import com.yjzfirst.util.beanParseUtility;

import org.json.JSONArray;
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
import static com.yzq.zxinglibrary.common.Constant.CODED_CONTENT;

public class ReportActivity extends AppCompatActivity {
    private CheckCardidTask mCheckTask = null;
    EditText eCardid;
    EditText eCurrentprocess;
    EditText eReportstate;
    EditText eContainerid;
    EditText eContainerweight;
    EditText eThousandweight;
    EditText eNetweight;
    EditText eWaste;
    EditText eGrossweight;
    EditText eReportnum;
//    EditText mcheckbatchnumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Window window = ReportActivity.this.getWindow();

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(Color.BLACK);



            //底部导航栏

            //window.setNavigationBarColor(activity.getResources().getColor(colorResId));

        }
        eCardid = (EditText) findViewById(R.id.edittext_report_card_id);
//        mcheckbatchnumber.addTextChangedListener(shipsWatcher);
        eCurrentprocess = (EditText) findViewById(R.id.edittext_current_process);
//        mcheckbarcode.addTextChangedListener(shipsWatcher);
        eReportstate = (EditText) findViewById(R.id.edittext_report_state);
//        mchecklibrarynumber.addTextChangedListener(shipsWatcher);
        eContainerid = (EditText) findViewById(R.id.edittext_container_id);
        eContainerweight = (EditText) findViewById(R.id.edittext_container_weight);
//        mcheckNumberperbox.addTextChangedListener(shipsWatcher);
        eThousandweight = (EditText) findViewById(R.id.edit_thousand_weight);
//        mchecknumboxes.addTextChangedListener(shipsWatcher);
        eNetweight = (EditText) findViewById(R.id.edit_net_weight);
        eWaste = (EditText) findViewById(R.id.edit_waste);
        eGrossweight = (EditText) findViewById(R.id.edit_gross_weight);
        eReportnum = (EditText) findViewById(R.id.edit_report_num);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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

    private TextWatcher shipsWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
//              if(s.){}
            System.err.println(s.toString());
        }
    };
    public void onClick(View view) {
        if (view.getId() == R.id.check_back) {
            finish();
        }else if (view.getId() == R.id.check_submit_button) {
            attemptCheck();
        }else if(view.getId() == R.id.report_card_id_button){
            Util.startQrCode(ReportActivity.this);
        }
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);



        // 扫描二维码/条码回传

        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {

            if (data != null) {

                String content = data.getStringExtra(CODED_CONTENT);
                eCardid.setText(content);
                Util.showShortToastMessage(ReportActivity.this,"扫描结果为："+ content);
                attemptCheck();
            }

        }

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
        String cardid=eCardid.getText().toString();
        String containerid="132323";//eContainerid.getText().toString();
        boolean cancel = false;
//
        if (cardid.equals("")||
                containerid.equals("")) {
            cancel=true;
        }
        if (cancel) {
//            // There was an error; don't attempt login and focus the first
//            // form field with an error.
//            focusView.requestFocus();
            Util.showShortToastMessage(ReportActivity.this,"请先扫描所有条目");
        } else {
//            // Show a progress spinner, and kick off a background task to
//            // perform the user login attempt.
//        showProgress(true);
        mCheckTask =new CheckCardidTask();
        mCheckTask.execute((Void) null);
        }


    }
    public String ip_key="ip";
    public String port_key="port";
    private String email_key = "email";
    ArrayList<ReportFormBean> reportFormBeans =new ArrayList<ReportFormBean>();
    public class CheckCardidTask extends AsyncTask<Void, Void, Boolean> {
//        String lot_no="";
        String cardid="";
        String success="";
        String msg="";
        int responsecode=0;
        CheckCardidTask() {
            cardid=eCardid.getText().toString();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                String url="http://"+
                        PreferencesUtils.getString(ReportActivity.this,ip_key,"120.27.2.177")
                        +":"+PreferencesUtils.getString(ReportActivity.this,port_key,"8061")+
                        IndexConstants.CHECKCARDID+"?token="+
                        PreferencesUtils.getString(ReportActivity.this,token_key,"")+"&runcard_no="+cardid;
//                "login:","登录帐号","Password":"密码"
                Print("url:::"+url);
//                Map<String,String> mparams=new HashMap<String,String>();
//                mparams.put("login",PreferencesUtils.getString(ReportActivity.this,email_key,"8061"));
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

                responsecode=conn.getResponseCode();
                if(responsecode==200) {
                    InputStream ins = conn.getInputStream();
                    JSONObject rootjsonObject= parseJson(ins);
                    JSONObject jsonObject= null;
                    if (rootjsonObject != null) {
                        jsonObject = rootjsonObject.getJSONArray("results").getJSONObject(0);
                    }
                    if(jsonObject!=null) {
                        Print(" return:::"+jsonObject);
                        reportFormBeans =new ArrayList<ReportFormBean>();
                        msg = jsonObject.getString("message");
                        success = jsonObject.getString("success");
                        JSONArray dataarr=jsonObject.getJSONArray("data");
                        for(int i=0;i<dataarr.length();i++){
                            JSONObject reprotformdataObject=dataarr.getJSONObject(i);
                            ReportFormBean reportFormBean=beanParseUtility.parse(reprotformdataObject, ReportFormBean.class);
                            reportFormBeans.add(reportFormBean);
                        }
                    }
//                    String s = ins.toString();
//                    System.err.println("sssssssss:::"+s);
                }
                Print(" return:::"+reportFormBeans.size()+"  reportFormBeans"+reportFormBeans.get(0).product_name);
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
            mCheckTask = null;
//            showProgress(false);

            if (success) {
//                PreferencesUtils.putString(CheckActivity.this,email_key,mEmail);
//                PreferencesUtils.putString(CheckActivity.this,password_key,mPassword);
//                Intent intent=new Intent(CheckActivity.this,MainActivity.class);
//                CheckActivity.this.startActivity(intent);
//                finish();
                Util.showShortToastMessage(ReportActivity.this,msg);
                 eCurrentprocess.setText(reportFormBeans.get(0).process_name);
                 eReportstate.setText(reportFormBeans.get(0).state);
                 eContainerid.setText(reportFormBeans.get(0).container_id);
                 eContainerweight.setText(reportFormBeans.get(0).container_weight);
                 eThousandweight.setText(reportFormBeans.get(0).unit_weight);
                 eNetweight.setText(reportFormBeans.get(0).weight);
//                 eWaste.setText(reportFormBeans.get(0).);
//                 eGrossweight.setText(reportFormBeans.get(0).weight);
//                 eReportnum.setText(reportFormBeans.get(0).);
            } else {
                Util.showShortToastMessage(ReportActivity.this,msg);
//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mCheckTask = null;
//            showProgress(false);
        }
        private JSONObject parseJson(InputStream ins){
            byte[] data = new byte[0];   // 把输入流转换成字符数组
            try {
                data = readStream(ins);

                String  json = new String(data);        // 把字符数组转换成字符串
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
        /**
         182.     * 把输入流转换成字符数组
         183.     * @param inputStream   输入流
         184.     * @return  字符数组
         185.     * @throws Exception
         186.     */
        public  byte[] readStream(InputStream inputStream) throws Exception {
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



//    public class TakingStockTask extends AsyncTask<Void, Void, Boolean> {
//        //        String lot_no="";
//        String location="";
//        String barcode="";
//        String min_box="";
//        String lot_no="";
//        String cn_box="";
//        String success="";
//        String msg="";
//        int responsecode=0;
//        TakingStockTask() {
//            lot_no=mcheckbatchnumber.getText().toString();
//            barcode=mcheckbarcode.getText().toString();
//            location=mchecklibrarynumber.getText().toString();
//            cn_box=mchecknumboxes.getText().toString();
//            min_box=mcheckNumberperbox.getText().toString();
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            // TODO: attempt authentication against a network service.
//
//            try {
//                String url="http://"+PreferencesUtils.getString(ReportActivity.this,ip_key,"120.27.2.177")
//                        +":"+PreferencesUtils.getString(ReportActivity.this,port_key,"8061")+ IndexConstants.TAKINGSTOCK;
////                "login:","登录帐号","Password":"密码"
//                Print("url:::"+url);
//                Map<String,String> mparams=new HashMap<String,String>();
//                mparams.put("login",PreferencesUtils.getString(ReportActivity.this,email_key,"8061"));
//                mparams.put("lot_no",lot_no);
//                mparams.put("barcode",barcode);
//                mparams.put("location",location);
//                mparams.put("min_box",min_box);
//                mparams.put("cn_box",cn_box);
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
//            Util.showShortToastMessage(ReportActivity.this,msg);
//            if (success) {
//
//                 mcheckbatchnumber.setText("");
//                 mcheckbarcode.setText("");
//                 mchecklibrarynumber.setText("");
//                 mcheckNumberperbox.setText("");
//                 mchecknumboxes.setText("");
//                 mcheckbatchnumber.requestFocus();
//
////                PreferencesUtils.putString(CheckActivity.this,email_key,mEmail);
////                PreferencesUtils.putString(CheckActivity.this,password_key,mPassword);
////                Intent intent=new Intent(CheckActivity.this,MainActivity.class);
////                CheckActivity.this.startActivity(intent);
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
    String TAG="Checkctivity::";
    public void Print(String s){
        System.out.println(TAG+s);
    }
}
