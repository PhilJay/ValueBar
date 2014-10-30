package com.philjay.valuebarexample;

import com.philjay.valuebar.ValueTextFormatter;

import java.text.DecimalFormat;

public class MyCustomValueTextFormatter implements ValueTextFormatter {
    
    private DecimalFormat mFormat;
    
    public MyCustomValueTextFormatter() {
        mFormat = new DecimalFormat("###,###,###");
    }

    @Override
    public String getValueText(float value, float maxVal, float minVal) {
        return value + "";
    }
    
    @Override
    public String getMaxVal(float maxVal) {
        return mFormat.format(maxVal) + " $";
    }
    
    @Override
    public String getMinVal(float minVal) {
        return mFormat.format(minVal) + " $";
    }
}
