package com.umair.beacons_plugin.localizationactivity;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* According to the Apache license, please be warned that this file was modified in order
 * to be integrated in the Beacon Simulator project. */

/**
 * Created by Aleksander Mielczarek on 03.04.2016.
 */
public class LocalizationDelegate {

    private static final String KEY_ACTIVIY_LOCALE_CHANGED = "activity_locale_changed";

    // Boolean flag to check that activity was recreated from locale changed.
    private boolean isLocalizationChanged = false;

    // Prepare default language.
    private String currentLanguage = LanguageSetting.getDefaultLanguage();

    private final Activity activity;
    private final List<OnLocaleChangedListener> localeChangedListeners = new ArrayList<>();

    public LocalizationDelegate(Activity activity) {
        this.activity = activity;
    }

    public void addOnLocaleChengedListener(OnLocaleChangedListener onLocaleChangedListener) {
        localeChangedListeners.add(onLocaleChangedListener);
    }

    @SuppressWarnings("UnusedParameters")
    public void onCreate(Bundle savedInstanceState) {
        setupLanguage();
        checkBeforeLocaleChanging();
    }

    // Provide method to set application language by country name.
    public final void setLanguage(String language) {
        if (!isDuplicatedLanguageSetting(language)) {
            LanguageSetting.setLanguage(activity, language);
            notifyLanguageChanged();
        }
    }

    // Provide method to set application language by locale.
    public final void setLanguage(Locale locale) {
        setLanguage(locale.getLanguage());
    }

    public final void setDefaultLanguage(String language) {
        LanguageSetting.setDefaultLanguage(language);
    }

    public final void setDefaultLanguage(Locale locale) {
        LanguageSetting.setDefaultLanguage(locale.getLanguage());
    }

    // Get current language
    public final String getLanguage() {
        return LanguageSetting.getLanguage();
    }

    // Get current locale
    public final Locale getLocale() {
        return LanguageSetting.getLocale(activity);
    }

    // Check that bundle come from locale change.
    // If yes, bundle will obe remove and set boolean flag to "true".
    private void checkBeforeLocaleChanging() {
        boolean isLocalizationChanged = activity.getIntent().getBooleanExtra(KEY_ACTIVIY_LOCALE_CHANGED, false);
        if (isLocalizationChanged) {
            this.isLocalizationChanged = true;
            activity.getIntent().removeExtra(KEY_ACTIVIY_LOCALE_CHANGED);
        }
    }

    // Setup language to locale and language preference.
    // This method will called before onCreate.
    private void setupLanguage() {
        Locale locale = LanguageSetting.getLocale(activity);
        setupLocale(locale);
        currentLanguage = locale.getLanguage();
        LanguageSetting.setLanguage(activity, locale.getLanguage());
    }

    // Set locale configuration.
    private void setupLocale(Locale locale) {
        updateLocaleConfiguration(activity, locale);
    }

    private void updateLocaleConfiguration(Context context, Locale locale) {
        Configuration config = context.getResources().getConfiguration();
        config.locale = locale;
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        context.getResources().updateConfiguration(config, dm);
    }

    // Avoid duplicated setup
    private boolean isDuplicatedLanguageSetting(String language) {
        return language.toLowerCase(Locale.getDefault()).equals(LanguageSetting.getLanguage());
    }

    // Let's take it change! (Using recreate method that available on API 11 or more.
    private void notifyLanguageChanged() {
        for (OnLocaleChangedListener changedListener : localeChangedListeners) {
            changedListener.onBeforeLocaleChanged();
        }
        activity.getIntent().putExtra(KEY_ACTIVIY_LOCALE_CHANGED, true);
        callDummyActivity();
        activity.recreate();
    }

    // If activity is run to backstack. So we have to check if this activity is resume working.
    public void onResume() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                checkLocaleChange();
                checkAfterLocaleChanging();
            }
        });
    }

    // Check if locale has change while this activity was run to backstack.
    private void checkLocaleChange() {
        if (!LanguageSetting.getLanguage().toLowerCase(Locale.getDefault())
                .equals(currentLanguage.toLowerCase(Locale.getDefault()))) {
            callDummyActivity();
            activity.recreate();
        }
    }

    // Call override method if local is really changed
    private void checkAfterLocaleChanging() {
        if (isLocalizationChanged) {
            for (OnLocaleChangedListener listener : localeChangedListeners) {
                listener.onAfterLocaleChanged();
            }
            isLocalizationChanged = false;
        }
    }

    private void callDummyActivity() {
        BlankDummyActivity.launchActivity(activity);
    }
}
