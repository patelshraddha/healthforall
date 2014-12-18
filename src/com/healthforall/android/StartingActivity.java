package com.healthforall.android;

import java.util.Timer;
import java.util.TimerTask;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;


public class StartingActivity
    extends SherlockFragmentActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.starting_activity);
		com.actionbarsherlock.app.ActionBar bar = getSupportActionBar();
		bar.hide();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

		   public void run() {

			   Intent myIntent = new Intent(StartingActivity.this, LoginActivity.class);
			  
			   startActivity(myIntent);
		     finish();

		   }

		}, 2000);
		
	}
	
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	


   

}
