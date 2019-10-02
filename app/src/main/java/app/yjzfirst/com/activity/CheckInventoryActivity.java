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
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.yjzfirst.adapter.ChooseShipAdapter;
import com.yjzfirst.adapter.InventoryAdapter;
import com.yjzfirst.bean.InventoryBean;
import com.yjzfirst.util.IndexConstants;
import com.yjzfirst.util.PreferencesUtils;
import com.yjzfirst.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yjzfirst.util.IndexConstants.ip_key;
import static com.yjzfirst.util.IndexConstants.port_key;
import static com.yjzfirst.util.IndexConstants.token_key;
import static com.yjzfirst.util.Util.REQUEST_CODE_SCAN;
import static com.yjzfirst.util.Util.readStream;
import static com.yjzfirst.util.Util.setListViewHeightBasedOnChildren;
import static com.yjzfirst.util.Util.textsetError;
import static com.yzq.zxinglibrary.common.Constant.CODED_CONTENT;

public class CheckInventoryActivity extends AppCompatActivity {
    EditText minventorybatchnumber;//批号
    EditText minventorybarcode;//产品编码
    EditText eproductplan;
    EditText minventorylibrarynumber;//库位号
    EditText minventoryNumberperbox;
    EditText minventorynumboxes;
    //	EditText minventorystocknumboxes;
    EditText minventoryInventoryquantity;
    EditText minventoryInventoryquantityboxes;
    EditText minventoryOrdernumber;
    EditText minventory_front_boxes;
    EditText minventory_front_boxes_num;
    EditText minventory_thisbatch_boxes;
    EditText minventory_thisbatch_boxes_num;
    EditText minventory_available_boxes;
    EditText minventory_available_boxes_num;
    ListView mSimpleDetailList;
    InventoryAdapter mAdapter;
    String lastproduct_content = "";
    List<InventoryBean> InventoryBean = new ArrayList<InventoryBean>();
    private OutStockTask minventoryTask = null;

    enum qrcodemode {
        BATCH_NUMBER, LIBRARY_NUMBER, BAR_CODE,PRODUCTPLAN
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
        setContentView(R.layout.activity_inventory_check);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Window window = CheckInventoryActivity.this.getWindow();

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(Color.BLACK);


            //底部导航栏

            //window.setNavigationBarColor(activity.getResources().getColor(colorResId));

        }

        eproductplan = (EditText) findViewById(R.id.product_plan_edittext);
        minventorybarcode = (EditText) findViewById(R.id.inventory_bar_code);//产品编号会有多个
//        minventorybarcode.addTextChangedListener(shipsWatcher);
        minventorylibrarynumber = (EditText) findViewById(R.id.inventory_library_number);

        minventorybatchnumber = (EditText) findViewById(R.id.inventory_batch_number);
        minventorybatchnumber.setFocusable(false);
        minventorybatchnumber.setFocusableInTouchMode(false);
//        minventorybatchnumber.addTextChangedListener(shipsWatcher);
//        minventorylibrarynumber.addTextChangedListener(shipsWatcher);
        minventoryNumberperbox = (EditText) findViewById(R.id.inventory_Number_per_box);
//        minventoryNumberperbox.addTextChangedListener(shipsWatcher);
        minventorynumboxes = (EditText) findViewById(R.id.inventory_num_boxes);
//		minventorystocknumboxes = (EditText) findViewById(R.id.inventory_stocknum_boxes);
        minventoryInventoryquantity = (EditText) findViewById(R.id.inventory_inventory_quantity);
        minventoryInventoryquantityboxes = (EditText) findViewById(R.id.inventory_inventory_boxes);
        minventoryInventoryquantity.setFocusable(false);
        minventoryInventoryquantity.setFocusableInTouchMode(false);
        minventoryInventoryquantityboxes.setFocusable(false);
        minventoryInventoryquantityboxes.setFocusableInTouchMode(false);
        minventory_front_boxes = (EditText) findViewById(R.id.inventory_front_boxes);
        minventory_front_boxes.setFocusable(false);
        minventory_front_boxes.setFocusableInTouchMode(false);
        minventory_front_boxes_num = (EditText) findViewById(R.id.inventory_front_boxes_num);
        minventory_front_boxes_num.setFocusable(false);
        minventory_front_boxes_num.setFocusableInTouchMode(false);
        minventory_thisbatch_boxes = (EditText) findViewById(R.id.inventory_thisbatch_boxes);
        minventory_thisbatch_boxes.setFocusable(false);
        minventory_thisbatch_boxes.setFocusableInTouchMode(false);
        minventory_thisbatch_boxes_num = (EditText) findViewById(R.id.inventory_thisbatch_boxes_num);
        minventory_thisbatch_boxes_num.setFocusable(false);
        minventory_thisbatch_boxes_num.setFocusableInTouchMode(false);
        minventory_available_boxes = (EditText) findViewById(R.id.inventory_available_boxes);
        minventory_available_boxes.setFocusable(false);
        minventory_available_boxes.setFocusableInTouchMode(false);
        minventory_available_boxes_num = (EditText) findViewById(R.id.inventory_available_boxes_num);
        minventory_available_boxes_num.setFocusable(false);
        minventory_available_boxes_num.setFocusableInTouchMode(false);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        minventoryOrdernumber = (EditText) findViewById(R.id.inventory_Order_number);

