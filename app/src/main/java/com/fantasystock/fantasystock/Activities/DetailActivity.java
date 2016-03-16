package com.fantasystock.fantasystock.Activities;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import com.fantasystock.fantasystock.DataCenter;
import com.fantasystock.fantasystock.Fragments.DetailFragment;
import com.fantasystock.fantasystock.Models.Stock;
import com.fantasystock.fantasystock.Models.User;
import com.fantasystock.fantasystock.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
    @Bind(R.id.vpViewPager) ViewPager vpViewPager;
    private ArrayList<String> stocks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        String symbol = intent.getStringExtra("symbol");

        ButterKnife.bind(this);

        if (!User.currentUser.watchlistSet.contains(symbol)) {
            stocks = new ArrayList<>();
            stocks.add(symbol);
        } else {
            stocks = User.currentUser.watchlist;
        }

        Drawable fadeBlue = ContextCompat.getDrawable(this, R.drawable.fade_blue);
        DetailsPagerAdapter detailsPagerAdapter = new DetailsPagerAdapter(getSupportFragmentManager(), fadeBlue, stocks);
        vpViewPager.setAdapter(detailsPagerAdapter);
        vpViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (stocks.size()>0) {
                    DataCenter.getInstance().setLastViewedStock(stocks.get(position));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setCurrentPageToStock(symbol);

    }


    private void setCurrentPageToStock(String symbol) {


        int len = stocks.size();
        for (int i=0;i<len; ++i) {
            if (symbol.equals(stocks.get(i))) {
                vpViewPager.setCurrentItem(i);
            }
        }
    }

    private static class DetailsPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<String> stocks;
        private Drawable fadeBlue;
        public DetailsPagerAdapter(FragmentManager fm, Drawable fadeBlue, ArrayList<String> stocks) {
            super(fm);
            this.fadeBlue = fadeBlue;
            if (stocks == null) {
                this.stocks = User.currentUser.watchlist;
            } else {
                this.stocks = stocks;
            }

        }

        @Override
        public Fragment getItem(int position) {
            DetailFragment detailFragment = DetailFragment.newInstance(stocks.get(position));
            detailFragment.fadeBlue = fadeBlue;
            return detailFragment;
        }

        @Override
        public int getCount() {
            return stocks.size();
        }



    }
}
