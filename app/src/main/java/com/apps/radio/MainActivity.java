package com.apps.radio;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private ViewPager viewPager;
    private RCPagerAdapter pagerAdapter;
    private ArrayList<View> viewList;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.tab_categories:
                    Log.i("0", "clicked categories button");
                    viewPager.setCurrentItem(0);
                    updateTitle(R.string.title_0);
                    return true;
                case R.id.tab_favorites:
                    Log.i("0", "clicked favorites button");
                    viewPager.setCurrentItem(1);
                    updateTitle(R.string.tab_1);
                    return true;
                case R.id.tab_courses:
                    Log.i("0", "clicked courses button");
                    viewPager.setCurrentItem(2);
                    updateTitle(R.string.tab_2);
                    return true;
                case R.id.tab_more:
                    Log.i("0", "clicked more button");
                    viewPager.setCurrentItem(3);
                    updateTitle(R.string.tab_3);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //隐藏底部导航栏
        //this.hideSystemBottomNavigationBar();

        //自定义Title Bar
        initActionBar();
        updateTitle(getResources().getString(R.string.title_0));

        //取消Tab Bar的shift mode
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation_bar);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //ViewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                Log.i("onPageSelected", "position:" + position);

                BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_bar);
                switch (position)
                {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.tab_categories);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.tab_favorites);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.tab_courses);
                        break;
                    case 3:
                        bottomNavigationView.setSelectedItemId(R.id.tab_more);
                        break;

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        //Set adapter for view pager
        setupViewPager(viewPager);

//        fragmentPagerAdapter = new RCFragmentPagerAdapter(getSupportFragmentManager());
//        fragmentPagerAdapter.addFragment(new CategoryFragment());
//        fragmentPagerAdapter.addFragment(new FavoriteFragment());
//        fragmentPagerAdapter.addFragment(new CourseFragment());
//        fragmentPagerAdapter.addFragment(new MoreFragment());
//        viewPager.setAdapter(fragmentPagerAdapter);


    }

    private void setupViewPager(ViewPager viewPager) {

        viewList = new ArrayList<View>();
        LayoutInflater li = getLayoutInflater();
        viewList.add(li.inflate(R.layout.pageview0,null,false));
        viewList.add(li.inflate(R.layout.pageview1,null,false));
        viewList.add(li.inflate(R.layout.pageview2,null,false));
        viewList.add(li.inflate(R.layout.pageview3,null,false));
        pagerAdapter = new RCPagerAdapter(viewList);
        viewPager.setAdapter(pagerAdapter);
    }


}
