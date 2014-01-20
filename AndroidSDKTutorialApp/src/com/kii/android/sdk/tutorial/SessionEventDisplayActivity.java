package com.kii.android.sdk.tutorial;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TextView;

public class SessionEventDisplayActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_event);
        final String jsonStr = getIntent().getStringExtra("eventJson");
        TableLayout table = (TableLayout) findViewById(R.id.showview);
        setTextToTable(jsonStr, this, table);
    }

    @SuppressWarnings("deprecation")
    private void setTextToTable(String jsonStr, Activity activity,
            TableLayout table) {
        JSONObject json = null;
        try {
            json = new JSONObject(jsonStr);

            @SuppressWarnings("unchecked")
            Iterator<String> keys = json.keys();
            String hkey;
            String hvalue;

            while (keys.hasNext()) {
                hkey = keys.next();
                hvalue = json.getString(hkey);

                android.widget.TableRow row = new android.widget.TableRow(
                        activity);
                row.setLayoutParams(new TableLayout.LayoutParams(
                        LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                row.setGravity(Gravity.CENTER);
                table.addView(row);

                TextView tvkey = new TextView(activity);
                tvkey.setText(hkey + "  ");
                row.addView(tvkey);

                TextView tvvalue = new TextView(activity);
                tvvalue.setText(hvalue);
                row.addView(tvvalue);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
