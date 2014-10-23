
package com.philjay.valuebar;

import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Utilities class that has some helper methods. Needs to be initialized by
 * calling Utils.init(...) before usage. Inside the Chart.init() method, this is
 * done, if the Utils are used before that, Utils.init(...) needs to be called
 * manually.
 * 
 * @author Philipp Jahoda
 */
public abstract class Utils {

    private static DisplayMetrics mMetrics;

    /**
     * initialize method, called inside the Chart.init() method.
     * 
     * @param res
     */
    public static void init(Resources res) {
        mMetrics = res.getDisplayMetrics();
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device
     * density. NEEDS UTILS TO BE INITIALIZED BEFORE USAGE.
     * 
     * @param dp A value in dp (density independent pixels) unit. Which we need
     *            to convert into pixels
     * @return A float value to represent px equivalent to dp depending on
     *         device density
     */
    public static float convertDpToPixel(float dp) {

        if (mMetrics == null) {

            Log.e("ValueBar-Utils",
                    "Utils NOT INITIALIZED. You need to call Utils.init(...) at least once before calling Utils.convertDpToPixel(...). Otherwise conversion does not take place.");
            return dp;
            // throw new IllegalStateException(
            // "Utils NOT INITIALIZED. You need to call Utils.init(...) at least once before calling Utils.convertDpToPixel(...).");
        }

        DisplayMetrics metrics = mMetrics;
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent
     * pixels. NEEDS UTILS TO BE INITIALIZED BEFORE USAGE.
     * 
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px) {

        if (mMetrics == null) {

            Log.e("ValueBar-Utils",
                    "Utils NOT INITIALIZED. You need to call Utils.init(...) at least once before calling Utils.convertPixelsToDp(...). Otherwise conversion does not take place.");
            return px;
            // throw new IllegalStateException(
            // "Utils NOT INITIALIZED. You need to call Utils.init(...) at least once before calling Utils.convertPixelsToDp(...).");
        }

        DisplayMetrics metrics = mMetrics;
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    /**
     * calculates the approximate width of a text, depending on a demo text
     * avoid repeated calls (e.g. inside drawing methods)
     * 
     * @param paint
     * @param demoText
     * @return
     */
    public static int calcTextWidth(Paint paint, String demoText) {
        return (int) paint.measureText(demoText);
    }

    /**
     * calculates the approximate height of a text, depending on a demo text
     * avoid repeated calls (e.g. inside drawing methods)
     * 
     * @param paint
     * @param demoText
     * @return
     */
    public static int calcTextHeight(Paint paint, String demoText) {

        Rect r = new Rect();
        paint.getTextBounds(demoText, 0, demoText.length(), r);
        return r.height();
    }
}
