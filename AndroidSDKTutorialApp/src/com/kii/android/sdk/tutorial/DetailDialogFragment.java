package com.kii.android.sdk.tutorial;

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
    private DetailDialogResource resource;

    public static DetailDialogFragment newInstance(DetailDialogResource resource) {
        DetailDialogFragment frag = new DetailDialogFragment();
        frag.resource = resource;
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        TextView tv = (TextView)v.findViewById(R.id.detail_textview);
        //int page = getArguments().getInt("page");
        ImageView iv = (ImageView)v.findViewById(R.id.storage);
        if(resource.getImageId() > 0)
            iv.setImageResource(resource.getImageId());
        String text = resource.getDetail();
        String docUrl = resource.getDocsUrl();
        if( docUrl!= null)
            text = text+" "+docUrl;
        ((TextView) tv).setText(text);
        Linkify.addLinks((TextView) tv, Linkify.WEB_URLS);
        getDialog().setTitle(resource.getTitle());

        // Watch for button clicks.
        Button button = (Button) v.findViewById(R.id.detail_ok_button);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        return v;
    }
}
