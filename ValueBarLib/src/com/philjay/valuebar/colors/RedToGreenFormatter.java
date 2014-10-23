
package com.philjay.valuebar.colors;

import android.graphics.Color;

public class RedToGreenFormatter implements BarColorFormatter {

    @Override
    public int getColor(float value, float maxVal, float minVal) {
        
        float hsv[] = new float[] {  120f - ((120f * (maxVal - value)) / maxVal), 1f, 1f };

        return Color.HSVToColor(hsv);
        
//        return Color.rgb((int) ((255 * (maxVal - value)) / maxVal), (int) ((255 * value) / maxVal),
//                0);
    }
}
