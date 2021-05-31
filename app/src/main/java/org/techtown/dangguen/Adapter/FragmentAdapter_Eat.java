package org.techtown.dangguen.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.techtown.dangguen.Fragments.Frag_res1;
import org.techtown.dangguen.Fragments.Frag_res2;
import org.techtown.dangguen.Fragments.Frag_res3;
import org.techtown.dangguen.Fragments.Frag_res4;
import org.techtown.dangguen.Fragments.Frag_res5;

public class FragmentAdapter_Eat extends FragmentStateAdapter {
    public FragmentAdapter_Eat(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new Frag_res2();
            case 2:
                return new Frag_res3();
            case 3:
                return new Frag_res4();
            case 4:
                return new Frag_res5();
        }

        return new Frag_res1();
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
