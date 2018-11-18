package com.github.characterdog.share_my_number;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.danielstone.materialaboutlibrary.ConvenienceBuilder;
import com.danielstone.materialaboutlibrary.MaterialAboutFragment;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutItemOnClickAction;
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;
import com.danielstone.materialaboutlibrary.util.OpenSourceLicense;

public class AboutActivity extends AppCompatActivity {
    public final static String EXTRA_PRIVACY_POLICY = "privacy_policy";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        if (savedInstanceState == null) {
            Fragment f;
            if (getIntent().getBooleanExtra(EXTRA_PRIVACY_POLICY, false)) {
                f = new ShowPrivacyPolicyFragment();
            } else {
                f = new MyMaterialAboutFragment();
            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.about_container, f)
                    .commit();
        }
    }

    public static class ShowPrivacyPolicyFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.privacy_policy_fragment, container, false);
        }

        @Override
        public void onResume() {
            super.onResume();
            TextView privacyPolicy = getView().findViewById(R.id.privacy_policy);
            privacyPolicy.setText(getString(R.string.privacy_policy_content));
        }
    }

    public static class MyMaterialAboutFragment extends MaterialAboutFragment {
        private static String PLAY_STORE_LINK;

        @Override
        protected MaterialAboutList getMaterialAboutList(final Context activityContext) {
            PLAY_STORE_LINK = "https://play.google.com/store/apps/details?id=" + activityContext.getPackageName();
            MaterialAboutCard.Builder firstCard = new MaterialAboutCard.Builder();
            firstCard.addItem(new MaterialAboutTitleItem.Builder()
                    .text(getString(R.string.app_name))
                    .icon(R.mipmap.ic_launcher)
                    .build());
            firstCard.addItem(new MaterialAboutActionItem.Builder()
                    .text(activityContext.getString(R.string.author))
                    .subText("CharacterDog")
                    .icon(R.drawable.ic_person_teal_24dp)
                    .build());
            firstCard.addItem(new MaterialAboutActionItem.Builder()
                    .text(activityContext.getString(R.string.help_translating_this_app))
                    .icon(R.drawable.ic_language_teal_24dp)
                    .setOnClickAction(new MaterialAboutItemOnClickAction() {
                        @Override
                        public void onClick() {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://crowdin.com/project/share-my-number"));
                            startActivity(intent);
                        }
                    })
                    .build());
            firstCard.addItem(new MaterialAboutActionItem.Builder()
                    .text(activityContext.getString(R.string.rate_in_play_store))
                    .icon(R.drawable.ic_star_teal_24dp)
                    .setOnClickAction(new MaterialAboutItemOnClickAction() {
                        @Override
                        public void onClick() {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_LINK));
                            startActivity(intent);
                        }
                    })
                    .build());
            firstCard.addItem(new MaterialAboutActionItem.Builder()
                    .text(activityContext.getString(R.string.share_with_friends))
                    .icon(R.drawable.ic_share_teal_24dp)
                    .setOnClickAction(new MaterialAboutItemOnClickAction() {
                        @Override
                        public void onClick() {
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT,
                                    String.format(activityContext.getString(R.string.check_out),
                                            getString(R.string.app_name), PLAY_STORE_LINK));
                            sendIntent.setType("text/plain");
                            startActivity(sendIntent);
                        }
                    })
                    .build());

            MaterialAboutCard.Builder legalCard = new MaterialAboutCard.Builder();
            legalCard.title(activityContext.getString(R.string.legal));
            legalCard.addItem(new MaterialAboutActionItem.Builder()
                    .text(activityContext.getString(R.string.license))
                    .subText("GNU General Public License v3.0")
                    .icon(R.drawable.ic_account_balance_teal_24dp)
                    .setOnClickAction(new MaterialAboutItemOnClickAction() {
                        @Override
                        public void onClick() {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/characterdog/share-my-number/blob/master/LICENSE"));
                            startActivity(intent);
                        }
                    })
                    .build());
            legalCard.addItem(new MaterialAboutActionItem.Builder()
                    .text(activityContext.getString(R.string.privacy_policy))
                    .icon(R.drawable.ic_security_teal_24dp)
                    .setOnClickAction(new MaterialAboutItemOnClickAction() {
                        @Override
                        public void onClick() {
                            ShowPrivacyPolicyFragment f = new ShowPrivacyPolicyFragment();
                            getActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.about_container, f)
                                    .addToBackStack(null)
                                    .commit();
                        }
                    })
                    .build());

            Drawable codeDrawable = activityContext.getResources().getDrawable(R.drawable.ic_code_teal_24dp);

            MaterialAboutCard materialAboutLibraryLicenseCard = ConvenienceBuilder
                    .createLicenseCard(activityContext, codeDrawable,
                            "material-about-library", "2016", "Daniel Stone",
                            OpenSourceLicense.APACHE_2);

            MaterialAboutCard qrGenLicenseCard = ConvenienceBuilder
                    .createLicenseCard(activityContext, codeDrawable,
                            "QRGen", "2018", "Ken Gullaksen",
                            OpenSourceLicense.APACHE_2);

            MaterialAboutCard iconLicenseCard = ConvenienceBuilder
                    .createLicenseCard(activityContext, codeDrawable,
                            "Material Design icons", "2018", "Google",
                            OpenSourceLicense.APACHE_2);

            return new MaterialAboutList.Builder()
                    .addCard(firstCard.build())
                    .addCard(legalCard.build())
                    .addCard(materialAboutLibraryLicenseCard)
                    .addCard(qrGenLicenseCard)
                    .addCard(iconLicenseCard)
                    .build();
        }

        @Override
        protected int getTheme() {
            return R.style.AppTheme_MaterialAboutActivity_Fragment;
        }
    }
}