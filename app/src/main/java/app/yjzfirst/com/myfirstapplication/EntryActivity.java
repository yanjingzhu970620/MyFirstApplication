package app.yjzfirst.com.myfirstapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class EntryActivity extends AppCompatActivity {
    EditText mentrybatchnumber;
    EditText mentrybarcode;
    EditText mentrylibrarynumber;
    EditText mentryNumberperbox;
    EditText mentrynumboxes;
    EditText entryweightthousands;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        mentrybatchnumber = (EditText) findViewById(R.id.entry_batch_number);
//        mcheckbatchnumber.addTextChangedListener(shipsWatcher);
        mentrybarcode = (EditText) findViewById(R.id.entry_bar_code);
//        mcheckbarcode.addTextChangedListener(shipsWatcher);
        mentrylibrarynumber = (EditText) findViewById(R.id.entry_library_number);
//        mchecklibrarynumber.addTextChangedListener(shipsWatcher);
        mentryNumberperbox = (EditText) findViewById(R.id.entry_Number_per_box);
//        mcheckNumberperbox.addTextChangedListener(shipsWatcher);
        mentrynumboxes = (EditText) findViewById(R.id.entry_num_boxes);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        entryweightthousands = (EditText) findViewById(R.id.entry_weight_thousands);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.entry_back) {
            finish();
        }else if (view.getId() == R.id.entry_submit_button) {

        }
    }
}
