package com.xie.designpatterns.widget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.xie.designpatterns.R;

/**
 * des:
 * author: marc
 * date:  2017/2/22 09:18
 * email：aliali_ha@yeah.net
 */

public class BounchingActivity extends AppCompatActivity {

    boolean isOpen = false;
    private BouncingMenu bouncingMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.start) {
            if (bouncingMenu != null) {
                bouncingMenu.dissmiss();
                bouncingMenu = null;
                return true;
            }
            RcyclerAdapter adapter = new RcyclerAdapter(getBaseContext());
            bouncingMenu = BouncingMenu.makeMenu(findViewById(R.id.rl), R.layout.layout_rv_sweet, adapter).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
