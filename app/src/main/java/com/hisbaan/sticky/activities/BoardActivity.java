package com.hisbaan.sticky.activities;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;

import com.hisbaan.sticky.R;
import com.hisbaan.sticky.models.Canvas;

//import android.graphics.Canvas;

public class BoardActivity extends AppCompatActivity {

    Canvas canvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(BoardActivity.this);
            }
        });


        canvas = findViewById(R.id.canvas);
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.setBoardName(getIntent().getStringExtra("board_name") + ".txt");
        canvas.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

    }
}
