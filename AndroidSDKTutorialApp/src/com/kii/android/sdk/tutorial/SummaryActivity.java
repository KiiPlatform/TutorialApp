package com.kii.android.sdk.tutorial;

import java.util.Locale;
import org.xml.sax.XMLReader;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Html.TagHandler;
import android.text.util.Linkify;
import android.util.Log;
import android.widget.TextView;
public class SummaryActivity extends Activity {
    private static final String TAG = "KiiObjectAttachFileActivity";
    private static final String SUMMARY_HTML_1 = "Lets give a look what we have done: " +
            "<ul>" + 
                "<li>Created an object in KiiCloud </li>" +
                "<li>Attached body to the object</li>" + 
            "</ul>";
    
    private static final String SUMMARY_HTML_2 = "To check the details of created object:" +
            "<ul>" + 
                "<li>Go to http://developer.kii.com </li>" +
                "<li>Login using KiiCloud account</li>" +
                "<li>Open App console.</li>" +
                "<li>From left side pane click to the 'Objects'</li>" +
                "<li>Click to 'Data Browser' shows search box</li>" +
                "<li>Search by typing 'tutorial' shows the list of objects under the bucket</li>" +
            "</ul>";
    
    private static final String SUMMARY_HTML_3 = "To move forward:\n" +
                "We have guides, references, samples and tutorials " +
                "describes the details of developing Apps with KiiCloud." +
                "You can get these from ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        TextView tv1 = (TextView) findViewById(R.id.summary_textView_1);
        TextView tv2 = (TextView) findViewById(R.id.summary_textView_2);
        TextView tv3 = (TextView) findViewById(R.id.summary_textView_3);
        TextView tv4 = (TextView) findViewById(R.id.summary_textView_4);
        tv1.setText("Summary");
        tv2.setText(Html.fromHtml(SUMMARY_HTML_1, null, new MyTagHandler()));
        tv3.setText(Html.fromHtml(SUMMARY_HTML_2, null, new MyTagHandler()));
        Linkify.addLinks(tv3, Linkify.WEB_URLS);
        tv4.setText(SUMMARY_HTML_3+this.getKiiDocsUrl());
        Linkify.addLinks(tv4, Linkify.WEB_URLS);
    }

    private String getKiiDocsUrl() {
        Locale locale = Locale.getDefault();
        String language = locale.getDisplayLanguage();
        String langPath = (language == "jp" || language == "cn") ? language
                : "en";
        return String.format(
                "http://documentation.kii.com/%s/guides/starts",
                langPath);
    }

    class MyTagHandler implements TagHandler {
        boolean first = true;
        String parent = null;

        @Override
        public void handleTag(boolean opening, String tag, Editable output,
                XMLReader xmlReader) {
            Log.v(TAG, "Tag is: "+tag);
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
}
