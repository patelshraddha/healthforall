package com.healthforall.android.profile;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.healthforall.android.JSONParser;
import com.healthforall.android.R;
import com.healthforall.android.Vars;
import com.healthforall.android.profile.HospitalFragment.HospitalAsync;

public class ViewCase extends Fragment {

	private ViewCaseAdapter adapter;
	private String[] dates = new String[] { "Date" };
	private String[] hosp = new String[] { "Hospital" };
	private String[] doc = new String[] { "Doctor" };
	private String[] ref = new String[] { "Referred to" };
	private String[] type = new String[] { "Ailment type" };
	private String[] description = new String[] { "Description" };
	private String[] major = new String[] { "Major/Minor" };
	private String[] medication = new String[] { "Medication" };

	private ListView list;
	private String profileof;
	private String author;
	private String pid;
	private Context context;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle bundle) {

		View rootView = inflater.inflate(R.layout.profile_activity, container,
				false);

		Bundle args = getArguments();
		this.profileof = args.getString("profileof");
		this.author = args.getString("author");
		this.pid = args.getString("pid");
		context = getActivity();

		list = (ListView) rootView.findViewById(R.id.profilelistview);

		adapter = new ViewCaseAdapter(getActivity(), dates, hosp, doc, ref,
				type, description, major, medication);

		list.setAdapter(adapter);

		new DataAsync().execute();
		return rootView;
	}

	JSONObject json = null;

	private ProgressDialog pDialog = null;

	JSONParser jParser = new JSONParser();

	class DataAsync extends AsyncTask<String, String, String> {
		private String message;
		private ProgressDialog pDialog;
		private JSONArray details;
		private ArrayList<NameValuePair> params;

		protected void onPreExecute(ProfileViewAdapter adapt) {
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
				String url = Vars.getIp() + "getcases.php";

				params = new ArrayList<NameValuePair>();

				params.add(new BasicNameValuePair("pid", pid));

				json = jParser.makeHttpRequest(url, "POST", params);

				 Log.d("Create Response", json.toString()+""+pid);

				try {

					if (json.get("error_msg").equals("")) {

						message = "success";

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

			if (message.equals("server")) {
				Toast.makeText(context, "Server error..try again",
						Toast.LENGTH_SHORT).show();
			} else if (message.equals("problem")) {
				Toast.makeText(context,
						"Server down or internet conection error..",
						Toast.LENGTH_SHORT).show();
			}

			else if (message.equals("success")) {
				try {
					details = json.getJSONArray("details");
					int count = details.length() ;
					dates = new String[count];
					hosp = new String[count];
					doc = new String[count];
					ref = new String[count];
					type = new String[count];
					description = new String[count];
					major = new String[count];
					medication = new String[count];

//					dates[0] = "Date";
//					hosp[0] = "Hospital";
//					doc[0] = "Doctor";
//					ref[0] = "Referred to";
//					type[0] = "Ailment type";
//					description[0] = "Description";
//					major[0] = "Major/Minor";
//					medication[0] = "Medication";

					for (int i = 0; i < details.length(); i++) {
						JSONObject c = details.getJSONObject(i);
						dates[i] = c.getString("dates");
						
						
						hosp[i] = "Hospital: \n"+"Name: "+c.getString("hname")+"\n ID: "+c.getString("hid");
						doc[i] = "Doctor: \n"+"Name: "+c.getString("dname")+"\n ID: "+c.getString("did");;
						ref[i] ="Doctor Refrenced: \n"+"Name:"+ c.getString("rname")+"\nID: "+c.getString("rid");;
						type[i] = "Type:"+c.getString("type");
						description[i] ="Description: \n"+ c.getString("description");
						major[i] = "Major/Minor: \n"+c.getString("major");
						medication[i] = "Medication: \n"+c.getString("medication");
						
						
						

					}
					adapter = new ViewCaseAdapter(getActivity(), dates, hosp,
							doc, ref, type, description, major, medication);

					list.setAdapter(adapter);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {

				Toast.makeText(context, "No records yet...", Toast.LENGTH_SHORT)
						.show();

			}
			if (!(pDialog == null))
				pDialog.dismiss();

		}

	}

}
