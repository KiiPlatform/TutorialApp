package com.kii.android.sdk.tutorial;

import java.io.File;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.resumabletransfer.KiiRTransfer;
import com.kii.cloud.storage.resumabletransfer.KiiRTransferCallback;
import com.kii.cloud.storage.resumabletransfer.KiiUploader;

public class KiiObjectAttachFileActivity extends FragmentActivity {
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
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            Uri selectedFileUri = data.getData();
            String filePath = getFilePathByUri(selectedFileUri);
            Log.v(TAG, "Picture Path : " + filePath);
            if (filePath == null) {
                InvalidFileDialogFragment fragment = InvalidFileDialogFragment
                        .newInstance("File not exists!",
                                "Please select an image that exists locally.");
                fragment.show(getSupportFragmentManager(), TAG);
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
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(selectedFileUri,
                filePathColumn, null, null, null);
        if (cursor == null)
            return null;
        try {
            if (!cursor.moveToFirst())
                return null;
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            return picturePath;
        } finally {
            cursor.close();
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
                    newFragment.show(getSupportFragmentManager(), TAG);
                } else {
                    showToast("Error in file upload :"
                            + e.getLocalizedMessage());
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

    public static class InvalidFileDialogFragment extends DialogFragment {

        public static InvalidFileDialogFragment newInstance(String title,
                String msg) {
            InvalidFileDialogFragment frag = new InvalidFileDialogFragment();
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
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                        int whichButton) {
                                    dialog.cancel();
                                }
                            }).create();
        }
    }

}
