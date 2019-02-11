package com.zj.tronwallet.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.annotation.Nullable;
import com.zj.tronwallet.base.BaseFragment;

/**
 * create by zj on 2019/1/7
 */
public class NormalFragmentAdapter extends FragmentPagerAdapter {
    private BaseFragment[] fragments;

    public NormalFragmentAdapter(FragmentManager fm, BaseFragment[] fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragments[position].getTitle();
    }
}

