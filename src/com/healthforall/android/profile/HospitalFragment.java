package com.healthforall.android.profile;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
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
import android.content.Context;

public class HospitalFragment extends Fragment {

	private HospitalViewAdapter adapter;
	private String[] files;
	private String[] designation;
	private String[] timings;
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
		if (profileof.equals("hospital")) {
			files = new String[] { "Doctors" };
			designation = new String[] { "Designation" };
			timings = new String[] { "Timings" };
		} else {
			files = new String[] { "Hospitals" };
			designation = new String[] { "Designation" };
			timings = new String[] { "Timings" };

		}

		list = (ListView) rootView.findViewById(R.id.profilelistview);

		adapter = new HospitalViewAdapter(getActivity(), files, designation,timings,new String[]{},profileof);

		list.setAdapter(adapter);
		
			new HospitalAsync().execute();
			return rootView;
	}

	JSONObject json = null;

	private ProgressDialog pDialog = null;

	JSONParser jParser = new JSONParser();

	class HospitalAsync extends AsyncTask<String, String, String> {
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
			if(Vars.isURLReachable(context))
			{
			String url = Vars.getIp() + "gethospitaldoctor.php";

			params = new ArrayList<NameValuePair>();

			String tag = "";
			if (profileof.equals("doctor"))
				tag = "doctor";
			else
				tag = "hospital";

			params.add(new BasicNameValuePair("tag", tag));
			params.add(new BasicNameValuePair("pid", pid));

			json = jParser.makeHttpRequest(url, "POST", params);

			Log.d("Create Response", json.toString() + tag + pid);

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

			if (message.equals("server")) {
				Toast.makeText(context, "Server error..try again",
						Toast.LENGTH_SHORT).show();
			}
			else if (message.equals("problem")) {
				Toast.makeText(context, "Server down or internet conection error..",
						Toast.LENGTH_SHORT).show();
			}

			else if (message.equals("success")) {
				try {
					details = json.getJSONArray("details");
					int count = details.length() + 1;
					files = new String[count];
					designation = new String[count];
					timings = new String[count];
					String[] pidr = new String[count];

					if (profileof.equals("doctor"))
						files[0] = "Hospital";
					else
						files[0] = "Doctor";

					designation[0] = "Designation";
					timings[0] = "Timings";
					
					for (int i = 0; i < details.length(); i++) {
						JSONObject c = details.getJSONObject(i);
						
						designation[i + 1] = c.getString("designation");
						timings[i + 1] = c.getString("timings");
						pidr[i+1]=c.getInt("pid")+"";
						
						if (profileof.equals("doctor"))
							files[i + 1] = c.getString("name");
						else
							files[i + 1] = c.getString("name")+"  "+c.getString("surname");
						
						

						

					}
					adapter = new HospitalViewAdapter(getActivity(), files,
							designation, timings,pidr,profileof);

					list.setAdapter(adapter);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				if(profileof.equals("doctor"))
				Toast.makeText(context, "No records of hospital served in...", Toast.LENGTH_SHORT)
						.show();
				else
					Toast.makeText(context, "No records of doctor serving in this hospital...", Toast.LENGTH_SHORT)
					.show();
			}
			if(!(pDialog==null))
			 pDialog.dismiss();

		}

	}

}
