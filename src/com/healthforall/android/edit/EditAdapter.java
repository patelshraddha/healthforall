package com.healthforall.android.edit;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.healthforall.android.JSONParser;
import com.healthforall.android.R;
import com.healthforall.android.Vars;
import android.app.Activity;

public class EditAdapter extends BaseAdapter {
	Context context;
	String[] taluka;
	String[] district;
	String[] state;
	String[] type;
	String[] buttons;
	String[] values;
	int count = 0;
	EditText[] value = new EditText[15];

	LayoutInflater inflater;
	private View itemView;
	private String profileof;
	private String pid;
	private String newstate = "";
	private String newdistrict = "";
	private ArrayAdapter<String> adapter0, adapter1, adapter2, adapter3;
	private Spinner valuer0, valuer1, valuer2, valuer3;
	private BootstrapButton savebutton;
	private String name = "", surname = "", phone = "", email = "",
			address = "", updatetype = "", updatestate = "", updatecity = "",
			updatetaluka = "", updatedistrict = "";
	private Dialog alertdialog;

	public EditAdapter(Context context, String[] buttons, String[] district,
			String[] state, String[] type, String[] values, String[] taluka,
			String profileof, String pid) {
		this.context = context;
		this.buttons = buttons;
		this.district = district;
		this.taluka = taluka;
		this.type = type;
		this.values = values;
		this.state = state;
		this.profileof = profileof;
		this.pid = pid;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return buttons.length + 1;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		itemView = null;
		// Declare Variables

		BootstrapButton button;

		Point size = new Point();

		Display display;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			WindowManager wm = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
			display = wm.getDefaultDisplay();

			display.getSize(size);
		} else {
			display = ((Activity) context).getWindowManager()
					.getDefaultDisplay();
			size.x = display.getWidth();
			size.y = display.getHeight();
		}

		int width = size.x;
		int height = size.y;

