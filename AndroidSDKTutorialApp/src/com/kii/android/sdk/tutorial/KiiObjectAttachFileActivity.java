package com.kii.android.sdk.tutorial;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.kii.android.sdk.tutorial.R;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.resumabletransfer.KiiRTransfer;
import com.kii.cloud.storage.resumabletransfer.KiiRTransferCallback;
import com.kii.cloud.storage.resumabletransfer.KiiUploader;


public class KiiObjectAttachFileActivity extends Activity {
    private static final String TAG = "KiiObjectAttachFileActivity";
    ProgressBar progressBar = null;
    String objectUri = null;
    private static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attach_file);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setProgress(0);
        objectUri = getIntent().getStringExtra("object_uri");
    }

    public void onAttachFileButtonClicked(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }


    

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
        if(requestCode == PICK_IMAGE) {
            Uri selectedFileUri = data.getData();
            String filePath = getFilePathByUri(selectedFileUri);
            Log.v(TAG, "Picture Path : "+filePath);
            uploadFile(filePath);
        } else {
            showToast("picking file failed!");
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message,Toast.LENGTH_SHORT).show();
    }

    private String getFilePathByUri(Uri selectedFileUri) {
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(selectedFileUri,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }

    private void uploadFile(String path) {
        KiiObject object = KiiObject.createByUri(Uri.parse(objectUri));
        File f = new File(path);
        Log.v(TAG, "file can read : "+f.canRead());
        KiiUploader uploader = object.uploader(this,f);
        uploader.transferAsync(new KiiRTransferCallback() {

            @Override
            public void onProgress(KiiRTransfer operator,
                    long completedInBytes, long totalSizeinBytes) {
                progressBar.setProgress(getProgressParcentage(
                        completedInBytes, totalSizeinBytes));
                Log.v(TAG, "Progress: completed: "+completedInBytes+" total:"+totalSizeinBytes);
                Log.v(TAG, "progress:"+getProgressParcentage(
                        completedInBytes, totalSizeinBytes));
            }

            @Override
            public void onStart(KiiRTransfer operator) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTransferCompleted(KiiRTransfer operator, Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                showToast("File uploaded!");
            }
        });
    }

    private int getProgressParcentage(long completedInBytes, long totalSizeinBytes) {
       return  (int)((completedInBytes*100.0f)/totalSizeinBytes);
    }

}
