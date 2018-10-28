package com.github.characterdog.share_my_number;

import android.content.Context;
import android.preference.EditTextPreference;
import android.text.TextUtils;
import android.util.AttributeSet;

public class SummaryShowingEditTextPreference extends EditTextPreference {

    public SummaryShowingEditTextPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SummaryShowingEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SummaryShowingEditTextPreference(Context context) {
        super(context);
    }

    // According to ListPreference implementation
    @Override
    public CharSequence getSummary() {
        String text = getText();
        if (TextUtils.isEmpty(text)) {
            CharSequence hint = getEditText().getHint();
            if (!TextUtils.isEmpty(hint) || super.getSummary().equals("%s")) {
                return hint;
            } else {
                return super.getSummary();
            }
        } else {
            CharSequence summary = super.getSummary();
            if (!TextUtils.isEmpty(summary)) {
                return String.format(summary.toString(), text);
            } else {
                return summary;
            }
        }
    }
}