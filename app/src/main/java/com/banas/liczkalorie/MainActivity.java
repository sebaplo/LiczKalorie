package com.banas.liczkalorie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;
import com.banas.liczkalorie.Database.DatabaseHelper;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.io.File;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{

    DatabaseHelper databaseHelper;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem tabMain, tabSearch, tabStats, tabProfile;
    private PageAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);
        File database = getDatabasePath(DatabaseHelper.DBNAME);
        if(false==database.exists()){
            databaseHelper.getReadableDatabase();
            if(databaseHelper.copyDatabase(this)){
                //Toast.makeText(this,"Baza danych aktywna", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this,"Błąd danych", Toast.LENGTH_SHORT).show();
            }
        }
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabMain = (TabItem) findViewById(R.id.tabMain);
        tabSearch = (TabItem) findViewById(R.id.tabSearch);
        tabStats = (TabItem) findViewById(R.id.tabStats);
        tabProfile = (TabItem) findViewById(R.id.tabProfile);

        pagerAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        tabLayout.setOnTabSelectedListener(this);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        viewPager.setOffscreenPageLimit(0);//kasowanie poprzednich tabów
        tabLayout.getTabAt(0).getIcon().setColorFilter(getResources().getColor(android.R.color.black), PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        tabLayout.getTabAt(tab.getPosition()).getIcon().setColorFilter(getResources().getColor(android.R.color.black), PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        tabLayout.getTabAt(tab.getPosition()).getIcon().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                view.setAlpha(0f);

            } else if (position <= 1) { // [-1,1]
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));
            } else {
                view.setAlpha(0f);
            }
        }

    }
}
