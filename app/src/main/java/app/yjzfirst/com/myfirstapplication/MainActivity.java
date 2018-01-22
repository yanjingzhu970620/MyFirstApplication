package app.yjzfirst.com.myfirstapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Window window = MainActivity.this.getWindow();

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(Color.BLACK);



            //底部导航栏

            //window.setNavigationBarColor(activity.getResources().getColor(colorResId));

        }

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
            Intent intent=new Intent(MainActivity.this,CheckActivity.class);
            MainActivity.this.startActivity(intent);
        }
    }

}
