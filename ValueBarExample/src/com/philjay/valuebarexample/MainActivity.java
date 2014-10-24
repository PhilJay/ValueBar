
package com.philjay.valuebarexample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.philjay.valuebar.ValueBar;
import com.philjay.valuebar.ValueBarSelectionListener;
import com.philjay.valuebar.colors.RedToGreenFormatter;

public class MainActivity extends Activity implements ValueBarSelectionListener {

    private ValueBar[] mValueBars = new ValueBar[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mValueBars[0] = (ValueBar) findViewById(R.id.valueBar1);
        mValueBars[1] = (ValueBar) findViewById(R.id.valueBar2);
        mValueBars[2] = (ValueBar) findViewById(R.id.valueBar3);
        mValueBars[3] = (ValueBar) findViewById(R.id.valueBar4);
        mValueBars[4] = (ValueBar) findViewById(R.id.valueBar5);

        setup();
    }

    private void setup() {

        for (ValueBar bar : mValueBars) {

            bar.setMinMax(0, 1000);
            bar.animate(0, 900, 1500);
            bar.setDrawBorder(false);
            bar.setValueBarSelectionListener(this);
            bar.setColorFormatter(new RedToGreenFormatter());
            // bar.setColor(Color.BLUE);
        }
    }

    private void animateUp() {

        for (ValueBar bar : mValueBars)
            bar.animateUp(800, 1500);
    }

    private void animateDown() {

        for (ValueBar bar : mValueBars)
            bar.animateDown(0, 1500);
    }

    private void toggleMinMaxLabel() {

        for (ValueBar bar : mValueBars) {
            bar.setDrawMinMaxText(bar.isDrawMinMaxTextEnabled() ? false : true);
            bar.invalidate();
        }
    }

    private void toggleValueLabel() {

        for (ValueBar bar : mValueBars) {
            bar.setDrawValueText(bar.isDrawValueTextEnabled() ? false : true);
            bar.invalidate();
        }
    }

    @Override
    public void onSelectionUpdate(float val, float maxval, float minval, ValueBar bar) {
        Log.i("ValueBar", "Value selection update: " + val);
    }

    @Override
    public void onValueSelected(float val, float maxval, float minval, ValueBar bar) {
        Log.i("ValueBar", "Value selected: " + val);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.animUp:
                animateUp();
                break;
            case R.id.animDown:
                animateDown();
                break;
            case R.id.toggleMinMaxLabel:
                toggleMinMaxLabel();
                break;
            case R.id.toggleValueLabel:
                toggleValueLabel();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
