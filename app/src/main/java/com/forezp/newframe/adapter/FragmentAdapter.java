package com.forezp.newframe.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.forezp.newframe.fragment.HomeFragment;
import com.forezp.newframe.fragment.LifeFrgment;
import com.forezp.newframe.fragment.ProductFragment;

public class FragmentAdapter extends FragmentStatePagerAdapter {
	
	public final static int TAB_COUNT = 3;
	
	public FragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int id) {
		switch (id) {
		case 0:
			HomeFragment nf = new HomeFragment();
			return nf;
		case 1:
			ProductFragment nf1 = new ProductFragment();
			return nf1;
		case 2:
			LifeFrgment nf2 = new LifeFrgment();
			return nf2;
		
			default:
				break;
		}
		return null;
	}

	@Override
	public int getCount() {
		return TAB_COUNT;
	}
	
}
