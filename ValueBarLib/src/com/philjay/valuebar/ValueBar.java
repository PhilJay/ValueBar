
package com.philjay.valuebar;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.philjay.valuebar.colors.BarColorFormatter;

/**
 * ValueBar is a custom View for displaying values in an edgy bar.
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

    /** space between bar and borders of view */
    private int mOffset = 1;

    private RectF mBar;

    private Paint mBarPaint;
    private Paint mBorderPaint;

    private ObjectAnimator mAnimator;

    private boolean mDrawBorder = true;

    private boolean mTouchEnabled = true;

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

        mBarPaint.setColor(mColorFormatter.getColor(mValue, mMaxVal, mMinVal));

        // draw the value-bar
        canvas.drawRect(mBar, mBarPaint);

        // draw the border
        if (mDrawBorder)
            canvas.drawRect(mOffset, mOffset, getWidth() - mOffset, getHeight() - mOffset,
                    mBorderPaint);
    }

    /**
     * Prepares the bar according to the current value.
     */
    private void prepareBarSize() {

        float length = (((float) getWidth() - mOffset * 2f) / (mMaxVal - mMinVal)) * mValue;

        mBar.set(mOffset, mOffset, length - mOffset, getHeight() - mOffset);
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
     * Returns the maximum value the bar can display.
     * 
     * @return
     */
    public float getMax() {
        return mMaxVal;
    }

    /**
     * Returns the minimum value the bar can display.
     * 
     * @return
     */
    public float getMin() {
        return mMinVal;
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
     * Animates the bar up from it's minimum value to the specified value.
     * 
     * @param to
     * @param durationMillis
     */
    public void animateUp(float to, int durationMillis) {

        mValue = mMinVal;
        mAnimator = ObjectAnimator.ofFloat(this, "value", mValue, to);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.setDuration(durationMillis);
        mAnimator.addUpdateListener(this);
        mAnimator.start();
    }

    /**
     * Animates the bar down from it's current value to the specified value.
     * 
     * @param to
     * @param durationMillis
     */
    public void animateDown(float to, int durationMillis) {

        mAnimator = ObjectAnimator.ofFloat(this, "value", mValue, to);
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

        if (formatter == null)
            formatter = new DefaultColorFormatter(Color.rgb(39, 140, 230));
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
     * Set an offset in pixels that defines the space that is left between the
     * bar and the borders of the View. Default: 1.
     * 
     * @param offsetPx
     */
    public void setOffset(int offsetPx) {

        if (offsetPx < 0)
            offsetPx = 0;
        mOffset = offsetPx;
    }

    /**
     * Set this to true to enable touch gestures on the ValueBar.
     * 
     * @param enabled
     */
    public void setTouchEnabled(boolean enabled) {
        mTouchEnabled = enabled;
    }

    /**
     * Sets a GestureDetector for the ValueBar to receive callbacks on gestures.
     * 
     * @param gd
     */
    public void setGestureDetector(GestureDetector gd) {
        mGestureDetector = gd;
    }

    /**
     * Sets a selectionlistener for callbacks when selecting values on the
     * ValueBar.
     * 
     * @param l
     */
    public void setValueBarSelectionListener(ValueBarSelectionListener l) {
        mSelectionListener = l;
    }

    /** listener called when a value has been selected on touch */
    private ValueBarSelectionListener mSelectionListener;

    /** gesturedetector for recognizing single-taps */
    private GestureDetector mGestureDetector;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (mTouchEnabled) {

            if (mSelectionListener == null)
                Log.w("ValueBar",
                        "No SelectionListener specified. Use setSelectionListener(...) to set a listener for callbacks when selecting values.");

            // if the detector recognized a gesture, consume it
            if (mGestureDetector != null && mGestureDetector.onTouchEvent(e))
                return true;

            float x = e.getX();
            float y = e.getY();

            if (x > mOffset && x < getWidth() - mOffset) {

                switch (e.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        updateValue(x, y);
                        invalidate();
                    case MotionEvent.ACTION_MOVE:
                        updateValue(x, y);
                        invalidate();
                        if (mSelectionListener != null)
                            mSelectionListener.onSelectionUpdate(mValue, mMaxVal, mMinVal, this);
                        break;
                    case MotionEvent.ACTION_UP:
                        updateValue(x, y);
                        invalidate();
                        if (mSelectionListener != null)
                            mSelectionListener.onValueSelected(mValue, mMaxVal, mMinVal, this);
                        break;
                }
            }

            return true;
        }
        else
            return super.onTouchEvent(e);
    }

    /**
     * Updates the value on the ValueBar depending on the touch position.
     * 
     * @param x
     * @param y
     */
    private void updateValue(float x, float y) {

        float factor = (x - mOffset) / (getWidth() - mOffset * 2f);

        mValue = mMaxVal * factor;
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
        public int getColor(float value, float maxVal, float minVal) {
            return mColor;
        }
    }
}
