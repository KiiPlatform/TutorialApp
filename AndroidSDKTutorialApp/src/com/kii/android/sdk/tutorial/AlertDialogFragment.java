package com.kii.android.sdk.tutorial;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class AlertDialogFragment extends DialogFragment {
    public static final String TAG = "AlertDialogFragment";
    private AlertDialogListener listener = null;
    public static AlertDialogFragment newInstance(int title, String message,
            AlertDialogListener listener) {
        AlertDialogFragment frag = new AlertDialogFragment();
        frag.listener = listener;
        Bundle args = new Bundle();
        args.putInt("title", title);
        args.putString("message", message);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");
        String message = getArguments().getString("message");

        return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.launcher)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                    int whichButton) {
                                if(listener != null)
                                    listener.onAlertFinished();
                                else
                                    dialog.dismiss();
                            }
                        }).create();
    }

    static interface AlertDialogListener {
        public void onAlertFinished();
    }
}
