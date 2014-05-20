package com.kii.android.sdk.tutorial;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.kii.cloud.storage.exception.CloudExecutionException;

public class Util {
    public static String generateAlertMessage(CloudExecutionException ex) {
        StringBuffer sb = new StringBuffer();
        sb.append("KiiCloud returned error response.");
        sb.append("\n");
        sb.append("Http status: ");
        sb.append(ex.getStatus());
        sb.append("\n");
        if (!TextUtils.isEmpty(ex.getBody())) {
            sb.append("Reason: ");
            try {
                JSONObject body = new JSONObject(ex.getBody());
                sb.append(body.getString("error_description"));
            } catch (JSONException e) {
                // not happen.
            }
        }
        return sb.toString();
    }
}
