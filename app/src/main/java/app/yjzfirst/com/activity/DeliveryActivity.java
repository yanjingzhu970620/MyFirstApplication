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
import android.widget.ListView;

import com.google.gson.Gson;
import com.yjzfirst.adapter.ChooseShipAdapter;
import com.yjzfirst.bean.DeliveryBean;
import com.yjzfirst.util.IndexConstants;
import com.yjzfirst.util.PreferencesUtils;
import com.yjzfirst.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yjzfirst.util.IndexConstants.ip_key;
import static com.yjzfirst.util.IndexConstants.port_key;
import static com.yjzfirst.util.IndexConstants.token_key;
import static com.yjzfirst.util.Util.REQUEST_CODE_SCAN;
import static com.yjzfirst.util.Util.readStream;
import static com.yzq.zxinglibrary.common.Constant.CODED_CONTENT;

public class DeliveryActivity extends AppCompatActivity {
    EditText mdeliverybatchnumber;
    EditText mdeliverybarcode;
    EditText mdeliverylibrarynumber;
    EditText mdeliveryNumberperbox;
    EditText mdeliverynumboxes;
    EditText mdeliverystocknumboxes;
    EditText mdeliveryOrdernumber;
    ListView mSimpleDetailList;
    ChooseShipAdapter mAdapter;
    List<DeliveryBean> deliveryBean= new ArrayList<DeliveryBean>();
    private OutStockTask mdeliveryTask = null;
    enum qrcodemode  {
        BATCH_NUMBER, LIBRARY_NUMBER,BAR_CODE
    };
    private qrcodemode qrcodetextmode=qrcodemode.BAR_CODE;
    private int boxnum=0;
    private ArrayList<Map<String,String>> boxesnum=new ArrayList<Map<String,String>>();
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
        mdeliverystocknumboxes= (EditText) findViewById(R.id.delivery_stocknum_boxes);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mdeliveryOrdernumber = (EditText) findViewById(R.id.delivery_Order_number);

        mSimpleDetailList = (ListView) findViewById(R.id.delivery_infolist);
        mAdapter = new ChooseShipAdapter(this, deliveryBean);
        mSimpleDetailList.setAdapter(mAdapter);
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

