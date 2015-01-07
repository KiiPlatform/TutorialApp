package com.kii.android.sdk.tutorial;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class ProgressFragment extends Fragment {
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress, container,
                false);
        return view;
    }

    public void setProgressBarVisiblity(int v) {
        progressBar = (ProgressBar) getView().findViewById(R.id.progressBar);
        progressBar.setVisibility(v);
    }

    public void setPageImage(int page) {
        ImageView iv = (ImageView) getView().findViewById(R.id.page);
        int id;
        switch (page) {
        case 1:
            id = R.drawable.page1;
            break;
        case 2:
            id = R.drawable.page2;
            break;
        case 3:
            id = R.drawable.page3;
            break;
        case 4:
            id = R.drawable.page4;
            break;
        default:
            throw new RuntimeException("unknown page");
        }
        iv.setImageResource(id);
    }
}
