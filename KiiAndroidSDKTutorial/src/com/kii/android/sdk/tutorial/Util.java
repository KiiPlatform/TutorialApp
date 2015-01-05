package com.kii.android.sdk.tutorial;

import java.util.Locale;

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
                if(body.has("error_description"))
                    sb.append(body.getString("error_description"));
                else if (body.has("message"))
                    sb.append(body.getString("message"));
                else
                    sb.append(ex.getLocalizedMessage());
            } catch (JSONException e) {
                // not happen.
            }
        }
        return sb.toString();
    }

    public static String getKiiDocsBaseUrl() {
        Locale locale = Locale.getDefault();
        String lan = locale.getDisplayLanguage();
        String langPath = (lan == "jp" || lan == "cn") ? lan : "en";
        return "http://documentation.kii.com/" + langPath;
    }
}
