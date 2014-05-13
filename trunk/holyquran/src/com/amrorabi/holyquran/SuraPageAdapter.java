package com.amrorabi.holyquran;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SuraPageAdapter extends FragmentPagerAdapter {
	
	private List<Fragment> fragments;

	public SuraPageAdapter(FragmentManager fm, List<Fragment> fragments) {
		
		super(fm);

		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int i) {
		
		return this.fragments.get(i);
	}

	@Override
	public int getCount() {
		
		return this.fragments.size();
	}

}
