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
import android.widget.TextView;

import com.google.gson.Gson;
import com.yjzfirst.adapter.ProductmaterialAdapter;
import com.yjzfirst.bean.InventoryBean;
import com.yjzfirst.bean.ProductMaterialBean;
import com.yjzfirst.bean.ProductMaterialorderBean;
import com.yjzfirst.bean.ProductMaterialproductBean;
import com.yjzfirst.util.IndexConstants;
import com.yjzfirst.util.PreferencesUtils;
import com.yjzfirst.util.Util;
import com.yjzfirst.util.beanParseUtility;

import org.json.JSONArray;
import org.json.JSONException;
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

public class CheckproductMaterialActivity extends AppCompatActivity {

    EditText eorderid;
    EditText epackagename;

    TextView tproduction_name;
    TextView tprocess_name;
    TextView tdate;
    TextView tstate;

    TextView tbrand_name;
    TextView tsupplier_lot_no;
    TextView tlot_no;
    TextView tfurnace_no;
    TextView tmaterial_name;
    TextView tdiameter;
    TextView tproduct_default;
    TextView tlocation_barcode;
    TextView tqty;
    TextView tpackagenum;
    ListView mSimpleDetailList;
    ProductmaterialAdapter mAdapter;
    String lastproduct_content = "";
    List<ProductMaterialBean> productMaterialBeans = new ArrayList<ProductMaterialBean>();
    List<String> packagecode = new ArrayList<String>();
    List<ProductMaterialproductBean> productBeans = new ArrayList<ProductMaterialproductBean>();
    ProductMaterialorderBean orderbean=new ProductMaterialorderBean();
    private EnsureWarehouseTask minventoryTask = null;

    enum qrcodemode {
        ORDERID, PACKAGECODE
    }

    ;
    private qrcodemode qrcodetextmode = qrcodemode.ORDERID;
    private int packagenum = 0;
    private HashMap<String, String> productcodemap = new HashMap<String, String>();

