package com.healthforall.android.profile;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.healthforall.android.JSONParser;
import com.healthforall.android.R;
import com.healthforall.android.Vars;

public class AddCaseAdapter extends BaseAdapter {
	Context context;
	String[] ailments = new String[] {};
	String[] buttons;
	String[] values;
	String[] hids = new String[] {};
	String[] type = new String[] { "minor", "major" };
	int count = 0;
	EditText[] value;

	LayoutInflater inflater;
	private View itemView;
	private ArrayAdapter<String> adapter0, adapter1, adapter2;
	private Spinner valuer0, valuer1, valuer2;
	private BootstrapButton savebutton;

	private Dialog alertdialog;

	public AddCaseAdapter(Context context, String[] buttons) {
		this.context = context;
		this.buttons = buttons;
		this.value = new EditText[buttons.length + 5];

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
			Boolean condition = (position == 0) || (position == 2)
					|| (position == 4) || (position == 5);

			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if (condition) {
				if (itemView == null) {
					itemView = (View) inflater.inflate(R.layout.editlistitem,
							parent, false);
				}

				value[position] = (EditText) itemView
						.findViewById(R.id.editvalue);
				value[position].setEnabled(true);
				button = (BootstrapButton) itemView
						.findViewById(R.id.editbutton);
				if (position == 5) {
					value[position].setEnabled(false);
					value[position].setBackgroundColor(Color.GRAY);
				}
				button.setText(buttons[position]);
				value[position].setWidth((int) (width * 0.6));
				button.setMinimumWidth((int) (width * 0.4));
				count++;
			} else {
				if (itemView == null) {
					itemView = (View) inflater.inflate(R.layout.autolistitem,
							parent, false);
				}
				String[] data = new String[] {};
				if (position == 1)
					data = hids;
				else if (position == 6)
					data = type;
				else if (position == 3)
					data = ailments;

				int pos = 0;
				switch (position) {
				case 1: {

					valuer0 = (Spinner) itemView.findViewById(R.id.autovalue);
					adapter0 = new ArrayAdapter<String>(context,
							android.R.layout.simple_spinner_item, data);
					adapter0.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					valuer0.setAdapter(adapter0);
					new GetDoctorsAsync().execute();

				}

					break;
				case 3: {

					valuer1 = (Spinner) itemView.findViewById(R.id.autovalue);
					adapter1 = new ArrayAdapter<String>(context,
							android.R.layout.simple_spinner_item, data);
					adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					valuer1.setAdapter(adapter1);
					new GetAilmentsAsync().execute();
				}
					break;
				case 6: {
					pos = 1;
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
								if (valuer2.getSelectedItem().toString()
										.equals("minor")) {
									value[5].setBackgroundColor(Color.rgb(192,
											192, 192));
									value[5].setEnabled(false);
									value[5].setText("");
								} else {

									Drawable shapes = context.getResources()
											.getDrawable(R.drawable.shapes);
									if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
										value[5].setBackground(shapes);
									else
										value[5].setBackgroundDrawable(shapes);
									value[5].setEnabled(true);
								}
							}
						}

						public void onNothingSelected(AdapterView<?> parent) {

						}
					});
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
					int count = 0;
					String ailment = value[2].getText().toString().trim();
					String medication = value[4].getText().toString().trim();

					String major = valuer2.getSelectedItem().toString();
					if (major.equals("major")) {
						String refid = value[5].getText().toString().trim();
						if (refid.length() == 0) {
							count = 5;
						} else {
							int counter = 0;
							for (int i = 0; i < refid.length(); i++) {
								char p = refid.charAt(i);
								boolean check = (p == '0') || (p == '1')
										|| (p == '2') || (p == '3')
										|| (p == '4') || (p == '5')
										|| (p == '6') || (p == '7')
										|| (p == '8') || (p == '9');
								if (!check) {
									counter++;
									break;
								}
							}
							if (counter > 0)
								count = 6;
						}
					}

					if (medication.length() == 0)
						count = 4;

					if (ailment.length() == 0)
						count = 3;

