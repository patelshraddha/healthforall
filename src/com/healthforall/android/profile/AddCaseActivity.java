package com.healthforall.android.profile;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.healthforall.android.JSONParser;
import com.healthforall.android.R;
import com.healthforall.android.Vars;
import com.healthforall.android.edit.EditActivity;
import com.healthforall.android.edit.EditAdapter;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

public class AddCaseActivity extends Activity {
	@Override
	public void onBackPressed() {
		finish();
	}
	
	String[] buttons;
	String[] values;
	String[] ailments;
	String profileof;
	String pid;
	private AddCaseAdapter adapter;
	private ListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_activity);
		SharedPreferences settings = getSharedPreferences("storage", 0);
		
		
			buttons = new String[] { "Patient id", "Doctor id", "Ailment", "Ailmenttype",
					"Medication","Referred to","Minor/Major" };
		

		

		
		list = (ListView) findViewById(R.id.profilelistview);

		adapter = new AddCaseAdapter(this, buttons);
//		new EditAsync().execute();
		list.setAdapter(adapter);

	}

}
