package net.evoir.avenue225;


 

 
import java.sql.SQLException;
import java.util.List;

import net.evoir.avenue225.db.Model;
import net.evoir.avenue225.objects.Post;
import net.evoir.utils.JsonService;

import com.j256.ormlite.dao.Dao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

 
public class SplashScreen extends Activity {
 
    private Context mContext;
    private TextView versionText;
	private TextView logoText;
	private Dao <Post, Integer> dao;
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
        	try {
        		ConnectionDetector detector = new ConnectionDetector(mContext);
        		boolean isConnectingToInternet = detector.isConnectingToInternet();
        		if (isConnectingToInternet) {
        			// check if there is at least one post in database
        				try {
        					dao = Model.getHelper(mContext).getDao(Post.class);
        					postList = dao.queryForAll();
        					if (postList.size() <1) {
        						SPLASH_TIME_OUT = 10000;
        					}
        					else {
        						SPLASH_TIME_OUT = 4000;
        					}
        				} catch (SQLException e) {
        					// TODO Auto-generated catch block
        					e.printStackTrace();
        				}
        				
        			startService();
        		}
         
                Thread.sleep(SPLASH_TIME_OUT); // sleep for a certain period depending on network status
                // shut down the activity
                finish();
                // launch home fragment
                loadHomeFragment();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        	
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
