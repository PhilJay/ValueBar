
package com.philjay.valuebarexample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.philjay.valuebar.ValueBar;

public class MainActivity extends Activity {

    private ValueBar[] mValueBars = new ValueBar[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mValueBars[0] = (ValueBar) findViewById(R.id.valueBar1);
        mValueBars[1] = (ValueBar) findViewById(R.id.valueBar2);
        mValueBars[2] = (ValueBar) findViewById(R.id.valueBar3);
        mValueBars[3] = (ValueBar) findViewById(R.id.valueBar4);

        setup();
    }

    private void setup() {

        for (ValueBar bar : mValueBars) {

            bar.setMinMax(0, 1000);
            bar.animate(0, 800, 2000);
            bar.setDrawBorder(false);
            bar.setOffset(3);
        }
    }

    private void animateUp() {

        for (ValueBar bar : mValueBars)
            bar.animateUp(800, 2000);
    }
    
    private void animateDown() {

        for (ValueBar bar : mValueBars)
            bar.animateDown(0, 2000);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        switch(item.getItemId()) {
            case R.id.animUp:
                animateUp();
                break;
            case R.id.animDown:
                animateDown();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
