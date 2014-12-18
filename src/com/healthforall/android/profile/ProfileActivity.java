package com.healthforall.android.profile;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.healthforall.android.JSONParser;
import com.healthforall.android.LoginActivity;
import com.healthforall.android.R;
import com.healthforall.android.Vars;
import com.healthforall.android.edit.EditActivity;

public class ProfileActivity extends SherlockFragmentActivity implements
		com.actionbarsherlock.app.ActionBar.TabListener {

	AppSectionsPagerAdapter mAppSectionsPagerAdapter;

	ViewPager mViewPager;

	private String[] tab = new String[4];

	private String[] titletab = new String[4];

	private String profileof = "";

	private String author = "";
	private String pid = "";

	private com.actionbarsherlock.app.ActionBar actionBar;

	private DrawerLayout mDrawerLayout;

	private ListView mDrawerList;

	private String[] mPlanetTitles;

	private ActionBarDrawerToggle mDrawerToggle;

	Context context;

	private Dialog alertdialog;

	private EditText newp;

	private EditText oldp;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		alertdialog = new Dialog(this, R.style.AppTheme);
		context = this;

		SharedPreferences settings = getSharedPreferences("storage", 0);
		if (!settings.getString("pid", "").equals("root"))
			mPlanetTitles = new String[] { "Logout", "Reset Password",
					"Edit Profile" };
		else
			mPlanetTitles = new String[] { "Logout", "Reset Password" };

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.drawer_list);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener
		
		
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
			mDrawerList.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, mPlanetTitles));
			
		   
		}
		else
			mDrawerList.setAdapter(new ArrayAdapter<String>(this,R.layout.drawer_list_item, mPlanetTitles));	
		
		 
		
	    
 
		
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		
		
		
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
			com.actionbarsherlock.app.ActionBar ab = getSupportActionBar();
			    ab.setDisplayShowHomeEnabled(true);
			    ab.setDisplayHomeAsUpEnabled(false);

			    ab.setHomeButtonEnabled(true);
			    ab.setLogo(null);	    
		}
		else
		{
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setHomeButtonEnabled(true);
		}
		

		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_fa_home, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open,  /* "open drawer" description */
        R.string.drawer_close 
		) {
			public void onDrawerClosed(View view) {

				supportInvalidateOptionsMenu();// creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {

				supportInvalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		Intent intent = getIntent();

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {

			author = bundle.getString("author");
			profileof = bundle.getString("profileof");
			pid = bundle.getString("pid");
		}

		if (profileof.equals("doctor")) {
			titletab[0] = "Details of the doctor..";
			titletab[1] = "Details of hospitals served in..";
			titletab[2] = "Add hospital";
			titletab[3] = "Search..";
			tab[0] = "Profile";
			tab[1] = "Hospitals";
			tab[3] = "Search";
			tab[2] = "Add hospital";

		} else if (profileof.equals("patient")) {

			titletab[0] = "Details of the patient..";
			titletab[1] = "Case History..";
			titletab[2] = "Search..";
			tab[0] = "Profile";
			tab[1] = "Case History";
			tab[2] = "Search";
		} else if (profileof.equals("hospital")) {

			titletab[0] = "Details of the hospital..";
			titletab[1] = "Details of doctors..";
			titletab[2] = "Add patient or case..";
			titletab[3] = "Search..";
			tab[0] = "Profile";
			tab[1] = "Doctors";
			tab[2] = "Add..";
			tab[3] = "Search";

		} else if (profileof.equals("root")) {
			titletab[0] = "Root functions..";
			titletab[1] = "Search..";
			tab[0] = "Functions";
			tab[1] = "Search";
		}

		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(
				getSupportFragmentManager(), profileof, author, pid);

		actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		Drawable image = getResources()
				.getDrawable(R.drawable.bg_striped_split);

		actionBar.setBackgroundDrawable(image);

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mAppSectionsPagerAdapter);
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {

			actionBar.addTab(actionBar.newTab().setText(tab[i])
					.setTabListener(this));

		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// super.onConfigurationChanged(newConfig);
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	@Override
	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
		
		 if (item.getItemId() == android.R.id.home) {
			 
	            if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
	                mDrawerLayout.closeDrawer(mDrawerList);
	            } else {
	                mDrawerLayout.openDrawer(mDrawerList);
	            }
	        }
	 
	        return super.onOptionsItemSelected(item);
        
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		mDrawerList.setItemChecked(position, true);
		mDrawerLayout.closeDrawer(mDrawerList);
		if (position == 0) {
			alertdialog.setContentView(R.layout.confirm_dialog);
			alertdialog.setTitle("Wait...");

			final TextView alertmessage = (TextView) alertdialog
					.findViewById(R.id.confirmmessage);
			alertmessage.setText("Are you sure you want to logout??");
			BootstrapButton ok = (BootstrapButton) alertdialog
					.findViewById(R.id.cancel);
			ok.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					alertdialog.dismiss();
				}
			});

			BootstrapButton logout = (BootstrapButton) alertdialog
					.findViewById(R.id.logout);
			logout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					SharedPreferences settings = getSharedPreferences(
							"storage", 0);
					Editor p = settings.edit();
					p.putBoolean("loggedin", false);
					p.commit();
					alertdialog.dismiss();
					Intent i = new Intent(context, LoginActivity.class);
					startActivity(i);
					finish();
				}
			});

			// if(!this.isFinishing()){
			alertdialog.show();
			// }
			
		} else if (position == 1) {
			alertdialog = new Dialog(context, R.style.AppTheme);
			alertdialog.setContentView(R.layout.resetpassword);
			alertdialog.setTitle("Enter details...");

			oldp = (EditText) alertdialog.findViewById(R.id.oldpassword);
			newp = (EditText) alertdialog.findViewById(R.id.newpassword);
			final EditText confirmp = (EditText) alertdialog
					.findViewById(R.id.confirmpassword);

			BootstrapButton ok = (BootstrapButton) alertdialog
					.findViewById(R.id.resetnegative);
			ok.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					alertdialog.dismiss();
				}
			});

			BootstrapButton logout = (BootstrapButton) alertdialog
					.findViewById(R.id.resetpositive);
			logout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					SharedPreferences settings = getSharedPreferences(
							"storage", 0);
					String old = settings.getString("password", "");

					if (!old.equals(oldp.getText().toString())) {
						createAlert("Old password doesnot match", context);
					} else if (!newp.getText().toString()
							.equals(confirmp.getText().toString())) {
						createAlert("Two passwords do not match..", context);
					} else
					{
						
						new PasswordAsync().execute();
									}

				}
			});

			// if(!this.isFinishing()){
			alertdialog.show();
			// }

		} else {
			Intent i = new Intent(context, EditActivity.class);
			startActivity(i);

		}

	}

	JSONObject json = null;

	private ProgressDialog pDialog=null;

	JSONParser jParser = new JSONParser();

	class PasswordAsync extends AsyncTask<String, String, String> {
		private String message;
		private ProgressDialog pDialog;

		protected void onPreExecute() {
			super.onPreExecute();

			message = "";

			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Resetting password...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();

		}

		protected String doInBackground(String... args) {
			if(Vars.isURLReachable(context))
			{
			String url = Vars.getIp() + "resetpassword.php";

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			String type = "";
			SharedPreferences settings = getSharedPreferences("storage", 0);

			String loginvia = settings.getString("loginvia", "");
			String id = settings.getString("pid", "");
			String password = newp.getText().toString();

			params.add(new BasicNameValuePair("tag", loginvia));
			params.add(new BasicNameValuePair("password", password));
			params.add(new BasicNameValuePair("pid", id));

			json = jParser.makeHttpRequest(url, "POST", params);

			try {

				if (json.get("error_msg").equals("")) {

					message = "success";

				} else {
					message = "absent";

				}
			} catch (JSONException e) {

				message = "server";

			}
			}
			else
				message="problem";
			return null;
		}

		protected void onPostExecute(String file_url) {
			if(!(pDialog==null))
			pDialog.dismiss();
			if (message.equals("server")) {
				Toast.makeText(context, "Server error..try again",
						Toast.LENGTH_SHORT).show();
			} else if (message.equals("success")) {
				alertdialog.dismiss();
				Toast.makeText(context, "Password reset successful.",
						Toast.LENGTH_SHORT).show();
				SharedPreferences settings = getSharedPreferences("storage", 0);
				Editor p = settings.edit();
				p.putString("password", newp.getText().toString());
				p.commit();
			}
			else if(message.equals("problem"))
				Toast.makeText(context,"Server down..or internet connection problem..",Toast.LENGTH_SHORT).show();

			
			else {
				Toast.makeText(context, "Error changing password.",
						Toast.LENGTH_SHORT).show();
			}

		}

	}

	private void createAlert(String message, Context context) {

		alertdialog.setContentView(R.layout.alert_dialog);
		alertdialog.setTitle("Alert...");

		final TextView alertmessage = (TextView) alertdialog
				.findViewById(R.id.alertmessage);
		alertmessage.setText(message);
		BootstrapButton ok = (BootstrapButton) alertdialog
				.findViewById(R.id.ok);
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alertdialog.dismiss();
			}
		});

		if (!this.isFinishing()) {
			alertdialog.show();
		}

	}

	@Override
	public void onTabSelected(Tab tab,
			android.support.v4.app.FragmentTransaction ft) {
		mViewPager.setCurrentItem(tab.getPosition());
		actionBar.setTitle(titletab[tab.getPosition()]);
	}

	@Override
	public void onTabUnselected(Tab tab,
			android.support.v4.app.FragmentTransaction ft) {

	}

	@Override
	public void onTabReselected(Tab tab,
			android.support.v4.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

}
