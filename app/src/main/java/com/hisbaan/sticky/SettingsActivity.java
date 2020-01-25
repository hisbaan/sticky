package com.hisbaan.sticky;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.preference.*;

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
        Toolbar toolbar = findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitle(R.string.settings);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(SettingsActivity.this);
            }
        });

        //Put settings fragment into the settings activity
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();

    } //End Method onCreate.

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
    } //End Method onPause.

    /**
     * Holds the fragment where the preferences reside.
     */
    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            //TODO find a way to dynamically change the status bar text colour to match the theme.

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
            });
        } //End Method onCreatePreferences.
    } //End Class SettingsFragment.
} //End Class SettingsActivity.