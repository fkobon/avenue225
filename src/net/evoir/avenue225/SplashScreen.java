package net.evoir.avenue225;


 

 
import java.sql.SQLException;
import java.util.List;

import net.evoir.avenue225.db.Model;
import net.evoir.avenue225.objects.Post;
import net.evoir.utils.ConnectionDetector;
import net.evoir.utils.Constants;
import net.evoir.utils.JsonService;
import net.evoir.utils.NotificationService;
import net.evoir.utils.VolleyRequest;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.TextView;

 
public class SplashScreen extends Activity {
 
    private Context mContext;
    private TextView versionText,logoText, sloganText;
	private static Dao<Post, String> dao;
	private List<Post> postList;
	// Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;
    
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		mContext = this;
		
        setContentView(R.layout.splashscreen);
        logoText = (TextView) this.findViewById(R.id.logoText);
        logoText.setText(R.string.app_name);
        
        
        versionText = (TextView) this.findViewById(R.id.versionText);
        versionText.setText(getVersion());
        
        sloganText = (TextView) this.findViewById(R.id.sloganText);
		sloganText.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
		
		
		//notifyTemp(); //for test purpose only 
		
		

		//prepare the Async task
		loadAsync loadAsync = new loadAsync();
		loadAsync.execute();
		//launch notification service
		startNotificationService();

		
    }
    
    public void startNotificationService() {
    	
    	Log.v(Constants.TAG,"startNotificationService() called");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int minutes = Integer.parseInt(prefs.getString("update_interval", "1"));
        //int minutes = 1;
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent i = new Intent(this, NotificationService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        am.cancel(pi);
        // by my own convention, minutes <= 0 means notifications are disabled
        if (minutes > 0) {
            am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + minutes*Constants.MINUTE_VALUE*1000,
                minutes*Constants.MINUTE_VALUE*1000, pi);
        }
    
        
    	//to stuff
    }
	public String getVersion(){
		PackageManager manager = mContext.getPackageManager();
		PackageInfo info;
		String version = null;
		try {
			info = manager.getPackageInfo(mContext.getPackageName(), 0);
			//version = "" + info.versionCode;
			version = "" + info.versionName;

		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return version;
	}
	
 
   
    /*
     * ASYNC JOB
     * 
     * */
    private class loadAsync  extends AsyncTask <Void, Void, Void>  {

     
        @Override
        protected void onPreExecute() {

        	
        }
         
        @Override
        protected void onPostExecute(Void result) {

        }
         
        @Override
        protected Void doInBackground(Void... params) {
        	
        		ConnectionDetector detector = new ConnectionDetector(mContext);
        		boolean isConnectingToInternet = detector.isConnectingToInternet();
        		if (isConnectingToInternet) {
        			
        			//start downloading feed silently 
        			//Log.v(Constants.TAG, "launch startService()");
        			startService();
        			
        			// check if there is at least one post in database

        				try {   
                			dao = Model.getHelper(mContext).getDao(Post.class);
        					QueryBuilder<Post, String> queryBuilder =
        		    		    	dao.queryBuilder();
        		    		//get posts from database and group them by category
        			    	queryBuilder.limit(10);
        					postList = queryBuilder.query();
        					if (postList.size() <1) {
        						SPLASH_TIME_OUT = 8000;
        					}
        					/*else {
        						SPLASH_TIME_OUT = 4000;
        					}*/
        				} catch (SQLException e) {
        					// TODO Auto-generated catch block
        					e.printStackTrace();
        				}
        							
        		}
        		
        		// Execute some code after some seconds have passed
        	    Handler handler = new Handler(Looper.getMainLooper()); 
        	    handler.postDelayed(new Runnable() {
        	      @Override
        	      public void run() { 
      	        	// shut down the activity
  	                finish(); 
  	                // launch home fragment
  	                loadHomeFragment();
  	         }
        	    }, SPLASH_TIME_OUT );
        	
            return null;

        }
    
    	public void loadHomeFragment() {
    		Intent i = new Intent(mContext, MainActivity.class);
            startActivity(i);
    	}
	   	private void startService() {
	   		Intent intent = new Intent(mContext, JsonService.class);
	   		mContext.startService(intent);		
	   	} 
    }
}
