package com.hisbaan.sticky.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.hisbaan.sticky.R;
import com.hisbaan.sticky.utils.Refactor;

import java.io.File;
import java.util.Objects;

/**
 * Creates the settings activity of the program where the user can manipulate the preferences.
 */
public class SettingsActivity extends AppCompatActivity {

    //Final variables that are used for SharedPreferences.
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String THEME = "theme";
    
    

    /**
     * Sets up variables and places the settings fragment inside of the activity.
     *
     * @param savedInstanceState Saved information that has been placed in it.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        //Setting up toolbar, adding icons, title, etc.
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(SettingsActivity.this);
            }
        });

        //Sets status bar colour based on current theme
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        View decorView = getWindow().getDecorView();
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                break;
        }

        //Put settings fragment into the settings activity
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();

    } //End method onCreate.

    /**
     * Saves the theme in the sharedPreference when the activity is paused.
     */
    @Override
    protected void onPause() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(THEME, AppCompatDelegate.getDefaultNightMode());
        editor.apply();

        super.onPause();
    } //End method onPause.

    /**
     * Holds the fragment where the preferences reside.
     */
    public static class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener {
        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            //TODO fix bug where the selected theme changes but is not applied.
            //When the theme preference is changed, applies the appropriate code.
            Preference themeList = findPreference("theme");
            assert themeList != null;
            themeList.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    switch ((String) newValue) {
                        case "light": //If the light theme is selected, enable the light theme.
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                            break;
                        case "dark": //If the dark theme is selected, enable the dark theme.
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                            break;
                        case "follow_system": //If the follow system theme is selected, allow the theme of the application to follow the system theme.
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                            break;
                        case "follow_battery_saver": //If the follow battery saver theme is selected, allow the theme of the application to follow the battery saver state.
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
                            break;
                    }

                    return true;
                }

                Preference.OnPreferenceClickListener onPreferenceClickListener = new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        String key = preference.getKey();
                        Toast.makeText(getContext(), key, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                };
            });

            Preference contact = findPreference("contact");
            Preference feedback = findPreference("feedback");
            Preference tips = findPreference("tips");
            Preference github = findPreference("github");
            Preference credits = findPreference("credits");

            assert contact != null;
            assert feedback != null;
            assert tips != null;
            assert github != null;
            assert credits != null;
            contact.setOnPreferenceClickListener(this);
            feedback.setOnPreferenceClickListener(this);
            tips.setOnPreferenceClickListener(this);
            github.setOnPreferenceClickListener(this);
            credits.setOnPreferenceClickListener(this);
        } //End method onCreatePreferences.

        /**
         * Method that runs when a preference is clicked on.
         * @param preference The preference that was clicked on.
         * @return Whether or not an action was taken.
         */
        @Override
        public boolean onPreferenceClick(Preference preference) {
            switch (preference.getKey()) {
                case "contact": //Opens an email send intent with the send to and subject lines filled.
                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{"hisbaan@gmail.com"});
                    email.putExtra(Intent.EXTRA_SUBJECT, "Sticky feedback/help");
                    email.setType("message/rfc822");
                    startActivity(Intent.createChooser(email, "Choose an email client"));
                    return true;
                case "feedback": //Opens a feedback google form.
                    Uri feedback = Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLScqm8FnLu_HyxQM1pKXTxy-C05B9tu9s3l3_F7HUeuGrEGFDA/viewform?usp=sf_link");
                    startActivity(new Intent(Intent.ACTION_VIEW, feedback));
                    return true;
                case "tips": //Opens the tips activity.
                    Intent tips = new Intent(requireContext(), TipsActivity.class);
                    startActivity(tips);
                    return true;
                case "github": //Opens the github page for the app.
                    Uri github = Uri.parse("https://github.com/hisbaan/sticky");
                    startActivity(new Intent(Intent.ACTION_VIEW, github));
                    return true;
                case "credits": //Opens the credits dialog.
                    AlertDialog.Builder alert = new AlertDialog.Builder(requireContext());
                    alert.setTitle("Credits");
                    alert.setMessage("• Image processing - OpenCV Android Library (v4.3.0)\n\n• Material Design Guidelines - Material.io");
                    alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();
                    return true;
            }
            return false;
        } //End method onPreferenceClick.
    } //End class SettingsFragment.
} //End class SettingsActivity.