package net.evoir.avenue225;
import net.evoir.avenue225.fragments.SettingsFragment;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;


	public class SettingsActivity extends PreferenceActivity {
		 
		 @Override
		 public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  getFragmentManager().beginTransaction().replace(android.R.id.content,
		    new SettingsFragment()).commit();
		  PreferenceManager.setDefaultValues(SettingsActivity.this, R.xml.settings_fragment, false);
		  
		//set gradient background to action bar
	        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_orange));
		   
		 }
  
		    @Override
		    public boolean onCreateOptionsMenu(Menu menu) {
		        // Inflate the menu; this adds items to the action bar if it is present.
		        this.getMenuInflater().inflate(R.menu.post_list, menu);
		        

		        
		        return true;
		    }
		    @Override
		    public boolean onOptionsItemSelected(MenuItem item) {

		    	if (item.getItemId() == R.id.action_back) {
		    		finish();
		    	}

		        return super.onOptionsItemSelected(item);
		    }
  	}



