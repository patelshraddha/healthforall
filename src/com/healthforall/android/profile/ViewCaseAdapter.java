/**
 * 
 */
package com.healthforall.android.profile;

import com.healthforall.android.R;

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

public class ViewCaseAdapter extends BaseAdapter {

	private Context context;
	private String[] dates;
	private String[] hosp;
	private String[] doc;
	private String[] type;
	private String[] ref;
	private String[] description;
	private String[] major;
	private String[] medication;
	private LayoutInflater inflater;
	private View itemView;
	private TextView date;
	private TextView hospital;
	private TextView doctor;
	private TextView descript;
	private TextView typer;
	private TextView medicator;
	private TextView majorer;
	private TextView refer;

	ViewCaseAdapter(Context context, String[] dates, String[] hosp, String[] doc,
			String[] ref, String[] type, String[] description, String[] major,
			String[] medication) {
		
		this.context = context;
		this.dates =dates;
		this.hosp = hosp;
		this.doc =doc;
		this.type = type;
		this.ref =ref;
		this.description = description;
		this.major=major;
		this.medication = medication;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dates.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
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
			itemView = (View) inflater.inflate(R.layout.viewcase, parent,false);
		}

		// Locate the TextViews in listview_item.xml
		date = (TextView) itemView.findViewById(R.id.dates); 
		date.setText(dates[position].split(" ")[0]);
		hospital = (TextView) itemView.findViewById(R.id.hospital); 
		hospital.setText(hosp[position]);
		doctor = (TextView) itemView.findViewById(R.id.doctor); 
		doctor.setText(doc[position]);
		
		descript = (TextView) itemView.findViewById(R.id.description); 
		descript.setText(description[position]);
		 typer= (TextView) itemView.findViewById(R.id.type); 
		typer.setText(type[position]);
		medicator = (TextView) itemView.findViewById(R.id.medication); 
		medicator.setText(medication[position]);
		majorer = (TextView) itemView.findViewById(R.id.major); 
		majorer.setText(major[position]);
		
		refer = (TextView) itemView.findViewById(R.id.rid); 
		refer.setText(ref[position]);
//		files[position] = (TextView) itemView.findViewById(R.id.file);
//		if (position != 0) {
//			files[position].setPaintFlags(files[position].getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);
//			files[position].setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View view) {
//					String get="";
//                    if(profileof.equals("doctor"))
//                    	get="hospital";
//                    else
//                    	get="doctor";
//					Intent i = new Intent(context, ProfileActivity.class);
//					i.putExtra("profileof", get);
//					i.putExtra("author", "other");
//					i.putExtra("pid", pid[position]);
//					((Activity) context).startActivity(i);
//				}
//
//			});
//		}
//		post = (TextView) itemView.findViewById(R.id.designation);
//		time = (TextView) itemView.findViewById(R.id.timings);
//		if (position == 0) {
//			files[position].setBackgroundResource(R.drawable.bg_striped_split);
//			post.setBackgroundResource(R.drawable.bg_striped_split);
//			time.setBackgroundResource(R.drawable.bg_striped_split);
//			files[position].setTextColor(Color.WHITE);
//			post.setTextColor(Color.WHITE);
//			time.setTextColor(Color.WHITE);
//
//		}
//		files[position].setText(file[position]);
//		post.setText(designation[position]);
//		time.setText(timings[position]);

		return itemView;
	}

}
