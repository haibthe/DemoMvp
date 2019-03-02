package com.hb.uiwidget.viewpager;

import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Created by haibt3 on 10/3/2016.
 */

public class MinFragmentPagerAdapter extends FragmentPagerAdapter {

    private FragmentPagerAdapter adapter;

    public MinFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setAdapter(FragmentPagerAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public int getCount() {
        int realCount = adapter.getCount();
        if (realCount == 1) {
            return 4;
        } else if (realCount == 2 || realCount == 3) {
            return realCount * 2;
        } else {
            return realCount;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return adapter.isViewFromObject(view, object);
    }

    /**
     * Warning: If you only have 1-3 real pages, this method will create multiple, duplicate
     * instances of your Fragments to ensure wrap-around is possible. This may be a problem if you
     * have editable fields or transient state (they will not be in sync).
     *
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        int realCount = adapter.getCount();
        if (realCount == 1) {
            return adapter.getItem(0);
        } else if (realCount == 2 || realCount == 3) {
            return adapter.getItem(position % realCount);
        } else {
            return adapter.getItem(position);
        }
    }


}
