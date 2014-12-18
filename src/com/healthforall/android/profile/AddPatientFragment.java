package com.healthforall.android.profile;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.healthforall.android.JSONParser;
import com.healthforall.android.R;
import com.healthforall.android.Vars;
import com.healthforall.android.addpatient.AddPatientActivity;
import com.healthforall.android.profile.SearchFragment.SearchAsync;

public class AddPatientFragment extends Fragment {

	private String profileof;
	private BootstrapButton patient;
	private BootstrapButton hospital;

	private Dialog dialog;
	private EditText uid;
	private Dialog alertdialog;
	private String searchof;
	private Context context;
	private BootstrapButton addcase;

	@Override
	public View onCreateView(LayoutInflater inflater,
			final ViewGroup container, Bundle bundle) {
		View rootView = inflater.inflate(R.layout.login_activity, container,
				false);
		context = getActivity();
		Bundle args = getArguments();
		this.profileof = args.getString("profileof");

		patient = (BootstrapButton) rootView.findViewById(R.id.patientlogin);
		hospital = (BootstrapButton) rootView.findViewById(R.id.hospitallogin);
		addcase = (BootstrapButton) rootView.findViewById(R.id.doctorlogin);

		hospital.setVisibility(View.GONE);

		patient.setText("Add a patient");

		addcase.setText("Add new case..");
		dialog = new Dialog(getActivity(), R.style.AppTheme);
		alertdialog = new Dialog(getActivity());

		patient.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent i = new Intent(context, AddPatientActivity.class);

				startActivity(i);

			}
		});

		addcase.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent i = new Intent(context, AddCaseActivity.class);

				startActivity(i);

			}
		});

		return rootView;
	}

	protected void launch(final Context context, final String string) {

		dialog.setContentView(R.layout.searchdialog);
		dialog.setTitle("Search details... ");

		uid = (EditText) dialog.findViewById(R.id.searchuserid);

		BootstrapButton goback = (BootstrapButton) dialog
				.findViewById(R.id.searchnegative);

		goback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		BootstrapButton login = (BootstrapButton) dialog
				.findViewById(R.id.searchpositive);

		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String userid = uid.getText().toString();

				if (userid.equals("")) {

					createAlert("Userid is undefined", context);
					dialog.dismiss();
				} else {
					if (userid.contains("@")) {
						searchof = string;
						dialog.dismiss();
						new SearchAsync().execute();

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
						if (count == 0) {
							searchof = string;
							dialog.dismiss();
							new SearchAsync().execute();
						} else
							createAlert(
									"This field should get data in form of emailid or id containing 0-9 only.",
									context);

					}
				}

			}
		});

		dialog.show();

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

		alertdialog.show();

		alertdialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
	}

	JSONObject json = null;

	private ProgressDialog pDialog = null;

	JSONParser jParser = new JSONParser();

	class SearchAsync extends AsyncTask<String, String, String> {
		private String message;
		private ProgressDialog pDialog;

		protected void onPreExecute(Context context) {
			super.onPreExecute();

			message = "";

			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Fetching account information...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();

		}

		protected String doInBackground(String... args) {
			if (Vars.isURLReachable(context)) {
				String url = Vars.getIp() + "getdetails.php";

				List<NameValuePair> params = new ArrayList<NameValuePair>();
				String type = "";
				String userid = uid.getText().toString();
				if (userid != "") {
					if (userid.contains("@"))
						type = "email";
					else
						type = "id";
				}

				params.add(new BasicNameValuePair("type", type));
				params.add(new BasicNameValuePair("tag", searchof));
				params.add(new BasicNameValuePair("pid", userid));

				json = jParser.makeHttpRequest(url, "POST", params);

				Log.d("Create Response", json.toString());

				try {

					if (json.get("error_msg").equals("")) {

						message = "success";
						Log.d("make", "yes");
					} else {
						message = "absent";

					}
				} catch (JSONException e) {

					message = "server";

				}
			} else
				message = "problem";
			return null;
		}

		protected void onPostExecute(String file_url) {

			if (message.equals("success")) {
				if (dialog.isShowing())
					dialog.dismiss();
				if (alertdialog.isShowing())
					alertdialog.dismiss();
				Intent i = new Intent(getActivity(), ProfileActivity.class);
				i.putExtra("profileof", searchof);

				i.putExtra("author", "other");
				i.putExtra("pid", uid.getText().toString());
				startActivity(i);

			} else if (message.equals("problem"))
				Toast.makeText(context,
						"Server Down...or internet connection problem..",
						Toast.LENGTH_SHORT).show();
			else {
				createAlert("No such user or server error.", context);
			}
			if (!(pDialog == null))
				pDialog.dismiss();

		}

	}

}
