package app.yjzfirst.com.myfirstapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class DeliveryActivity extends AppCompatActivity {
    EditText mdeliverybatchnumber;
    EditText mdeliverybarcode;
    EditText mdeliverylibrarynumber;
    EditText mdeliveryNumberperbox;
    EditText mdeliverynumboxes;
    EditText mdeliveryOrdernumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        mdeliverybatchnumber = (EditText) findViewById(R.id.delivery_batch_number);
//        mcheckbatchnumber.addTextChangedListener(shipsWatcher);
        mdeliverybarcode = (EditText) findViewById(R.id.delivery_bar_code);
//        mcheckbarcode.addTextChangedListener(shipsWatcher);
        mdeliverylibrarynumber = (EditText) findViewById(R.id.delivery_library_number);
//        mchecklibrarynumber.addTextChangedListener(shipsWatcher);
        mdeliveryNumberperbox = (EditText) findViewById(R.id.delivery_Number_per_box);
//        mcheckNumberperbox.addTextChangedListener(shipsWatcher);
        mdeliverynumboxes = (EditText) findViewById(R.id.delivery_num_boxes);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mdeliveryOrdernumber = (EditText) findViewById(R.id.delivery_Order_number);

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

        }
    }
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

//        private final String mEmail;
//        private final String mPassword;
//
//        UserLoginTask(String email, String password) {
//            mEmail = email;
//            mPassword = password;
//        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

//            for (String credential : DUMMY_CREDENTIALS) {
//                String[] pieces = credential.split(":");
//                if (pieces[0].equals(mEmail)) {
//                    // Account exists, return true if the password matches.
//                    return pieces[1].equals(mPassword);
//                }
//            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
//            mAuthTask = null;
//            showProgress(false);
//
//            if (success) {
//                PreferencesUtils.putString(LoginActivity.this,email_key,mEmail);
//                PreferencesUtils.putString(LoginActivity.this,password_key,mPassword);
//                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
//                LoginActivity.this.startActivity(intent);
//                finish();
//            } else {
//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
//            }
        }

        @Override
        protected void onCancelled() {
//            mAuthTask = null;
//            showProgress(false);
        }
    }
}
