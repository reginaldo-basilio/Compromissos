package com.example.compromissos.Adapter;

import com.example.compromissos.Fragments.PassadosFragment;
import com.example.compromissos.Fragments.PendentesFragment;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class FragmentAdapter extends FragmentPagerAdapter {

    int numberTabs;

/*
    public FragmentAdapter(@NonNull @NotNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }
*/
    public FragmentAdapter(@NonNull @NotNull FragmentManager fm, int numTabs) {
        super(fm);
        this.numberTabs = numTabs;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                PendentesFragment pendentesFragment= new PendentesFragment();
                return pendentesFragment;
            case 1:
                PassadosFragment passadosFragment= new PassadosFragment();
                return passadosFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberTabs;
    }
}
