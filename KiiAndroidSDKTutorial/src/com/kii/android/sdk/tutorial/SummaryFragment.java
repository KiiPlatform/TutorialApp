package com.kii.android.sdk.tutorial;

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
import android.widget.TextView;

public class SummaryFragment extends Fragment implements OnClickListener {
    private static final String TAG = "KiiObjectAttachFileActivity";
    private static final String SUMMARY_HTML_1 = "Lets give a look what we have done: "
            + "<ul>"
            + "<li>Signup/login to KiiCloud.</li>"
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

    private static final String SUMMARY_HTML_3 = "We have guides,"
            + " references, samples and tutorials "
            + "describes the details of developing Apps with KiiCloud."
            + "You can get these from ";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary, container,
                false);
        TextView tv2 = (TextView) view.findViewById(R.id.summary_textView_2);
        tv2.setText(Html.fromHtml(SUMMARY_HTML_1, null, new MyTagHandler()));
        Button b2 = (Button) view.findViewById(R.id.databrowse_btn);
        Button b3 = (Button) view.findViewById(R.id.next_btn);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        setPageImage(4);
        return view;
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
        DetailDialogResource resource = null;
        switch (id) {
        
        case R.id.databrowse_btn:
            titleId = R.string.how_to_browse_data;
            detail = Html.fromHtml(SUMMARY_HTML_2, null, new MyTagHandler()).toString();
            resource = new DetailDialogResource(getResources().getString(titleId), detail);
            break;
        case R.id.next_btn:
            titleId = R.string.how_to_move_forword;
            detail = Html.fromHtml(SUMMARY_HTML_3, null, new MyTagHandler()).toString();
            resource = new DetailDialogResource(getResources().getString(titleId), detail);
            resource.setDocsUrl(Util.getKiiDocsBaseUrl()+"/starts");
            break;
        default:
            throw new RuntimeException("Unknown button");
        }
        
        DialogFragment newFragment = DetailDialogFragment.newInstance(resource);
        newFragment.show(getFragmentManager(), "dialog");
    }
}
