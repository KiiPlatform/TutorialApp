package com.kii.android.sdk.tutorial;

import android.app.Activity;
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
import android.widget.Toast;

import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiBucket;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.callback.KiiObjectCallBack;
import com.kii.cloud.storage.exception.CloudExecutionException;

public class KiiObjectCreateFragment extends Fragment {
    private Activity activity;

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
                DialogFragment newFragment = DetailDialogFragment.newInstance(2);
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
                    showToast("Object created!");
                    Bundle args = new Bundle();
                    args.putString("object_uri", object.toUri().toString());
                    Fragment fragment = new KiiObjectAttachFileFragment();
                    fragment.setArguments(args);
                    FragmentTransaction ft = getFragmentManager()
                            .beginTransaction();
                    ft.replace(R.id.mainFragment, fragment);
                    ft.commit();
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
        Toast.makeText(this.activity, message, Toast.LENGTH_SHORT).show();
    }

    void showAlert(String message) {
        DialogFragment newFragment = AlertDialogFragment.newInstance(
                R.string.operation_failed, message);
        newFragment.show(getFragmentManager(), "dialog");
    }
}
