package com.hb.uiwidget.viewpager;

import android.view.ViewGroup;
import androidx.collection.SparseArrayCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.lang.ref.WeakReference;



public abstract class SmartFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
    // Sparse array to keep track of registered fragments in memory
    private SparseArrayCompat<WeakReference<Fragment>> registeredFragments = new SparseArrayCompat<>();

    public SmartFragmentStatePagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Register the fragment when the item is instantiated
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object item = super.instantiateItem(container, position);
        if (item instanceof Fragment) {
            Fragment fragment = (Fragment) item;
            registeredFragments.put(position, new WeakReference<>(fragment));
        }
        return item;
    }

    // Unregister when the item is inactive
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        if (BuildConfig.DEBUG) {
//            Log.e("SmartFragmentStatePagerAdapter", "destroyItem: " + position);
//        }
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    // Returns the fragment for the position (if instantiated)
    public Fragment getRegisteredFragment(int position) {
        final WeakReference<Fragment> weakRefItem = registeredFragments.get(position);
        return weakRefItem.get();
    }
}