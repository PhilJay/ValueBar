
package com.philjay.valuebar;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
    
    private ValueBar vb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        vb = (ValueBar) findViewById(R.id.valueBar);
        vb.setMinMax(0, 1000);
        vb.animate(0, 800, 2000);
        vb.setDrawBorder(false);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        vb.animateUp(2000);
        return super.onOptionsItemSelected(item);
    }
}
