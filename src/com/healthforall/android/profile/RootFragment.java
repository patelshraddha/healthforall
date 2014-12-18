package com.healthforall.android.profile;

import java.util.ArrayList;
import java.util.UUID;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.healthforall.android.JSONParser;
import com.healthforall.android.R;
import com.healthforall.android.Vars;

public class RootFragment extends Fragment {

	private String profileof;
	private BootstrapButton patient;
	private BootstrapButton hospital;
	private BootstrapButton doctor;
	private Dialog dialog;
	private EditText uid;
	private Dialog alertdialog;
	String add = "";
	private EditText password;
	private Context context;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle bundle) {

		View rootView = inflater.inflate(R.layout.login_activity, container,
				false);

		Bundle args = getArguments();
		this.profileof = args.getString("profileof");
		context=getActivity();

		patient = (BootstrapButton) rootView.findViewById(R.id.patientlogin);
		patient.setVisibility(View.GONE);
		hospital = (BootstrapButton) rootView.findViewById(R.id.hospitallogin);
		doctor = (BootstrapButton) rootView.findViewById(R.id.doctorlogin);

		hospital.setText("Add new hospital");
		doctor.setText(" Add new doctor ");

		hospital.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				add = "hospital";
				launch(getActivity(), "hospital");

			}
		});

		doctor.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				add = "doctor";
				launch(getActivity(), "doctor");

			}
		});

		return rootView;
	}

	protected void launch(final Context context, String string) {
		dialog = new Dialog(context, R.style.AppTheme);
		dialog.setContentView(R.layout.add_dialog);
		dialog.setTitle("Enter Details Of " + add.toUpperCase());

		uid = (EditText) dialog.findViewById(R.id.email);
		password = (EditText) dialog.findViewById(R.id.password);

		BootstrapButton goback = (BootstrapButton) dialog
				.findViewById(R.id.loginnegative);
		BootstrapButton random = (BootstrapButton) dialog
				.findViewById(R.id.random);

		random.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				password.setText(UUID.randomUUID().toString().substring(0, 10));
			}
		});

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
				String pass = password.getText().toString();
				if ((userid.equals("")) || (pass.equals(""))) {

					createAlert("Emailid or password is undefined", context);
					;
				} else {
					if (!userid.contains("@"))
						createAlert("Not a valid email id..", context);
					else if (pass.length() > 15) {
						createAlert("Password must be 15 characters or less..",
								context);
					} else {
						
						new AddAsync().execute();
						}
				}

			}
		});

		dialog.show();

	}

	protected void createAlert(String message, Context context) {
		alertdialog = new Dialog(context, R.style.AppTheme);
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

		alertdialog.show();
	}

	JSONObject json = null;

	private ProgressDialog pDialog=null;
   
	JSONParser jParser = new JSONParser();

	class AddAsync extends AsyncTask<String, String, String> {
		private String message;
		private ProgressDialog pDialog;
		private ArrayList<NameValuePair> params;
		

		protected void onPreExecute() {
			super.onPreExecute();

			message = "";

			 pDialog = new ProgressDialog(context);
			 pDialog.setMessage("Operating information...");
			 pDialog.setIndeterminate(false); pDialog.setCancelable(true);
			 pDialog.show();
			

		}

		protected String doInBackground(String... args) {
			if(Vars.isURLReachable(context))
			{
			String url = Vars.getIp() + "check.php";

			params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("tag", add));
			params.add(new BasicNameValuePair("email", uid.getText().toString()));

			json = jParser.makeHttpRequest(url, "POST", params);

			

			try {

				if (json.get("error_msg").equals("")) {

					message = "";
					
				} else {
					url = Vars.getIp() + add + "/new" + add + ".php";

					params = new ArrayList<NameValuePair>();

					params.add(new BasicNameValuePair("tag", add));
					params.add(new BasicNameValuePair("email", uid.getText()
							.toString()));
					params.add(new BasicNameValuePair("password", password
							.getText().toString()));

					json = jParser.makeHttpRequest(url, "POST", params);
					if (json.get("error_msg").equals("success")) {
						message = "success";
					} else {
						message = "error";
					}

				}
			} catch (JSONException e) {

				message = "server";

			}
			}
			else message="problem";
			return null;
		}

		protected void onPostExecute(String file_url) {
			if(!(pDialog==null))
			 pDialog.dismiss();
			 if(message.equals("server"))
			 {
				 Toast.makeText(context,"Server error..try again",Toast.LENGTH_SHORT).show();
			 }
			 if(message.equals("problem"))
			 {
				 Toast.makeText(context,"Server down or internet connection issue..",Toast.LENGTH_SHORT).show();
			 }
			 else if(message.equals(""))
			 {
				 Toast.makeText(context,"User already exists.",Toast.LENGTH_SHORT).show();
			 }	 
			 else if(message.equals("error"))
			 {
				 Toast.makeText(context,"Registration failed..try again",Toast.LENGTH_SHORT).show();
			 }
			 else
			 {
			    try {
					String info= "\n"+"\n"+"Registration successful:"+"\n"+"Id:"+json.getInt("pid")+"\n"+"Password:"+json.getString("password")+"\n"+"\n";
					Toast.makeText(context,info,Toast.LENGTH_LONG).show();
			    } catch (JSONException e) {
					
				}
			 }
			

		}
	}

}
