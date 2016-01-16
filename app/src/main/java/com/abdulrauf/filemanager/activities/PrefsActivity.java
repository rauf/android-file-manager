package com.abdulrauf.filemanager.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import com.abdulrauf.filemanager.R;

import java.util.List;

/**
 * Created by abdul on 15/1/16.
 */
public class PrefsActivity extends PreferenceActivity {


    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preference_headers, target);

    }

    @Override
    protected boolean isValidFragment(String fragmentName) {

        if(Header1.class.getName().equals(fragmentName))
            return true;

        return false;
    }

    public static class Header1 extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference_common);
        }
    }


}
