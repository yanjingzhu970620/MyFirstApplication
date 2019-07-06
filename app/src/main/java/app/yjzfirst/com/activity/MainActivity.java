package app.yjzfirst.com.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.yjzfirst.util.PreferencesUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public String rights_key="rights";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout entry_main=(LinearLayout) findViewById(R.id.entry_main);
        LinearLayout delivery_main=(LinearLayout) findViewById(R.id.delivery_main);
        LinearLayout Check_main=(LinearLayout) findViewById(R.id.Check_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Window window = MainActivity.this.getWindow();

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(Color.BLACK);

            //底部导航栏

            //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
        }
        String rights= PreferencesUtils.getString(MainActivity.this,rights_key,"rights");
        if(!rights.contains("group_app_mrp_finish_in")){
            entry_main.setVisibility(View.GONE);
        }
        if(!rights.contains("group_app_mrp_finish_in_confirm")){
            entry_main.setVisibility(View.GONE);
        }
        if(!rights.contains("group_app_mrp_move")){
            Check_main.setVisibility(View.GONE);
        }
        if(!rights.contains("group_app_sales_delivery")){
            delivery_main.setVisibility(View.GONE);
        }

        SimpleDateFormat sdf = new SimpleDateFormat(
                "yyyyMMdd");
        Date nowdate=new Date();
        Date expiredate=new Date();;
        try {
            expiredate=sdf.parse("20191001");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(nowdate.after(expiredate)){

            finish();
            System.exit(0);
        }
        //"group_app_mrp_finish_in","group_app_mrp_finish_in_confirm","group_app_mrp_move","group_app_sales_delivery"
    }

    public void onClick(View view) {
        if (view.getId() == R.id.delivery_main) {
            System.err.println("delivery_main delivery_main");
            Intent intent=new Intent(MainActivity.this,DeliveryActivity.class);
            MainActivity.this.startActivity(intent);
        }else if (view.getId() == R.id.entry_main) {
            Intent intent=new Intent(MainActivity.this,EntryActivity.class);
            MainActivity.this.startActivity(intent);
        }else if (view.getId() == R.id.Check_main) {
            Intent intent=new Intent(MainActivity.this,ReportActivity.class);
            MainActivity.this.startActivity(intent);
        }
    }

}
