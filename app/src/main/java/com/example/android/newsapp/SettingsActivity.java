package com.example.android.newsapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class ArticlePreferenceFragment extends PreferenceFragment implements
            Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            final com.example.android.newsapp.DatePreference dp = (com.example.android.newsapp.DatePreference) findPreference("keyname");
            dp.setText("2016-11-17");
            dp.setSummary("2016-11-17");
            dp.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference,Object newValue) {
                    //your code to change values.
                    dp.setSummary((String) newValue);
                    return true;
                }
            });

            Preference fromDate = findPreference(getString(R.string.settings_from_date_key));
            bindPreferenceSummaryToValue(fromDate);

            Preference sort = findPreference(getString(R.string.settings_sort_key));
            bindPreferenceSummaryToValue(sort);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {

            String sValue = newValue.toString();
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(sValue);
                if (prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            } else {
                preference.setSummary(sValue);
            }
            return true;
        }

        private void bindPreferenceSummaryToValue(Preference pref) {
            pref.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(pref.getContext());
            String preferenceString = preferences.getString(pref.getKey(), "");
            onPreferenceChange(pref, preferenceString);

        }
    }


}
