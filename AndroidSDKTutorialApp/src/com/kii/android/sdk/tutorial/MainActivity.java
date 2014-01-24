package com.kii.android.sdk.tutorial;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kii.cloud.analytics.KiiAnalytics;
import com.kii.cloud.analytics.SessionTracker;
import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiUser;
import com.kii.cloud.storage.callback.KiiUserCallBack;

public class MainActivity extends Activity {
    private static final String TAG = "KiiMainActivity";
    private TextView mUsernameField;
    private TextView mPasswordField;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUsernameField = (EditText) findViewById(R.id.username_field);
        mPasswordField = (EditText) findViewById(R.id.password_field);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Kii.initialize(AppConstants.APP_ID, AppConstants.APP_KEY,
                AppConstants.APP_SITE);

        // Specify EditText type now, due to control font size of EditText hint.
        EditText editPassword = (EditText) this
                .findViewById(R.id.password_field);
        editPassword
                .setTransformationMethod(new PasswordTransformationMethod());
        editPassword.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        // start session event notification service
        Intent service = new Intent(this, SessionEventNotificationService.class);
        this.startService(service);
    }

    

    @Override
    protected void onStart() {
        super.onResume();
        Log.v(TAG, "MainActivity session start");
        SessionTracker.onStartActivity(this, AppConstants.APP_ID,
                AppConstants.APP_KEY, KiiAnalytics.Site.US);
    }



    @Override
    protected void onStop() {
        super.onPause();
        Log.v(TAG, "MainActivity session stop");
        SessionTracker.onStopActivity(this);
    }



    public void onLoginButtonClicked(View v) {
        // Hide soft keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        progressBar.setVisibility(View.VISIBLE);
        Log.v(TAG, "login clicked");
        String username = mUsernameField.getText().toString();
        String password = mPasswordField.getText().toString();
        try {
            KiiUser.logIn(callback, username, password);
        } catch (Exception e) {
            progressBar.setVisibility(View.INVISIBLE);
            showToast("Error : " + e.getLocalizedMessage());
        }
    }

    public void onSignupButtonClicked(View v) {
        // Hide soft keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        progressBar.setVisibility(View.VISIBLE);

        String username = mUsernameField.getText().toString();
        String password = mPasswordField.getText().toString();
        try {
            KiiUser user = KiiUser.createWithUsername(username);
            user.register(callback, password);
        } catch (Exception e) {
            e.printStackTrace();
            progressBar.setVisibility(View.INVISIBLE);
            showToast("Error : " + e.getLocalizedMessage());
        }
    }

    KiiUserCallBack callback = new KiiUserCallBack() {
        @Override
        public void onLoginCompleted(int token, KiiUser user, Exception e) {
            progressBar.setVisibility(View.INVISIBLE);
            if (e == null) {
                showToast("User logged-in!");
                Intent i = new Intent(MainActivity.this,
                        KiiObjectCreateActivity.class);
                startActivity(i);
            } else {
                showToast("Error : " + e.getLocalizedMessage());
            }
        }

        @Override
        public void onRegisterCompleted(int token, KiiUser user, Exception e) {
            progressBar.setVisibility(View.INVISIBLE);
            if (e == null) {
                showToast("User registered!");
                Intent i = new Intent(MainActivity.this,
                        KiiObjectCreateActivity.class);
                startActivity(i);
            } else {
                showToast("Error : " + e.getLocalizedMessage());
            }
        }
    };

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
