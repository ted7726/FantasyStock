package com.fantasystock.fantasystock.Activities;


import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;


import com.fantasystock.fantasystock.DataClient;
import com.fantasystock.fantasystock.Fragments.MainListFragment;
import com.fantasystock.fantasystock.R;

import com.parse.Parse;

import butterknife.Bind;
import butterknife.ButterKnife;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.flMainListHolder) FrameLayout flMainListHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

//        setupParse();


        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Create dummy watchlist
        getWatchlist();

        // get list view
        MainListFragment fragment = new MainListFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flMainListHolder, fragment)
                .commit();

    }

    private void getWatchlist() {
        DataClient.watchlist.add("MRVL");
        DataClient.watchlist.add("AAPL");
        DataClient.watchlist.add("YHOO");
        DataClient.watchlist.add("MSFT");
        DataClient.watchlist.add("FB");
        DataClient.watchlist.add("GOOGL");
        DataClient.watchlist.add("LNKD");
        DataClient.watchlist.add("TWTR");
    }

    @OnClick(R.id.ibSearch)
    public void onSearchClick() {
        startActivity(new Intent(getApplicationContext(), SearchActivity.class));
    }
}