    public void onClick(View view) {
        if (view.getId() == R.id.delivery_back) {
            finish();
        }else if (view.getId() == R.id.delivery_submit_button) {
            Print("delivery_submit_button:::");
            attemptCheck();
        }else if (view.getId() == R.id.delivery_bar_code_button) {
            Print("delivery_bar_code_button:::");
            qrcodetextmode=qrcodemode.BAR_CODE;
            Util.startQrCode(DeliveryActivity.this);
        }
        else if (view.getId() == R.id.delivery_library_number_button) {
            Print("delivery_library_number_button:::");
            qrcodetextmode=qrcodemode.LIBRARY_NUMBER;
            Util.startQrCode(DeliveryActivity.this);
        }
        else if (view.getId() == R.id.delivery_batch_number_button) {
            Print("delivery_batch_number_button:::");
            qrcodetextmode=qrcodemode.BATCH_NUMBER;
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

                Util.showShortToastMessage(DeliveryActivity.this,"扫描结果为："+ content);
                if(qrcodetextmode==qrcodemode.BAR_CODE){

//                    CheckproductinfoTask checkproductinfoTask=new CheckproductinfoTask(content);
                    CheckproductinfoTask checkproductinfoTask=new CheckproductinfoTask(content);
                    checkproductinfoTask.execute();
                }else if(qrcodetextmode==qrcodemode.LIBRARY_NUMBER){
//                    mdeliverylibrarynumber.setText(content);
                    mdeliverylibrarynumber.setText("9995-YF10102");
                    CheckLibrarynumTask checklibrarynumTask=new CheckLibrarynumTask();
                    checklibrarynumTask.execute();
                }else if(qrcodetextmode==qrcodemode.BATCH_NUMBER){
//                    mdeliverybatchnumber.setText(content);//
                    mdeliverybatchnumber.setText("SD20190416-01");//测试数据
                    GetforminfoTask getforminfotask=new GetforminfoTask();
                    getforminfotask.execute((Void) null);
                }
            }

        }

    }
    private void attemptCheck() {
        if (mdeliveryTask != null) {
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
        String lot_no=mdeliverybatchnumber.getText().toString();
        String barcode=mdeliverybarcode.getText().toString();
        String location=mdeliverylibrarynumber.getText().toString();
        String cn_box=mdeliverynumboxes.getText().toString();
        String min_box=mdeliveryNumberperbox.getText().toString();

        String so=mdeliveryOrdernumber.getText().toString();
        boolean cancel = false;
//
        if (lot_no.equals("")||
                barcode.equals("")||
                location.equals("")||
                cn_box.equals("")||
                min_box.equals("")||
                so.equals("")) {
            cancel=true;
        }
        if (cancel) {
//            // There was an error; don't attempt login and focus the first
//            // form field with an error.
//            focusView.requestFocus();
            Util.showShortToastMessage(DeliveryActivity.this,"请先扫描所有条目");
        } else {
//            // Show a progress spinner, and kick off a background task to
//            // perform the user login attempt.
//        showProgress(true);
            mdeliveryTask =new OutStockTask();
            mdeliveryTask.execute((Void) null);
        }


    }


    private String email_key = "email";
    public class GetforminfoTask extends AsyncTask<Void, Void, Boolean> {
        String token="";
        String batch_num="";
        String success="";
        String msg="";
        int responsecode=0;
        GetforminfoTask() {
            batch_num=mdeliverybatchnumber.getText().toString();
            token=PreferencesUtils.getString(DeliveryActivity.this,token_key,"");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                String url="http://"+PreferencesUtils.getString(DeliveryActivity.this,ip_key,"106.15.187.52")
                        +":"+PreferencesUtils.getString(DeliveryActivity.this,port_key,"8061")+
                        IndexConstants.CHECKDELIVERYFORM+"?name="+batch_num+"&token="+token;
                Print("url:::"+url);
                URL posturl = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) posturl.openConnection();
                conn.setConnectTimeout(10000);
                            //使用Post方式不能使用缓存

                responsecode=conn.getResponseCode();
                if(responsecode==200) {
                    InputStream ins = conn.getInputStream();
                    JSONObject rootjsonObject= parseJson(ins);
                    JSONObject jsonObject= null;
                    if (rootjsonObject != null) {
                        jsonObject = rootjsonObject.getJSONArray("results").getJSONObject(0);
                    }
                    if(jsonObject!=null) {
                        msg = jsonObject.getString("message");
                        success = jsonObject.getString("success");
                        if(success.equals("true")) {
                            JSONArray data = jsonObject.getJSONArray("data");
                            for(int d=0;d<data.length();d++) {
//                        String token =data.getString("line_data");
                                JSONArray line_data = data.getJSONObject(d).getJSONArray("line_data");
                                for (int i = 0; i < line_data.length(); i++) {
                                    DeliveryBean deliver = new DeliveryBean();
                                    deliver.bar_code = line_data.getJSONObject(i).getString("product_code");
                                    deliver.product_specification = line_data.getJSONObject(i).getString("product_name");
                                    deliver.number_applications = line_data.getJSONObject(i).getString("qty");
                                    deliver.number_boxes = line_data.getJSONObject(i).getString("box_qty");
                                    deliver.numbers = line_data.getJSONObject(i).getString("product_qty");
                                    deliveryBean.add(deliver);
                                }
                            }
                        }
                    }
                }
                Print(" return:::"+responsecode);

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
                Util.showShortToastMessage(DeliveryActivity.this,msg);
                mdeliverylibrarynumber.requestFocus();
                mAdapter = new ChooseShipAdapter(DeliveryActivity.this, deliveryBean);
                mSimpleDetailList.setAdapter(mAdapter);
            } else {
                Util.showShortToastMessage(DeliveryActivity.this,msg);
                mdeliverybatchnumber.setError("出货单号有错");
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
                Print("check forminfo msgmsg:::"+json);
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
    public class CheckLibrarynumTask extends AsyncTask<Void, Void, Boolean> {
        String token="";
        String library_num="";
        String success="";
        String msg="";
        int responsecode=0;
        CheckLibrarynumTask() {
            library_num=mdeliverylibrarynumber.getText().toString();
            token=PreferencesUtils.getString(DeliveryActivity.this,token_key,"");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                String url="http://"+PreferencesUtils.getString(DeliveryActivity.this,ip_key,"106.15.187.52")
                        +":"+PreferencesUtils.getString(DeliveryActivity.this,port_key,"8061")+
                        IndexConstants.CHECKLIBRARY+"?location_barcode="+library_num+"&token="+token;
//                "login:","登录帐号","Password":"密码"
                Print("url:::"+url);
                URL posturl = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) posturl.openConnection();
                conn.setConnectTimeout(10000);
                //使用Post方式不能使用缓存

                responsecode=conn.getResponseCode();
                if(responsecode==200) {
                    InputStream ins = conn.getInputStream();
                    JSONObject rootjsonObject= parseJson(ins);
                    JSONObject jsonObject= null;
                    if (rootjsonObject != null) {
                        jsonObject = rootjsonObject.getJSONArray("results").getJSONObject(0);
                    }
                    if(jsonObject!=null) {
                        msg = jsonObject.getString("message");
                        success = jsonObject.getString("success");
//                        JSONObject data =jsonObject.getJSONObject("data");
//                        String token =data.getString("token");
//                        JSONArray rights=data.getJSONArray("rights");//"group_app_mrp_finish_in","group_app_mrp_finish_in_confirm","group_app_mrp_move","group_app_sales_delivery"

                    }
//                    ins.close();
                }
                Print(" return:::"+responsecode);

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
                Util.showShortToastMessage(DeliveryActivity.this,msg);
                mdeliverybarcode.requestFocus();
            } else {
                Util.showShortToastMessage(DeliveryActivity.this,msg);
                mdeliverylibrarynumber.setError("库位编号有错");
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
//            }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


    }
    public class CheckproductinfoTask extends AsyncTask<Void, Void, Boolean> {
        String token="";
        String batch_num="";
        String product_code="";
        String success="";
        String msg="";
        String lot_id="";
        String qty="";
        int responsecode=0;
        CheckproductinfoTask(String content) {
            String productinfo[]=content.split(",");
            for(int i=0;i<productinfo.length;i++){
               String info= productinfo[i];
                if(i==0){
//                    product_code=info;
                    product_code="123";
                }else if(i==1){
                    lot_id=info;
                }else if(i==2){
                    qty=info;
                }else if(i>2){
                    break;
                }
            }
//            product_code=content;
            batch_num=mdeliverybatchnumber.getText().toString();
            token=PreferencesUtils.getString(DeliveryActivity.this,token_key,"");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                String url="http://"+PreferencesUtils.getString(DeliveryActivity.this,ip_key,"106.15.187.52")
                        +":"+PreferencesUtils.getString(DeliveryActivity.this,port_key,"8061")+
                        IndexConstants.CHECKDELIVERYPRODUCT+"?product_code="+product_code+"&name="+batch_num+"&token="+token;
//                "login:","登录帐号","Password":"密码"
                Print("lot_id:::"+lot_id+"qty:::"+qty);
                Print("url:::"+url);
                URL posturl = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) posturl.openConnection();
                conn.setConnectTimeout(10000);
                //使用Post方式不能使用缓存

                responsecode=conn.getResponseCode();
                if(responsecode==200) {
                    InputStream ins = conn.getInputStream();
                    JSONObject rootjsonObject= parseJson(ins);
                    JSONObject jsonObject= null;
                    if (rootjsonObject != null) {
                        jsonObject = rootjsonObject.getJSONArray("results").getJSONObject(0);
                    }
                    if(jsonObject!=null) {
                        msg = jsonObject.getString("message");
                        success = jsonObject.getString("success");
//                        JSONObject data =jsonObject.getJSONObject("data");
//                        String token =data.getString("token");
//                        JSONArray rights=data.getJSONArray("rights");//"group_app_mrp_finish_in","group_app_mrp_finish_in_confirm","group_app_mrp_move","group_app_sales_delivery"

                    }
//                    ins.close();
                }
                Print(" return:::"+responsecode);

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
                Util.showShortToastMessage(DeliveryActivity.this,msg);
                if(mdeliverybarcode.getText().toString().equals(product_code)){
                    boxnum++;
                    boxesnum.remove(boxesnum.size()-1);
                    saveBoxNum();
                }else {
                    saveBoxNum();

                    mdeliverybarcode.setText(product_code);
                    boxnum=1;
                }
                mdeliverynumboxes.setText(boxnum+"");
                mdeliveryOrdernumber.setText(lot_id);
                mdeliveryNumberperbox.setText(qty);
            } else {
                Util.showShortToastMessage(DeliveryActivity.this,msg);
                mdeliverybarcode.setError("产品编号有错");
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
                Print("check forminfo msgmsg:::"+json);
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
    private void saveBoxNum(){
        Map<String,String> map=new HashMap<String,String>();
        map.put(mdeliverybarcode.getText().toString(),boxnum+"");
        boxesnum.add(map);//存起每个产品的数量 提交使用

    }
    public class CheckCodeTask extends AsyncTask<Void, Void, Boolean> {
        //        String lot_no="";
        String location="";
        String barcode="";
        String lot_no="";
        String success="";
        String msg="";
        int responsecode=0;
        CheckCodeTask() {
            lot_no=mdeliverybatchnumber.getText().toString();
            barcode=mdeliverybarcode.getText().toString();
            location=mdeliverylibrarynumber.getText().toString();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                String url="http://"+PreferencesUtils.getString(DeliveryActivity.this,ip_key,"106.15.187.52")
                        +":"+PreferencesUtils.getString(DeliveryActivity.this,port_key,"8061")+ IndexConstants.TAKINGCHECKBARCODE;
//                "login:","登录帐号","Password":"密码"
                Print("url:::"+url);
                Map<String,String> mparams=new HashMap<String,String>();
                mparams.put("login",PreferencesUtils.getString(DeliveryActivity.this,email_key,"8061"));
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
            mdeliveryTask = null;
//            showProgress(false);
//            Util.showShortToastMessage(EntryActivity.this,msg);
            if (success) {
//                PreferencesUtils.putString(EntryActivity.this,email_key,mEmail);
//                PreferencesUtils.putString(EntryActivity.this,password_key,mPassword);
//                Intent intent=new Intent(EntryActivity.this,MainActivity.class);
//                EntryActivity.this.startActivity(intent);
//                finish();
                OutStockTask tst =new OutStockTask();
                tst.execute();
            } else {
                Util.showShortToastMessage(DeliveryActivity.this,msg);
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


    }
    public class OutStockTask extends AsyncTask<Void, Void, Boolean> {
        //        String lot_no="";
//        String location="";
        String barcode="";
//        String min_box="";
//        String lot_no="";
//        String cn_box= "";
//        String so="";
        String success="";
        String msg="";
        String warehouse_id="";
        String location_id="";
        String batch_num="";
        String lot_id="";//批号
        String qty="";//数量
        String box_qty="";//箱数
        String token="";//token
        int responsecode=0;
        OutStockTask() {

            batch_num=mdeliverybatchnumber.getText().toString();
            barcode=mdeliverybarcode.getText().toString();
            String Warehouse=mdeliverylibrarynumber.getText().toString();
            String Warehouseids[]=Warehouse.split("-");
            warehouse_id=Warehouseids[0];
            location_id=Warehouseids[1];
            box_qty=mdeliverynumboxes.getText().toString();
            qty=mdeliveryNumberperbox.getText().toString();
            lot_id=mdeliveryOrdernumber.getText().toString();
            token=PreferencesUtils.getString(DeliveryActivity.this,token_key,"");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                String url="http://"+PreferencesUtils.getString(DeliveryActivity.this,ip_key,"106.15.187.52")
                        +":"+PreferencesUtils.getString(DeliveryActivity.this,port_key,"8061")+ IndexConstants.OUTSTOCK;
//                "login:","登录帐号","Password":"密码"
                Print("url:::"+url);
                Map<String,HashMap<String,String>> mparams=new HashMap<String,HashMap<String,String>>();
//                mparams.put("login",PreferencesUtils.getString(DeliveryActivity.this,email_key,"8061"));
                Print("boxesnum.size():::"+boxesnum.size());
                for(int i=0;i<boxesnum.size();i++){
                    for(String productkey : boxesnum.get(i).keySet()){
                        String key="("+productkey+","+warehouse_id+","
                                +location_id+","+lot_id+","+""+","+""+")";
                        Print("boxesnum. key:::"+key+"  "+productkey);
                        String boxnum=boxesnum.get(i).get(productkey);
                        HashMap<String,String> nums=new HashMap<String,String>();
                        nums.put("qty",boxnum);
                        nums.put("box_qty",boxnum);
                        mparams.put(key,nums);
                    }
                }

                String postparams = new Gson().toJson(mparams);
                Print("urlpostparams:::"+postparams);
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
            Util.showShortToastMessage(DeliveryActivity.this,msg);
            if (success) {

                 mdeliverybatchnumber.setText("");
                 mdeliverybarcode.setText("");
                 mdeliverylibrarynumber.setText("");
                 mdeliveryNumberperbox.setText("");
                 mdeliverynumboxes.setText("");
                 mdeliveryOrdernumber.setText("");
                 mdeliverybatchnumber.requestFocus();
//                PreferencesUtils.putString(DeliveryActivity.this,email_key,mEmail);
//                PreferencesUtils.putString(DeliveryActivity.this,password_key,mPassword);
//                Intent intent=new Intent(DeliveryActivity.this,MainActivity.class);
//                DeliveryActivity.this.startActivity(intent);
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


    }
    String TAG="Deliveryactivity::";
    public void Print(String s){
        System.out.println(TAG+s);
    }
}