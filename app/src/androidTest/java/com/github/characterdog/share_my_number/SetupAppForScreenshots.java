package com.github.characterdog.share_my_number;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.github.characterdog.share_my_number.PreferenceActivity.*;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SetupAppForScreenshots {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void initSettings() {
        Context context = InstrumentationRegistry.getTargetContext();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit()
                .putString(PREF_PRIVATE_NAME, context.getString(R.string.screenshots_private_name))
                .putString(PREF_PRIVATE_EMAIL, context.getString(R.string.screenshots_private_email))
                .putString(PREF_PRIVATE_ADDRESS, context.getString(R.string.screenshots_private_address))
                .putString(PREF_PRIVATE_TITLE, context.getString(R.string.screenshots_private_title))
                .putString(PREF_PRIVATE_COMPANY, context.getString(R.string.screenshots_private_company))
                .putString(PREF_PRIVATE_PHONE, context.getString(R.string.screenshots_private_phone))
                .putString(PREF_PRIVATE_WEBSITE, context.getString(R.string.screenshots_private_website))

                .putString(PREF_COMPANY_NAME, context.getString(R.string.screenshots_company_name))
                .putString(PREF_COMPANY_EMAIL, context.getString(R.string.screenshots_company_email))
                .putString(PREF_COMPANY_ADDRESS, context.getString(R.string.screenshots_company_address))
                .putString(PREF_COMPANY_TITLE, context.getString(R.string.screenshots_company_title))
                .putString(PREF_COMPANY_COMPANY, context.getString(R.string.screenshots_company_company))
                .putString(PREF_COMPANY_PHONE, context.getString(R.string.screenshots_company_phone))
                .putString(PREF_COMPANY_WEBSITE, context.getString(R.string.screenshots_company_website))
                .apply();
    }

    @Test
    public void mainActivityTest() {
        ViewInteraction tabView = onView(withContentDescription("Company"));
        tabView.perform(click());

        ViewInteraction actionMenuItemView = onView(withId(R.id.action_settings));
        actionMenuItemView.perform(click());
    }
}
