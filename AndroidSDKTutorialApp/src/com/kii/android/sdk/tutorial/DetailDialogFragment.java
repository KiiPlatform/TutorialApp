package com.kii.android.sdk.tutorial;

import java.util.Locale;

import android.app.DialogFragment;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailDialogFragment extends DialogFragment {
    public static final String TAG = "DetailDialogFragment";

    public static DetailDialogFragment newInstance(int page) {
        DetailDialogFragment frag = new DetailDialogFragment();
        Bundle args = new Bundle();
        args.putInt("page", page);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        TextView tv = (TextView)v.findViewById(R.id.detail_textview);
        int page = getArguments().getInt("page");
        ImageView iv = (ImageView)v.findViewById(R.id.storage);
        if(page == 2) {
            iv.setImageResource(R.drawable.datastore);
        } else if (page == 3) {
            iv.setImageResource(R.drawable.bodyattach);
        }
        String text = getResources().getString(
                getDetailId(page))
                + " " + getKiiDocsUrl();
        ((TextView) tv).setText(text);
        Linkify.addLinks((TextView) tv, Linkify.WEB_URLS);
        getDialog().setTitle(getTitleId(page));

        // Watch for button clicks.
        Button button = (Button) v.findViewById(R.id.detail_ok_button);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        return v;
    }

    private String getKiiDocsUrl() {
        Locale locale = Locale.getDefault();
        String language = locale.getDisplayLanguage();
        String langPath = (language == "jp" || language == "cn") ? language
                : "en";
        return String.format(
                "http://documentation.kii.com/%s/starts",
                langPath);
    }

    private int getTitleId(int page) {
        int id;
        switch (page) {
        case 1:
            id = R.string.login_detail;
            break;
        case 2:
            id = R.string.kiiobject_detail;
            break;
        case 3:
            id = R.string.attach_file_detail;
            break;
        default:
            throw new RuntimeException("unknown page");
        }
        return id;
    }

    private int getDetailId(int page) {
        int id;
        switch (page) {
        case 1:
            id = R.string.login_description;
            break;
        case 2:
            id = R.string.create_object_description;
            break;
        case 3:
            id = R.string.attach_body_description;
            break;
        default:
            throw new RuntimeException("unknown page");
        }
        return id;
    }
}
