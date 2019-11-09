package app.yjzfirst.com.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yjzfirst.util.PreferencesUtils;
import com.yjzfirst.util.UserLogout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
//    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

//    private LinearLayout entry;
//    private LinearLayout entry;
//    private LinearLayout entry;
//
//
//    private TextView entrytext;
//    private TextView entrytext;
//    private TextView entrytext;
///7 10 12
    public String rights_key="rights";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout entry_main=(LinearLayout) findViewById(R.id.entry_main);
        LinearLayout entry_main_warehouse=(LinearLayout) findViewById(R.id.entry_main_warehouse);
        LinearLayout delivery_main=(LinearLayout) findViewById(R.id.delivery_main);
        LinearLayout report_check_main=(LinearLayout) findViewById(R.id.Report_Check_main);
        LinearLayout report_check_main_material=(LinearLayout) findViewById(R.id.Report_Check_main_material);
        LinearLayout report_check_main_inspect=(LinearLayout) findViewById(R.id.Report_Check_main_inspect);
        LinearLayout Search_inventory_main=(LinearLayout) findViewById(R.id.Search_inventory_main);
        LinearLayout manufacture_main=(LinearLayout) findViewById(R.id.manufacture_main);
        LinearLayout report_check_main_material_content=(LinearLayout) findViewById(R.id.Report_Check_main_material_content);
        LinearLayout report_check_main_content=(LinearLayout) findViewById(R.id.Report_Check_main_content);
        LinearLayout material_Check_main_content=(LinearLayout) findViewById(R.id.Material_Check_main_content);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Window window = MainActivity.this.getWindow();

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(Color.BLACK);

            //底部导航栏

            //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
        }
        String rightstring= PreferencesUtils.getString(MainActivity.this,rights_key,"rights");
        int coloumn=9;
        String[] rights=rightstring.replace("[","")
                .replace("]","").replace("\"","").split(",");
        ArrayList<String> right = new ArrayList<String>(Arrays.asList(rights));
        System.out.println(right.contains("group_app_mrp_material_picking")+" "+right.size()+right.get(0));
        if (!right.contains("group_app_mrp_finish_in")) {
            entry_main.setVisibility(View.GONE);
            coloumn = coloumn - 1;
        }
        if (!right.contains("group_app_mrp_finish_in_confirm")) {
            entry_main_warehouse.setVisibility(View.GONE);
            coloumn = coloumn - 1;
        }
        if (!right.contains("group_app_sales_delivery")) {
            delivery_main.setVisibility(View.GONE);
            coloumn = coloumn - 1;
        }
        if (!right.contains("group_app_mrp_move")) {
            report_check_main.setVisibility(View.GONE);
            coloumn = coloumn - 1;
        }
        if (!right.contains("group_app_mrp_material")) {
            report_check_main_material.setVisibility(View.GONE);
            coloumn = coloumn - 1;
        }
        if (!right.contains("group_app_mrp_inspect")) {
            report_check_main_inspect.setVisibility(View.GONE);
            coloumn = coloumn - 1;
        }
        if (!right.contains("group_app_inventory_query")) {
            Search_inventory_main.setVisibility(View.GONE);
            coloumn = coloumn - 1;
        }
        if (!right.contains("group_app_manufacture_query")) {
            manufacture_main.setVisibility(View.GONE);
            coloumn = coloumn - 1;
        }
        if (!right.contains("group_app_mrp_material_picking")) {
            material_Check_main_content.setVisibility(View.GONE);
            coloumn = coloumn - 1;
        }

        if(coloumn<=2) {
            report_check_main_material_content.setVisibility(View.INVISIBLE);
            report_check_main_content.setVisibility(View.INVISIBLE);
        }else{
            report_check_main_material_content.setVisibility(View.GONE);
            report_check_main_content.setVisibility(View.GONE);
        }


        //"group_app_mrp_finish_in","group_app_mrp_finish_in_confirm","group_app_mrp_move","group_app_sales_delivery"
    }

    public void onClick(View view) {
        if (view.getId() == R.id.delivery_main) {
            System.err.println("delivery_main delivery_main");
            Intent intent=new Intent(MainActivity.this,DeliveryActivity.class);
            MainActivity.this.startActivity(intent);
        }else if (view.getId() == R.id.entry_main) {
            Intent intent=new Intent(MainActivity.this,EntryFormActivity.class);
            MainActivity.this.startActivity(intent);
        }else if (view.getId() == R.id.entry_main_warehouse) {
            Intent intent=new Intent(MainActivity.this,EntryWarehouseActivity.class);
            MainActivity.this.startActivity(intent);
        }else if (view.getId() == R.id.Report_Check_main) {
            Intent intent=new Intent(MainActivity.this,ReportActivity.class);
            MainActivity.this.startActivity(intent);
        }else if (view.getId() == R.id.Report_Check_main_material) {
            Intent intent=new Intent(MainActivity.this,ReportmaterialActivity.class);
            MainActivity.this.startActivity(intent);
        }else if (view.getId() == R.id.Report_Check_main_inspect) {
            Intent intent=new Intent(MainActivity.this,ReportcheckActivity.class);
            MainActivity.this.startActivity(intent);
        }else if (view.getId() == R.id.Search_inventory_main) {
            Intent intent=new Intent(MainActivity.this,CheckInventoryActivity.class);
            MainActivity.this.startActivity(intent);
        }else if (view.getId() == R.id.Material_Check_main_content) {
            Intent intent=new Intent(MainActivity.this,CheckproductMaterialActivity.class);
            MainActivity.this.startActivity(intent);
        }

    }

    long exitTime=0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        System.out.println("LoginActivity 按两次退出。");
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), /* "再按退出程序" */
                        getResources().getString(R.string.tap_twice_to_exit),
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                UserLogout thread = null;
                thread = new UserLogout(MainActivity.this);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    // print("MoreActivity 》 Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB");
                    thread.executeOnExecutor(
                            Executors.newCachedThreadPool(),
                            new String[0]);
                } else {
                    thread.execute();
                }
                finish();
                System.exit(0);
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
