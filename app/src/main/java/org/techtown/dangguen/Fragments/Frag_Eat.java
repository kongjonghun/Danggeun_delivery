package org.techtown.dangguen.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import org.techtown.dangguen.Adapter.FragmentAdapter_Eat;
import org.techtown.dangguen.R;

public class Frag_Eat extends Fragment
{

    private View view;
    FragmentAdapter_Eat adapter;
    TabLayout tabLayout;
    ViewPager2 pager2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.eat, container, false); // home 화면 연결
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        pager2 = (ViewPager2) view.findViewById(R.id.view_pager2);

        FragmentManager fm = getChildFragmentManager();
        adapter = new FragmentAdapter_Eat(fm, getLifecycle());
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
