package com.healthforall.android.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.healthforall.android.profile.ProfileFragment;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one
 * of the primary sections of the app.
 */
public class AppSectionsPagerAdapter extends FragmentPagerAdapter {
	private String profileof = "";
	private String author = "";
	private String pid = "";

	public AppSectionsPagerAdapter(FragmentManager fm, String profileof,
			String author, String pid) {
		super(fm);
		this.profileof = profileof;
		this.author = author;
		this.pid = pid;
	}

	@Override
	public Fragment getItem(int i) {
		if (profileof.equals("patient")) {
			switch(i)
			{
			case 0:
			{
				Fragment fragment = new ProfileFragment();
				Bundle args = new Bundle();
				args.putString("profileof", profileof);
				args.putString("author", author);
				args.putString("pid", pid);
				fragment.setArguments(args);
				return fragment;
			}
			case 1:
			{
				Fragment fragment = new ViewCase();
				Bundle args = new Bundle();
				args.putString("profileof", profileof);
				args.putString("author", author);
				args.putString("pid", pid);
				fragment.setArguments(args);
				return fragment;
			}
			case 2:
			{
				Fragment fragment = new SearchFragment();
				Bundle args = new Bundle();
				args.putString("profileof", profileof);
				fragment.setArguments(args);
				return fragment;
			}
			}
		} else if (profileof.equals("doctor")) {
			switch(i)
			{
			case 0:
			{
				Fragment fragment = new ProfileFragment();
				Bundle args = new Bundle();
				args.putString("profileof", profileof);
				args.putString("author", author);
				args.putString("pid", pid);
				fragment.setArguments(args);
				return fragment;
			}
			case 1:
			{
				Fragment fragment = new HospitalFragment();
				Bundle args = new Bundle();
				args.putString("profileof", profileof);
				args.putString("author", author);
				args.putString("pid", pid);
				fragment.setArguments(args);
				return fragment;
			}
			case 2:
			{
				Fragment fragment = new AddHospitalFragment();
				Bundle args = new Bundle();
				args.putString("profileof", profileof);
				fragment.setArguments(args);
				return fragment;
			}
			case 3:
			{
				Fragment fragment = new SearchFragment();
				Bundle args = new Bundle();
				args.putString("profileof", profileof);
				fragment.setArguments(args);
				return fragment;
			}
			}
			
		} else if (profileof.equals("hospital")) {
			switch(i)
			{
			case 0:
			{
				Fragment fragment = new ProfileFragment();
				Bundle args = new Bundle();
				args.putString("profileof", profileof);
				args.putString("author", author);
				args.putString("pid", pid);
				fragment.setArguments(args);
				return fragment;
				
			}
			
			case 1:
			{
				Fragment fragment = new HospitalFragment();
				Bundle args = new Bundle();
				args.putString("profileof", profileof);
				args.putString("author", author);
				args.putString("pid", pid);
				fragment.setArguments(args);
				return fragment;
			}
			case 2:
			{
				Fragment fragment = new AddPatientFragment();
				Bundle args = new Bundle();
				args.putString("profileof", profileof);
				args.putString("author", author);
				args.putString("pid", pid);
				fragment.setArguments(args);
				return fragment;
			}
			case 3:
			{
				Fragment fragment = new SearchFragment();
				Bundle args = new Bundle();
				args.putString("profileof", profileof);
				fragment.setArguments(args);
				return fragment;
			}
			}
		} else
		{
			switch(i)
			{
			case 0:
			{
				Fragment fragment = new RootFragment();
				Bundle args = new Bundle();
				args.putString("profileof", profileof);
				fragment.setArguments(args);
				return fragment;
			}
			case 1:
			
			{
				Fragment fragment = new SearchFragment();
				Bundle args = new Bundle();
				args.putString("profileof", profileof);
				fragment.setArguments(args);
				return fragment;
			}
			}
		}
		Fragment fragment = new ProfileFragment();
		Bundle args = new Bundle();
		args.putString("profileof", profileof);
		args.putString("author", author);
		args.putString("pid", pid);
		fragment.setArguments(args);
		return fragment;
		
		
		
		
		
		
		

	}

	

	@Override
	public int getCount() {
		int count = 0;

		if (profileof.equals("patient")) {
			if (author.equals("self"))
				count = 3;
			else
				count = 2;
		} else if (profileof.equals("doctor")) {
			if (author.equals("self"))
				count = 4;
			else
				count = 2;
		} else if (profileof.equals("hospital")) {
			if (author.equals("self"))
				count = 4;
			else
				count = 2;
		} else
			count = 2;
		return count;

	}
}