					String pid = value[0].getText().toString().trim();
					if (pid.length() == 0) {
						count = 1;
					} else {
						int counter = 0;
						for (int i = 0; i < pid.length(); i++) {
							char p = pid.charAt(i);
							boolean check = (p == '0') || (p == '1')
									|| (p == '2') || (p == '3') || (p == '4')
									|| (p == '5') || (p == '6') || (p == '7')
									|| (p == '8') || (p == '9');
							if (!check) {
								counter++;
								break;
							}
						}
						if (counter > 0)
							count = 2;
					}

					String message = "";

					switch (count) {
					case 0:
						message = "";
						break;
					case 1:
						message = "Patient id cannot be undefined.";
						break;
					case 2:
						message = "Patient id contains digits only.";
						break;
					case 3:
						message = "Ailment cannot be undefined.";
						break;
					case 4:
						message = "Medication cannot be undefined.";
						break;
					case 5:
						message = "Id of the doctor referred to cannot be undefined.";
						break;
					case 6:
						message = "Id of the doctor referred to contains digits only.";
						break;
					}
					if (!message.equals(""))
						Toast.makeText(context, message, Toast.LENGTH_SHORT)
								.show();
					else
						new storeAsync().execute();

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

	JSONObject json = null;
	JSONObject json1 = null;
	JSONObject json2 = null;

	private ProgressDialog pDialog = null;

	JSONParser jParser = new JSONParser();

	class GetDoctorsAsync extends AsyncTask<String, String, String> {
		private String message;
		private ProgressDialog pDialog;

		protected void onPreExecute() {
			super.onPreExecute();

			message = "";

			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Fetching doctor information...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();

		}

