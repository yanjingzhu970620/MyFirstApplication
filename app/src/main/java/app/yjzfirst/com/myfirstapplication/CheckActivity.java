package app.yjzfirst.com.myfirstapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.yjzfirst.util.PreferencesUtils;

import java.util.concurrent.Executors;

public class CheckActivity extends AppCompatActivity {
    EditText mcheckbatchnumber;
    EditText mcheckbarcode;
    EditText mchecklibrarynumber;
    EditText mcheckNumberperbox;
    EditText mchecknumboxes;
//    EditText mcheckbatchnumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        mcheckbatchnumber = (EditText) findViewById(R.id.check_batch_number);
//        mcheckbatchnumber.addTextChangedListener(shipsWatcher);
        mcheckbarcode = (EditText) findViewById(R.id.check_bar_code);
//        mcheckbarcode.addTextChangedListener(shipsWatcher);
        mchecklibrarynumber = (EditText) findViewById(R.id.check_library_number);
//        mchecklibrarynumber.addTextChangedListener(shipsWatcher);
        mcheckNumberperbox = (EditText) findViewById(R.id.check_Number_per_box);
//        mcheckNumberperbox.addTextChangedListener(shipsWatcher);
        mchecknumboxes = (EditText) findViewById(R.id.check_num_boxes);
//        mchecknumboxes.addTextChangedListener(shipsWatcher);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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

    private TextWatcher shipsWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
//              if(s.){}
            System.err.println(s.toString());
        }
    };
    public void onClick(View view) {
        if (view.getId() == R.id.check_back) {
            finish();
        }else if (view.getId() == R.id.check_submit_button) {

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
