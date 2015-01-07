package com.kii.android.sdk.tutorial;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiBucket;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.callback.KiiObjectCallBack;
import com.kii.cloud.storage.exception.CloudExecutionException;

public class KiiObjectCreateFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_object,
                container, false);
        Button createObjectBtn = (Button) view
                .findViewById(R.id.create_object_button);
        createObjectBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                KiiObjectCreateFragment.this.onCreateObjectButtonClicked(v);
            }
        });
        setPageImage(2);
        ImageView imageView = (ImageView) view.findViewById(R.id.details);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailDialogResource resource = new DetailDialogResource(
                        getResources().getString(R.string.kiiobject_detail),
                        getResources().getString(
                                R.string.create_object_description));
                resource.setImageId(R.drawable.datastore);
                resource.setDocsUrl(Util.getKiiDocsBaseUrl()+"/guides/android/managing-data/buckets");
                DialogFragment newFragment = DetailDialogFragment.newInstance(resource);
                newFragment.show(getFragmentManager(), "dialog");
            }
        });
        return view;
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

    public void onCreateObjectButtonClicked(View v) {
        setFragmentProgress(View.VISIBLE);
        KiiBucket bucket = Kii.bucket(AppConstants.APP_BUCKET_NAME);
        KiiObject object = bucket.object();
        object.set("score", (int) (Math.random() * 100));
        object.save(new KiiObjectCallBack() {

            @Override
            public void onSaveCompleted(int token, KiiObject object,
                    Exception exception) {
                setFragmentProgress(View.INVISIBLE);
                if (exception == null) {
                    final String uri = object.toUri().toString();
                    AlertDialogFragment.AlertDialogListener listener = new AlertDialogFragment.AlertDialogListener(){
                        @Override
                        public void onAlertFinished() {
                            Bundle args = new Bundle();
                            args.putString("object_uri", uri);
                            Fragment fragment = new KiiObjectAttachFileFragment();
                            fragment.setArguments(args);
                            FragmentTransaction ft = getFragmentManager()
                                    .beginTransaction();
                            ft.replace(R.id.mainFragment, fragment);
                            ft.addToBackStack(null);
                            ft.commit();
                        }
                        
                    };
                    DialogFragment newFragment = AlertDialogFragment
                            .newInstance(
                                    R.string.create_object,
                                    "Object creted successfully. \nNow lets attach a file to the object!",
                                    listener);
                    newFragment.show(getFragmentManager(), "dialog");
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

    void showAlert(String message) {
        DialogFragment newFragment = AlertDialogFragment.newInstance(
                R.string.operation_failed, message, null);
        newFragment.show(getFragmentManager(), "dialog");
    }
}
