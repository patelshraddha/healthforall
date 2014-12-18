package com.healthforall.android.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.healthforall.android.R;

public class HospitalViewAdapter extends BaseAdapter {

	// Declare Variables
	Context context;
	String[] file;
	String[] designation;
	String[] timings;
	String[] pid;
	TextView files[];
	String profileof;

	LayoutInflater inflater;
	private View itemView;

	public HospitalViewAdapter(Context context, String[] file,
			String[] designation, String[] timings, String[] pid,
			String profileof) {
		this.file = file;
		this.designation = designation;
		this.timings = timings;
		this.context = context;
		this.pid = pid;
		this.profileof = profileof;
		this.files = new TextView[file.length];
	}

	public int getCount() {
		return file.length;
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		itemView = null;
		// Declare Variables

		TextView post;
		TextView time;

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
		if (itemView == null) {
			itemView = (View) inflater.inflate(R.layout.doctorhospital, parent,
					false);
		}

		// Locate the TextViews in listview_item.xml

		files[position] = (TextView) itemView.findViewById(R.id.file);
		if (position != 0) {
			files[position].setPaintFlags(files[position].getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);
			files[position].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					String get="";
                    if(profileof.equals("doctor"))
                    	get="hospital";
                    else
                    	get="doctor";
					Intent i = new Intent(context, ProfileActivity.class);
					i.putExtra("profileof", get);
					i.putExtra("author", "other");
					i.putExtra("pid", pid[position]);
					((Activity) context).startActivity(i);
				}

			});
		}
		post = (TextView) itemView.findViewById(R.id.designation);
		time = (TextView) itemView.findViewById(R.id.timings);
		if (position == 0) {
			files[position].setBackgroundResource(R.drawable.bg_striped_split);
			post.setBackgroundResource(R.drawable.bg_striped_split);
			time.setBackgroundResource(R.drawable.bg_striped_split);
			files[position].setTextColor(Color.WHITE);
			post.setTextColor(Color.WHITE);
			time.setTextColor(Color.WHITE);

		}
		files[position].setText(file[position]);
		post.setText(designation[position]);
		time.setText(timings[position]);

		return itemView;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}