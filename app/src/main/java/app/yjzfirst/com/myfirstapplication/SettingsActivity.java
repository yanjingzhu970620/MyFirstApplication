package app.yjzfirst.com.myfirstapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.yjzfirst.util.PreferencesUtils;

public class SettingsActivity extends AppCompatActivity {
    EditText eip;
    EditText eport;
    public String ip_key="ip";
    public String port_key="port";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        eip = (EditText) findViewById(R.id.ip_edit);
        eip.setText(PreferencesUtils.getString(SettingsActivity.this,ip_key,"1.1.1.1"));
        eport = (EditText) findViewById(R.id.port_edit);
        eport.setText(PreferencesUtils.getString(SettingsActivity.this,port_key,"1111"));
    }

    public void onClick(View view) {
        if (view.getId() == R.id.settings_back) {
            finish();
        }else if(view.getId() == R.id.set_ipport_button){
            PreferencesUtils.putString(SettingsActivity.this,ip_key,eip.getText().toString());
            PreferencesUtils.putString(SettingsActivity.this,port_key,eport.getText().toString());
            finish();
        }
    }

}
