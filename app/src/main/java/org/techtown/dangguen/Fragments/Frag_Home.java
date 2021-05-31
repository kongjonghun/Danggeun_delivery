package org.techtown.dangguen.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import org.techtown.dangguen.Adapter.FragmentAdapter_Home;
import org.techtown.dangguen.R;

public class Frag_Home extends Fragment
{
    private View view;
    TabLayout tabLayout;
    ViewPager2 pager2;
    FragmentAdapter_Home adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        Log.e("@@@@@@@@@@@@", "액티비티 시작");
        view = inflater.inflate(R.layout.home, container, false); // home 화면 연결
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        pager2 = (ViewPager2) view.findViewById(R.id.view_pager);

        FragmentManager fm = getChildFragmentManager();
        adapter = new FragmentAdapter_Home(fm, getLifecycle());
        pager2.setAdapter(adapter);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback()
        {
            @Override
            public void onPageSelected(int position)
            {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        return view;
    }
}
