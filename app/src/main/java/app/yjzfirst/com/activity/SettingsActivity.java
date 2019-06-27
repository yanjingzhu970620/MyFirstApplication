package app.yjzfirst.com.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.yjzfirst.util.PreferencesUtils;

import static com.yjzfirst.util.IndexConstants.ip_key;
import static com.yjzfirst.util.IndexConstants.port_key;

public class SettingsActivity extends AppCompatActivity {
    EditText eip;
    EditText eport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Window window = SettingsActivity.this.getWindow();

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(Color.BLACK);

            //底部导航栏
            //window.setNavigationBarColor(activity.getResources().getColor(colorResId));

        }
        eip = (EditText) findViewById(R.id.ip_edit);
        eip.setText(PreferencesUtils.getString(SettingsActivity.this, ip_key, "120.27.2.177"));
        eport = (EditText) findViewById(R.id.port_edit);
        eport.setText(PreferencesUtils.getString(SettingsActivity.this, port_key, "8061"));
    }

    public void onClick(View view) {
        if (view.getId() == R.id.settings_back) {
            finish();
        } else if (view.getId() == R.id.set_ipport_button) {
            PreferencesUtils.putString(SettingsActivity.this, ip_key, eip.getText().toString());
            PreferencesUtils.putString(SettingsActivity.this, port_key, eport.getText().toString());
            finish();
        }
    }

}
