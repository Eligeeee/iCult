package com.example.luisguilherme.icult;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Luis Guilherme on 10/08/2017.
 */

public class TabAdapter extends FragmentStatePagerAdapter {

    private String[] abasTitulos = {"FOTOS", "VÍDEOS"};

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new UpFotoFragment();
                break;
            case 1:
                fragment = new UpVideoFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return abasTitulos.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return abasTitulos[position];
    }
}
