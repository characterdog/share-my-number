package com.github.characterdog.share_my_number;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.EncodeHintType;

import net.glxn.qrgen.android.QRCode;
import net.glxn.qrgen.core.scheme.VCard;

import static com.github.characterdog.share_my_number.PreferenceActivity.*;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate()");
        setContentView(R.layout.activity_tabbed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent i = new Intent(this, PreferenceActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class ContactInfoFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String TAG = ContactInfoFragment.class.getSimpleName();
        private static final String ARG_SECTION_NUMBER = "section_number";
        public static final int TAB_PRIVATE = 1;
        public static final int TAB_CORP = 2;

        public ContactInfoFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static ContactInfoFragment newInstance(int sectionNumber) {
            ContactInfoFragment fragment = new ContactInfoFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Log.i(TAG, "onCreateView()");
            return inflater.inflate(R.layout.fragment_tabbed, container, false);
        }

        @Override
        public void onResume() {
            Log.i(TAG, "onResume()");
            super.onResume();
            View view = getView();
            if (view == null) {
                Log.e(TAG, "view is null!");
                return;
            }
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            String name;
            String email;
            String address;
            String title;
            String company;
            String phoneNumber;
            String website;
            if (getArguments().getInt(ARG_SECTION_NUMBER) == TAB_PRIVATE) {
                name = sharedPreferences.getString(PREF_PRIVATE_NAME, null);
                email = sharedPreferences.getString(PREF_PRIVATE_EMAIL, null);
                address = sharedPreferences.getString(PREF_PRIVATE_ADDRESS, null);
                title = sharedPreferences.getString(PREF_PRIVATE_TITLE, null);
                company = sharedPreferences.getString(PREF_PRIVATE_COMPANY, null);
                phoneNumber = sharedPreferences.getString(PREF_PRIVATE_PHONE, null);
                website = sharedPreferences.getString(PREF_PRIVATE_WEBSITE, null);
            } else {
                name = sharedPreferences.getString(PREF_COMPANY_NAME, null);
                email = sharedPreferences.getString(PREF_COMPANY_EMAIL, null);
                address = sharedPreferences.getString(PREF_COMPANY_ADDRESS, null);
                title = sharedPreferences.getString(PREF_COMPANY_TITLE, null);
                company = sharedPreferences.getString(PREF_COMPANY_COMPANY, null);
                phoneNumber = sharedPreferences.getString(PREF_COMPANY_PHONE, null);
                website = sharedPreferences.getString(PREF_COMPANY_WEBSITE, null);
            }

            ImageView qrCode = view.findViewById(R.id.qr);
            if (TextUtils.isEmpty(name) && TextUtils.isEmpty(email) && TextUtils.isEmpty(address)
                    && TextUtils.isEmpty(title) && TextUtils.isEmpty(company)
                    && TextUtils.isEmpty(phoneNumber) && TextUtils.isEmpty(website)) {
                qrCode.setVisibility(View.GONE);
                view.findViewById(R.id.cardView).setVisibility(View.GONE);
                Button openSettingsButton = view.findViewById(R.id.open_settings);
                openSettingsButton.setVisibility(View.VISIBLE);
                openSettingsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getContext(), PreferenceActivity.class);
                        startActivity(i);
                    }
                });
                return;
            }

            if (TextUtils.isEmpty(name)) {
                name = getString(R.string.no_name);
            }

            VCard vCard = new VCard(name)
                    .setEmail(email)
                    .setAddress(address)
                    .setTitle(title)
                    .setCompany(company)
                    .setPhoneNumber(phoneNumber)
                    .setWebsite(website);

            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            view.getHeight();
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int qrSize = (int) (0.8 * (double) Math.min(size.x, size.y));

            setTextToTextViewOrHide(name, R.id.name, view);
            setTextToTextViewOrHide(email, R.id.email, view);
            setTextToTextViewOrHide(address, R.id.address, view);
            setTextToTextViewOrHide(title, R.id.title, view);
            setTextToTextViewOrHide(company, R.id.company, view);
            setTextToTextViewOrHide(phoneNumber, R.id.phoneNumber, view);
            setTextToTextViewOrHide(website, R.id.website, view);

            Bitmap myBitmap = QRCode.from(vCard).withColor(0xFF000000, 0x00000000)
                    .withSize(qrSize, qrSize).withHint(EncodeHintType.CHARACTER_SET, "UTF-8")
                    .bitmap();
            qrCode.setImageBitmap(myBitmap);
        }

        private void setTextToTextViewOrHide(String value, @IdRes int id, View view) {
            TextView textView = view.findViewById(id);
            if (TextUtils.isEmpty(value)) {
                textView.setVisibility(View.GONE);
            } else {
                textView.setText(value);
            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a ContactInfoFragment (defined as a static inner class below).
            return ContactInfoFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }
}
