package com.hisbaan.sticky.models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.hisbaan.sticky.R;
import com.hisbaan.sticky.activities.CropActivity;

/**
 * Canvas that paints the lines between the points on the crop menu.
 */
public class Canvas extends View {
    Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    float x1;
    float y1;
    float x2;
    float y2;
    float x3;
    float y3;
    float x4;
    float y4;

    /**
     * Constructor.
     *
     * @param context Context of creation location.
     */
    public Canvas(Context context) {
        super(context);
    } //End constructor canvas

    /**
     * Constructor.
     *
     * @param context Context of creation location.
     * @param attrs   Attributes.
     */
    public Canvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    } //End constructor canvas

    /**
     * Constructor.
     *
     * @param context      Context of creation location.
     * @param attrs        Attributes.
     * @param defStyleAttr More attributes.
     */
    public Canvas(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    } //End constructor canvas

    /**
     * Constructor.
     *
     * @param context      Context of creation location.
     * @param attrs        Attributes.
     * @param defStyleAttr More attributes.
     * @param defStyleRes  More attributes.
     */
    public Canvas(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    } //End constructor canvas

    /**
     * Draw method that draws the lines.
     *
     * @param canvas Canvas that is to be drawn on.
     */
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(android.graphics.Canvas canvas) {
        super.onDraw(canvas);

        linePaint.setColor(ContextCompat.getColor(getContext(), R.color.colorAccentLight));
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(10);
        canvas.drawLine(x1, y1, x2, y2, linePaint);
        canvas.drawLine(x2, y2, x4, y4, linePaint);
        canvas.drawLine(x4, y4, x3, y3, linePaint);
        canvas.drawLine(x3, y3, x1, y1, linePaint);
    } //End method onDraw.

    /**
     * Method that sets the initial location of the lines.
     *
     * @param screenX Width of the screen.
     * @param screenY Height of the screen.
     */
    public void initialSetup(float screenX, float screenY) {

        this.x1 = (float) (screenX * 0.14);
        this.y1 = (float) (screenY * 0.286);
        this.x2 = (float) (screenX * 0.865);
        this.y2 = (float) (screenY * 0.286);
        this.x3 = (float) (screenX * 0.14);
        this.y3 = (float) (screenY * 0.7045);
        this.x4 = (float) (screenX * 0.865);
        this.y4 = (float) (screenY * 0.7045);
    } //End method initialSetup.

    /**
     * Method to update the points of from where to where the line should be drawn.
     *
     * @param x1 A point.
     * @param y1 A point.
     * @param x2 A point.
     * @param y2 A point.
     * @param x3 A point.
     * @param y3 A point.
     * @param x4 A point.
     * @param y4 A point.
     */
    public void updatePoints(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {

        this.x1 = x1 + CropActivity.offCenterAdjustment;
        this.y1 = y1 + CropActivity.offCenterAdjustment;
        this.x2 = x2 + CropActivity.offCenterAdjustment;
        this.y2 = y2 + CropActivity.offCenterAdjustment;
        this.x3 = x3 + CropActivity.offCenterAdjustment;
        this.y3 = y3 + CropActivity.offCenterAdjustment;
        this.x4 = x4 + CropActivity.offCenterAdjustment;
        this.y4 = y4 + CropActivity.offCenterAdjustment;
    } //End method updatePoints.
} //End class Canvas.