		if (position < (getCount() - 1)) {
			Boolean condition = (position == 3) || (position == 7)
					|| (position == 8) || (position == 6);

			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if (!(condition && profileof.equals("hospital"))) {
				if (itemView == null) {
					itemView = (View) inflater.inflate(R.layout.editlistitem,
							parent, false);
				}

				value[count] = (EditText) itemView.findViewById(R.id.editvalue);
				value[count].setEnabled(true);
				button = (BootstrapButton) itemView
						.findViewById(R.id.editbutton);
				value[count].setText(values[position]);
				button.setText(buttons[position]);
				value[count].setWidth((int) (width * 0.6));
				button.setMinimumWidth((int) (width * 0.4));
				count++;
			} else {
				if (itemView == null) {
					itemView = (View) inflater.inflate(R.layout.autolistitem,
							parent, false);
				}
				String[] data = new String[] {};
				if (position == 3)
					data = type;
				else if (position == 6)
					data = taluka;
				else if (position == 7)
					data = district;
				else
					data = state;
				int pos = 0;
				switch (position) {
				case 8: {
					pos = 3;
					valuer3 = (Spinner) itemView.findViewById(R.id.autovalue);
					adapter3 = new ArrayAdapter<String>(context,
							android.R.layout.simple_spinner_item, data);
					adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					valuer3.setAdapter(adapter3);
					valuer3.setOnItemSelectedListener(new OnItemSelectedListener() {

						public void onItemSelected(AdapterView<?> parent,
								View view, int t, long id) {

							{
								// Toast.makeText(context,"Here we are"+parent.getItemAtPosition(pos).toString(),Toast.LENGTH_SHORT).show();
								newstate = parent.getItemAtPosition(t)
										.toString();
								new DistrictAsync().execute();

							}
						}

						public void onNothingSelected(AdapterView<?> parent) {
							new DistrictAsync();
							// if(pos==8)

						}
					});

				}

					break;
				case 7: {
					pos = 2;
					valuer2 = (Spinner) itemView.findViewById(R.id.autovalue);
					adapter2 = new ArrayAdapter<String>(context,
							android.R.layout.simple_spinner_item, data);
					adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					valuer2.setAdapter(adapter2);
					valuer2.setOnItemSelectedListener(new OnItemSelectedListener() {

						public void onItemSelected(AdapterView<?> parent,
								View view, int t, long id) {

							{
								// Toast.makeText(context,"Here we are"+parent.getItemAtPosition(pos).toString(),Toast.LENGTH_SHORT).show();
								newdistrict = parent.getItemAtPosition(t)
										.toString();
								new TalukaAsync().execute();

							}
						}

						public void onNothingSelected(AdapterView<?> parent) {

						}
					});
				}
					break;
				case 6: {
					pos = 1;
					valuer1 = (Spinner) itemView.findViewById(R.id.autovalue);
					adapter1 = new ArrayAdapter<String>(context,
							android.R.layout.simple_spinner_item, data);
					adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					valuer1.setAdapter(adapter1);
				}
					break;
				case 3: {
					pos = 0;
					valuer0 = (Spinner) itemView.findViewById(R.id.autovalue);
					adapter0 = new ArrayAdapter<String>(context,
							android.R.layout.simple_spinner_item, data);
					adapter0.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					valuer0.setAdapter(adapter0);
				}
					break;
				}

				button = (BootstrapButton) itemView
						.findViewById(R.id.autobutton);

				button.setText(buttons[position]);

				button.setMinimumWidth((int) (width * 0.4));
			}

			return itemView;
		} else {
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			if (itemView == null) {
				itemView = (View) inflater.inflate(R.layout.buttonitem, parent,
						false);
			}
			savebutton = (BootstrapButton) itemView
					.findViewById(R.id.savebutton);
			savebutton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					name = value[0].getText().toString().trim();
					if (profileof.equals("patient")) {
						surname = value[1].getText().toString().trim();
						phone = value[2].getText().toString().trim();
						email = value[3].getText().toString().trim();
						address = value[4].getText().toString().trim();
						if ((surname == "") || (name == "") || (address == ""))
							createAlert("Name, surname or address cannot be empty");
						else
							new StoreAsync().execute();

					} else if (profileof.equals("doctor")) {
						surname = value[1].getText().toString().trim();
						phone = value[2].getText().toString().trim();
						email = value[3].getText().toString().trim();

						if ((surname == "") || (name == "") || (email == ""))
							createAlert("Name, surname or email cannot be empty");
						else if (!email.contains("@"))
							createAlert("Not a valid email id");
						else
							new StoreAsync().execute();
					} else if (profileof.equals("hospital")) {

						phone = value[1].getText().toString().trim();
						email = value[2].getText().toString().trim();
						address = value[3].getText().toString().trim();
						updatecity = value[4].getText().toString().trim();
						if ((phone == "") || (name == "") || (email == "")
								|| (address == "") || (updatecity == ""))
							createAlert("Name,phone,city/village, address or email cannot be empty");
						else if (!email.contains("@"))
							createAlert("Not a valid email id");
						else
							new StoreAsync().execute();
					}

				}

			});

			return itemView;

		}

	}

	protected void createAlert(String message) {
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

		alertdialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);

	}

	JSONObject json2 = null;

	private ProgressDialog pDialog = null;

	JSONParser jParser = new JSONParser();

	class DistrictAsync extends AsyncTask<String, String, String> {
		private String message;
		

		protected void onPreExecute() {
			super.onPreExecute();

			message = "";

			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Fetching district information...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();

		}

		protected String doInBackground(String... args) {
			if (Vars.isURLReachable(context)) {
				String url = Vars.getIp() + "gettables.php";

				List<NameValuePair> params2 = new ArrayList<NameValuePair>();
				params2.add(new BasicNameValuePair("tag", "district"));
				params2.add(new BasicNameValuePair("state", newstate));

				json2 = jParser.makeHttpRequest(url, "POST", params2);
				try {

					if (json2.get("error_msg").equals("")) {

						message = "success";

					} else {
						message = "";

					}
				} catch (JSONException e) {

					message = "server";

				}
			} else
				message = "problem";
			return null;
		}

		protected void onPostExecute(String file_url) {
			if (!(pDialog == null))
				pDialog.dismiss();
			if (message.equals("server")) {
				Toast.makeText(context, "Server error..try again",
						Toast.LENGTH_SHORT).show();
			} else if (message.equals("problem")) {
				Toast.makeText(context,
						"Internet connection error or server down..",
						Toast.LENGTH_SHORT).show();
			}

			else if (message.equals("success")) {
				try {
					JSONArray details = json2.getJSONArray("details");
					district = new String[details.length()];
					for (int i = 0; i < details.length(); i++) {
						JSONObject p = details.getJSONObject(i);
						district[i] = p.getString("name");
					}

					adapter2 = new ArrayAdapter<String>(context,
							android.R.layout.simple_spinner_item, district);
					adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					valuer2.setAdapter(adapter2);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				Toast.makeText(context, "Server error..try again",
						Toast.LENGTH_SHORT).show();
			}

		}

	}

	class TalukaAsync extends AsyncTask<String, String, String> {
		private String message;
		private ProgressDialog pDialog;

		protected void onPreExecute() {
			super.onPreExecute();

			message = "";

			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Fetching taluka information...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();

		}

		protected String doInBackground(String... args) {

			if (Vars.isURLReachable(context)) {
				String url = Vars.getIp() + "gettables.php";

				List<NameValuePair> params2 = new ArrayList<NameValuePair>();
				params2.add(new BasicNameValuePair("tag", "taluka"));
				params2.add(new BasicNameValuePair("district", newdistrict));

				json2 = jParser.makeHttpRequest(url, "POST", params2);
				try {

					if (json2.get("error_msg").equals("")) {

						message = "success";

					} else {
						message = "";

					}
				} catch (JSONException e) {

					message = "server";

				}
			} else
				message = "problem";
			return null;
		}

		protected void onPostExecute(String file_url) {
			if (!(pDialog == null))
				pDialog.dismiss();
			if (message.equals("server")) {
				Toast.makeText(context, "Server error..try again",
						Toast.LENGTH_SHORT).show();
			} else if (message.equals("problem")) {
				Toast.makeText(context,
						"Internet connection error or server down..",
						Toast.LENGTH_SHORT).show();
			}

			else if (message.equals("success")) {
				try {
					JSONArray details = json2.getJSONArray("details");
					taluka = new String[details.length()];
					for (int i = 0; i < details.length(); i++) {
						JSONObject p = details.getJSONObject(i);
						taluka[i] = p.getString("name");
					}

					adapter1 = new ArrayAdapter<String>(context,
							android.R.layout.simple_spinner_item, taluka);
					adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					valuer1.setAdapter(adapter1);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				Toast.makeText(context, "Server error..try again",
						Toast.LENGTH_SHORT).show();
			}

		}

	}

	class StoreAsync extends AsyncTask<String, String, String> {
		private String message;
		private ProgressDialog pDialog1=null;
		

		protected void onPreExecute() {
			super.onPreExecute();

			message = "";

			pDialog1 = new ProgressDialog(context);
			pDialog1.setMessage("Updating information...");
			pDialog1.setIndeterminate(false);
			pDialog1.setCancelable(true);
			pDialog1.show();

		}

		protected String doInBackground(String... args) {

			if (Vars.isURLReachable(context)) {

				String url = Vars.getIp() + profileof + "/update" + profileof
						+ ".php";

				List<NameValuePair> params2 = new ArrayList<NameValuePair>();
				
				if (profileof.equals("patient")) {
					Log.d("Tag","HEre"+profileof);
					params2.add(new BasicNameValuePair("name", name));
					params2.add(new BasicNameValuePair("surname", surname));
					params2.add(new BasicNameValuePair("pid", pid));
					params2.add(new BasicNameValuePair("email", email));
					params2.add(new BasicNameValuePair("phone", phone));
					params2.add(new BasicNameValuePair("address", address));
				} else if (profileof.equals("doctor")) {
					params2.add(new BasicNameValuePair("name", name));
					params2.add(new BasicNameValuePair("surname", surname));
					params2.add(new BasicNameValuePair("pid", pid));
					params2.add(new BasicNameValuePair("email", email));
					params2.add(new BasicNameValuePair("phone", phone));

				} else if (profileof.equals("hospital")) {
					Log.d("Tag","Why");
					if (valuer0.getSelectedItem().toString().trim() != null)
						updatetype = valuer0.getSelectedItem().toString()
								.trim();

					if (valuer1.getSelectedItem().toString().trim() != null)
						updatetaluka = valuer1.getSelectedItem().toString()
								.trim();
					if (valuer2.getSelectedItem().toString().trim() != null)
						updatedistrict = valuer2.getSelectedItem().toString()
								.trim();
					if (valuer3.getSelectedItem().toString().trim() != null)
						updatestate = valuer3.getSelectedItem().toString()
								.trim();

					params2.add(new BasicNameValuePair("name", name));
					params2.add(new BasicNameValuePair("pid", pid));
					params2.add(new BasicNameValuePair("email", email));
					params2.add(new BasicNameValuePair("phone", phone));
					params2.add(new BasicNameValuePair("address", address));
					params2.add(new BasicNameValuePair("type", updatetype));
					params2.add(new BasicNameValuePair("city", updatecity));
					params2.add(new BasicNameValuePair("taluka", updatetaluka));
					params2.add(new BasicNameValuePair("district",
							updatedistrict));
					params2.add(new BasicNameValuePair("state", updatestate));
				}

				json2 = jParser.makeHttpRequest(url, "POST", params2);
				try {

					if (json2.get("error_msg").equals("")) {

						message = "success";

					} else {
						message = "";

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
			}
			if (message.equals("problem")) {
				Toast.makeText(context,
						"Internet connection error or server down",
						Toast.LENGTH_SHORT).show();
			} else if (message.equals("success")) {
				SharedPreferences settings = context.getSharedPreferences(
						"storage", 0);
				SharedPreferences.Editor editor = settings.edit();

				editor.putString("name", name);
				editor.putString("phone", phone);
				editor.putString("email", email);

				if (profileof.equals("patient")) {

					editor.putString("surname", surname);
					editor.putString("address", address);
					

				}
				else if (profileof.equals("doctor")) {

					editor.putString("surname", surname);
							

				}
				else if (profileof.equals("hospital")) {

					editor.putString("city", updatecity);
					editor.putString("address", address);
					editor.putString("type", updatetype);
					editor.putString("taluka", updatetaluka);
					editor.putString("state", updatestate);
					editor.putString("district", updatedistrict);
					
				}

				editor.commit();
				Toast.makeText(context, "Update successful..",
						Toast.LENGTH_SHORT).show();
				
				if (!(pDialog1 == null))
					pDialog1.dismiss();
				((Activity) (context)).finish();

			} else {
				Toast.makeText(context, "Server error..try again",
						Toast.LENGTH_SHORT).show();
			}
			if (!(pDialog1 == null))
				pDialog1.dismiss();

		}

	}

}
