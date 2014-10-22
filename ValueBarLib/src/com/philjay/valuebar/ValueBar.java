
package com.philjay.valuebar;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * ValueBar is a cusom View for displaying values in an edgy bar.
 * 
 * @author Philipp Jahoda
 */
public class ValueBar extends View implements AnimatorUpdateListener {

    /** minimum value the bar can display */
    private float mMinVal = 0f;

    /** maximum value the bar can display */
    private float mMaxVal = 100f;

    /** the value the bar currently displays */
    private float mValue = 75f;

    private RectF mBar;

    private Paint mBarPaint;
    private Paint mBorderPaint;

    private ObjectAnimator mAnimator;

    private boolean mDrawBorder = true;

    private BarColorFormatter mColorFormatter;

    public ValueBar(Context context) {
        super(context);
        init();
    }

    public ValueBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ValueBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * Do all preparations.
     */
    private void init() {
        mBar = new RectF();
        mBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBarPaint.setStyle(Paint.Style.FILL);

        mColorFormatter = new DefaultColorFormatter(Color.rgb(39, 140, 230));

        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(2f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        prepareBarSize();

        mBarPaint.setColor(mColorFormatter.getColor(mValue));

        // draw the value-bar
        canvas.drawRect(mBar, mBarPaint);

        // draw the border
        if (mDrawBorder)
            canvas.drawRect(0, 0, getWidth(), getHeight(), mBorderPaint);
    }

    /**
     * Prepares the bar according to the current value.
     */
    private void prepareBarSize() {

        float length = ((float) getWidth() / (mMaxVal - mMinVal)) * mValue;

        mBar.set(0, 0, length, getHeight());
    }

    /**
     * Sets the minimum and maximum value the bar can display.
     * 
     * @param min
     * @param max
     */
    public void setMinMax(float min, float max) {
        mMaxVal = max;
        mMinVal = min;
    }

    /**
     * Sets the actual value the bar displays.
     * 
     * @param value
     */
    public void setValue(float value) {
        mValue = value;
    }

    /**
     * Returns the currently displayed value.
     * 
     * @return
     */
    public float getValue() {
        return mValue;
    }

    /**
     * Returns the bar that represents the value.
     * 
     * @return
     */
    public RectF getBar() {
        return mBar;
    }

    /**
     * Animates the bar from a specific value to a specific value.
     * 
     * @param from
     * @param to
     * @param durationMillis
     */
    public void animate(float from, float to, int durationMillis) {
        mValue = from;
        mAnimator = ObjectAnimator.ofFloat(this, "value", mValue, to);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.setDuration(durationMillis);
        mAnimator.addUpdateListener(this);
        mAnimator.start();
    }

    /**
     * Animates the bar up from it's minimum value to the currently set value.
     * 
     * @param durationMillis
     */
    public void animateUp(int durationMillis) {

        float save = mValue;

        mValue = mMinVal;
        mAnimator = ObjectAnimator.ofFloat(this, "value", mValue, save);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.setDuration(durationMillis);
        mAnimator.addUpdateListener(this);
        mAnimator.start();
    }

    /**
     * Animates the bar down from it's current value to the minimum value.
     * 
     * @param durationMillis
     */
    public void animateDown(int durationMillis) {

        mAnimator = ObjectAnimator.ofFloat(this, "value", mValue, mMinVal);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.setDuration(durationMillis);
        mAnimator.addUpdateListener(this);
        mAnimator.start();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator va) {
        invalidate();
    }

    /**
     * Set this to true to enable drawing the border around the bar, or false to
     * disable it.
     * 
     * @param enabled
     */
    public void setDrawBorder(boolean enabled) {
        mDrawBorder = enabled;
    }

    /**
     * Sets the width of the border around the bar (if drawn).
     * 
     * @param width
     */
    public void setBorderWidth(float width) {
        mBorderPaint.setStrokeWidth(width);
    }

    /**
     * Sets the color of the border around the bar (if drawn).
     * 
     * @param color
     */
    public void setBorderColor(int color) {
        mBorderPaint.setColor(color);
    }

    /**
     * Sets a custom BarColorFormatter for the ValueBar. Implement the
     * BarColorFormatter interface in your own formatter class and return
     * whatever color you like from the getColor(...) method. You can for
     * example make the color depend on the current value of the bar.
     * 
     * @param formatter
     */
    public void setColorFormatter(BarColorFormatter formatter) {
        mColorFormatter = formatter;
    }

    /**
     * Sets the color the ValueBar should have.
     * 
     * @param color
     */
    public void setColor(int color) {
        mColorFormatter = new DefaultColorFormatter(color);
    }

    /**
     * Returns the paint object that is used for drawing the bar.
     * 
     * @return
     */
    public Paint getBarPaint() {
        return mBarPaint;
    }

    /**
     * Default BarColorFormatter class that supports a single color.
     * 
     * @author Philipp Jahoda
     */
    private class DefaultColorFormatter implements BarColorFormatter {

        private int mColor;

        public DefaultColorFormatter(int color) {
            mColor = color;
        }

        @Override
        public int getColor(float value) {
            return mColor;
        }
    }
}
