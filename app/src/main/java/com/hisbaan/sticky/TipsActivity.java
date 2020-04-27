package com.hisbaan.sticky;

import android.content.res.Configuration;
import android.os.Build;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

public class TipsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);

        //Setting up toolbar, adding icons, title, etc.
        Toolbar toolbar = findViewById(R.id.toolbar_tips);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(TipsActivity.this);
            }
        });
        setSupportActionBar(toolbar);

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
    }
}
