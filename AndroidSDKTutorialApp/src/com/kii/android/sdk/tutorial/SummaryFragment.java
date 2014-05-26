package com.kii.android.sdk.tutorial;

import java.util.Locale;

import org.xml.sax.XMLReader;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Html.TagHandler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class SummaryFragment extends Fragment implements OnClickListener {
    private static final String TAG = "KiiObjectAttachFileActivity";
    private static final String SUMMARY_HTML_1 = "Lets give a look what we have done: "
            + "<ul>"
            + "<li>Created an object in KiiCloud </li>"
            + "<li>Attached body to the object</li>" + "</ul>";

    private static final String SUMMARY_HTML_2 = "To check the details of created object:"
            + "<ul>"
            + "<li>Go to http://developer.kii.com </li>"
            + "<li>Login using KiiCloud account</li>"
            + "<li>Open App console.</li>"
            + "<li>From left side pane click to the 'Objects'</li>"
            + "<li>Click to 'Data Browser' shows search box</li>"
            + "<li>Search by typing 'tutorial' shows the list of objects under the bucket</li>"
            + "</ul>";

    private static final String SUMMARY_HTML_3 = "To move forward:\n"
            + "We have guides, references, samples and tutorials "
            + "describes the details of developing Apps with KiiCloud."
            + "You can get these from ";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary, container,
                false);
//        TextView tv2 = (TextView) view.findViewById(R.id.summary_textView_2);
//        TextView tv3 = (TextView) view.findViewById(R.id.summary_textView_3);
//        TextView tv4 = (TextView) view.findViewById(R.id.summary_textView_4);
//        tv2.setText(Html.fromHtml(SUMMARY_HTML_1, null, new MyTagHandler()));
//        tv3.setText(Html.fromHtml(SUMMARY_HTML_2, null, new MyTagHandler()));
//        Linkify.addLinks(tv3, Linkify.WEB_URLS);
//        tv4.setText(SUMMARY_HTML_3 + this.getKiiDocsUrl());
//        Linkify.addLinks(tv4, Linkify.WEB_URLS);
        Button b1 = (Button) view.findViewById(R.id.what_done_btn);
        Button b2 = (Button) view.findViewById(R.id.databrowse_btn);
        Button b3 = (Button) view.findViewById(R.id.next_btn);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        setPageImage(4);
        return view;
    }

    private String getKiiDocsUrl() {
        Locale locale = Locale.getDefault();
        String language = locale.getDisplayLanguage();
        String langPath = (language == "jp" || language == "cn") ? language
                : "en";
        return String.format("http://documentation.kii.com/%s/guides/starts",
                langPath);
    }

    void setPageImage(int page) {
        ProgressFragment fragment = (ProgressFragment) getFragmentManager()
                .findFragmentById(R.id.progressFragment);
        if (fragment != null && fragment.isInLayout()) {
            fragment.setPageImage(page);
        }
    }

    class MyTagHandler implements TagHandler {
        boolean first = true;
        String parent = null;

        @Override
        public void handleTag(boolean opening, String tag, Editable output,
                XMLReader xmlReader) {
            Log.v(TAG, "Tag is: " + tag);
            if (tag.equals("ul"))
                parent = "ul";
            if (tag.equals("li")) {
                if (first) {
                    output.append("\n\t•\t");
                    first = false;
                } else {
                    first = true;
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        int titleId;
        String detail;
        int id = v.getId();
        switch (id) {
        case R.id.what_done_btn:
            titleId = R.string.what_we_have_done;
            detail = Html.fromHtml(SUMMARY_HTML_1, null, new MyTagHandler()).toString();
            break;
        case R.id.databrowse_btn:
            titleId = R.string.how_to_browse_data;
            detail = Html.fromHtml(SUMMARY_HTML_2, null, new MyTagHandler()).toString();
            break;
        case R.id.next_btn:
            titleId = R.string.how_to_move_forword;
            detail = Html.fromHtml(SUMMARY_HTML_3, null, new MyTagHandler()).toString();
            break;
        default:
            throw new RuntimeException("Unknown button");
        }
        DetailDialogResource resource = new DetailDialogResource(getResources().getString(titleId), detail, 0);
        DialogFragment newFragment = DetailDialogFragment.newInstance(resource);
        newFragment.show(getFragmentManager(), "dialog");
    }
}
