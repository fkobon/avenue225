package net.evoir.avenue225.fragments;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import net.evoir.avenue225.R;
import net.evoir.avenue225.db.Model;
import net.evoir.avenue225.objects.Post;
import net.evoir.utils.AppController;
import net.evoir.utils.ConnectionDetector;
import net.evoir.utils.Constants;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
public class SyncFragment extends Fragment {
    private Context mContext = getActivity();
    private View view;
    private Dao <Post, String> dao;
  	private Button reloadButton;
  	private ProgressBar reloadBar;

  	private String lastPubDate;

	
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle args) {
    	
        view = inflater.inflate(R.layout.sync_fragment, container, false);

        mContext = getActivity();
        //dialog = new ProgressDialog(mContext); 
        reloadButton = (Button) view.findViewById(R.id.reload_button);
        reloadBar = (ProgressBar) view.findViewById(R.id.reload_progress);
        
        //initiate lastPubDate variable   
		lastPubDate = lastPubDate();
		
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
            makeJsonArrayRequest();
        }

        
        reloadButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
            	makeJsonArrayRequest();
            }
        });
        
        return view;
        

    }

	private void hideBar() {
    	// TODO Auto-generated method stub
    	reloadBar.setVisibility(view.GONE);
    	reloadButton.setVisibility(view.VISIBLE);
    	reloadButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_border_orange));
    	reloadButton.setText(getResources().getText(R.string.reload_text));

    	
    }
    private void showBar() {
		// TODO Auto-generated method stub
    	reloadBar.setVisibility(View.VISIBLE);
    	reloadButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_border_grey));
    	reloadButton.setText(getResources().getText(R.string.loading_text));
    	
		
	}

	@Override 
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);

        
        
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
			showBar();
			ConnectionDetector detector = new ConnectionDetector(mContext);
      		
      		boolean isConnectingToInternet = detector.isConnectingToInternet();
      		if (isConnectingToInternet) {
      					//Log.v(Constants.TAG,"start making JsonRequest on JsonService.makeJsonArrayRequest()"); 
      					
      					//start making JsonRequest on JsonService.makeJsonArrayRequest()
      			        JsonArrayRequest req = new JsonArrayRequest(Constants.JSON_LINK,
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
      			    								String title = c.getString(Constants.TAG_TITLE);
      			    								String link = c.getString(Constants.TAG_LINK);
      			    								String description = c.getString(Constants.TAG_DESC);
      			    								String content = c.getString(Constants.TAG_CONTENT);
      			    								String pubDate = c.getString(Constants.TAG_PUBDATE);				
      			    								String image = c.getString(Constants.TAG_IMG);
      			    								
      			    								String category = c.getString(Constants.TAG_CATEGORY);
      			    								String categorySlug = c.getString(Constants.TAG_CATEGORY_SLUG);
      			    								Log.v("mytag","in makeJsonArrayRequest() title "+i+" is " +title);						
      			    						
      			    								/*
      		    									 * TODO
      		    									 * Make sure string are not empty
      		    									 * */
      			    								
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
      			    						
      			    									/*
      			    									 * TODO
      			    									 * Make sure string are not empty
      			    									 * */
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
      			                                homeFragment();
      				                            Toast.makeText(mContext,getNewPosts()+" new article(s)",
      				                                    Toast.LENGTH_LONG).show();
      				                            		hideBar();
			                            		// launch homeFragment
		                            		
      			                        	}

      			 
      			                        
      			                        } catch (JSONException e) {
      			                            e.printStackTrace();
      			                            /*
      			                            Toast.makeText(mContext,
      			                                    "Error: " + e.getMessage()+". Try again",
      			                                    Toast.LENGTH_LONG).show();*/
      			                            //Log.v(Constants.TAG, "error while downloading:"+e.getMessage());
      			                            //hideBar();
      			                        }
      			 
      			                    }
      			                }, new Response.ErrorListener() {
      			                    @Override
      			                    public void onErrorResponse(VolleyError error) {
      			                        //Log.v(Constants.TAG, "Error: " + error.getMessage());
      			                        Toast.makeText(mContext,getResources().getText(R.string.download_error_text), Toast.LENGTH_SHORT).show();
      			                        hideBar();
      			                    }
      			                });
      			 
      			        // Adding request to request queue
      			        AppController.getInstance().addToRequestQueue(req);
      		}else {
				hideBar();
      			Toast.makeText(mContext, getResources().getText(R.string.network_error_text),
    					Toast.LENGTH_LONG).show();
      		}

	    }
		public void homeFragment() {
	          Fragment homeFragment = new CategoryFragment();
		
		      FragmentManager fragmentManager = getFragmentManager();      
		      fragmentManager.beginTransaction().replace(R.id.content_frame, homeFragment).commit();
		}
		public int getNewPosts(){
			int postNumber=0;
			try {
				dao = Model.getHelper(mContext).getDao(Post.class);
				QueryBuilder<Post, String> queryBuilder =
						dao.queryBuilder();
				//get posts from database where status is -1 (brand new)
				queryBuilder.orderBy("pubDate", false);
				List<Post> listPosts = new ArrayList<Post>();
				List<Post> tempList=queryBuilder.query();
					
					// loop through database and search for brand new posts
					for(int i=0;i<tempList.size();i++) {
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
						try {
							Date LastPubDate =format.parse(lastPubDate);
							Date PostPubDate =format.parse(tempList.get(i).getPubDate());
							if (LastPubDate.compareTo(PostPubDate) < 0) {
								//the post is a brand new one
								listPosts.add(tempList.get(i));
								
							}
							
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// if list of brand new post is not null then postNumber will count those posts
						if (listPosts!=null) {
							postNumber = listPosts.size();

						}
					}

				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return postNumber;
		}
		@SuppressWarnings("deprecation")
		public String lastPubDate() {
			String pubDate=null;
			
				try {
					dao = Model.getHelper(mContext).getDao(Post.class);
					QueryBuilder<Post, String> queryBuilder =
				    	dao.queryBuilder();
						//get posts from database where status is -1 (brand new)
						queryBuilder.orderBy("pubDate", false);
						queryBuilder.limit(1);
						List<Post> listPosts =queryBuilder.query();
						if (listPosts.size()>0){
							pubDate = listPosts.get(0).getPubDate().toString();
						}
						
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
			return pubDate;
					
			
		}
}