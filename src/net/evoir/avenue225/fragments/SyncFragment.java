package net.evoir.avenue225.fragments;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.j256.ormlite.dao.Dao;

import net.evoir.avenue225.ConnectionDetector;
import net.evoir.avenue225.Constants;
import net.evoir.avenue225.MainActivity;
import net.evoir.avenue225.R;
import net.evoir.avenue225.db.Model;
import net.evoir.avenue225.objects.Post;
import net.evoir.utils.AppController;
import net.evoir.utils.VolleyDataLoading;
import net.evoir.utils.JsonService;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
public class SyncFragment extends Fragment {
    private Context mContext = getActivity();
    private View view;
    private Dao <Post, Integer> dao;
  	private List<Post> postList;
  	private Button reloadButton;
  	private ProgressBar reloadBar;


	private static final String JSON_LINK = "http://www.avenue225.com/json";

	//JSON Node names
	private static final String TAG_LINK = "link";
	private static final String TAG_TITLE = "title";
	private static final String TAG_IMG = "image";
	private static final String TAG_DESC = "description";
	private static final String TAG_PUBDATE = "pubDate";
	private static final String TAG_CONTENT = "content";
	private static final String TAG_CATEGORY = "category";
	private static final String TAG_CATEGORY_SLUG = "categorySlug";
	
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle args) {
    	
        view = inflater.inflate(R.layout.sync_fragment, container, false);

        mContext = getActivity();
        //dialog = new ProgressDialog(mContext); 
        reloadButton = (Button) view.findViewById(R.id.reload_button);
        reloadBar = (ProgressBar) view.findViewById(R.id.reload_progress);
        
        /*
         * Deal with progress dialog
         * 1. check if the Fragment has the "isFirstTime" variable has argument
         * isFirstTime argument is suppose to specify that the fragment was launched via
         * the Drawer menu (onClick) 
         * 2. If the isFirstTime arguments does not exist we hide the progressBar and display
         * the button
         * */ 
        if(getArguments() == null) {
        	hideBar();
        	//category = getArguments().getString("category");   

        }else{
        	reloadButton.setVisibility(view.GONE);
            showBar();
            makeJsonArrayRequest();
        }

        
        reloadButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
            	showBar();
            	makeJsonArrayRequest();
            }
        });
        
        return view;
        

    }

    private void hideBar() {
    	// TODO Auto-generated method stub
    	reloadBar.setVisibility(View.GONE);
    	reloadButton.setVisibility(view.VISIBLE);
    	
    }
    private void showBar() {
		// TODO Auto-generated method stub
    	reloadBar.setVisibility(View.VISIBLE);
    	
		
	}

	@Override 
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);
  		ConnectionDetector detector = new ConnectionDetector(mContext);
  		
  		boolean isConnectingToInternet = detector.isConnectingToInternet();
  		if (!isConnectingToInternet) {
  			Toast.makeText(mContext, "No Internet, Check your network connection",
					Toast.LENGTH_LONG).show();
  					hideBar();
  		}
        
        
    }
	  public void setTitle(CharSequence title) {
		  CharSequence mTitle = title;
	      getActivity().getActionBar().setTitle(mTitle);
	  }

 /*
  * JSON DOWNLOAD WITH VOLLEY
  * */ 
	   /**
	     * Async Task to make http call
	     */

		private void makeJsonArrayRequest() {
			 
			Log.v(Constants.TAG,"start making JsonRequest on JsonService.makeJsonArrayRequest()"); 
	        JsonArrayRequest req = new JsonArrayRequest(JSON_LINK,
	                new Response.Listener<JSONArray>() {
	                    @Override
	                    public void onResponse(JSONArray posts) {
	                    	Log.v(Constants.TAG, "start downloading Json feed");
	                        try {
	                            // Parsing json array posts
	                            // loop through each json object

	                            if(posts!=null){
	                            	Log.v(Constants.TAG, "posts contains "+posts.length()+" items");						

	                        		List<Post> postList = new ArrayList<Post>();
	                                for (int i = 0; i < posts.length(); i++) {
	                                	 
	                                    JSONObject c = (JSONObject) posts
	                                            .get(i);
	     

	    						
	    								// Storing each json item in variable
	    								String title = c.getString(TAG_TITLE);
	    								String link = c.getString(TAG_LINK);
	    								String description = c.getString(TAG_DESC);
	    								String content = c.getString(TAG_CONTENT);
	    								String pubDate = c.getString(TAG_PUBDATE);				
	    								String image = c.getString(TAG_IMG);
	    								
	    								String category = c.getString(TAG_CATEGORY);
	    								String categorySlug = c.getString(TAG_CATEGORY_SLUG);
	    								Log.v("mytag","in makeJsonArrayRequest() title "+i+" is " +title);						
	    						
	    						
	    								if (title != null && link != null && description != null && content != null && pubDate != null && image != null && category != null && categorySlug != null) {
	    									/*DateFormat formatter = new DateFormat();
	    									pubDate = formatter.Format(pubDate);*/
	    									Post item = new Post(link, title,description,content,pubDate,image,category,categorySlug);
	    									//Log.v("mytag","in JsonParser image "+i+" is " +image);						
	    						
	    									postList.add(item);
	    									try {
	    										Model.getHelper(mContext).getDao(Post.class).createOrUpdate(item);
	    									} catch (SQLException er) {
	    										// TODO Auto-generated catch block
	    										er.printStackTrace();
	    										Log.v("mytag"," Dao error" + er.getMessage());		
	    						
	    									}
	    						
	    									title = null;
	    									link = null;
	    									description = null;
	    									image = null;
	    									pubDate = null;
	    									content = null;
	    									image = null;
	    									category = null;
	    									categorySlug = null;
	    								}     
	    							
									Log.v(Constants.TAG, "finish downloading Json feed");		
	                                }
	                                
		                            Toast.makeText(mContext,postList.size()+" new posts downloaded",
		                                    Toast.LENGTH_LONG).show();
		                            		hideBar();
	                        	}

	 
	                        
	                        } catch (JSONException e) {
	                            e.printStackTrace();
	                            Toast.makeText(mContext,
	                                    "Error: " + e.getMessage()+". Try again",
	                                    Toast.LENGTH_LONG).show();
	                            Log.v(Constants.TAG, "error while downloading:"+e.getMessage());
	                            hideBar();
	                        }
	 
	                    }
	                }, new Response.ErrorListener() {
	                    @Override
	                    public void onErrorResponse(VolleyError error) {
	                        Log.v(Constants.TAG, "Error: " + error.getMessage());
	                        //Toast.makeText(mContext,error.getMessage(), Toast.LENGTH_SHORT).show();
	                    }
	                });
	 
	        // Adding request to request queue
	        AppController.getInstance().addToRequestQueue(req);
	    }
}