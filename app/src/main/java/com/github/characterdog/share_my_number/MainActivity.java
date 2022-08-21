package com.github.characterdog.share_my_number;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.IdRes;

import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

import static com.github.characterdog.share_my_number.AboutActivity.EXTRA_PRIVACY_POLICY;
import static com.github.characterdog.share_my_number.PreferenceActivity.*;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    public static final int TAB_PRIVATE = 1;
    public static final int TAB_WORK = 2;
    public static final int TAB_OTHER = 3;

    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
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

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            tabLayout.getTabAt(TAB_PRIVATE - 1).setText(sharedPreferences.getString(PREF_PRIVATE_PROFILE, getString(R.string.tab_private)));
            tabLayout.getTabAt(TAB_WORK - 1).setText(sharedPreferences.getString(PREF_COMPANY_PROFILE, getString(R.string.tab_work)));
            tabLayout.getTabAt(TAB_OTHER - 1).setText(sharedPreferences.getString(PREF_OTHER_PROFILE, getString(R.string.tab_other)));
        } catch (Exception e) {
            Log.e(TAG, "Error setting tab title", e);
        }
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
        } else if (id == R.id.action_about) {
            Intent i = new Intent(this, AboutActivity.class);
            startActivity(i);
            return true;
        } else if (id == R.id.action_privacy) {
            Intent i = new Intent(this, AboutActivity.class);
            i.putExtra(EXTRA_PRIVACY_POLICY, true);
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
            int tab = getArguments() != null ? getArguments().getInt(ARG_SECTION_NUMBER, TAB_PRIVATE) : TAB_PRIVATE;
            if (tab == TAB_PRIVATE) {
                name = sharedPreferences.getString(PREF_PRIVATE_NAME, null);
                email = sharedPreferences.getString(PREF_PRIVATE_EMAIL, null);
                address = sharedPreferences.getString(PREF_PRIVATE_ADDRESS, null);
                title = sharedPreferences.getString(PREF_PRIVATE_TITLE, null);
                company = sharedPreferences.getString(PREF_PRIVATE_COMPANY, null);
                phoneNumber = sharedPreferences.getString(PREF_PRIVATE_PHONE, null);
                website = sharedPreferences.getString(PREF_PRIVATE_WEBSITE, null);
            } else if (tab == TAB_WORK) {
                name = sharedPreferences.getString(PREF_COMPANY_NAME, null);
                email = sharedPreferences.getString(PREF_COMPANY_EMAIL, null);
                address = sharedPreferences.getString(PREF_COMPANY_ADDRESS, null);
                title = sharedPreferences.getString(PREF_COMPANY_TITLE, null);
                company = sharedPreferences.getString(PREF_COMPANY_COMPANY, null);
                phoneNumber = sharedPreferences.getString(PREF_COMPANY_PHONE, null);
                website = sharedPreferences.getString(PREF_COMPANY_WEBSITE, null);
            } else {
                name = sharedPreferences.getString(PREF_OTHER_NAME, null);
                email = sharedPreferences.getString(PREF_OTHER_EMAIL, null);
                address = sharedPreferences.getString(PREF_OTHER_ADDRESS, null);
                title = sharedPreferences.getString(PREF_OTHER_TITLE, null);
                company = sharedPreferences.getString(PREF_OTHER_COMPANY, null);
                phoneNumber = sharedPreferences.getString(PREF_OTHER_PHONE, null);
                website = sharedPreferences.getString(PREF_OTHER_WEBSITE, null);
            }

            if (TextUtils.isEmpty(name) && TextUtils.isEmpty(email) && TextUtils.isEmpty(address)
                    && TextUtils.isEmpty(title) && TextUtils.isEmpty(company)
                    && TextUtils.isEmpty(phoneNumber) && TextUtils.isEmpty(website)) {
                setUiNoInformation(true, view);
                return;
            }
            setUiNoInformation(false, view);

            ImageView qrCode = view.findViewById(R.id.qr);
            qrCode.setImageDrawable(getResources().getDrawable(R.drawable.ic_hourglass_full_teal_24dp));

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

            new SetQrCodeTask().execute(new SetQrCodeTaskBundle(vCard, qrSize));
        }

        private void setUiNoInformation(boolean noInformation, View view) {
            view.findViewById(R.id.qr).setVisibility(noInformation ? View.GONE : View.VISIBLE);
            view.findViewById(R.id.cardView).setVisibility(noInformation ? View.GONE : View.VISIBLE);
            Button openSettingsButton = view.findViewById(R.id.open_settings);
            openSettingsButton.setVisibility(noInformation ? View.VISIBLE : View.GONE);
            if (noInformation) {
                openSettingsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getContext(), PreferenceActivity.class);
                        startActivity(i);
                    }
                });
            } else {
                openSettingsButton.setOnClickListener(null);
            }
        }

        private void setTextToTextViewOrHide(String value, @IdRes int id, View view) {
            TextView textView = view.findViewById(id);
            if (TextUtils.isEmpty(value)) {
                textView.setVisibility(View.GONE);
            } else {
                textView.setText(value);
            }
        }

        private class SetQrCodeTaskBundle {
            private VCard mVCard;
            private int mQrSize;

            SetQrCodeTaskBundle(VCard vCard, int qrSize) {
                mVCard = vCard;
                mQrSize = qrSize;
            }

            VCard getVCard() {
                return mVCard;
            }

            int getQrSize() {
                return mQrSize;
            }
        }

        private class SetQrCodeTask extends AsyncTask<SetQrCodeTaskBundle, Void, Bitmap> {
            private final String TAG = SetQrCodeTask.class.getSimpleName();

            @Override
            protected Bitmap doInBackground(SetQrCodeTaskBundle... setQrCodeTaskBundles) {
                Log.d(TAG, "Generate QR code");
                return QRCode
                        .from(setQrCodeTaskBundles[0].getVCard())
                        .withColor(0xFF000000, 0xFFEEEEEE)
                        .withSize(setQrCodeTaskBundles[0].getQrSize(), setQrCodeTaskBundles[0].getQrSize())
                        .withHint(EncodeHintType.CHARACTER_SET, "UTF-8")
                        .bitmap();
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                Log.d(TAG, "Set QR code");
                View view = getView();
                if (view == null) {
                    Log.e(TAG, "View is null");
                    return;
                }
                ImageView qrCode = getView().findViewById(R.id.qr);
                qrCode.setImageBitmap(bitmap);
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
            return ContactInfoFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
