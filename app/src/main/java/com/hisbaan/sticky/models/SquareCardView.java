package com.hisbaan.sticky.models;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

/**
 * Custom cardview class that adds some functionality to an existing view to force it to be square.
 */
public class SquareCardView extends CardView {
    public SquareCardView(@NonNull Context context) {
        super(context);
    } //End constructor SquareCardView.

    public SquareCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    } //End constructor SquareCardView.

    public SquareCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    } //End constructor SquareCardView.

    /**
     * Makes the height equal to the width.
     *
     * @param widthMeasureSpec  Measured width.
     * @param heightMeasureSpec Measured height.
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

//        int width = getMeasuredWidth();
//        setMeasuredDimension(width, width);
    } //End method onMeasure.
} //End class SquareCardView.
