package com.hisbaan.sticky.models;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.hisbaan.sticky.R;

public class Canvas extends View {
    Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    String boardName;

    public Canvas(Context context) {
        super(context);
    }

    public Canvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Canvas(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Canvas(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(android.graphics.Canvas canvas) {
        super.onDraw(canvas);

        textPaint.setTextSize(100);
        textPaint.setColor(Color.WHITE);

        canvas.drawText(boardName, 100, 100, textPaint);
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }
}
