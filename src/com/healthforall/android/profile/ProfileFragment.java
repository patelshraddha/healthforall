package com.healthforall.android.profile;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
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

/**
 * A fragment that launches other parts of the demo application.
 */
public class ProfileFragment extends Fragment {

	private ProfileViewAdapter adapter;
	private String[] buttons;
	private String[] details;
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
		context = getActivity();
		Bundle args = getArguments();
		this.profileof = args.getString("profileof");
		this.author = args.getString("author");
		this.pid = args.getString("pid");
		Log.d("tag is", pid + "here");
		
		   new ProfileAsync().execute();
		
		if (profileof.equals("patient"))
			buttons = new String[] { "Id", "Name", "Surname", "Phone", "Email",
					"Address" };
		else if (profileof.equals("hospital"))
			buttons = new String[] { "Id", "Name", "Phone", "Email", "Address",
					"Type", "City/village", "Taluka", "District", "State" };
		else if (profileof.equals("doctor"))
			buttons = new String[] { "Id", "Name", "Surname", "Phone", "Email" };
		else
			buttons = new String[] {};

		{
			if (profileof.equals("patient"))
				details = new String[] { "Id", "Name", "Surname", "Phone",
						"Email", "Address" };
			else if (profileof.equals("hospital"))
				details = new String[] { "Id", "Name", "Phone", "Email",
						"Address", "Type", "City/village", "Taluka",
						"District", "State" };
			else if (profileof.equals("doctor"))
				details = new String[] { "Id", "Name", "Surname", "Phone",
						"Email" };
			else
				details = new String[] {};
		}

		// details = new String[] { "Pakistan", "Nigeria", "Bangladesh",
		// "Russia", "Japan" };

		list = (ListView) rootView.findViewById(R.id.profilelistview);

		adapter = new ProfileViewAdapter(getActivity(), buttons, details);

		list.setAdapter(adapter);

		return rootView;
	}

	JSONObject json = null;

	private ProgressDialog pDialog=null;

	JSONParser jParser = new JSONParser();

	class ProfileAsync extends AsyncTask<String, String, String> {
		private String message;
		

		protected void onPreExecute(ProfileViewAdapter adapt) {
			super.onPreExecute();

			message = "";

			pDialog = new ProgressDialog(((Activity)context));
			pDialog.setMessage("Fetching account information...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();

		}

		protected String doInBackground(String... args) {
            
			
			if(Vars.isURLReachable(context)==true)
			{
			
			String url = Vars.getIp() + "getdetails.php";

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			String type = "";
			String userid = pid;
			if (pid != "") {
				if (userid.contains("@"))
					type = "email";
				else
					type = "id";
			}

			params.add(new BasicNameValuePair("type", type));
			params.add(new BasicNameValuePair("tag", profileof));
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
			}
			else
				message = "problem";

			return null;
		}

		protected void onPostExecute(String file_url) {
			if(message.equals("server"))
			 {
				 Toast.makeText(context,"Server error..try again",Toast.LENGTH_SHORT).show();
			 }
			else if(message.equals("problem"))
			 {
				 Toast.makeText(context,"Server down..or internet connection problems..",Toast.LENGTH_SHORT).show();
			 }
			 
			else if (message.equals("success")) {

				if (profileof.equals("patient"))
					buttons = new String[] { "Id", "Name", "Surname", "Phone",
							"Email", "Address" };
				else if (profileof.equals("hospital"))
					buttons = new String[] { "Id", "Name", "Phone", "Email",
							"Address", "Type", "City/village", "Taluka",
							"District", "State" };
				else if (profileof.equals("doctor"))
					buttons = new String[] { "Id", "Name", "Surname", "Phone",
							"Email" };
				else
					buttons = new String[] {};

				Log.d("heyo", profileof);

				try {
					if (profileof.equals("patient"))

						details = new String[] { json.getInt("pid") + "",
								json.getString("name"),
								json.getString("surname"),
								json.getInt("phone") + "",
								json.getString("email"),
								json.getString("address") };

					else if (profileof.equals("hospital"))
						details = new String[] { json.getInt("pid") + "",
								json.getString("name"),
								json.getInt("phone") + "",
								json.getString("email"),
								json.getString("address"),
								json.getString("type"), json.getString("city"),
								json.getString("taluka"),
								json.getString("district"),
								json.getString("state") };
					else if (profileof.equals("doctor"))
						details = new String[] { json.getInt("pid") + "",
								json.getString("name"),
								json.getString("surname"),
								json.getInt("phone") + "",
								json.getString("email") };
					else
						details = new String[] {};

					adapter = new ProfileViewAdapter(getActivity(), buttons,
							details);

					list.setAdapter(adapter);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			 
		 else 
		 {
			 Toast.makeText(context,"User is absent",Toast.LENGTH_SHORT).show();
		 }
          
			if(pDialog!=null)
				pDialog.dismiss();

		}

	}

}
