package com.kii.android.sdk.tutorial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kii.android.sdk.tutorial.R;
import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiUser;
import com.kii.cloud.storage.callback.KiiUserCallBack;

public class MainActivity extends Activity {
    private static final String TAG = "LoginActivity";
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
        Kii.initialize(AppConstants.APP_ID, AppConstants.APP_KEY, AppConstants.APP_SITE);
    }

    public void onLoginButtonClicked(View v) {
        progressBar.setVisibility(View.VISIBLE);
        Log.v(TAG, "login clicked");
        String username = mUsernameField.getText().toString();
        String password = mPasswordField.getText().toString();

        KiiUser.logIn(callback, username, password);
    }

    public void onSignupButtonClicked(View v) {
        progressBar.setVisibility(View.VISIBLE);

        String username = mUsernameField.getText().toString();
        String password = mPasswordField.getText().toString();
        KiiUser user = KiiUser.createWithUsername(username);
        user.register(callback, password);

    }

    KiiUserCallBack callback = new KiiUserCallBack() {
        @Override
        public void onLoginCompleted(int token, KiiUser user, Exception e) {
            progressBar.setVisibility(View.INVISIBLE);
            if (e == null) {
                showToast("User logged-in!");
                Intent i = new Intent(MainActivity.this, KiiObjectCreateActivity.class);
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
                Intent i = new Intent(MainActivity.this, KiiObjectCreateActivity.class);
                startActivity(i);
            } else {
                showToast("Error : " + e.getLocalizedMessage());
            }
        }
    };

    private void showToast(String message) {
        Toast.makeText(this, message,Toast.LENGTH_SHORT).show();
    }

}
