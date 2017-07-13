package com.picture.picture_manager.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


import com.picture.picture_manager.Adapters.IconAdapter;
import com.picture.picture_manager.R;
import com.picture.picture_manager.Utils.ActivityCollector;
import com.picture.picture_manager.Utils.GlobalFlags;

import java.util.ArrayList;

public class SelectUserIcon extends AppCompatActivity {
    private ArrayList<Integer> icons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_user_icon);
        ActivityCollector.activities.add(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initIcons();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_user_icons);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        IconAdapter adapter = new IconAdapter(this, icons);
        recyclerView.setAdapter(adapter);
    }

    private void initIcons() {
        for (int i = 0; i < GlobalFlags.UserIcons.length; i++) {
            icons.add(GlobalFlags.UserIcons[i]);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
        }
        return true;
    }
}
