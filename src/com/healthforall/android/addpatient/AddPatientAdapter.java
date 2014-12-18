package com.healthforall.android.addpatient;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Point;
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
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.healthforall.android.JSONParser;
import com.healthforall.android.LoginActivity;
import com.healthforall.android.R;
import com.healthforall.android.Vars;

public class AddPatientAdapter extends BaseAdapter {

	private String[] buttons;
	private Context context;
	private LayoutInflater inflater;
	private View itemView;
	private EditText[] value;
	private BootstrapButton savebutton;
	private String name = "", surname = "", phone = "", email = "",
			address = "", password = "";
	private Dialog alertdialog;

	public AddPatientAdapter(Context context, String[] buttons) {
		this.context = context;
		this.buttons = buttons;
		this.value = new EditText[10];
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

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (position != getCount() - 1) {
			if (itemView == null) {
				itemView = (View) inflater.inflate(R.layout.editlistitem,
						parent, false);
			}

			value[position] = (EditText) itemView.findViewById(R.id.editvalue);
			value[position].setEnabled(true);
			value[position].setWidth((int) (width * 0.6));

			button = (BootstrapButton) itemView.findViewById(R.id.editbutton);
			button.setMinimumWidth((int) (width * 0.4));
			button.setText(buttons[position]);

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

					surname = value[1].getText().toString().trim();
					phone = value[3].getText().toString().trim();
					email = value[4].getText().toString().trim();
					address = value[5].getText().toString().trim();
					password = value[2].getText().toString().trim();
					if ((surname.length() == 0) || (name.length() == 0)
							|| (address.length() == 0) || (phone.length() == 0))
						createAlert("Any field cannot be empty.");
					else if (!email.contains("@"))
						createAlert("Not a valid email id");
					else {
						int count = 0;
						if (email.length() != 0) {
							for (int i = 0; i < phone.length(); i++) {
								char p = phone.charAt(i);
								boolean check = (p == '0') || (p == '1')
										|| (p == '2') || (p == '3')
										|| (p == '4') || (p == '5')
										|| (p == '6') || (p == '7')
										|| (p == '8') || (p == '9');
								if (!check) {
									count++;
									break;
								}
							}
						} else
							count = 0;
						if (count == 0)
							new AddPatientAsync().execute();
						else
							createAlert("Phone number should get data in form 0-9 only.");
					}
				}
			});
		}

		return itemView;
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

	private ProgressDialog pDialog = null;

	JSONParser jParser = new JSONParser();

	class AddPatientAsync extends AsyncTask<String, String, String> {
		private String message;
		private ProgressDialog pDialog;
		private ArrayList<NameValuePair> params;

		protected void onPreExecute() {
			super.onPreExecute();

			message = "";

			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Operating information...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();

		}

		protected String doInBackground(String... args) {
			if (Vars.isURLReachable(context)) {
				String url = Vars.getIp() + "/patient/newpatient.php";

				params = new ArrayList<NameValuePair>();

				params.add(new BasicNameValuePair("name", value[0].getText()
						.toString().trim()));
				params.add(new BasicNameValuePair("surname", value[1].getText()
						.toString().trim()));
				params.add(new BasicNameValuePair("password", value[2]
						.getText().toString().trim()));
				params.add(new BasicNameValuePair("phone", value[3].getText()
						.toString().trim()));
				params.add(new BasicNameValuePair("email", value[4].getText()
						.toString().trim()));
				params.add(new BasicNameValuePair("address", value[5].getText()
						.toString().trim()));
				json = jParser.makeHttpRequest(url, "POST", params);

				Log.d("Checker", json.toString() + "Hi");
				try {

					if (json.get("error_msg").equals("success")) {

						message = "success";

					} else {

						message = "error";

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
			}
			if (message.equals("problem")) {
				Toast.makeText(context,
						"Server down or internet connection issue..",
						Toast.LENGTH_SHORT).show();
			}

			else if (message.equals("error")) {
				Toast.makeText(context, "Registration failed..try again",
						Toast.LENGTH_SHORT).show();
			} else if (message.equals("success")) {
				Log.d("check", "Here");
				try {
					String info = "\n" + "\n" + "Registration successful:"
							+ "\n" + "Id:" + json.getInt("pid") + "\n"
							+ "Password:" + json.getString("password") + "\n"
							+ "\n";
					Toast.makeText(context, info, Toast.LENGTH_LONG).show();
					
				} catch (JSONException e) {

				}
				((Activity) context).finish();
			}

		}
	}
}
