package com.xie.designpatterns.widget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.xie.designpatterns.R;
import com.xie.designpatterns.recyckerview.decoration.MyRecyclerAdapter;

import java.util.Arrays;

/**
 * des:
 * author: marc
 * date:  2017/2/22 09:18
 * email：aliali_ha@yeah.net
 */

public class BounchingActivity extends AppCompatActivity {

    boolean isOpen = false;
    private BouncingMenu bouncingMenu;
    private String[] mTitles;

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
            mTitles = getResources().getStringArray(R.array.titles);
            MyRecyclerAdapter adapter = new MyRecyclerAdapter(Arrays.asList(mTitles));
            bouncingMenu = BouncingMenu.makeMenu(findViewById(R.id.rl), R.layout.layout_rv_sweet, adapter).show();
            adapter.setOnItemClickListener(new MyRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Toast.makeText(BounchingActivity.this, "position"+position, Toast.LENGTH_SHORT).show();
                    if (bouncingMenu != null) {
                        bouncingMenu.dissmiss();
                        bouncingMenu = null;
                    }
                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
