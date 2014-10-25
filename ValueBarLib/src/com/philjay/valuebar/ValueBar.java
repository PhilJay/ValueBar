
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
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.philjay.valuebar.colors.BarColorFormatter;

import java.text.DecimalFormat;

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

    /** the interval in which values can be chosen and displayed */
    private float mInterval = 1f;

    private RectF mBar;

    private Paint mBarPaint;
    private Paint mBorderPaint;
    private Paint mValueTextPaint;
    private Paint mMinMaxTextPaint;
    private Paint mOverlayPaint;

    private ObjectAnimator mAnimator;

    private boolean mDrawBorder = true;
    private boolean mDrawValueText = true;
    private boolean mDrawMinMaxText = true;
    private boolean mTouchEnabled = true;

    private BarColorFormatter mColorFormatter;
    private ValueTextFormatter mValueTextFormatter;

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

        Utils.init(getResources());

        mBar = new RectF();
        mBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBarPaint.setStyle(Paint.Style.FILL);

        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(Utils.convertDpToPixel(2f));

        mValueTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mValueTextPaint.setColor(Color.WHITE);
        mValueTextPaint.setTextSize(Utils.convertDpToPixel(18f));

        mMinMaxTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMinMaxTextPaint.setColor(Color.WHITE);
        mMinMaxTextPaint.setTextSize(Utils.convertDpToPixel(18f));

        mOverlayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOverlayPaint.setStyle(Paint.Style.FILL);
        mOverlayPaint.setColor(Color.WHITE);
        mOverlayPaint.setAlpha(120);

        mColorFormatter = new DefaultColorFormatter(Color.rgb(39, 140, 230));
        mValueTextFormatter = new DefaultValueTextFormatter();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        prepareBarSize();

        if (mDrawMinMaxText)
            drawMinMaxText(canvas);

        mBarPaint.setColor(mColorFormatter.getColor(mValue, mMaxVal, mMinVal));

        // draw the value-bar
        canvas.drawRect(mBar, mBarPaint);

        // draw the border
        if (mDrawBorder)
            canvas.drawRect(0, 0, getWidth(), getHeight(),
                    mBorderPaint);

        if (mDrawValueText)
            drawValueText(canvas);
    }

    /**
     * Draws all text on the ValueBar.
     * 
     * @param canvas
     */
    private void drawValueText(Canvas canvas) {

        if (mValue <= mMinVal && mDrawMinMaxText)
            return;

        String text = mValueTextFormatter.getValueText(mValue, mMaxVal, mMinVal);

        float textHeight = Utils.calcTextHeight(mValueTextPaint, text) * 1.5f;
        float textWidth = Utils.calcTextWidth(mValueTextPaint, text);

        float x = mBar.right - textHeight / 2f;
        float y = getHeight() / 2f + textWidth / 2f;

        if (x < textHeight)
            x = textHeight;

        // draw overlay
        canvas.drawRect(x - textHeight / 1.5f - textHeight / 2f, 0, mBar.right,
                getHeight(),
                mOverlayPaint);

        drawTextVertical(canvas, text, x, y, mValueTextPaint);
    }

    /**
     * Draws the minimum and maximum text values.
     * 
     * @param canvas
     */
    private void drawMinMaxText(Canvas canvas) {

        String max = mValueTextFormatter.getMaxVal(mMaxVal);
        String min = mValueTextFormatter.getMinVal(mMinVal);

        float textHeight = Utils.calcTextHeight(mValueTextPaint, min) * 1.5f;

        // draw max
        drawTextVertical(canvas, max, getWidth() - textHeight / 2f,
                getHeight() / 2f + Utils.calcTextWidth(mMinMaxTextPaint, max) / 2f,
                mMinMaxTextPaint);

        if (!mDrawValueText || mValue <= mMinVal) // draw min
            drawTextVertical(canvas, min, textHeight,
                    getHeight() / 2f + Utils.calcTextWidth(mMinMaxTextPaint, min) / 2f,
                    mMinMaxTextPaint);
    }

    /**
     * Draws the text vertically at the provided position.
     * 
     * @param canvas
     * @param text
     * @param x
     * @param y
     * @param p
     */
    private void drawTextVertical(Canvas canvas, String text, float x, float y, Paint p) {

        canvas.save();

        canvas.rotate(270, x, y);
        canvas.drawText(text,
                x,
                y,
                p);
        canvas.restore();
    }

    /**
     * Prepares the bar according to the current value.
     */
    private void prepareBarSize() {

        float length = ((float) getWidth() / (mMaxVal - mMinVal)) * (mValue - mMinVal);

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
     * Sets the actual value the bar displays. Do not forget to set a minimum
     * and maximum value.
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
     * Sets the interval in which the values can be chosen and dispalyed from /
     * on the ValueBar. If interval <= 0, there is no interval.
     * 
     * @param interval
     */
    public void setInterval(float interval) {
        mInterval = interval;
    }

    /**
     * Returns the interval in which values can be chosen and displayed.
     * 
     * @return
     */
    public float getInterval() {
        return mInterval;
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

        if (from < mMinVal)
            from = mMinVal;
        if (from > mMaxVal)
            from = mMaxVal;

        if (to < mMinVal)
            to = mMinVal;
        if (to > mMaxVal)
            to = mMaxVal;

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

        if (to > mMaxVal)
            to = mMaxVal;

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

        if (to < mMinVal)
            to = mMinVal;

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
     * Sets a custom formatter that formats the value-text. Provide null to
     * reset all changes and use the default formatter.
     * 
     * @param formatter
     */
    public void setValueTextFormatter(ValueTextFormatter formatter) {

        if (formatter == null)
            formatter = new DefaultValueTextFormatter();
        mValueTextFormatter = formatter;
    }

    /**
     * Sets a custom BarColorFormatter for the ValueBar. Implement the
     * BarColorFormatter interface in your own formatter class and return
     * whatever color you like from the getColor(...) method. You can for
     * example make the color depend on the current value of the bar. Provide
     * null to reset all changes.
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
     * Returns the Paint object used for drawing the value-text.
     * 
     * @return
     */
    public Paint getValueTextPaint() {
        return mValueTextPaint;
    }

    /**
     * Returns the Paint object used for drawing min an max text.
     * 
     * @return
     */
    public Paint getMinMaxTextPaint() {
        return mMinMaxTextPaint;
    }

    /**
     * Sets the size of the value-text in density pixels.
     * 
     * @param size
     */
    public void setValueTextSize(float size) {
        mValueTextPaint.setTextSize(Utils.convertDpToPixel(size));
    }

    /**
     * Sets the Typeface of the value-text.
     * 
     * @param size
     */
    public void setValueTextTypeface(Typeface tf) {
        mValueTextPaint.setTypeface(tf);
    }

    /**
     * Sets the size of the min-max text in density pixels.
     * 
     * @param size
     */
    public void setMinMaxTextSize(float size) {
        mMinMaxTextPaint.setTextSize(Utils.convertDpToPixel(size));
    }

    /**
     * Sets the Typeface of the min-max text.
     * 
     * @param size
     */
    public void setMinMaxTextTypeface(Typeface tf) {
        mMinMaxTextPaint.setTypeface(tf);
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
     * Set this to true to enable drawing the actual value that is currently
     * displayed onto the bar.
     * 
     * @param enabled
     */
    public void setDrawValueText(boolean enabled) {
        mDrawValueText = enabled;
    }

    /**
     * Returns true if drawing the text that describes the actual value is
     * enabled.
     * 
     * @return
     */
    public boolean isDrawValueTextEnabled() {
        return mDrawValueText;
    }

    /**
     * Set this to true to enable drawing the minimum and maximum labels below
     * the bar.
     * 
     * @param enabled
     */
    public void setDrawMinMaxText(boolean enabled) {
        mDrawMinMaxText = enabled;
    }

    /**
     * Returns true if drawing the minimum and maximum label is enabled.
     * 
     * @return
     */
    public boolean isDrawMinMaxTextEnabled() {
        return mDrawMinMaxText;
    }

    /**
     * Returns the corresponding value for a pixel-position on the horizontal
     * axis.
     * 
     * @param xPos
     * @return
     */
    public float getValueForPosition(int xPos) {

        float factor = xPos / getWidth();
        return mMaxVal * factor;
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

        float newVal = 0f;

        if (x <= 0)
            newVal = mMinVal;
        else if (x > getWidth())
            newVal = mMaxVal;
        else {
            float factor = x / getWidth();

            newVal = (mMaxVal - mMinVal) * factor + mMinVal;
        }

        if (mInterval > 0f) {

            float remainder = newVal % mInterval;

            // check if the new value is closer to the next, or the previous
            if (remainder <= mInterval / 2f) {

                newVal = newVal - remainder;
            } else {
                newVal = newVal - remainder + mInterval;
            }
        }

        mValue = newVal;
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

    /**
     * Default ValueTextFormatter that simply returns the value as a string.
     * 
     * @author Philipp Jahoda
     */
    private class DefaultValueTextFormatter implements ValueTextFormatter {

        private DecimalFormat mFormat;

        public DefaultValueTextFormatter() {
            mFormat = new DecimalFormat("###,###,##0.00");
        }

        @Override
        public String getValueText(float value, float maxVal, float minVal) {
            return mFormat.format(value);
        }

        @Override
        public String getMinVal(float minVal) {
            return mFormat.format(minVal);
        }

        @Override
        public String getMaxVal(float maxVal) {
            return mFormat.format(maxVal);
        }
    }
}