		protected String doInBackground(String... args) {

			if (Vars.isURLReachable(context)) {
				String url = Vars.getIp() + "gethospitaldoctor.php";

				List<NameValuePair> params2 = new ArrayList<NameValuePair>();
				params2.add(new BasicNameValuePair("tag", "hospital"));
				SharedPreferences settings = context.getSharedPreferences(
						"storage", 0);
				params2.add(new BasicNameValuePair("pid", settings.getString(
						"pid", "")));

				json1 = jParser.makeHttpRequest(url, "POST", params2);
				Log.d("Here", json1.toString());
				try {

					if (json1.get("error_msg").equals("")) {

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

			if (message.equals("problem")) {
				Toast.makeText(context,
						"Server down or internet connection problem..",
						Toast.LENGTH_SHORT).show();
			}

			else if (message.equals("server")) {
				Toast.makeText(context, "Server error..try again",
						Toast.LENGTH_SHORT).show();
			} else if (message.equals("success")) {
				try {
					JSONArray details = json1.getJSONArray("details");
					hids = new String[details.length()];
					for (int i = 0; i < details.length(); i++) {
						JSONObject p = details.getJSONObject(i);
						hids[i] = p.getString("name") + " "
								+ p.getString("surname") + " " + "ID:"
								+ p.getString("pid");
					}

					adapter0 = new ArrayAdapter<String>(context,
							android.R.layout.simple_spinner_item, hids);
					adapter0.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					valuer0.setAdapter(adapter0);

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

	class GetAilmentsAsync extends AsyncTask<String, String, String> {
		private String message;
		private ProgressDialog pDialog;

		protected void onPreExecute() {
			super.onPreExecute();

			message = "";

			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Fetching diseases information...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();

		}

		protected String doInBackground(String... args) {

			if (Vars.isURLReachable(context)) {

				String url = Vars.getIp() + "getdiseases.php";

				List<NameValuePair> params2 = new ArrayList<NameValuePair>();

				json = jParser.makeHttpRequest(url, "POST", params2);
				Log.d("Debug", json.toString());
				try {

					if (json.get("error_msg").equals("")) {

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
			if (message.equals("problem")) {
				Toast.makeText(context,
						"Server down or no internet connection...",
						Toast.LENGTH_SHORT).show();
			} else if (message.equals("server")) {
				Toast.makeText(context, "Server error..try again",
						Toast.LENGTH_SHORT).show();
			} else if (message.equals("success")) {
				try {
					JSONArray details = json.getJSONArray("details");
					ailments = new String[details.length()];
					for (int i = 0; i < details.length(); i++) {
						JSONObject p = details.getJSONObject(i);
						ailments[i] = p.getString("name");
					}

					adapter1 = new ArrayAdapter<String>(context,
							android.R.layout.simple_spinner_item, ailments);
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

	class storeAsync extends AsyncTask<String, String, String> {
		private String message;
		private ProgressDialog pDialog;
		private JSONObject json3;

		protected void onPreExecute() {
			super.onPreExecute();

			message = "";

			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Fetching diseases information...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();

		}

		protected String doInBackground(String... args) {

			if (Vars.isURLReachable(context)) {

				String url = Vars.getIp() + "checkcase.php";

				List<NameValuePair> params2 = new ArrayList<NameValuePair>();

				SharedPreferences settings = context.getSharedPreferences(
						"storage", 0);
				params2.add(new BasicNameValuePair("hid", settings.getString(
						"pid", "")));
				params2.add(new BasicNameValuePair("pid", value[0].getText()
						.toString().trim()));
				if (hids.length > 0)
					{params2.add(new BasicNameValuePair("did", valuer0
							.getSelectedItem().toString().split("ID:")[1]
							.trim()));}
				else
					{
					params2.add(new BasicNameValuePair("did",""));
					}

				if (valuer2.getSelectedItem().equals("major"))
					params2.add(new BasicNameValuePair("rid", value[5]
							.getText().toString().trim()));
				else
					params2.add(new BasicNameValuePair("rid", "NA"));

				json3 = jParser.makeHttpRequest(url, "POST", params2);
				Log.d("Other", json3.toString());
				try {

					if (json3.get("error_msg").equals("")) {

						// message = "success";
						url = Vars.getIp() + "storecase.php";

						params2 = new ArrayList<NameValuePair>();

						params2.add(new BasicNameValuePair("hid", settings
								.getString("pid", "")));
						params2.add(new BasicNameValuePair("pid", value[0]
								.getText().toString().trim()));
						params2.add(new BasicNameValuePair("did", valuer0
								.getSelectedItem().toString().split("ID:")[1]
								.trim()));
						if (valuer2.getSelectedItem().equals("major"))
							params2.add(new BasicNameValuePair("rid", value[5]
									.getText().toString().trim()));
						else
							params2.add(new BasicNameValuePair("rid", 0 + ""));

						params2.add(new BasicNameValuePair("ailment", value[2]
								.getText().toString().trim()));
						params2.add(new BasicNameValuePair("ailmenttype",
								valuer1.getSelectedItem().toString()));
						params2.add(new BasicNameValuePair("medication",
								value[4].getText().toString().trim()));
						params2.add(new BasicNameValuePair("major", valuer2
								.getSelectedItem().toString()));

						json3 = jParser.makeHttpRequest(url, "POST", params2);
						Log.d("Other", json3.toString());
						if (json3.getString("error_msg").equals(""))
							message = "success";
						else
							message = "error";

					} else {
						message = json3.get("error_msg").toString();

					}
				} catch (JSONException e) {

					message = "server";

				}
			} else
				message = "problem";
			return null;
		}

		protected void onPostExecute(String file_url) {
			if (pDialog != null)
				pDialog.dismiss();
			if (message.equals("problem")) {
				Toast.makeText(context,
						"Server down or no internet connection...",
						Toast.LENGTH_SHORT).show();
			} else if (message.equals("server")) {
				Toast.makeText(context, "Server error..try again",
						Toast.LENGTH_SHORT).show();
			} else if (message.equals("patient")) {
				Toast.makeText(context, "Patient doesnot exist",
						Toast.LENGTH_SHORT).show();
			} else if (message.equals("doctor")) {
				Toast.makeText(context, "Doctor doesnot exist",
						Toast.LENGTH_SHORT).show();
			} else if (message.equals("refrences")) {
				Toast.makeText(context, "Doctor referred to doesnot exist",
						Toast.LENGTH_SHORT).show();
			}

			else if (message.equals("success")) {
				((Activity) context).finish();

			} else {
				Toast.makeText(context, "Server error..try again",
						Toast.LENGTH_SHORT).show();
			}

		}

	}
}
