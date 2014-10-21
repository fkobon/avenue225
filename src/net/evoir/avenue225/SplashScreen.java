package net.evoir.avenue225;


 

 
import java.sql.SQLException;
import java.util.List;

import net.evoir.avenue225.db.Model;
import net.evoir.avenue225.objects.Post;

import com.j256.ormlite.dao.Dao;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

 
public class SplashScreen extends Activity {
 
    private Context mContext;
    private TextView versionText;
	private TextView logoText;
	private Dao <Post, Integer> dao;
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
        
        //set Spinner style for progressDialog
        ProgressDialog progressDialog = new ProgressDialog(mContext,R.id.progressBar);
        //progressDialog.setCancelable(true);
        //progressDialog.setMessage("Initializing Please Wait");
        //progressDialog.setTitle("Loading");
        //progressDialog.setIcon(R.drawable.home_add);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        
        versionText = (TextView) this.findViewById(R.id.versionText);
		versionText.setText(getVersion());
		
        /**
         * Showing splashscreen while making network calls to download necessary
         * data before launching the app Will use AsyncTask to make http call
         */
		ConnectionDetector detector = new ConnectionDetector(mContext);
		boolean isConnectingToInternet = detector.isConnectingToInternet();
		if (isConnectingToInternet) {
			// check if there is at least one post in database
				try {
					dao = Model.getHelper(mContext).getDao(Post.class);
					postList = dao.queryForAll();
					if (postList.size() <1) {
						SPLASH_TIME_OUT = 8000;
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			startService();
			SplashLoading();
		}
		else {
			SplashLoading();
		}
 
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
	public void SplashLoading() {
		  new Handler().postDelayed(new Runnable() {
			  
	            /*
	             * Showing splash screen with a timer. This will be useful when you
	             * want to show case your app logo / company
	             */
	 
	            @Override
	            public void run() {
	                // This method will be executed once the timer is over
	                // Start your app main activity
	                Intent i = new Intent(SplashScreen.this, MainActivity.class);
	                startActivity(i);
	 
	                // close this activity
	                finish();
	            }
	        }, SPLASH_TIME_OUT);
	    }
	private void startService() {
		Intent intent = new Intent(mContext, JsonService.class);
		startService(intent);		
	} 
   
}
