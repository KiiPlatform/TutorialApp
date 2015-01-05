package com.kii.android.sdk.tutorial;

import java.io.File;
import java.io.FileOutputStream;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
        Button attachButton = (Button) view
                .findViewById(R.id.attach_file_button);
        attachButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onAttachFileButtonClicked(v);
            }
        });
        setPageImage(3);
        ImageView imageView = (ImageView) view.findViewById(R.id.details);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailDialogResource resource = new DetailDialogResource(
                        getResources().getString(R.string.attach_file_detail),
                        getResources().getString(
                                R.string.attach_body_description));
                resource.setImageId(R.drawable.bodyattach);
                resource.setDocsUrl(Util.getKiiDocsBaseUrl()+"/guides/android/managing-data/buckets");
                DialogFragment newFragment = DetailDialogFragment.newInstance(resource);
                newFragment.show(getFragmentManager(), "dialog");
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
        startActivityForResult(
                Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
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
                    AlertDialogFragment.AlertDialogListener listener = new AlertDialogFragment.AlertDialogListener(){
                        @Override
                        public void onAlertFinished() {
                            FragmentTransaction ft = getFragmentManager()
                                    .beginTransaction();
                            ft.replace(R.id.mainFragment, new SummaryFragment());
                            ft.addToBackStack(null);
                            ft.commit();
                        }
                    };
                    DialogFragment newFragment = AlertDialogFragment
                            .newInstance(
                                    R.string.attach_file,
                                    "File attached successfully!\nNow lets check what we have done!",
                                    listener);
                    newFragment.show(getFragmentManager(), "dialog");
                } else {
                    Throwable cause = e.getCause();
                    if (cause instanceof CloudExecutionException)
                        showAlert(Util
                                .generateAlertMessage((CloudExecutionException) cause));
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
                R.string.operation_failed, message, null);
        newFragment.show(getFragmentManager(), "dialog");
    }

    void setFragmentProgress(int v) {
        ProgressFragment fragment = (ProgressFragment) getFragmentManager()
                .findFragmentById(R.id.progressFragment);
        if (fragment != null && fragment.isInLayout()) {
            fragment.setProgressBarVisiblity(v);
        }
    }

    void setPageImage(int page) {
        ProgressFragment fragment = (ProgressFragment) getFragmentManager()
                .findFragmentById(R.id.progressFragment);
        if (fragment != null && fragment.isInLayout()) {
            fragment.setPageImage(page);
        }
    }
}
