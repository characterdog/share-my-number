package com.github.characterdog.share_my_number;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;

public class PreferenceActivity extends AppCompatPreferenceActivity {
    public final static String PREF_PRIVATE_NAME = "name_private";
    public final static String PREF_PRIVATE_EMAIL = "email_private";
    public final static String PREF_PRIVATE_ADDRESS = "address_private";
    public final static String PREF_PRIVATE_TITLE = "title_private";
    public final static String PREF_PRIVATE_COMPANY = "company_private";
    public final static String PREF_PRIVATE_PHONE = "phoneNumber_private";
    public final static String PREF_PRIVATE_WEBSITE = "website_private";

    public final static String PREF_COMPANY_NAME = "name_company";
    public final static String PREF_COMPANY_EMAIL = "email_company";
    public final static String PREF_COMPANY_ADDRESS = "address_company";
    public final static String PREF_COMPANY_TITLE = "title_company";
    public final static String PREF_COMPANY_COMPANY = "company_company";
    public final static String PREF_COMPANY_PHONE = "phoneNumber_company";
    public final static String PREF_COMPANY_WEBSITE = "website_company";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        addPreferencesFromResource(R.xml.preferences);
    }
}
