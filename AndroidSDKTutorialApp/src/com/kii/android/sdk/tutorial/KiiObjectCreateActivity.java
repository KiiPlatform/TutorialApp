package com.kii.android.sdk.tutorial;

import java.util.Locale;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiBucket;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.callback.KiiObjectCallBack;
import com.kii.cloud.storage.exception.CloudExecutionException;

public class KiiObjectCreateActivity extends Activity {
    ProgressBar progressBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_object);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        TextView tv = (TextView)findViewById(R.id.description_textView);
        tv.setText(tv.getText()+" "+this.getKiiDocsUrl());
        Linkify.addLinks(tv, Linkify.WEB_URLS);
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
                    if (exception instanceof CloudExecutionException)
                        showAlert(Util
                                .generateAlertMessage((CloudExecutionException) exception));
                    else
                        showAlert(exception.getLocalizedMessage());
                }
            }

        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    void showAlert(String message) {
        DialogFragment newFragment = AlertDialogFragment.newInstance(
                R.string.operation_failed, message);
        newFragment.show(getFragmentManager(), "dialog");
    }

    private String getKiiDocsUrl() {
        Locale locale = Locale.getDefault();
        String language = locale.getDisplayLanguage();
        String langPath = (language == "jp" || language == "cn") ? language
                : "en";
        return String.format(
                "http://documentation.kii.com/%s/guides/android/managing-data",
                langPath);
    }

}
