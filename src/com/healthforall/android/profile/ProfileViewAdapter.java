package com.healthforall.android.profile;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.healthforall.android.R;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;

public class ProfileViewAdapter extends BaseAdapter {
 
    // Declare Variables
    Context context;
    String[] button;
    String[] detail;
    
    LayoutInflater inflater;
	private View itemView;
 
    public ProfileViewAdapter(Context context, String[] button, String[] detail) {
        this.context = context;
        this.button = button;
        this.detail = detail;
       
    }
 
    public int getCount() {
        return button.length;
    }
 
   
    public long getItemId(int position) {
        return 0;
    }
 
    
    
    @SuppressWarnings("deprecation")
    public View getView(int position, View convertView, ViewGroup parent) {
        itemView=null;
        // Declare Variables
        TextView value;
        BootstrapButton buttons;
        Point size = new Point();
        
        
        
        Display display;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
        	WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            display = wm.getDefaultDisplay();
            
            
            display.getSize(size);
        }
        else {
            display = ((Activity) context).getWindowManager().getDefaultDisplay();
            size.x = display.getWidth();
            size.y = display.getHeight();
        }
        
		int width = size.x;
        int height = size.y;
 
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(itemView == null) {
        	itemView = (View)inflater.inflate(R.layout.profilelistitem, parent, false);
        }
        
 
       
        value = (TextView) itemView.findViewById(R.id.profilevalue);
        buttons= (BootstrapButton) itemView.findViewById(R.id.profilebutton);
        value.setText(detail[position]);
        buttons.setText(button[position]);
        value.setWidth((int)(width*0.6));
        buttons.setMinimumWidth((int)(width*0.4)); 
        
 
        return itemView;
    }

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}