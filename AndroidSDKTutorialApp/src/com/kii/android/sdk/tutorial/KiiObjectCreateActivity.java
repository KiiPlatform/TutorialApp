package com.kii.android.sdk.tutorial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.kii.cloud.analytics.KiiAnalytics;
import com.kii.cloud.analytics.SessionTracker;
import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiBucket;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.callback.KiiObjectCallBack;

public class KiiObjectCreateActivity extends Activity {
    static final String TAG = "KiiObjectCreateActivity";
    ProgressBar progressBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_object);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    protected void onStart() {
        super.onResume();
        Log.v(TAG, "KiiObjectCreateActivity session start");
        SessionTracker.onStartActivity(this, AppConstants.APP_ID,
                AppConstants.APP_KEY, KiiAnalytics.Site.US);
    }



    @Override
    protected void onStop() {
        super.onPause();
        Log.v(TAG, "KiiObjectCreateActivity session stop");
        SessionTracker.onStopActivity(this);
    }

    public void onCreateObjectButtonClicked(View v) {
        progressBar.setVisibility(View.VISIBLE);
        KiiBucket bucket = Kii.bucket(AppConstants.APP_BUCKET_NAME);
        KiiObject object = bucket.object();
        object.set("score", (int) (Math.random() * 100));
        object.save(new KiiObjectCallBack() {

            @Override
            public void onSaveCompleted(int token, KiiObject object,
                    Exception exception) {
                progressBar.setVisibility(View.INVISIBLE);
                if (exception == null) {
                    showToast("Object created!");
                    Intent i = new Intent(KiiObjectCreateActivity.this,
                            KiiObjectAttachFileActivity.class);
                    i.putExtra("object_uri", object.toUri().toString());
                    startActivity(i);
                } else {
                    showToast("Error : " + exception.getLocalizedMessage());
                }
            }

        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