    private ArrayList<Map<String, String>> boxesnum = new ArrayList<Map<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productmaterial_check);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Window window = CheckproductMaterialActivity.this.getWindow();

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(Color.BLACK);


            //底部导航栏

            //window.setNavigationBarColor(activity.getResources().getColor(colorResId));

        }

        eorderid = (EditText) findViewById(R.id.material_order_edittext);

        epackagename = (EditText) findViewById(R.id.material_package_name_edittext);

         tproduction_name = (TextView) findViewById(R.id.material_product_plan_text);;
         tprocess_name = (TextView) findViewById(R.id.material_process_name_text);
         tdate= (TextView) findViewById(R.id.material_orderdate_text);
         tstate= (TextView) findViewById(R.id.material_state_text);

         tbrand_name= (TextView) findViewById(R.id.material_branname_text);
         tsupplier_lot_no= (TextView) findViewById(R.id.material_supplier_code_text);
         tlot_no= (TextView) findViewById(R.id.material_lotno_text);
         tfurnace_no= (TextView) findViewById(R.id.material_furnace_no_text);
         tmaterial_name= (TextView) findViewById(R.id.material_name_text);
         tdiameter= (TextView) findViewById(R.id.material_diameter_text);
         tproduct_default= (TextView) findViewById(R.id.material_productcode_text);
         tlocation_barcode= (TextView) findViewById(R.id.material_locationbarcode_text);
         tqty= (TextView) findViewById(R.id.material_packageqty_text);
         tpackagenum= (TextView) findViewById(R.id.material_packagenum_text);
         addTextWatcher();

        mSimpleDetailList = (ListView) findViewById(R.id.inventory_infolist);
        refreshdatalist();

    }

    public void refreshdatalist() {
        Collections.sort(productBeans, idComparator);
        mAdapter = new ProductmaterialAdapter(CheckproductMaterialActivity.this, productBeans);
        mSimpleDetailList.setAdapter(mAdapter);
        setListViewHeightBasedOnChildren(mSimpleDetailList);
    }

    public void reloadOrderdata() {
         tproduction_name.setText(orderbean.production_name);
         tprocess_name.setText(orderbean.process_name);
         tdate.setText(orderbean.date);
         tstate.setText(orderbean.state);
    }

    public void reloadPackagedata() {
        Print("reloadPackagedata::"+productMaterialBeans.size());
        if(productMaterialBeans!=null&&productMaterialBeans.size()>=1) {
            ProductMaterialBean packagebean = productMaterialBeans.get(productMaterialBeans.size() - 1);
            tbrand_name.setText(Util.CheckNullString(packagebean.brand_name));
            tsupplier_lot_no.setText(packagebean.supplier_lot_no);
            tlot_no.setText(packagebean.lot_no);
            tfurnace_no.setText(packagebean.furnace_no);
            tmaterial_name.setText(packagebean.material_name);
            tdiameter.setText(packagebean.diameter);
            tproduct_default.setText(packagebean.product_default);
            tlocation_barcode.setText(packagebean.location_barcode);
            tqty.setText(packagebean.qty);
            tpackagenum.setText(packagecode.size()+"");
        }
    }
    public void addTextWatcher() {

        eorderid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//				minventorylibrarynumber.removeTextChangedListener(this);
                if (!eorderid.getText().toString().equals("")) {
                    CheckOrderid checkorderidTask = new CheckOrderid();
                    checkorderidTask.execute();
                }
//				minventorylibrarynumber.addTextChangedListener(this);
            }
        });
        epackagename.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String content = epackagename.getText().toString();
                if(content.contains(",")) {
                    String productinfo[] = content.split(",");
                    if (productinfo.length > 1) {
                        String productplan = productinfo[1];
                        epackagename.setText(productplan);
                    }
                }else {
                    if (!content.equals("")) {
                        CheckPackagenameTask CheckPackagenameTask = new CheckPackagenameTask(content);
                        CheckPackagenameTask.execute();
                    }
                }

            }
        });
    }

    public void onClick(View view) {
        if (view.getId() == R.id.materialproduct_back) {
            finish();
        } else if (view.getId() == R.id.material_submitscan_button) {
            Print("inventory_submit_button:::");
            SubmitscanTask Submitscantask = new SubmitscanTask();
            Submitscantask.execute();
        }
        else if (view.getId() == R.id.material_canclescan_button) {
            Print("inventory_submit_button:::");
            cancleALLdata();
        } else if (view.getId() == R.id.material_ensure_warehouse) {
            Print("inventory_submit_button:::");
            EnsureWarehouseTask mCheckwareinfoTask = new EnsureWarehouseTask();
            mCheckwareinfoTask.execute((Void) null);

        }
        else if (view.getId() == R.id.material_order_button) {
            Print("inventory_bar_code_button:::");
            qrcodetextmode = qrcodemode.ORDERID;
            Util.startQrCode(CheckproductMaterialActivity.this);
        } else if (view.getId() == R.id.material_package_name_button) {
            Print("inventory_library_number_button:::");
            qrcodetextmode = qrcodemode.PACKAGECODE;
            Util.startQrCode(CheckproductMaterialActivity.this);
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
                if (qrcodetextmode == qrcodemode.ORDERID) {
                    eorderid.setText(content);
                } else if (qrcodetextmode == qrcodemode.PACKAGECODE) {
                    epackagename.setText(content);
                }
            }

        }

    }


    private void cancleALLdata() {

         epackagename.setText("");

         tproduction_name.setText("");
         tprocess_name.setText("");
         tdate.setText("");
         tstate.setText("");

         tbrand_name.setText("");
         tsupplier_lot_no.setText("");
         tlot_no.setText("");
         tfurnace_no.setText("");
         tmaterial_name.setText("");
         tdiameter.setText("");
         tproduct_default.setText("");
         tlocation_barcode.setText("");
         tqty.setText("");
         tpackagenum.setText("");
    }

    private String email_key = "email";


    public static Comparator idComparator = new Comparator() {
        @Override
        public int compare(Object o1, Object o2) {
            return (Integer.compare(Integer.parseInt(((ProductMaterialproductBean) o1).sequence), Integer.parseInt(((ProductMaterialproductBean) o2).sequence)));
        }
    };

    public class CheckPackagenameTask extends AsyncTask<Void, Void, Boolean> {

        String token = "";
        String package_name="";
        String name="";
        String success = "";
        String msg = "";
        int responsecode = 0;

        CheckPackagenameTask(String content) {
            package_name=epackagename.getText().toString();
            name=eorderid.getText().toString();
            token = PreferencesUtils.getString(CheckproductMaterialActivity.this, token_key, "");

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                String url = "http://" + PreferencesUtils.getString(CheckproductMaterialActivity.this, ip_key, "120.27.2.177")
                        + ":" + PreferencesUtils.getString(CheckproductMaterialActivity.this, port_key, "8069")
                        + IndexConstants.CHECKMATERIALPACKAGE + "?package_name=" + package_name + "&name=" + name+ "&token=" + token;
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
                        parsePackageinfo(jsonObject);
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
                Util.showShortToastMessage(CheckproductMaterialActivity.this, msg);
                epackagename.setError(null,null);
                reloadPackagedata();
                refreshdatalist();
            } else {
                Util.showShortToastMessage(CheckproductMaterialActivity.this, msg);
                textsetError(CheckproductMaterialActivity.this, epackagename, msg);
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


    public class CheckOrderid extends AsyncTask<Void, Void, Boolean> {
        String token = "";
        String name = "";
        String success = "";
        String msg = "";
        int responsecode = 0;

        CheckOrderid() {
            name = eorderid.getText().toString();

//            customercode = productMaterialBeans.get(productMaterialBeans.size() - 1).customer_code;
            token = PreferencesUtils.getString(CheckproductMaterialActivity.this, token_key, "");

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                String url = "http://" + PreferencesUtils.getString(CheckproductMaterialActivity.this, ip_key, "120.27.2.177")
                        + ":" + PreferencesUtils.getString(CheckproductMaterialActivity.this, port_key, "8062") +
                        IndexConstants.CHECKMATERIALORDERID + "?name=" + name + "&token=" + token;

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
//                        Print("parse orderid json:::" + jsonObject);
                        parseOrderinfo(jsonObject);
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
                reloadOrderdata();
                refreshdatalist();
                eorderid.setError(null,null);
            } else {
                Util.showShortToastMessage(CheckproductMaterialActivity.this, msg);
//                minventorybarcode.setError("产品编号有错");
                textsetError(CheckproductMaterialActivity.this, eorderid, msg);
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

    public class SubmitscanTask extends AsyncTask<Void, Void, Boolean> {
        String token = "";
        String name = "";
        String success = "";
        String msg = "";
        String quant_ids="";
        int responsecode = 0;

        SubmitscanTask() {
            name = eorderid.getText().toString();
            quant_ids = "[";
            if(packagecode.size()>=1) {
                for (int i = 0; i < packagecode.size(); i++) {
                    if (i == 0) {
                        quant_ids = quant_ids + packagecode.get(i);
                    } else if (i == packagecode.size() - 1) {
                        quant_ids = quant_ids +","+ packagecode.get(i);
                    }else{
                        quant_ids = quant_ids +","+ packagecode.get(i);
                    }
                }
            }
            quant_ids = quant_ids+"]";
//            customercode = productMaterialBeans.get(productMaterialBeans.size() - 1).customer_code;
            token = PreferencesUtils.getString(CheckproductMaterialActivity.this, token_key, "");

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                String url = "http://" + PreferencesUtils.getString(CheckproductMaterialActivity.this, ip_key, "120.27.2.177")
                        + ":" + PreferencesUtils.getString(CheckproductMaterialActivity.this, port_key, "8062") +
                        IndexConstants.CHECKMATERIALSUBMIT + "?name=" + name + "&token=" + token+"&quant_ids="+quant_ids;

                Print("material SubmitscanTask url:::" + url);
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
//                        orderbean = new ProductMaterialorderBean();
//                        parseOrderinfo(jsonObject);
                        JSONObject datajsonobj=jsonObject.getJSONObject("data");
                        parseProductlineinfo(datajsonobj);

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

            } else {
                Util.showShortToastMessage(CheckproductMaterialActivity.this, msg);
//                minventorybarcode.setError("产品编号有错");
                textsetError(CheckproductMaterialActivity.this, eorderid, "提交错误：："+msg);
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


    protected void parseOrderinfo(JSONObject jsonObject) {
        JSONArray dataarr = null;
        try {
            dataarr = jsonObject.getJSONArray("data");
            for (int i = 0; i < dataarr.length(); i++) {
                JSONObject dataObject = dataarr.getJSONObject(i);
                orderbean = beanParseUtility.parse(dataObject, ProductMaterialorderBean.class);
                parseProductlineinfo(dataObject);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void parsePackageinfo(JSONObject jsonObject) {
        JSONArray dataarr = null;
//        productMaterialBeans = new ArrayList<ProductMaterialBean>();
        try {
            dataarr = jsonObject.getJSONArray("data");
            for (int i = 0; i < dataarr.length(); i++) {
                JSONObject dataObject = dataarr.getJSONObject(i);
                ProductMaterialBean packagebean = beanParseUtility.parse(dataObject, ProductMaterialBean.class);
                if(!packagecode.contains(packagebean.quant_id)){
                    packagecode.add(packagebean.quant_id);
                }
                productMaterialBeans.add(packagebean);
//                parseProductlineinfo(dataObject);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void parseProductlineinfo(JSONObject jsonObject) {
        JSONArray dataarr = null;
        productBeans= new ArrayList<ProductMaterialproductBean>();
        try {
            dataarr = jsonObject.getJSONArray("line_data");
            for (int i = 0; i < dataarr.length(); i++) {
                JSONObject dataObject = dataarr.getJSONObject(i);
                ProductMaterialproductBean productbean = beanParseUtility.parse(dataObject, ProductMaterialproductBean.class);
                productBeans.add(productbean);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
//    private void saveBoxNum(String code, String num) {
//        for (int i = 0; i < boxesnum.size(); i++) {
//
//            for (String contentkey : boxesnum.get(i).keySet()) {
//                if (code.equals(contentkey)) {
//                    String oldnumstr = boxesnum.get(i).get(contentkey);
//                    try {
//                        int oldnum = Integer.valueOf(oldnumstr);
//                        int newnum = Integer.valueOf(num);
//                        int nownum = oldnum + newnum;
//                        System.err.println("oldnum " + oldnum + "newnum" + newnum + " now " + nownum);
//                        Map<String, String> map = new HashMap<String, String>();
//                        map.put(code, nownum + "");
//                        boxesnum.remove(i);
//                        boxesnum.add(map);//存起每个产品的数量 提交使用
//                        return;
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//        }
//
//        Map<String, String> map = new HashMap<String, String>();
//        map.put(code, num);
//        boxesnum.add(map);//存起每个产品的数量 提交使用
//        return;
//    }


    public class EnsureWarehouseTask extends AsyncTask<Void, Void, Boolean> {

        String success = "";
        String msg = "";
        String name = "";
        String token = "";//token
        String state="";
        int responsecode = 0;

        EnsureWarehouseTask() {


            name = eorderid.getText().toString();

            token = PreferencesUtils.getString(CheckproductMaterialActivity.this, token_key, "");


        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                String url = "http://" + PreferencesUtils.getString(CheckproductMaterialActivity.this, ip_key, "120.27.2.177")
                        + ":" + PreferencesUtils.getString(CheckproductMaterialActivity.this, port_key, "8062") +
                        IndexConstants.CHECKMATERIALWAREHOUSE + "?name="+name;//

                url = url + "&token="+PreferencesUtils.getString(CheckproductMaterialActivity.this,token_key,"");;

                Print("material ensurewarehouse url:::" + url);
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
                        Print("parse submit json:::" + jsonObject);
                        if (success.equals("true")) {
                            JSONObject datajsonobj=jsonObject.getJSONObject("data");
                            state=datajsonobj.getString("state");
                            parseProductlineinfo(datajsonobj);
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
            Util.showShortToastMessage(CheckproductMaterialActivity.this, msg);
//            }
            if (success) {
                tstate.setText(state);
                refreshdatalist();
            } else {

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
        if (keyCode == 301) {
//            if (minventorybatchnumber.isFocused()) {
//                minventorybatchnumber.setText("");
//            } else if (minventorybarcode.isFocused()) {
//                minventorybarcode.setText("");
//            } else if (minventorylibrarynumber.isFocused()) {
//                minventorylibrarynumber.setText("");
//            }
        }
        return super.onKeyDown(keyCode, event);
    }

    String TAG = "materialproduct activity::";

    public void Print(String s) {
        System.out.println(TAG + s);
    }
}
