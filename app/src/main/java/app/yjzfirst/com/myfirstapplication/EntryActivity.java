package app.yjzfirst.com.myfirstapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.yjzfirst.util.IndexConstants;
import com.yjzfirst.util.PreferencesUtils;
import com.yjzfirst.util.Util;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class EntryActivity extends AppCompatActivity {
    EditText mentrybatchnumber;
    EditText mentrybarcode;
    EditText mentrylibrarynumber;
    EditText mentryNumberperbox;
    EditText mentrynumboxes;
    EditText entryweightthousands;
    private CheckCodeTask mentryTask = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        mentrybatchnumber = (EditText) findViewById(R.id.entry_batch_number);
//        mentrybatchnumber.addTextChangedListener(shipsWatcher);
        mentrybarcode = (EditText) findViewById(R.id.entry_bar_code);
//        mentrybarcode.addTextChangedListener(shipsWatcher);
        mentrylibrarynumber = (EditText) findViewById(R.id.entry_library_number);
//        mentrylibrarynumber.addTextChangedListener(shipsWatcher);
        mentryNumberperbox = (EditText) findViewById(R.id.entry_Number_per_box);
//        mentryNumberperbox.addTextChangedListener(shipsWatcher);
        mentrynumboxes = (EditText) findViewById(R.id.entry_num_boxes);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        entryweightthousands = (EditText) findViewById(R.id.entry_weight_thousands);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.entry_back) {
            finish();
        }else if (view.getId() == R.id.entry_submit_button) {
            attemptCheck();
        }
    }

    private void attemptCheck() {
        if (mentryTask != null) {
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
        String lot_no=mentrybatchnumber.getText().toString();
        String barcode=mentrybarcode.getText().toString();
        String location=mentrylibrarynumber.getText().toString();
        String cn_box=mentrynumboxes.getText().toString();
        String min_box=mentryNumberperbox.getText().toString();

        boolean cancel = false;
//
        if (lot_no.equals("")||
                barcode.equals("")||
                location.equals("")||
                cn_box.equals("")||
                min_box.equals("")) {
            cancel=true;
        }
        if (cancel) {
//            // There was an error; don't attempt login and focus the first
//            // form field with an error.
//            focusView.requestFocus();
            Util.showShortToastMessage(EntryActivity.this,"请先扫描所有条目");
        } else {
//            // Show a progress spinner, and kick off a background task to
//            // perform the user login attempt.
//        showProgress(true);
            mentryTask =new CheckCodeTask();
            mentryTask.execute((Void) null);
        }


    }
    public String ip_key="ip";
    public String port_key="port";
    private String email_key = "email";
    public class CheckCodeTask extends AsyncTask<Void, Void, Boolean> {
        //        String lot_no="";
        String location="";
        String barcode="";
        String lot_no="";
        String success="";
        String msg="";
        int responsecode=0;
        CheckCodeTask() {
            lot_no=mentrybatchnumber.getText().toString();
            barcode=mentrybarcode.getText().toString();
            location=mentrylibrarynumber.getText().toString();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                String url="http://"+PreferencesUtils.getString(EntryActivity.this,ip_key,"101.132.164.169")
                        +":"+PreferencesUtils.getString(EntryActivity.this,port_key,"8090")+ IndexConstants.TAKINGCHECKBARCODE;
//                "login:","登录帐号","Password":"密码"
                Print("url:::"+url);
                Map<String,String> mparams=new HashMap<String,String>();
                mparams.put("login",PreferencesUtils.getString(EntryActivity.this,email_key,"8090"));
                mparams.put("lot_no",lot_no);
                mparams.put("barcode",barcode);
                mparams.put("location",location);


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

                responsecode=conn.getResponseCode();
                if(responsecode==200) {
                    InputStream ins = conn.getInputStream();
                    JSONObject jsonObject= parseJson(ins);
                    if(jsonObject!=null) {
                        msg = jsonObject.getString("message");
                        success = jsonObject.getString("success");
                    }
//                    String s = ins.toString();
//                    System.err.println("sssssssss:::"+s);
                }
                Print(" return:::"+responsecode);
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
            mentryTask = null;
//            showProgress(false);
//            Util.showShortToastMessage(EntryActivity.this,msg);
            if (success) {
//                PreferencesUtils.putString(EntryActivity.this,email_key,mEmail);
//                PreferencesUtils.putString(EntryActivity.this,password_key,mPassword);
//                Intent intent=new Intent(EntryActivity.this,MainActivity.class);
//                EntryActivity.this.startActivity(intent);
//                finish();
                InStockTask tst =new InStockTask();
                tst.execute();
            } else {
                Util.showShortToastMessage(EntryActivity.this,msg);
//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mentryTask = null;
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
    public class InStockTask extends AsyncTask<Void, Void, Boolean> {
        //        String lot_no="";
        String location="";
        String barcode="";
        String min_box="";
        String lot_no="";
        String cn_box="";
        String stock_weight="";
        String success="";
        String msg="";
        int responsecode=0;
        InStockTask() {
            lot_no=mentrybatchnumber.getText().toString();
            barcode=mentrybarcode.getText().toString();
            location=mentrylibrarynumber.getText().toString();
            cn_box=mentrynumboxes.getText().toString();
            min_box=mentryNumberperbox.getText().toString();
            stock_weight=entryweightthousands.getText().toString();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                String url="http://"+PreferencesUtils.getString(EntryActivity.this,ip_key,"101.132.164.169")
                        +":"+PreferencesUtils.getString(EntryActivity.this,port_key,"8090")+ IndexConstants.INSTOCK;
//                "login:","登录帐号","Password":"密码"
                Print("url:::"+url);
                Map<String,String> mparams=new HashMap<String,String>();
                mparams.put("login",PreferencesUtils.getString(EntryActivity.this,email_key,"8090"));
                mparams.put("lot_no",lot_no);
                mparams.put("barcode",barcode);
                mparams.put("location",location);
                mparams.put("min_box",min_box);
                mparams.put("cn_box",cn_box);
                mparams.put("stock_weight",stock_weight);

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

                responsecode=conn.getResponseCode();
                if(responsecode==200) {
                    InputStream ins = conn.getInputStream();
                    JSONObject jsonObject= parseJson(ins);
                    if(jsonObject!=null) {
                        msg = jsonObject.getString("message");
                        success = jsonObject.getString("success");
                    }
//                    String s = ins.toString();
//                    System.err.println("sssssssss:::"+s);
                }
                Print(" return:::"+responsecode);
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
//            mAuthTask = null;
//            showProgress(false);
            Util.showShortToastMessage(EntryActivity.this,msg);
            if (success) {
                 mentrybatchnumber.setText("");
                 mentrybarcode.setText("");
                 mentrylibrarynumber.setText("");
                 mentryNumberperbox.setText("");
                 mentrynumboxes.setText("");
                 entryweightthousands.setText("");
                mentrybatchnumber.requestFocus();
//                PreferencesUtils.putString(EntryActivity.this,email_key,mEmail);
//                PreferencesUtils.putString(EntryActivity.this,password_key,mPassword);
//                Intent intent=new Intent(EntryActivity.this,MainActivity.class);
//                EntryActivity.this.startActivity(intent);
//                finish();
            } else {
//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
//            mAuthTask = null;
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
    String TAG="Entryactivity::";
    public void Print(String s){
        System.out.println(TAG+s);
    }
}
