package com.kii.android.sdk.tutorial;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kii.cloud.storage.KiiUser;
import com.kii.cloud.storage.callback.KiiUserCallBack;
import com.kii.cloud.storage.exception.CloudExecutionException;

public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";
    private TextView mUsernameField;
    private TextView mPasswordField;
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        mUsernameField = (EditText) view.findViewById(R.id.username_field);
        mPasswordField = (EditText) view.findViewById(R.id.password_field);
        mPasswordField
                .setTransformationMethod(new PasswordTransformationMethod());
        mPasswordField.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        Button loginBtn = (Button) view.findViewById(R.id.login_button);
        Button signupBtn = (Button) view.findViewById(R.id.signup_button);
        loginBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginButtonClicked(v);
            }
        });
        signupBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignupButtonClicked(v);
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
        activity = null;
    }

    void setFragmentProgress(int v) {
        ProgressFragment fragment = (ProgressFragment) getFragmentManager()
                .findFragmentById(R.id.progressFragment);
        if (fragment != null && fragment.isInLayout()) {
            fragment.setProgressBarVisiblity(v);
        }
    }

    public void onLoginButtonClicked(View v) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        setFragmentProgress(View.VISIBLE);
        Log.v(TAG, "login clicked");
        String username = mUsernameField.getText().toString();
        String password = mPasswordField.getText().toString();
        try {
            KiiUser.logIn(callback, username, password);
        } catch (Exception e) {
            setFragmentProgress(View.INVISIBLE);
            showAlert(e.getLocalizedMessage());
        }
    }

    private void showToast(String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    void showAlert(String message) {
        DialogFragment newFragment = AlertDialogFragment.newInstance(
                R.string.operation_failed, message);
        newFragment.show(getFragmentManager(), "dialog");
    }

    public void onSignupButtonClicked(View v) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        setFragmentProgress(View.VISIBLE);

        String username = mUsernameField.getText().toString();
        String password = mPasswordField.getText().toString();
        try {
            KiiUser user = KiiUser.createWithUsername(username);
            user.register(callback, password);
        } catch (Exception e) {
            setFragmentProgress(View.INVISIBLE);
            showAlert(e.getLocalizedMessage());
        }
    }

    KiiUserCallBack callback = new KiiUserCallBack() {
        @Override
        public void onLoginCompleted(int token, KiiUser user, Exception e) {
            setFragmentProgress(View.INVISIBLE);
            if (e == null) {
                showToast("User logged-in!");
                loadFragment(new KiiObjectCreateFragment());
            } else {
                if (e instanceof CloudExecutionException)
                    showAlert(Util
                            .generateAlertMessage((CloudExecutionException) e));
                else
                    showAlert(e.getLocalizedMessage());
            }
        }

        @Override
        public void onRegisterCompleted(int token, KiiUser user, Exception e) {
            setFragmentProgress(View.INVISIBLE);
            if (e == null) {
                showToast("User registered!");
                loadFragment(new KiiObjectCreateFragment());
            } else {
                if (e instanceof CloudExecutionException)
                    showAlert(Util
                            .generateAlertMessage((CloudExecutionException) e));
                else
                    showAlert(e.getLocalizedMessage());
            }
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.mainFragment, fragment);
        ft.commit();
    }

}
