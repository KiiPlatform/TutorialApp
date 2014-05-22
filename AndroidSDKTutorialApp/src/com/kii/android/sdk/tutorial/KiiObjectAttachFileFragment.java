package com.kii.android.sdk.tutorial;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Locale;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.exception.CloudExecutionException;
import com.kii.cloud.storage.resumabletransfer.KiiRTransfer;
import com.kii.cloud.storage.resumabletransfer.KiiRTransferCallback;
import com.kii.cloud.storage.resumabletransfer.KiiUploader;

public class KiiObjectAttachFileFragment extends Fragment {
    private static final String TAG = "KiiObjectAttachFileFragment";
    String objectUri = null;
    private static final int PICK_IMAGE = 1;
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attach_file, container,
                false);
        Bundle args = getArguments();
        objectUri = args.getString("object_uri");
        TextView tv = (TextView) view.findViewById(R.id.attach_desc_textView);
        tv.setText(tv.getText() + " " + this.getKiiDocsUrl());
        Linkify.addLinks(tv, Linkify.WEB_URLS);
        Button attachButton = (Button) view
                .findViewById(R.id.attach_file_button);
        attachButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onAttachFileButtonClicked(v);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.activity = null;
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
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
        Toast.makeText(this.activity, message, Toast.LENGTH_SHORT).show();
    }

    private String getFilePathByUri(Uri selectedFileUri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            // Workaround of retrieving file image through ContentResolver
            // for Android4.2 or later
            String filePath = null;
            FileOutputStream fos = null;
            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(
                        this.activity.getContentResolver(), selectedFileUri);

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
            Cursor cursor = this.activity.getContentResolver().query(
                    selectedFileUri, filePathColumn, null, null, null);

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
        KiiUploader uploader = object.uploader(this.activity, f);
        uploader.transferAsync(new KiiRTransferCallback() {

            @Override
            public void onStart(KiiRTransfer operator) {
                setFragmentProgress(View.VISIBLE);
            }

            @Override
            public void onTransferCompleted(KiiRTransfer operator, Exception e) {
                setFragmentProgress(View.INVISIBLE);
                if (e == null) {
                    showToast("File attached successfully!");
                    FragmentTransaction ft = getFragmentManager()
                            .beginTransaction();
                    ft.replace(R.id.mainFragment, new SummaryFragment());
                    ft.commit();
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

    public void moveFromDialogFragment(Class<?> clazz) {
        if (clazz != null) {
            Intent i = new Intent(this.activity, clazz);
            startActivity(i);
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

    void setFragmentProgress(int v) {
        ProgressFragment fragment = (ProgressFragment) getFragmentManager()
                .findFragmentById(R.id.progressFragment);
        if (fragment != null && fragment.isInLayout()) {
            fragment.setProgressBarVisiblity(v);
        }
    }
}
