package net.evoir.avenue225.fragments;
import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;

import net.evoir.avenue225.ConnectionDetector;
import net.evoir.avenue225.MainActivity;
import net.evoir.avenue225.R;
import net.evoir.avenue225.db.Model;
import net.evoir.avenue225.objects.Post;
import net.evoir.utils.AsyncDataLoading;
import net.evoir.utils.JsonService;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
public class SyncFragment extends Fragment {
    private Context mContext = getActivity();
    private View view;
    private Dao <Post, Integer> dao;
  	private List<Post> postList;
  	// Splash screen timer
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle args) {
    	
        view = inflater.inflate(R.layout.loading_detail_fragment, container, false);

        mContext = getActivity();
        //dialog = new ProgressDialog(mContext); 
        
        return view;
    }

    @Override 
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);
  		ConnectionDetector detector = new ConnectionDetector(mContext);
  		boolean isConnectingToInternet = detector.isConnectingToInternet();
  		if (isConnectingToInternet) {
  			// check if there is at least one post in database
  			new AsyncDataLoading(mContext).execute();
  				

   
          // shut down the activity
          // launch home fragment
          //getActivity().finish();
  		}else {
  			Toast.makeText(mContext, "No Internet, Check your network connection",
					Toast.LENGTH_LONG).show();
  		}
        
        
    }
	  public void setTitle(CharSequence title) {
		  CharSequence mTitle = title;
	      getActivity().getActionBar().setTitle(mTitle);
	  }
	  public void loadHomeFragment() {
  		Intent i = new Intent(mContext, MainActivity.class);
          startActivity(i);
  	}
}