        addTextWatcher();

        mSimpleDetailList = (ListView) findViewById(R.id.inventory_infolist);
        refreshdatalist();

//        Button minventorybarcodebtn = (Button) findViewById(R.id.inventory_bar_code_button);
//        Button minventorylibrarynumberbtn = (Button) findViewById(R.id.inventory_library_number_button);
//        Button minventorybatchnumberbtn = (Button) findViewById(R.id.inventory_batch_number_button);
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

    public void refreshdatalist() {
        Collections.sort(InventoryBean, idComparator);
        mAdapter = new InventoryAdapter(CheckInventoryActivity.this, InventoryBean);
        mSimpleDetailList.setAdapter(mAdapter);
        setListViewHeightBasedOnChildren(mSimpleDetailList);
    }

    public void addTextWatcher() {

        minventorylibrarynumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//				minventorylibrarynumber.removeTextChangedListener(this);
                if (!minventorylibrarynumber.getText().toString().equals("")
                        &&!minventorybarcode.getText().toString().equals("")) {
                    CheckLibrarynumTask checklibrarynumTask = new CheckLibrarynumTask();
                    checklibrarynumTask.execute();
                }else{
                    minventorylibrarynumber.setError("请先扫描产品编码");
                }
//				minventorylibrarynumber.addTextChangedListener(this);
            }
        });
        minventorybarcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = minventorybarcode.getText().toString();

                if (!content.equals("")) {
                    minventorybarcode.removeTextChangedListener(this);
                    CheckproductinfoTask checkproductinfoTask = new CheckproductinfoTask(content, this);
                    checkproductinfoTask.execute();
                }
            }
        });

        eproductplan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = eproductplan.getText().toString();
                if(content.contains(",")) {
                    String productinfo[] = content.split(",");
                    if (productinfo.length > 1) {
                        String productplan = productinfo[1];
                        eproductplan.setText(productplan);
                    }
                }else {
                    if (!content.equals("")) {
//                    minventorybarcode.removeTextChangedListener(this);
                        CheckproductplanTask checkproductinfoTask = new CheckproductplanTask(content);
                        checkproductinfoTask.execute();
                    }
                }
            }
        });
    }

    public void onClick(View view) {
        if (view.getId() == R.id.inventory_back) {
            finish();
        } else if (view.getId() == R.id.inventory_submit_button) {
            Print("inventory_submit_button:::");
            if(!minventorybarcode.getText().toString().equals("")) {
                GetinventoryinfoTask getinventoryinfoTask = new GetinventoryinfoTask();
                getinventoryinfoTask.execute();
            }else{
                Util.showToastMessage(CheckInventoryActivity.this,"产品编码不可为空！");
            }
        }
//        else if (view.getId() == R.id.inventory_submitcancle_button) {
//            Print("inventory_submit_button:::");
//            cancleALLdata();
//        } else if (view.getId() == R.id.inventory_warehouse_button) {
//            Print("inventory_submit_button:::");
//            CheckwareinfoTask mCheckwareinfoTask = new CheckwareinfoTask();
//            mCheckwareinfoTask.execute((Void) null);
//
//        }
        else if (view.getId() == R.id.inventory_bar_code_button) {
            Print("inventory_bar_code_button:::");
            qrcodetextmode = qrcodemode.BAR_CODE;
            Util.startQrCode(CheckInventoryActivity.this);
        } else if (view.getId() == R.id.inventory_library_number_button) {
            Print("inventory_library_number_button:::");
            qrcodetextmode = qrcodemode.LIBRARY_NUMBER;
            Util.startQrCode(CheckInventoryActivity.this);
        } else if (view.getId() == R.id.inventory_batch_number_button) {
            Print("inventory_batch_number_button:::");
            qrcodetextmode = qrcodemode.BATCH_NUMBER;
            Util.startQrCode(CheckInventoryActivity.this);
        } else if (view.getId() == R.id.product_plan_button) {
            Print("inventory_batch_number_button:::");
            qrcodetextmode = qrcodemode.PRODUCTPLAN;
            Util.startQrCode(CheckInventoryActivity.this);
        }
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        // 扫描二维码/条码回传

        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {

            if (data != null) {

                String content = data.getStringExtra(CODED_CONTENT);

//                Util.showShortToastMessage(inventoryActivity.this,"扫描结果为："+ content);
                if (qrcodetextmode == qrcodemode.BAR_CODE) {
                    minventorybarcode.setText(content);
                } else if (qrcodetextmode == qrcodemode.LIBRARY_NUMBER) {
                    minventorylibrarynumber.setText(content);
                }else if (qrcodetextmode == qrcodemode.PRODUCTPLAN) {

                        eproductplan.setText(content);
                }
            }

        }

    }

    private void attemptCheck() {
        if (minventoryTask != null) {
            return;
        }

//
        String lot_no = minventorybatchnumber.getText().toString();
        String barcode = minventorybarcode.getText().toString();
        String location = minventorylibrarynumber.getText().toString();
        String cn_box = minventorynumboxes.getText().toString();
        String min_box = minventoryNumberperbox.getText().toString();

        String so = minventoryOrdernumber.getText().toString();
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
            Util.showShortToastMessage(CheckInventoryActivity.this, "请先扫描所有条目");
        } else {
//            // Show a progress spinner, and kick off a background task to
//            // perform the user login attempt.
//        showProgress(true);
            minventoryTask = new OutStockTask();
            minventoryTask.execute((Void) null);
        }


    }

    private void cancleALLdata() {
//         minventorybatchnumber.setText("");
//         minventorybarcode.setText("");
//         minventorylibrarynumber.setText("");
//         minventoryNumberperbox.setText("");
//         minventorynumboxes.setText("");

        minventorybarcode.setText("");
        minventoryNumberperbox.setText("");
        minventorynumboxes.setText("");
//		minventorystocknumboxes.setText("");
        minventoryInventoryquantity.setText("");
        minventoryInventoryquantityboxes.setText("");
        minventory_front_boxes.setText("");
        minventory_front_boxes_num.setText("");
        minventory_thisbatch_boxes.setText("");
        minventory_thisbatch_boxes_num.setText("");
        minventory_available_boxes.setText("");
        minventory_available_boxes_num.setText("");
        minventoryOrdernumber.setText("");
        minventoryTask = null;
        boxnum = 0;
        eproductplan.setText("");
//        productinfomap=new HashMap<String, HashMap<String,String>>();
//        locationmap=new HashMap<String, String>();
//        boxesnum=new ArrayList<Map<String,String>>();
//        mAdapter = new ChooseShipAdapter(inventoryActivity.this, productMaterialBeans);
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
            batch_num = minventorybatchnumber.getText().toString();
            token = PreferencesUtils.getString(CheckInventoryActivity.this, token_key, "");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                String url = "http://" + PreferencesUtils.getString(CheckInventoryActivity.this, ip_key, "120.27.2.177")
                        + ":" + PreferencesUtils.getString(CheckInventoryActivity.this, port_key, "8062") +
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
                            InventoryBean = new ArrayList<InventoryBean>();
                            JSONArray data = jsonObject.getJSONArray("data");
                            Print(" return:::" + data);
                            for (int d = 0; d < data.length(); d++) {
//                        String token =data.getString("line_data");
                                JSONArray line_data = data.getJSONObject(d).getJSONArray("line_data");
                                for (int i = 0; i < line_data.length(); i++) {
//                                    productMaterialBeans deliver = new productMaterialBeans();
//                                    deliver.sequence = line_data.getJSONObject(i).getString("sequence");
//                                    deliver.bar_code = line_data.getJSONObject(i).getString("product_code");
//                                    deliver.product_id = line_data.getJSONObject(i).getString("product_id");
//                                    deliver.product_specification = line_data.getJSONObject(i).getString("product_name");
//                                    deliver.number_applications = line_data.getJSONObject(i).getString("qty");
//                                    deliver.number_boxes = line_data.getJSONObject(i).getString("box_qty");
//                                    deliver.numbers = line_data.getJSONObject(i).getString("product_qty");
//
//                                    HashMap<String, String> map = new HashMap<String, String>();
//                                    map.put(deliver.bar_code, deliver.product_id);
//                                    productidmap.put(deliver.bar_code + "id", map);

//                                    productMaterialBeans.add(deliver);

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
                Util.showShortToastMessage(CheckInventoryActivity.this, msg);
                minventorylibrarynumber.requestFocus();

                refreshdatalist();
                minventorybatchnumber.setError(null, null);//焦点聚焦时去除错误图标
            } else {
                Util.showShortToastMessage(CheckInventoryActivity.this, msg);
//                minventorybatchnumber.requestFocus();
//                minventorybatchnumber.setError("出货单号有错");
                textsetError(CheckInventoryActivity.this, minventorybatchnumber, msg);
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
            if(!((InventoryBean) o1).location_code.equals("null")&&!((InventoryBean) o1).location_code.equals("null")) {
                return (Integer.compare(Integer.parseInt(((InventoryBean) o1).location_code), Integer.parseInt(((InventoryBean) o2).location_code)));
            }else{
                return 1;
            }
        }
    };

    public class CheckLibrarynumTask extends AsyncTask<Void, Void, Boolean> {
        String token = "";
        String library_num = "";
        String success = "";
        String msg = "";
        int responsecode = 0;

        CheckLibrarynumTask() {
            library_num = minventorylibrarynumber.getText().toString();
            token = PreferencesUtils.getString(CheckInventoryActivity.this, token_key, "");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                String url = "http://" + PreferencesUtils.getString(CheckInventoryActivity.this, ip_key, "120.27.2.177")
                        + ":" + PreferencesUtils.getString(CheckInventoryActivity.this, port_key, "8062") +
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
                            String warehouse_code = locationdata.getString("warehouse_code");
                            String location_id = locationdata.getString("location_id");
                            String location_code = locationdata.getString("location_code");
                            HashMap<String, String> map = new HashMap<>();
                            map.put("warehouse_id", warehouse_id);
                            map.put("warehouse_code", warehouse_code);
                            map.put("location_id", location_id);
                            map.put("location_code", location_code);
                            locationmap.put(library_num, map);
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
                Util.showShortToastMessage(CheckInventoryActivity.this, msg);
//                mdeliverybarcode.requestFocus();
                minventorylibrarynumber.setError(null, null);//焦点聚焦时去除错误图标
                GetinventoryinfoTask getinventoryinfoTask = new GetinventoryinfoTask();
                getinventoryinfoTask.execute();
            } else {
                Util.showShortToastMessage(CheckInventoryActivity.this, msg);
//                mdeliverylibrarynumber.requestFocus();
//                mdeliverylibrarynumber.setError("库位编号有错");
                textsetError(CheckInventoryActivity.this, minventorylibrarynumber, "库位编号有错" + msg);
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
        TextWatcher textwatcher;
        String content = "";
        String token = "";
        String product_code = "";
        String lot_name = "";
        String success = "";
        String msg = "";
        int responsecode = 0;

        CheckproductinfoTask(String content, TextWatcher textwatcher) {
            this.content = content;
            this.textwatcher = textwatcher;
            String productinfo[] = content.split(",");
            for (int i = 0; i < productinfo.length; i++) {
                String info = productinfo[i];
                if (i == 0) {
                    product_code = info;
                } else if (i == 1) {
//                    lot_id = info;
                } else if (i == 2) {
//                    qty = info;
                } else if (i == 3) {

                } else if (i == 4) {
                    lot_name = info;
                } else if (i == 5) {
//                    package_code = info;
                } else if (i == 6) {

                } else if (i == 7) {

                }
            }
//            product_code=content;
            token = PreferencesUtils.getString(CheckInventoryActivity.this, token_key, "");

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                String url = "http://" + PreferencesUtils.getString(CheckInventoryActivity.this, ip_key, "120.27.2.177")
                        + ":" + PreferencesUtils.getString(CheckInventoryActivity.this, port_key, "8062")
                        + IndexConstants.CHECKINVERTORYPRODUCT + "?product_code=" + product_code + "&token=" + token;
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
//                        JSONArray rights=data.getJSONArray("rights");//"group_app_mrp_finish_in","group_app_mrp_finish_in_confirm","group_app_mrp_move","group_app_sales_inventory"

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
                Util.showShortToastMessage(CheckInventoryActivity.this, msg);


//                minventoryOrdernumber.setFocusable(false);
//                minventoryOrdernumber.setFocusableInTouchMode(false);
//                minventoryNumberperbox.setFocusable(false);
//                minventoryNumberperbox.setFocusableInTouchMode(false);
//                minventoryInventoryquantity.setFocusable(false);
//                minventoryInventoryquantity.setFocusableInTouchMode(false);
//                minventoryInventoryquantityboxes.setFocusable(false);
//                minventoryInventoryquantityboxes.setFocusableInTouchMode(false);
                lastproduct_content = this.content;
                minventorybarcode.setError(null, null);//焦点聚焦时去除错误图标
                minventorybarcode.setText(product_code);
                minventorybatchnumber.setText(lot_name);
//                CheckproductlabelTask checkproductlabelTask = new CheckproductlabelTask(content);
//                checkproductlabelTask.execute();
                minventorylibrarynumber.requestFocus();
                GetinventoryinfoTask getinventoryinfoTask = new GetinventoryinfoTask();
                getinventoryinfoTask.execute();
            } else {
                Util.showShortToastMessage(CheckInventoryActivity.this, msg);
//                minventorybarcode.requestFocus();
//                minventorybarcode.setError(msg);
                textsetError(CheckInventoryActivity.this, minventorybarcode, msg);
            }
            minventorybarcode.addTextChangedListener(textwatcher);
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

    public class CheckproductplanTask extends AsyncTask<Void, Void, Boolean> {
        String production_no = "";
        String product_code="";
        String token = "";
        String success = "";
        String msg = "";
        int responsecode = 0;

        CheckproductplanTask(String content) {

            production_no=content;
            token = PreferencesUtils.getString(CheckInventoryActivity.this, token_key, "");

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                String url = "http://" + PreferencesUtils.getString(CheckInventoryActivity.this, ip_key, "120.27.2.177")
                        + ":" + PreferencesUtils.getString(CheckInventoryActivity.this, port_key, "8062")
                        + IndexConstants.CHECKPRODUCTIONPLAN +"?production_no=" + production_no + "&token=" + token;
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
                        Print("CheckproductplanTask jsonobj:::" + jsonObject);
                        JSONArray data =jsonObject.getJSONArray("data");
                        for(int i=0;i<data.length();i++){

                            JSONObject dataobj=data.getJSONObject(i);
                            product_code= dataobj.getString("product_code");
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
            Util.showShortToastMessage(CheckInventoryActivity.this, msg);
            if (success) {
                minventorybarcode.setText(product_code);
                eproductplan.setError(null, null);//焦点聚焦时去除错误图标

//                GetinventoryinfoTask getinventoryinfoTask = new GetinventoryinfoTask();
//                getinventoryinfoTask.execute();
            } else {
                textsetError(CheckInventoryActivity.this, eproductplan, msg);
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
        String content = "";
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

            this.content = content;
//            product_code=content;
            batch_num = minventorybatchnumber.getText().toString();
            token = PreferencesUtils.getString(CheckInventoryActivity.this, token_key, "");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                String url = "http://" + PreferencesUtils.getString(CheckInventoryActivity.this, ip_key, "120.27.2.177")
                        + ":" + PreferencesUtils.getString(CheckInventoryActivity.this, port_key, "8062") +
//                        IndexConstants.CHECKinventoryPRODUCTLABEL + "?product_code=" + product_code +
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
//                Util.showShortToastMessage(inventoryActivity.this,msg);
                minventorybatchnumber.setError(null, null);
            } else {
                textsetError(CheckInventoryActivity.this, minventorybatchnumber, msg);
                Util.showShortToastMessage(CheckInventoryActivity.this, msg);
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
        String qty_0 = "";//,#数量
        String box_qty_0 = "";// 8#箱数
        String qty_1 = "";//,#数量
        String box_qty_1 = "";// 8#箱数
        String qty_2 = "";//,#数量
        String box_qty_2 = "";// 8#箱数
        int responsecode = 0;
        String location = "";
        String locationcode = "";
        String lot_no = "";
        String package_code = "";
        String customercode = "";
        String warehouse = "";
        String production_no="";

        GetinventoryinfoTask() {
            product_code = minventorybarcode.getText().toString();
            lot_no = minventorybatchnumber.getText().toString();
            location = minventorylibrarynumber.getText().toString();
            production_no=eproductplan.getText().toString();
            if (!location.equals("") && locationmap.get(location) != null) {
                warehouse = locationmap.get(location).get("warehouse_code");
                locationcode = locationmap.get(location).get("location_code");
            }
            String productinfo[] = lastproduct_content.split(",");
            for (int i = 0; i < productinfo.length; i++) {
                String info = productinfo[i];
                if (i == 0) {
//                    product_code = info;
                } else if (i == 1) {
//                    lot_id = info;
                } else if (i == 2) {
//                    qty = info;
                } else if (i == 3) {

                } else if (i == 4) {
//                    lot_name = info;
                } else if (i == 5) {
                    package_code = info;
                } else if (i == 6) {

                } else if (i == 7) {

                }
            }
//            customercode = productMaterialBeans.get(productMaterialBeans.size() - 1).customer_code;
            token = PreferencesUtils.getString(CheckInventoryActivity.this, token_key, "");

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                String url = "http://" + PreferencesUtils.getString(CheckInventoryActivity.this, ip_key, "120.27.2.177")
                        + ":" + PreferencesUtils.getString(CheckInventoryActivity.this, port_key, "8062") +
                        IndexConstants.CHECKINVENTORYINFO + "?product_code=" + product_code + "&warehouse_barcode=" + warehouse
                        + "&location_barcode=" + location + "&lot_no=" + lot_no +
                        "&package_code=" + package_code + "&customer_code=" + customercode +"&production_no="+production_no+ "&token=" + token;

                Print("CHECKinventoryINVENTORY url:::" + url);
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
                        InventoryBean = new ArrayList<InventoryBean>();
//                        JSONObject data =jsonObject.getJSONObject("data");
//                        String token =data.getString("token");
//                        JSONArray rights=data.getJSONArray("rights");//"group_app_mrp_finish_in","group_app_mrp_finish_in_confirm","group_app_mrp_move","group_app_sales_inventory"
                        JSONArray data = jsonObject.getJSONArray("data");
                        Print(" return:::" + data);
                        for (int d = 0; d < data.length(); d++) {
//                        String token =data.getString("line_data");
                            JSONObject numdata = data.getJSONObject(d);
                            box_qty = numdata.getString("box_qty");
                            qty = numdata.getString("qty");
                            box_qty_0 = numdata.getString("box_qty_0");
                            qty_0 = numdata.getString("qty_0");
                            box_qty_1 = numdata.getString("box_qty_1");
                            qty_1 = numdata.getString("qty_1");
                            box_qty_2 = numdata.getString("box_qty_2");
                            qty_2 = numdata.getString("qty_2");

//							Print(" return:::" + data);
//							for (int d = 0; d < data.length(); d++) {
////                        String token =data.getString("line_data");
                            JSONArray line_data = numdata.getJSONArray("line_data");//data.getJSONObject(d).getJSONArray("line_data");
                            for (int i = 0; i < line_data.length(); i++) {
                                InventoryBean deliver = new InventoryBean();
                                deliver.location_code = line_data.getJSONObject(i).getString("location_code");
                                deliver.location_name = line_data.getJSONObject(i).getString("location_name");
                                deliver.weight = line_data.getJSONObject(i).getString("weight");
                                deliver.qty = line_data.getJSONObject(i).getString("qty");

//									HashMap<String, String> map = new HashMap<String, String>();
//									map.put(deliver.bar_code, deliver.product_id);
//									productidmap.put(deliver.bar_code + "id", map);

                                InventoryBean.add(deliver);

                            }
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
                refreshdatalist();
                minventoryInventoryquantity.setText(qty);
                minventoryInventoryquantityboxes.setText(box_qty);
                minventory_front_boxes_num.setText(qty_0);
                if (!qty_0.equals("") && Float.valueOf(qty_0) > 0) {
                    minventory_front_boxes_num.setBackgroundColor(getResources().getColor(R.color.red));
                }
                minventory_front_boxes.setText(box_qty_0);
                if (!box_qty_0.equals("") && Float.valueOf(box_qty_0) > 0) {
                    minventory_front_boxes.setBackgroundColor(getResources().getColor(R.color.red));
                }
                minventory_thisbatch_boxes.setText(box_qty_1);
                minventory_thisbatch_boxes_num.setText(qty_1);
                minventory_available_boxes.setText(box_qty_2);
                minventory_available_boxes_num.setText(qty_2);
//                minventoryNumberperbox.setText(qty);

            } else {
                Util.showShortToastMessage(CheckInventoryActivity.this, msg);
//                minventorybarcode.setError("产品编号有错");
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
        for (int i = 0; i < boxesnum.size(); i++) {

            for (String contentkey : boxesnum.get(i).keySet()) {
                if (code.equals(contentkey)) {
                    String oldnumstr = boxesnum.get(i).get(contentkey);
                    try {
                        int oldnum = Integer.valueOf(oldnumstr);
                        int newnum = Integer.valueOf(num);
                        int nownum = oldnum + newnum;
                        System.err.println("oldnum " + oldnum + "newnum" + newnum + " now " + nownum);
                        Map<String, String> map = new HashMap<String, String>();
                        map.put(code, nownum + "");
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

    public class CheckCodeTask extends AsyncTask<Void, Void, Boolean> {
        //        String lot_no="";
        String location = "";
        String barcode = "";
        String lot_no = "";
        String success = "";
        String msg = "";
        int responsecode = 0;

        CheckCodeTask() {
            lot_no = minventorybatchnumber.getText().toString();
            barcode = minventorybarcode.getText().toString();
            location = minventorylibrarynumber.getText().toString();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                String url = "http://" + PreferencesUtils.getString(CheckInventoryActivity.this, ip_key, "120.27.2.177")
                        + ":" + PreferencesUtils.getString(CheckInventoryActivity.this, port_key, "8062") + IndexConstants.TAKINGCHECKBARCODE;
//                "login:","登录帐号","Password":"密码"
                Print("url:::" + url);
                Map<String, String> mparams = new HashMap<String, String>();
                mparams.put("login", PreferencesUtils.getString(CheckInventoryActivity.this, email_key, "8062"));
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
            minventoryTask = null;
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
                Util.showShortToastMessage(CheckInventoryActivity.this, msg);
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


            batch_num = minventorybatchnumber.getText().toString();
            barcode = minventorybarcode.getText().toString();
//            String Warehouse=minventorylibrarynumber.getText().toString();

//			box_qty = minventorynumboxes.getText().toString();
//			qty = minventoryNumberperbox.getText().toString();

            token = PreferencesUtils.getString(CheckInventoryActivity.this, token_key, "");


            if (!(boxnum + "").equals(minventorynumboxes.getText().toString())) {

                if (boxesnum.size() > 0) {
                    boxesnum.remove(boxesnum.size() - 1);
                }
                saveBoxNum(lastproduct_content, minventorynumboxes.getText().toString());
            }

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                String url = "http://" + PreferencesUtils.getString(CheckInventoryActivity.this, ip_key, "120.27.2.177")
                        + ":" + PreferencesUtils.getString(CheckInventoryActivity.this, port_key, "8062") +
                        IndexConstants.OUTSTOCK + "?";//
//                "login:","登录帐号","Password":"密码"
//				Map<String, String> mparams = new HashMap<String, String>();
                url = url + "inventory_no=" + batch_num+"&token="+token;
//                mparams.put("login",PreferencesUtils.getString(inventoryActivity.this,email_key,"8062"));
//                Print("boxesnum.size():::"+boxesnum.size());
//                String mparams="";
                String jsondata = "{";
                String parajsondata = "";
                String productkey = "";
                for (int i = 0; i < boxesnum.size(); i++) {
                    Print("productinfomap. key:::" + locationmap.size() + "   " + productinfomap.keySet());
                    Map<String, HashMap<String, Integer>> data
                            = new HashMap<String, HashMap<String, Integer>>();
                    for (String contentkey : boxesnum.get(i).keySet()) {
                        String qty = "";
                        String lot_id = "";
                        String location_id = "";
                        String warehouse_id = "";
                        String package_id = "";
                        String productinfo[] = contentkey.split(",");
                        for (int j = 0; j < productinfo.length; j++) {
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
                        HashMap<String, String> produntinfo = productidmap.get(productkey + "id");
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
                    parajsondata = parajsondata.substring(1, parajsondata.length() - 1);
                    jsondata = jsondata + parajsondata;
                    if (i == (boxesnum.size() - 1)) {
                        jsondata = jsondata + "}";
                    } else {
                        jsondata = jsondata + ",";
                    }
                    System.out.println("jsondata:::" + jsondata);
//					mparams.put("data", jsondata);
//					url = url + "&data=" + jsondata;
                }
                url = url + "&data=" + jsondata + "&token="+PreferencesUtils.getString(CheckInventoryActivity.this,token_key,"");;
//              mparams.put("inventory_no",batch_num);
//              JSONObject testjsonObject = new JSONObject(mparams);
//              System.out.println("输出的结果是：" + testjsonObject);

//                String postparams = "{\"params\":{\"data\":"+jsondata+","+"\"inventory_no\":\""+batch_num+"\"}}";
//                Print("urlpostparams:::"+postparams);
//                postparams= URLEncoder.encode(postparams,"utf-8");
//
////                String postparams ="{"+"login:",mEmail,"Password:",mPassword}//"login:"+mEmail+"&password:"+mPassword;
//                byte[] data = postparams.getBytes();
//                System.err.println("postparams postparams:::"+postparams+data.length);
//                url=url+"&"+mparams;
                Print("inventory url:::" + url);
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
                            InventoryBean = new ArrayList<InventoryBean>();
                            JSONObject data = jsonObject.getJSONObject("data");
//							Print(" return:::" + data);
//							for (int d = 0; d < data.length(); d++) {
////                        String token =data.getString("line_data");
                            JSONArray line_data = data.getJSONArray("line_data");//data.getJSONObject(d).getJSONArray("line_data");
                            for (int i = 0; i < line_data.length(); i++) {
//                                productMaterialBeans deliver = new productMaterialBeans();
//                                deliver.sequence = line_data.getJSONObject(i).getString("sequence");
//                                deliver.bar_code = line_data.getJSONObject(i).getString("product_code");
//                                deliver.product_id = line_data.getJSONObject(i).getString("product_id");
//                                deliver.product_specification = line_data.getJSONObject(i).getString("product_name");
//                                deliver.number_applications = line_data.getJSONObject(i).getString("qty");
//                                deliver.number_boxes = line_data.getJSONObject(i).getString("box_qty");
//                                deliver.numbers = line_data.getJSONObject(i).getString("product_qty");
//
////									HashMap<String, String> map = new HashMap<String, String>();
////									map.put(deliver.bar_code, deliver.product_id);
////									productidmap.put(deliver.bar_code + "id", map);
//
//                                productMaterialBeans.add(deliver);

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
            Util.showShortToastMessage(CheckInventoryActivity.this, msg);
//            }
            if (success) {
//                PreferencesUtils.putString(inventoryActivity.this,email_key,mEmail);
//                PreferencesUtils.putString(inventoryActivity.this,password_key,mPassword);
//                Intent intent=new Intent(inventoryActivity.this,MainActivity.class);
//                inventoryActivity.this.startActivity(intent);
//                finish();
//				GetforminfoTask getforminfotask = new GetforminfoTask();
//				getforminfotask.execute((Void) null);
                cancleALLdata();
                refreshdatalist();
//				boxnum = 0;
//				minventorybatchnumber.setText("");
//				minventorybarcode.setText("");
//				minventorylibrarynumber.setText("");
//				minventoryNumberperbox.setText("");
//				minventorynumboxes.setText("");
//				minventoryOrdernumber.setText("");
//				minventoryInventoryquantity.setText("");
//				minventoryInventoryquantityboxes.setText("");
//				minventorybatchnumber.requestFocus();
            } else {
//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
            }
            minventoryTask = null;
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
        if (keyCode == 301) {
            if (minventorybatchnumber.isFocused()) {
                minventorybatchnumber.setText("");
            } else if (minventorybarcode.isFocused()) {
                minventorybarcode.setText("");
            } else if (minventorylibrarynumber.isFocused()) {
                minventorylibrarynumber.setText("");
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    String TAG = "inventoryactivity::";

    public void Print(String s) {
        System.out.println(TAG + s);
    }
}
