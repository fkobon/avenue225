package net.evoir.avenue225;


 

 
import java.sql.SQLException;
import java.util.List;

import net.evoir.avenue225.db.Model;
import net.evoir.avenue225.objects.Post;
import net.evoir.utils.ConnectionDetector;
import net.evoir.utils.Constants;
import net.evoir.utils.JsonService;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

 
public class SplashScreen extends Activity {
 
    private Context mContext;
    private TextView versionText,logoText, sloganText;
	private static Dao<Post, String> dao;
	private List<Post> postList;
	// Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    
 
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
		
		
		
		//prepare the Async task
		loadAsync loadAsync = new loadAsync();
		loadAsync.execute();
		
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
        						SPLASH_TIME_OUT = 5000;
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
