package com.kii.android.sdk.tutorial;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.exception.CloudExecutionException;
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
        TextView tv = (TextView)findViewById(R.id.attach_desc_textView);
        tv.setText(tv.getText()+" "+this.getKiiDocsUrl());
        Linkify.addLinks(tv, Linkify.WEB_URLS);
    }

    public void onAttachFileButtonClicked(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            startActivityForResult(intent, PICK_IMAGE);
        } else {
            startActivityForResult(
                    Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            Uri selectedFileUri = data.getData();
            String filePath = getFilePathByUri(selectedFileUri);
            Log.v(TAG, "Picture Path : " + filePath);
            if (filePath == null) {
                showAlert("File not exists, Please select an image that exists locally.");
                return;
            }
            uploadFile(filePath);
        } else {
            showToast("picking file failed!");
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private String getFilePathByUri(Uri selectedFileUri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            // Workaround of retrieving file image through ContentResolver
            // for Android4.2 or later
            String filePath = null;
            FileOutputStream fos = null;
            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(
                        getContentResolver(), selectedFileUri);

                String cacheDir = Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + File.separator + "tutorialapp";
                File createDir = new File(cacheDir);
                if (!createDir.exists()) {
                    createDir.mkdir();
                }
                filePath = cacheDir + File.separator + "upload.jpg";
                File file = new File(filePath);

                fos = new FileOutputStream(file);
                bmp.compress(CompressFormat.JPEG, 95, fos);
                fos.flush();
                fos.getFD().sync();
            } catch (Exception e) {
                filePath = null;
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (Exception e) {
                        // Nothing to do
                    }
                }
            }
            return filePath;
        } else {
            String[] filePathColumn = { MediaStore.MediaColumns.DATA };
            Cursor cursor = getContentResolver().query(selectedFileUri,
                    filePathColumn, null, null, null);

            if (cursor == null)
                return null;
            try {
                if (!cursor.moveToFirst())
                    return null;
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                if (columnIndex < 0) {
                    return null;
                }
                String picturePath = cursor.getString(columnIndex);
                return picturePath;
            } finally {
                cursor.close();
            }
        }
    }

    private void uploadFile(String path) {
        KiiObject object = KiiObject.createByUri(Uri.parse(objectUri));
        File f = new File(path);
        Log.v(TAG, "file can read : " + f.canRead());
        KiiUploader uploader = object.uploader(this, f);
        uploader.transferAsync(new KiiRTransferCallback() {

            @Override
            public void onProgress(KiiRTransfer operator,
                    long completedInBytes, long totalSizeinBytes) {
                progressBar.setProgress(getProgressParcentage(completedInBytes,
                        totalSizeinBytes));
            }

            @Override
            public void onStart(KiiRTransfer operator) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTransferCompleted(KiiRTransfer operator, Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                if (e == null) {
                    DialogFragment newFragment = UploadFinishDialogFragment
                            .newInstance("File uploaded!",
                                    "Would you like to create another object?");
                    newFragment.show(getFragmentManager(), "dialog");
                } else {
                    if (e instanceof CloudExecutionException)
                        showAlert(Util
                                .generateAlertMessage((CloudExecutionException) e));
                    else
                        showAlert(e.getLocalizedMessage());
                }
            }
        });
    }

    private int getProgressParcentage(long completedInBytes,
            long totalSizeinBytes) {
        return (int) ((completedInBytes * 100.0f) / totalSizeinBytes);
    }

    public void moveFromDialogFragment(Class<?> clazz) {
        if (clazz != null) {
            Intent i = new Intent(this, clazz);
            startActivity(i);
        }
    }

    public static class UploadFinishDialogFragment extends DialogFragment {

        public static UploadFinishDialogFragment newInstance(String title,
                String msg) {
            UploadFinishDialogFragment frag = new UploadFinishDialogFragment();
            Bundle args = new Bundle();
            args.putString("title", title);
            args.putString("msg", msg);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String title = getArguments().getString("title");
            String msg = getArguments().getString("msg");

            return new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setMessage(msg)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                        int whichButton) {
                                    ((KiiObjectAttachFileActivity) getActivity())
                                            .moveFromDialogFragment(KiiObjectCreateActivity.class);

                                }
                            })
                    .setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                        int whichButton) {
                                    ((KiiObjectAttachFileActivity) getActivity())
                                            .moveFromDialogFragment(MainActivity.class);
                                }
                            }).create();
        }
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
