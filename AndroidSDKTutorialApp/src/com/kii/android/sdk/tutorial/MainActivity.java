package com.kii.android.sdk.tutorial;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.kii.cloud.storage.Kii;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Kii.initialize(AppConstants.APP_ID, AppConstants.APP_KEY,
                AppConstants.APP_SITE);
        loadFragment(new LoginFragment());
    }
    
    private void loadFragment(Fragment fragment){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.mainFragment, fragment);
        ft.commit();
    }
}
