package com.healthforall.android;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.healthforall.android.profile.ProfileActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	BootstrapButton patient;
	BootstrapButton hospital;
	BootstrapButton doctor;
	String loginvia;
	EditText uid;
	EditText pass;
	private Dialog alertdialog;
	private Dialog dialog ;
	private LoginActivity context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		context= this;
		dialog= new Dialog(this, R.style.AppTheme);
		alertdialog = new Dialog(this, R.style.AppTheme);
		setContentView(R.layout.login_activity);

		SharedPreferences settings = getSharedPreferences("storage", 0);

		Boolean loggedin = settings.getBoolean("loggedin", false);
		

		if (loggedin) {
			loginvia= settings.getString("loggedinas","");
			
			Intent i = new Intent(getApplicationContext(),ProfileActivity.class);
			i.putExtra("profileof", loginvia);
			

				i.putExtra("author", "self");
				i.putExtra("pid",settings.getString("pid",""));
			startActivity(i);
			finish();
		}

		patient = (BootstrapButton) findViewById(R.id.patientlogin);
		hospital = (BootstrapButton) findViewById(R.id.hospitallogin);
		doctor = (BootstrapButton) findViewById(R.id.doctorlogin);

		patient.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				loginvia = "patient";
				launchDialog(LoginActivity.this, "patient");
				
			}
		});
		hospital.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				loginvia = "hospital";
				launchDialog(LoginActivity.this, "hospital");
				
			}
		});
		doctor.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				loginvia = "doctor";
				launchDialog(LoginActivity.this, "doctor");
				
			}
		});

	}

	private void launchDialog(Context context, String type) {

		
		dialog.setContentView(R.layout.login_dialog);
		dialog.setTitle("Enter Login Details As " + type.toUpperCase());

		uid = (EditText) dialog.findViewById(R.id.userid);
		pass = (EditText) dialog.findViewById(R.id.password);

		BootstrapButton goback = (BootstrapButton) dialog
				.findViewById(R.id.loginnegative);

		goback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		BootstrapButton login = (BootstrapButton) dialog
				.findViewById(R.id.loginpositive);

		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String userid = uid.getText().toString();
				String password = pass.getText().toString();
				
				if ((userid.equals("")) || (password.equals(""))) {

					createAlert("Username or password is undefined",
							LoginActivity.this);
					dialog.dismiss();
				} else {
					if (userid.contains("@"))
						new Login().execute();
					else if (userid.contains("root")) {
						loginvia = "root";
						new Login().execute();
					} else {
						int count = 0;
						for (int i = 0; i < userid.length(); i++) {
							char p = userid.charAt(i);
							boolean check = (p == '0') || (p == '1')
									|| (p == '2') || (p == '3') || (p == '4')
									|| (p == '5') || (p == '6') || (p == '7')
									|| (p == '8') || (p == '9');
							if (!check) {
								count++;
								break;
							}
						}
						if (count == 0)
							new Login().execute();
						else
							createAlert(
									"This field should get data in form of emailid or id containing 0-9 only.",
									LoginActivity.this);

					}
				}

			}
		});
		if(!this.isFinishing()){
	        dialog.show();
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
		
		if(!this.isFinishing()){
	        alertdialog.show();
	    }
		
	}

	JSONObject json = null;

	private ProgressDialog pDialog=null;

	JSONParser jParser = new JSONParser();

	class Login extends AsyncTask<String, String, String> {
		private String message;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			message = "";
			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Fetching account information...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
            
			if (Vars.isURLReachable(LoginActivity.this)) {
				Log.d("Maker",uid.getText().toString());
			
			if (!loginvia.equals("root")) {
				String url = Vars.getIp() + loginvia + "/get" + loginvia
						+ ".php";

				List<NameValuePair> params = new ArrayList<NameValuePair>();
				String tag;
				String userid = uid.getText().toString();
				if (userid.contains("@"))
					tag = "email";
				else
					tag = "id";

				params.add(new BasicNameValuePair("tag", tag));
				params.add(new BasicNameValuePair("email", userid));
				params.add(new BasicNameValuePair("id", userid));
				params.add(new BasicNameValuePair("password", pass.getText()
						.toString()));

				json = jParser.makeHttpRequest(url, "POST", params);
				

//				Log.d("Create Response", json.toString());
//				Log.d("Check",json.toString());
				
				try {

					if (!json.get("error_msg").equals("Absent")) {

						if(json.get("error_msg").equals(""))
						{
							message = "";
						}
						else {
							message = "wrong";

						}
					} else {
						message = "absent";

					}
				} catch (JSONException e) {

					message = "server";

				}
			}

			else {
				String url = Vars.getIp() + "verifyroot.php";

				List<NameValuePair> params = new ArrayList<NameValuePair>();

				params.add(new BasicNameValuePair("password", pass.getText()
						.toString()));

				json = jParser.makeHttpRequest(url, "POST", params);
				
				Log.d("Create Response", json.toString());
				try {

					if (json.get("error_msg").equals("success")) {

						message = "success";
						
					} else {
						message = "wrong";

					}
				} catch (JSONException e) {

					message = "server";

				}

			}
			}
			else
				message="problem";

			return null;

		}

		protected void onPostExecute(String file_url) {
			
			
			
			if (message.equals("wrong"))
				createAlert("Wrong Id/password combination", LoginActivity.this);
			else if (message.equals("problem"))
				createAlert("Server connection problem...or internet disabled..", LoginActivity.this);
			else if (message.equals("server"))
				createAlert("Server error..Please try again.",
						LoginActivity.this);
			else if (message.equals("absent"))
				createAlert("User doesnot exist.", LoginActivity.this);
			else if (message.equals("success")) {
				SharedPreferences settings = getSharedPreferences("storage", 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean("loggedin", true);
				editor.putString("loggedinas", loginvia);
				editor.putString("pid","root");
				editor.putString("password",pass.getText().toString());
				editor.putString("loginvia",loginvia);
				editor.commit();

				Intent i = new Intent(getApplicationContext(),
						ProfileActivity.class);
				i.putExtra("profileof", loginvia);
				i.putExtra("author", "self");
				if(dialog.isShowing())
				dialog.dismiss();
				if(alertdialog.isShowing())
				alertdialog.dismiss();
				startActivity(i);
				
				finish();
			} else  if(message.equals("")){
				
				SharedPreferences settings = getSharedPreferences("storage", 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean("loggedin", true);
				editor.putString("loggedinas", loginvia);
				
				
				
				try {
					editor.putString("pid",json.getInt("pid")+"");
					editor.putString("name", json.getString("name"));
					editor.putString("phone", json.getString("phone"));
					editor.putString("email", json.getString("email"));
					editor.putString("created_at", json.getString("created_at"));
                    editor.putString("password",pass.getText().toString());
                    editor.putString("loginvia",loginvia);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                   
				if (loginvia.equals("patient")) {
					try {
						editor.putString("surname", json.getString("surname"));
						editor.putString("address", json.getString("address"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else if (loginvia.equals("hospital")) {
					try {
						editor.putString("city", json.getString("city"));
						editor.putString("address", json.getString("address"));
						editor.putString("type", json.getString("type"));
						editor.putString("taluka", json.getString("taluka"));
						editor.putString("state", json.getString("state"));
						editor.putString("district", json.getString("district"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					try {
						editor.putString("surname", json.getString("surname"));

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				editor.commit();

				Intent i = new Intent(getApplicationContext(),
						ProfileActivity.class);
				i.putExtra("profileof", loginvia);
				i.putExtra("author", "self");
				i.putExtra("pid", settings.getString("pid",""));
				if (pDialog.isShowing()) {
			           pDialog.dismiss();
			        }
				dialog.dismiss();
				alertdialog.dismiss();
				
				startActivity(i);
				finish();

			}
			 if (!(pDialog==null)) {
		           pDialog.dismiss();
		        }
			

		}

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		//super.onConfigurationChanged(newConfig);
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

}
