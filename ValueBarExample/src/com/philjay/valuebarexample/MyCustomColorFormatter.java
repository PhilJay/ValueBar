
package com.philjay.valuebarexample;

import android.graphics.Color;

import com.philjay.valuebar.colors.BarColorFormatter;

/**
 * Simple custom color formatter that returns green when the value is higher
 * than 50%, and red if the value is below 50%.
 * 
 * @author philipp
 */
public class MyCustomColorFormatter implements BarColorFormatter {

    @Override
    public int getColor(float value, float maxVal, float minVal) {

        float mid = (maxVal + minVal) / 2f;

        if (value > mid)
            return Color.GREEN;
        else
            return Color.RED;
    }

}
