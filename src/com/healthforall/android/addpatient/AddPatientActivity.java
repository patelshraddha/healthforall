package com.healthforall.android.addpatient;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.healthforall.android.JSONParser;
import com.healthforall.android.R;
import com.healthforall.android.Vars;


public class AddPatientActivity extends Activity{
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
	private AddPatientAdapter adapter;
	private ListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_activity);
		SharedPreferences settings = getSharedPreferences("storage", 0);
		profileof = settings.getString("loginvia", "");
		
			buttons = new String[] { "Name", "Surname","Password", "Phone", "Email",
					"Address" };
	

		
		list = (ListView) findViewById(R.id.profilelistview);

		adapter = new AddPatientAdapter(this,buttons);
//		new EditAsync().execute();
		list.setAdapter(adapter);

	}
	JSONObject json = null;
	JSONObject json1 = null;
	

	private ProgressDialog pDialog=null;

	JSONParser jParser = new JSONParser();
    
    
}
