package com.healthforall.android.profile;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.healthforall.android.JSONParser;
import com.healthforall.android.LoginActivity;
import com.healthforall.android.R;
import com.healthforall.android.Vars;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddHospitalFragment extends Fragment {
	private BootstrapButton patient;
	private BootstrapButton hospital;
	private BootstrapButton addhosp;
	private Dialog dialog;
	private EditText uid;
	private Dialog alertdialog;
	private String searchof;
	private Context context;
	private Spinner timings;
	private ArrayAdapter<String> adapter1;
	private EditText hospid;
	private EditText desig;

	@Override
	public View onCreateView(LayoutInflater inflater,
			final ViewGroup container, Bundle bundle) {
		View rootView = inflater
				.inflate(R.layout.addhospital, container, false);
		context = getActivity();
		Bundle args = getArguments();
		args.getString("profileof");

		addhosp = (BootstrapButton) rootView.findViewById(R.id.addhosp);
		timings = (Spinner) rootView.findViewById(R.id.time);
		adapter1 = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item, new String[] {
						"Visiting", "Permanent" });
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		timings.setAdapter(adapter1);

		hospid = (EditText) rootView.findViewById(R.id.hospid);
		desig = (EditText) rootView.findViewById(R.id.desig);

		addhosp.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				if (hospid.getText().toString().length() == 0)
					createAlert("Hospital id can't be empty.");
				else if (desig.getText().toString().trim().length() == 0)
					createAlert("Designation can't be empty.");
				else if (!hospid.getText().toString().contains("@")) {
					String userid = hospid.getText().toString();
					int count = 0;
					for (int i = 0; i < userid.length(); i++) {
						char p = userid.charAt(i);
						boolean check = (p == '0') || (p == '1') || (p == '2')
								|| (p == '3') || (p == '4') || (p == '5')
								|| (p == '6') || (p == '7') || (p == '8')
								|| (p == '9');
						if (!check) {
							count++;
							break;
						}
					}
					if (count == 0)
					{
						
						new AddHospAsync().execute();
							}   
					else
						createAlert("This field should get data in form of emailid or id containing 0-9 only.");
				} else if (hospid.getText().toString().contains("@")) {
					if(Vars.isURLReachable(context))
						new AddHospAsync().execute();
						else
							Toast.makeText(context,"Server down..or internet connection error..",Toast.LENGTH_SHORT).show();
					
				}

			}
		});

		return rootView;
	}

	private void createAlert(String message) {
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

	class AddHospAsync extends AsyncTask<String, String, String> {
		private String message;
		private ProgressDialog pDialog;

		protected void onPreExecute(Context context) {
			super.onPreExecute();

			message = "";

			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Fetching details...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();

		}

		protected String doInBackground(String... args) {
			if(Vars.isURLReachable(context))
			{
			String url = Vars.getIp() + "check.php";

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			String type = "";
			String userid = hospid.getText().toString().trim();
			if (userid != "") {
				if (userid.contains("@"))
					type = "email";
				else
					type = "id";
			}

			params.add(new BasicNameValuePair("type", type));
			params.add(new BasicNameValuePair("tag", "hospital"));
			params.add(new BasicNameValuePair("pid", userid));

			json = jParser.makeHttpRequest(url, "POST", params);

			Log.d("Create Response", json.toString());

			try {

				if (json.get("error_msg").equals("")) {

					
					url=Vars.getIp() + "storehospitaldoctor.php";
					params = new ArrayList<NameValuePair>();
					
					userid = hospid.getText().toString().trim();
					if (userid != "") {
						if (userid.contains("@"))
							type = "email";
						else
							type = "id";
					}
					SharedPreferences settings = context.getSharedPreferences(
							"storage", 0);
					params.add(new BasicNameValuePair("hid",userid));
					params.add(new BasicNameValuePair("did",settings.getString("pid","")));
					params.add(new BasicNameValuePair("desig",desig.getText().toString().trim()));
					params.add(new BasicNameValuePair("time",timings.getSelectedItem().toString()));
					
					

					json = jParser.makeHttpRequest(url, "POST", params);
					if (json.get("error_msg").equals(""))
						message="success";
					else if (json.get("error_msg").equals("Exists"))
						message="exists";
					else
						message="";
					
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

			if(message.equals("server"))
			 {
				 Toast.makeText(context,"Server error..try again",Toast.LENGTH_SHORT).show();
			 }
			 
			else if (message.equals("success")) {
				Toast.makeText(context,"Added successfully to your profile.",Toast.LENGTH_SHORT).show();
				
			}
			else if (message.equals("problem")) {
				Toast.makeText(context,"Server down or internt connection issue.",Toast.LENGTH_SHORT).show();
				
			}
			 
		 else if(message.equals("exists"))
		 {
			 Toast.makeText(context,"User is absent..",Toast.LENGTH_SHORT).show();
		 }
		 else if(message.equals("absent"))
		 {
			 Toast.makeText(context,"Hospital record not present.",Toast.LENGTH_SHORT).show();
		 }
		 else
		 {
			 Toast.makeText(context,"Operation failed...Please try again...",Toast.LENGTH_SHORT).show(); 
		 }
			if(!(pDialog==null))
			pDialog.dismiss();

		}

	}

}
