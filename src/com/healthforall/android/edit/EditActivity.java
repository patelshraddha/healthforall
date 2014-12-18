package com.healthforall.android.edit;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.healthforall.android.JSONParser;
import com.healthforall.android.R;
import com.healthforall.android.Vars;
import com.healthforall.android.profile.ProfileViewAdapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends Activity {

	@Override
	public void onBackPressed() {
		finish();
	}
	String[] taluka=new String[]{};
	String[] district=new String[]{};
	String[] state=new String[]{};
	String[] type=new String[]{};
	String[] buttons;
	String[] values;
	String profileof;
	String pid;
	private EditAdapter adapter;
	private ListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_activity);
		SharedPreferences settings = getSharedPreferences("storage", 0);
		profileof = settings.getString("loginvia", "");
		pid = settings.getString("pid", "");
		if (profileof.equals("patient"))
			buttons = new String[] { "Name", "Surname", "Phone", "Email",
					"Address" };
		else if (profileof.equals("hospital"))
			buttons = new String[] { "Name", "Phone", "Email", "Type",
					"Address", "City/village", "Taluka", "District", "State" };
		else if (profileof.equals("doctor"))
			buttons = new String[] { "Name", "Surname", "Phone", "Email" };
		else
			buttons = new String[] {};

		if (profileof.equals("patient"))
			values = new String[] { settings.getString("name", ""),
					settings.getString("surname", ""),
					settings.getString("phone", ""),
					settings.getString("email", ""),
					settings.getString("address", "") };
		else if (profileof.equals("hospital"))
			values = new String[] { settings.getString("name", ""),
					settings.getString("phone", ""),
					settings.getString("email", ""),
					settings.getString("address", ""),
					settings.getString("type", ""),
					settings.getString("taluka", ""),
					settings.getString("district", ""),
					settings.getString("state", "") };
		else if (profileof.equals("doctor"))
			values = new String[] { settings.getString("name", ""),
				settings.getString("surname", ""),
				settings.getString("phone", ""),
				settings.getString("email", "") };
		else
			values = new String[] {};

		
		list = (ListView) findViewById(R.id.profilelistview);

		adapter = new EditAdapter(this, buttons, district, state, type, values,
				taluka, profileof, pid);
		new EditAsync().execute();
		list.setAdapter(adapter);

	}
	JSONObject json = null;
	JSONObject json1 = null;
	

	private ProgressDialog pDialog=null;

	JSONParser jParser = new JSONParser();
    
    class EditAsync extends AsyncTask<String, String, String> {
		private String message;
		private ProgressDialog pDialog;
		
		

		protected void onPreExecute() {
			super.onPreExecute();
            
			message = "";

			 pDialog = new ProgressDialog(EditActivity.this);
			 pDialog.setMessage("Fetching information...");
			 pDialog.setIndeterminate(false); pDialog.setCancelable(true);
			 pDialog.show();

		}

		protected String doInBackground(String... args) {
			if (Vars.isURLReachable(EditActivity.this)) {
			String url = Vars.getIp() + "gettables.php";

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			List<NameValuePair> params1 = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tag","hospitaltype"));
            params1.add(new BasicNameValuePair("tag","state"));
            
			json = jParser.makeHttpRequest(url, "POST", params);
			json1 = jParser.makeHttpRequest(url, "POST", params1);
			
			try {

				if (json.get("error_msg").equals("")&&json1.get("error_msg").equals("")) {

					message = "success";
					
				} else {
					message = "";

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
				 Toast.makeText(EditActivity.this,"Server error..try again",Toast.LENGTH_SHORT).show();
			 }
			 if(message.equals("problem"))
			 {
				 Toast.makeText(EditActivity.this,"Server down or internet connection error...",Toast.LENGTH_SHORT).show();
			 }
			 else if(message.equals("success"))
			 {
				 try {
					JSONArray details = json.getJSONArray("details");
					type =new String[details.length()];
					for(int i=0;i<details.length();i++)
					{
					 JSONObject p = details.getJSONObject(i);
					  type[i]=p.getString("name");
					}
					details = json1.getJSONArray("details");
					state =new String[details.length()];
					for(int i=0;i<details.length();i++)
					{
					 JSONObject p = details.getJSONObject(i);
					  state[i]=p.getString("name");
					}
					adapter = new EditAdapter(EditActivity.this, buttons, district, state, type, values,
							taluka, profileof, pid);
					
					list.setAdapter(adapter);
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
				 
			 }	 
			 else 
			 {
				 Toast.makeText(EditActivity.this,"Server error..try again",Toast.LENGTH_SHORT).show();
			 }
			 
			
			
			

				
			}

			

		}
    
   
	
}
