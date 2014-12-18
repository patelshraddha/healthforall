package com.healthforall.android;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Vars {
  private static String ip="http://192.168.43.9/";
  //private static String ip="http://192.168.63.1/";
  
    
  public static String getIp(){
	  return ip;
  }
  
  static public boolean isURLReachable(Context context) {
	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnected()) {
	        try {
	            URL url = new URL(ip);   // Change to "http://google.com" for www  test.
	            HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
	            urlc.setConnectTimeout(10 * 1000);          // 10 s.
	            urlc.connect();
	            if (urlc.getResponseCode() == 200) {        // 200 = "OK" code (http connection is fine).
	               
	                return true;
	            } else {
	                return false;
	            }
	        } catch (MalformedURLException e1) {
	            return false;
	        } catch (IOException e) {
	            return false;
	        }
	    }
	    return false;
	}
}
