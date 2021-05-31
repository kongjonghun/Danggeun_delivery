package org.techtown.dangguen.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.techtown.dangguen.Fragments.Frag_room3;
import org.techtown.dangguen.Fragments.Frag_room1;
import org.techtown.dangguen.Fragments.Frag_room2;
import org.techtown.dangguen.Fragments.Frag_room4;
import org.techtown.dangguen.Fragments.Frag_roomAll;

public class FragmentAdapter_Home extends FragmentStateAdapter
{
    public FragmentAdapter_Home(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle)
    {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position)
    {
        switch (position)
        {
            case 0:
                return new Frag_room1();
            case 1:
                return new Frag_room2();
            case 2:
                return new Frag_room3();
            case 3:
                return new Frag_room4();
            case 4:
                return new Frag_roomAll();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount()
    {
        return 5;
    }
}
